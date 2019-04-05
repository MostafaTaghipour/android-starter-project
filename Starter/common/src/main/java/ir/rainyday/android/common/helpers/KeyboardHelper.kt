package ir.rainyday.android.common.helpers

import android.app.Activity
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import java.lang.ref.WeakReference

/**
 * Created by taghipour on 17/09/2017.
 */


class KeyboardHelper {

    interface KeyboardVisibilityListener {
        fun onKeyboardVisibilityChanged(isShown: Boolean)
    }


    var isKeyboardVisible = false
        private set
    private var weakListener: WeakReference<KeyboardVisibilityListener>? = null
    private var weakRootView: WeakReference<View>? = null
    private val keyboardLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

        private val DefaultKeyboardDP = 100
        // From @nathanielwolf answer...  Lollipop includes button bar in the root. Add height of button bar (48dp) to maxDiff
        private val EstimatedKeyboardDP = DefaultKeyboardDP + if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 48 else 0
        private val r = Rect()


        override fun onGlobalLayout() {

            val rootView = weakRootView?.get()
            val visibilityListener = weakListener?.get()


            if (rootView == null || visibilityListener == null)
                return

            // Convert the dp to pixels.
            val estimatedKeyboardHeight = TypedValue
                    .applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP.toFloat(), rootView.resources.displayMetrics).toInt()

            // Conclude whether the keyboard is shown or not.
            rootView.getWindowVisibleDisplayFrame(r)
            val heightDiff = rootView.rootView.height - (r.bottom - r.top)
            val isShown = heightDiff >= estimatedKeyboardHeight

            if (isShown == isKeyboardVisible) {
                Log.d("Keyboard state", "Ignoring global layout change...")
                return
            }

            isKeyboardVisible = isShown

            visibilityListener.onKeyboardVisibilityChanged(isShown)
        }
    }


    fun attachKeyboardListener(activity: Activity, listener: KeyboardVisibilityListener) {
        weakListener = WeakReference(listener)
        weakRootView = WeakReference(activity.getRootView())
        weakRootView?.get()?.viewTreeObserver?.addOnGlobalLayoutListener(keyboardLayoutListener)
    }

    fun detachKeyboardListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            weakRootView?.get()?.viewTreeObserver?.removeOnGlobalLayoutListener(keyboardLayoutListener)
        }
    }


    companion object {
        fun dismissKeyboard(activity: Activity) {
            val view = activity.getRootView()
                    activity.inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showKeyboard(focusView: View) {
            focusView.requestFocus()
            GlobalAppContext.instance.applicationContext?.inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }
}


