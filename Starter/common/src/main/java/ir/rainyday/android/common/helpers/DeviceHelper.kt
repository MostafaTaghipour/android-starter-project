@file:Suppress("DEPRECATED_IDENTITY_EQUALS", "NON_EXHAUSTIVE_WHEN","ObsoleteSdkInt", "DEPRECATION")


package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 15/10/2017.
 */
import android.annotation.SuppressLint
import android.app.Fragment
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager


enum class DeviceType {
    SMART_PHONE,
    TABLET
}

enum class DeviceSize {
    UNKNOWN,
    SMALL,
    NORMAL,
    LARGE,
    X_LARGE
}

enum class DisplayDensity {
    UNKNOWN,
    LOW,
    MEDIUM,
    HIGH,
    X_HIGH,
    XX_HIGH,
    XXX_HIGH
}

@SuppressLint("ObsoleteSdkInt")
object CurrentDevice {

    object Metrics {
        val density: Float
            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                return context.resources.displayMetrics.density
            }

        val scaledDensity: Float
            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                return context.resources.displayMetrics.scaledDensity
            }


        fun dpToPX(dp: Float): Int {
            return (dp * density).toInt()
        }

        fun pxToDP(px: Int): Float {
            return px / density
        }

        fun spToPX(sp: Float): Int {
            return (sp * scaledDensity).toInt()
        }

        fun pxToSP(px: Int): Float {
            return px / scaledDensity
        }

        fun dpToSP(dp: Float): Float {
            return pxToSP(dpToPX(dp))
        }

        fun spToDP(sp: Float): Float {
            return pxToDP(spToPX(sp))
        }


        val displayDensity: DisplayDensity
            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                var density = DisplayDensity.UNKNOWN

                val d = context.getResources().getDisplayMetrics().densityDpi

                when (d) {
                    DisplayMetrics.DENSITY_LOW -> density = DisplayDensity.LOW
                    DisplayMetrics.DENSITY_MEDIUM -> density = DisplayDensity.MEDIUM
                    DisplayMetrics.DENSITY_HIGH -> density = DisplayDensity.HIGH
                    DisplayMetrics.DENSITY_XHIGH -> density = DisplayDensity.X_HIGH
                    DisplayMetrics.DENSITY_XXHIGH -> density = DisplayDensity.XX_HIGH
                    DisplayMetrics.DENSITY_XXXHIGH -> density = DisplayDensity.XXX_HIGH
                }
                return density
            }


        val screenSize: DeviceSize
            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                var size = DeviceSize.UNKNOWN
                if (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_XLARGE) {
                    size = DeviceSize.X_LARGE
                } else if (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE) {
                    size = DeviceSize.LARGE
                } else if (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_NORMAL) {
                    size = DeviceSize.NORMAL
                } else if (context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_SMALL) {
                    size = DeviceSize.SMALL
                }

                return size
            }


        val screenWidth: Int



            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

                val measuredWidth: Int
                measuredWidth = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    val size = Point()
                    w.defaultDisplay.getSize(size)
                    size.x
                } else {
                    val d = w.defaultDisplay
                    d.width
                }

                return measuredWidth
            }

        val screenHeight: Int
            get() {
                val context = GlobalAppContext.instance.applicationContext!!
                val w = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager


                val measuredHeight: Int

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                    val size = Point()
                    w.defaultDisplay.getSize(size)
                    measuredHeight = size.y
                } else {
                    val d = w.defaultDisplay
                    measuredHeight = d.height
                }

                return measuredHeight
            }

        val screenWidthInDp: Float
            get() = pxToDP(screenWidth)

        val screenHeightInDp: Float
            get() = pxToDP(screenHeight)


        fun isDisplayDensitySmallerThan(density: DisplayDensity): Boolean {
            val displayDensity = displayDensity
            when (density) {
                DisplayDensity.XXX_HIGH -> return displayDensity == DisplayDensity.MEDIUM || displayDensity == DisplayDensity.HIGH || displayDensity == DisplayDensity.X_HIGH || displayDensity == DisplayDensity.XX_HIGH || displayDensity == DisplayDensity.LOW
                DisplayDensity.XX_HIGH -> return displayDensity == DisplayDensity.HIGH || displayDensity == DisplayDensity.X_HIGH || displayDensity == DisplayDensity.MEDIUM || displayDensity == DisplayDensity.LOW
                DisplayDensity.X_HIGH -> return displayDensity == DisplayDensity.LOW || displayDensity == DisplayDensity.MEDIUM || displayDensity == DisplayDensity.HIGH
                DisplayDensity.HIGH -> return displayDensity == DisplayDensity.MEDIUM || displayDensity == DisplayDensity.LOW
                DisplayDensity.MEDIUM -> return displayDensity == DisplayDensity.LOW
            }

            return false
        }

        fun isDisplayDensityBiggerThan(density: DisplayDensity): Boolean {
            val displayDensity = displayDensity
            when (density) {
                DisplayDensity.LOW -> return displayDensity == DisplayDensity.MEDIUM || displayDensity == DisplayDensity.HIGH || displayDensity == DisplayDensity.X_HIGH || displayDensity == DisplayDensity.XX_HIGH || displayDensity == DisplayDensity.XXX_HIGH
                DisplayDensity.MEDIUM -> return displayDensity == DisplayDensity.HIGH || displayDensity == DisplayDensity.X_HIGH || displayDensity == DisplayDensity.XX_HIGH || displayDensity == DisplayDensity.XXX_HIGH
                DisplayDensity.HIGH -> return displayDensity == DisplayDensity.X_HIGH || displayDensity == DisplayDensity.XX_HIGH || displayDensity == DisplayDensity.XXX_HIGH
                DisplayDensity.X_HIGH -> return displayDensity == DisplayDensity.XX_HIGH || displayDensity == DisplayDensity.XXX_HIGH
                DisplayDensity.XX_HIGH -> return displayDensity == DisplayDensity.XXX_HIGH
            }

            return false
        }

        fun isSizeSmallerThan(size: DeviceSize): Boolean {
            val screenSize = screenSize
            when (size) {
                DeviceSize.X_LARGE -> return screenSize == DeviceSize.NORMAL || screenSize == DeviceSize.LARGE || screenSize == DeviceSize.SMALL
                DeviceSize.LARGE -> return screenSize == DeviceSize.NORMAL || screenSize == DeviceSize.SMALL
                DeviceSize.NORMAL -> return screenSize == DeviceSize.SMALL
            }

            return false
        }

        fun isSizeBiggerThan(size: DeviceSize): Boolean {

            val screenSize = screenSize
            when (size) {
                DeviceSize.SMALL -> return screenSize == DeviceSize.NORMAL || screenSize == DeviceSize.LARGE || screenSize == DeviceSize.X_LARGE
                DeviceSize.NORMAL -> return screenSize == DeviceSize.LARGE || screenSize == DeviceSize.X_LARGE
                DeviceSize.LARGE -> return screenSize == DeviceSize.X_LARGE
            }

            return false
        }

    }


    object OS {

        val platformVersion: String
            get() = Build.VERSION.RELEASE

        val platformApiVersion: Int
            get() = Build.VERSION.SDK_INT

        val isPreLollipop: Boolean
            get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP

        val isLollipopAndAbove: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP


        val supportRTL: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1


        val supportTransition: Boolean
            get() = !isPreLollipop


        val supportRuntimePermission: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M


        val supportVectorDrawable: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP


        val supportAsyncTask: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB


        val supportAutoMirrored: Boolean
            get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT


        val supportBroadcastReceivers: Boolean
            get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.N


    }


    val type: DeviceType
        get() {
            val context = GlobalAppContext.instance.applicationContext!!
            return if (context.getResources().getConfiguration().screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE)
                DeviceType.TABLET
            else
                DeviceType.SMART_PHONE
        }

    val isTablet: Boolean
        get() = type == DeviceType.TABLET

    val isSmartPhone: Boolean
        get() = type == DeviceType.SMART_PHONE


    val isEmulator: Boolean
        get() {
            var result = (Build.FINGERPRINT.startsWith("generic")
                    || Build.FINGERPRINT.startsWith("unknown")
                    || Build.MODEL.contains("google_sdk")
                    || Build.MODEL.contains("Emulator")
                    || Build.MODEL.contains("Android SDK built for x86"))
            if (result)
                return true
            result = result or (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
            if (result)
                return true
            result = result or ("google_sdk" == Build.PRODUCT)
            return result
        }

    val isRealDevice: Boolean
        get() = !isEmulator


    val model: String
        get() {
            val manufacturer = Build.MANUFACTURER
            val model = Build.MODEL
            return if (model.startsWith(manufacturer)) {
                model.capitalize()
            } else {
                manufacturer.capitalize() + " " + model
            }
        }

    val deviceID: String
        @SuppressLint("HardwareIds")
        get() = Settings.Secure.getString(GlobalAppContext.instance.applicationContext!!.getContentResolver(),
                Settings.Secure.ANDROID_ID)


    val userAgent: String?
        get() = System.getProperty("http.agent")

    val hasCamera: Boolean
        get() {
            val context = GlobalAppContext.instance.applicationContext!!

            return context.packageManager.hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)
        }
}


