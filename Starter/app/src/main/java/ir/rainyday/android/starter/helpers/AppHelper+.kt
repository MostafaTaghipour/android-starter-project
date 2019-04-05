@file:Suppress("DEPRECATION")

package ir.rainyday.android.starter.helpers

import android.content.Context
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import androidx.fragment.app.Fragment
import com.bartoszlipinski.viewpropertyobjectanimator.ViewPropertyObjectAnimator
import ir.rainyday.android.starter.BuildConfig
import ir.rainyday.android.starter.app.App
import ir.rainyday.android.starter.app.AppConst
import ir.rainyday.fontmanager.AppFont
import ir.rainyday.localemanager.LocaleManager
import ir.rainyday.thememanager.ThemeManager
import org.koin.core.context.GlobalContext
import org.koin.core.parameter.ParametersDefinition
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.StringQualifier
import org.koin.core.scope.Scope
import java.util.*


/**
 * Created by taghipour on 14/11/2017.
 */


//region injection
inline fun <reified T : Any> inject(
                qualifier: Qualifier? = null,
                scope: Scope = Scope.GLOBAL,
                noinline parameters: ParametersDefinition? = null
) = GlobalContext.get().koin.get<T>(qualifier, scope, parameters)


val appInstance = inject<App>()

val appContext: Context = inject(StringQualifier(AppConst.APP_CONTEXT_KEY))

val appThemeRes : Int = inject(StringQualifier(AppConst.APP_THEME_KEY))

val appAccentColor: Int = inject(StringQualifier(AppConst.APP_ACCENT_COLOR_KEY))

val appPrimaryColor: Int = inject(StringQualifier(AppConst.APP_PRIMARY_COLOR_KEY))

val appLocale = inject<Locale>()

val appFont = inject<AppFont>()
//endregion

//region BuildConfig
class BuildConfigEx{
    companion object {
        val productFlavor:ProductFlavor
            get() {
                return when {
                    BuildConfig.FLAVOR.contains("production") -> ProductFlavor.PRODUCTION
                    BuildConfig.FLAVOR.contains("staging") -> ProductFlavor.STAGING
                    else -> ProductFlavor.DEVELOPMENT
                }
            }
    }
}



enum class ProductFlavor{
    DEVELOPMENT,
    STAGING,
    PRODUCTION
}
//endregion

//region Other
fun View.animateProperty(): ViewPropertyObjectAnimator {
    return ViewPropertyObjectAnimator.animate(this)
}
//endregion

//region Locale
fun Locale.isRTL(): Boolean {
    return LocaleManager.isRTL(this)
}

fun Locale.isPersian(): Boolean {
    return LocaleManager.isPersian(this)
}

fun Context.isPersian(): Boolean {
    return getCurrentLocale().isPersian()
}
fun Context.isRTL(): Boolean {
    return getCurrentLocale().isRTL()
}

@Suppress("DEPRECATION")
fun Context.getCurrentLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
    } else {
        resources.configuration.locale
    }
}
//endregion

//region Theme
val Context.accentColor: Int
    get() = ThemeManager.getInstance().getColorAccent(this)

val Context.primaryColor: Int
    get() = ThemeManager.getInstance().getColorPrimary(this)

val Context.primaryColorDark: Int
    get() = ThemeManager.getInstance().getColorPrimaryDark(this)

fun ContextThemeWrapper.applyTheme() =  ThemeManager.getInstance().applyTheme(this)


val android.app.Fragment.accentColor: Int
    get() = activity.accentColor

val android.app.Fragment.primaryColor: Int
    get() = activity.primaryColor

val android.app.Fragment.primaryColorDark: Int
    get() = activity.primaryColorDark

fun android.app.Fragment.applyTheme() =  activity.applyTheme()


val Fragment.accentColor: Int?
    get() = context?.accentColor

val Fragment.primaryColor: Int?
    get() = context?.primaryColor

val Fragment.primaryColorDark: Int?
    get() = context?.primaryColorDark

fun Fragment.applyTheme() =  activity?.applyTheme()
//endregion