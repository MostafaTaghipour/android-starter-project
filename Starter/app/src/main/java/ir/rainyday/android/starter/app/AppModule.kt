package ir.rainyday.android.starter.app

import android.preference.PreferenceManager
import android.view.ContextThemeWrapper
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import ir.rainyday.android.starter.BuildConfig
import ir.rainyday.android.starter.net.*
import com.securepreferences.SecurePreferences
import io.reactivex.schedulers.Schedulers
import ir.rainyday.android.common.helpers.CurrentDevice
import ir.rainyday.android.common.helpers.StandardDateFormat
import ir.rainyday.fontmanager.FontManager
import ir.rainyday.localemanager.LocaleManager
import ir.rainyday.thememanager.ThemeManager
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import org.koin.android.ext.koin.androidApplication
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.StringQualifier
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val appModule = module {

    // Application instance
    single { androidApplication() as App }


    factory(StringQualifier(AppConst.APP_CONTEXT_KEY)) {
        val applicationContext = get<App>().applicationContext

        val wrapContextTheme = ContextThemeWrapper(applicationContext, get(StringQualifier(AppConst.APP_THEME_KEY)) as Int)
        val wrapContextFont = FontManager.wrapContext(wrapContextTheme)
        val wrapContextLocale = LocaleManager.getInstance().wrapContext(wrapContextFont)

        wrapContextLocale
    }

    single<CredentialsKeysRepo> { CredentialsKeysRepoImp(get<SecurePreferences> { parametersOf("cred_pref.xml") }) }

    single { get<App>().networkInfo }

    factory { LocaleManager.getInstance().currentLocale }

    factory { FontManager.getInstance().currentFont!! }

    factory(StringQualifier(AppConst.APP_THEME_KEY)) { ThemeManager.getInstance().currentTheme!! }

    factory(StringQualifier(AppConst.APP_ACCENT_COLOR_KEY)) { ThemeManager.getInstance().getColorAccent(get(StringQualifier(AppConst.APP_CONTEXT_KEY))) }

    factory(StringQualifier(AppConst.APP_PRIMARY_COLOR_KEY)) { ThemeManager.getInstance().getColorPrimary(get(StringQualifier(AppConst.APP_CONTEXT_KEY))) }

    single { PreferenceManager.getDefaultSharedPreferences(androidApplication()) }

    single { (fileName: String?) ->
        val name = fileName ?: "sec_pref.xml"
        SecurePreferences(androidApplication(), androidApplication().packageName, name)
    }

    single {
        Room
                .databaseBuilder(androidApplication(), AppDataBase::class.java, "weather-db")
                .fallbackToDestructiveMigration()
                .build()
    }

    //region Network
    single {
        GsonBuilder()
                .setDateFormat(StandardDateFormat.ISO_8601_DATE_TIME_MILLIS.value)
                .create()
    }

    single {

        val timeout: Long = URLs.TIME_OUT

        val client = OkHttpClient.Builder()
        client.readTimeout(timeout, TimeUnit.SECONDS)
        client.connectTimeout(timeout, TimeUnit.SECONDS)
        client.addInterceptor(TokenInterceptor())
        client.authenticator(TokenAuthenticator())
        client.addInterceptor(UserAgentInterceptor(CurrentDevice.userAgent!!))

        // enable logging for debug builds
        val logging = com.ihsanbal.logging.LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .setLevel(Level.BASIC)
                .log(Platform.INFO)
                .build()
        client.addInterceptor(logging)


        if(BuildConfig.DEBUG){
            client.addInterceptor(MockInterceptor())
        }

        client.build()
    }


    single {
        Retrofit.Builder()
                .baseUrl(URLs.baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create(get()))
                .client(get())
                .build()
    }
    //endregion
}