/********************* Extensions *************************/
inline fun toApi(toVersion: Int, inclusive: Boolean = false, action: () -> Unit) {
    if (Build.VERSION.SDK_INT < toVersion || (inclusive && Build.VERSION.SDK_INT == toVersion)) action()
}

inline fun fromApi(fromVersion: Int, inclusive: Boolean = true, action: () -> Unit) {
    if (Build.VERSION.SDK_INT > fromVersion || (inclusive && Build.VERSION.SDK_INT == fromVersion)) action()
}

inline val Context.displayWidth
    get() = resources.displayMetrics.widthPixels

inline val Context.displayHeight
    get() = resources.displayMetrics.heightPixels

 fun Context.dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()

 fun Context.sp(value: Int): Int = (value * resources.displayMetrics.scaledDensity).toInt()

 fun Fragment.dp(value: Int): Int = activity.dp(value)

 fun Fragment.sp(value: Int): Int = activity.sp(value)

 fun androidx.fragment.app.Fragment.dp(value: Int): Int = activity?.dp(value) ?: 0

 fun androidx.fragment.app.Fragment.sp(value: Int): Int = activity?.sp(value) ?: 0

 fun View.dp(value: Int): Int = context.dp(value)

 fun View.sp(value: Int): Int = context.sp(value)

val Int.dp: Int
    get() = CurrentDevice.Metrics.dpToPX(this.toFloat())

val Int.sp: Int
    get() = CurrentDevice.Metrics.spToPX(this.toFloat())

