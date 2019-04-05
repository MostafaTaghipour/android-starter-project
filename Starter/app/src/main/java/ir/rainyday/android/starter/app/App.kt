package ir.rainyday.android.starter.app


import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDex
import com.squareup.leakcanary.LeakCanary
import ir.rainyday.android.common.helpers.NetworkHelper
import ir.rainyday.android.common.helpers.NetworkInfo
import ir.rainyday.android.common.helpers.NetworkStateReceiver
import ir.rainyday.android.common.supers.SuperApplication
import ir.rainyday.android.starter.R
import ir.rainyday.fontmanager.AppFont
import ir.rainyday.fontmanager.FontManager
import ir.rainyday.localemanager.LocaleManager
import ir.rainyday.thememanager.ThemeManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.EmptyLogger
import java.util.*


/**
 * Created by mostafataghipour on 6/25/2016 AD.
 */
class App : SuperApplication(), NetworkStateReceiver.Listener {

    lateinit var networkInfo: NetworkInfo

    override fun init(): Context {

        //enable CompatVector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        //listen to network state changed
        NetworkHelper.registerNetworkStateListener(this, this)

        //set app locale
        LocaleManager.getInstance().setCurrentLocale(this, Locale("fa"))

        //set app theme
        ThemeManager.getInstance().currentTheme = R.style.AppTheme_NoActionBar
        ThemeManager.getInstance().nightMode = AppCompatDelegate.MODE_NIGHT_NO

        //region app font
        val vazir = AppFont.AppFontBuilder(1, "Vazir", "fonts/vazir/", "Vazir-Medium-FD", "ttf")
                .thin("Vazir-Thin-FD")
                .extraLight("Vazir-Thin-FD")
                .light("Vazir-Light-FD")
                .regular("Vazir-Medium-FD")
                .medium("Vazir-Medium-FD")
                .semiBold("Vazir-Bold-FD")
                .bold("Vazir-Bold-FD")
                .extraBold("Vazir-Bold-FD")
                .black("Vazir-Bold-FD")
                .build()

        FontManager.getInstance().currentFont = vazir
        //endregion


        //enable memory leak detection
        LeakCanary.install(this)


        //firebase initialize
        //by default project contains firebase crashlytics and analytics, if you want use it you must go to firebase console (https://console.firebase.google.com)
        //create your project , download and add GoogleService-Info.plist to project
        //uncomment googleService and fabric plugin in dependencies.gradle
        //todo: uncomment this line
        //FirebaseApp.initializeApp(this)


        //start koin
        startKoin {
            androidContext(this@App)
            EmptyLogger()
            modules( AppDI.getModules)
        }

        return applicationContext
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    override fun onAppBackgrounded() {
        super.onAppBackgrounded()
    }

    override fun onAppForegrounded() {
        super.onAppForegrounded()
        onNetworkStateChange(NetworkInfo())
    }

    override fun onNetworkStateChange(info: NetworkInfo) {
        this.networkInfo = info
    }

}
