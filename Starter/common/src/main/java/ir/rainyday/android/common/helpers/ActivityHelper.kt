package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 15/10/2017.
 */

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi


enum class ScreenOrientation(val value: Int) {
    UNDEFINED(0),
    PORTRAIT(1),
    LANDSCAPE(2),
    SQUARE(3);


    companion object {

        fun fromValue(value: Int): ScreenOrientation? {
            for (item in ScreenOrientation.values()) {
                if (item.value == value) {
                    return item
                }
            }
            return null
        }
    }

}

fun Activity.lockCurrentScreenOrientation() {
    requestedOrientation = when (resources.configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        else -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
    }
}

fun Activity.unlockScreenOrientation() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
}

var Activity.currentOrientation: ScreenOrientation?
    get() {
        return ScreenOrientation.fromValue(resources.configuration.orientation)
    }
    set(value) {
        value?.let { requestedOrientation = it.value }
    }


val Activity.isLandscape: Boolean
    get() {
        return this.currentOrientation === ScreenOrientation.LANDSCAPE
    }

val Activity.isPortrait: Boolean
    get() {
        return this.currentOrientation === ScreenOrientation.PORTRAIT
    }


fun Activity.dismissKeyboard() {
    KeyboardHelper.dismissKeyboard(this)
}

fun Activity.showKeyboard(focusView: View) {
    KeyboardHelper.showKeyboard(focusView)
}


fun Activity.setStatusBarColor(color: Int) {
    fromApi(Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = color
    }
}

fun Activity.setStatusBarVisibility(visible: Boolean) {
    if (visible) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}


fun Activity.setLightStatusBar(light: Boolean) {
    fromApi(Build.VERSION_CODES.M) {
        if (light) {
            var flags = window.decorView.systemUiVisibility // get current flag
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR   // add LIGHT_STATUS_BAR to flag
            window.decorView.systemUiVisibility = flags
        } else {
            var flags = window.decorView.systemUiVisibility /// get current flag
            flags = flags xor View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR // use XOR here for remove LIGHT_STATUS_BAR from flags
            window.decorView.systemUiVisibility = flags
        }
    }
}



fun Activity.setNavigationBarColor(color: Int) {
    fromApi(Build.VERSION_CODES.LOLLIPOP) {
        window.navigationBarColor = color
    }
}

fun Activity.setNavigationBarVisibility(visible: Boolean) {
    fromApi(Build.VERSION_CODES.JELLY_BEAN) {
        if (!visible) {
            var flags = window.decorView.systemUiVisibility // get current flag
            flags = (flags
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window.decorView.systemUiVisibility = flags
        } else {
            var flags = window.decorView.systemUiVisibility // get current flag
            flags = (flags
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
            window.decorView.systemUiVisibility = flags
        }
    }
}


fun Activity.makeFullScreen() {
    fromApi(Build.VERSION_CODES.LOLLIPOP) {
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}


fun Activity.applySoftInputAdjustResize(visibleByDefault: Boolean = false) {
    if (visibleByDefault) {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    } else {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED or WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    }


}

fun Activity.getRootView(includeActionBar: Boolean = false): ViewGroup {
    val content = findViewById<ViewGroup>(android.R.id.content)

    if (includeActionBar)
        return content

    return content.getChildAt(0) as ViewGroup
}

val Activity.toolbarHeight: Int
    get() {
        val styledAttributes = theme.obtainStyledAttributes(
                intArrayOf(android.R.attr.actionBarSize))
        val toolbarHeight = styledAttributes.getDimension(0, 0f).toInt()
        styledAttributes.recycle()
        return toolbarHeight
    }


val Activity.statusBarHeight: Int
    get() {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

val Activity.NavigationBarHeight: Int
    get() {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }


