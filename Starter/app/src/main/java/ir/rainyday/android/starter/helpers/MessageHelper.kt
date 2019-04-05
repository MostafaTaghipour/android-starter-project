@file:Suppress("DEPRECATION")

package ir.rainyday.android.starter.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.crashlytics.android.Crashlytics
import com.google.firebase.analytics.FirebaseAnalytics
import es.dmoral.toasty.Toasty
import ir.rainyday.android.common.helpers.getRootView


/**
 * log error in firebase by crashlytics
 *
 * @param  throwable  happened error
 */
fun logException(throwable: Throwable){
    Crashlytics.logException(throwable)
}

/**
 * log events in firebase by analytics
 *
 * @param  name  name of event
 * @param  parameters additional parameters
 */
fun logEvent(name: String, parameters: Bundle? = null){
    FirebaseAnalytics.getInstance(appContext).logEvent(name,parameters)
}

//region Styleable Toast
fun Context.errorToast(@NonNull message: CharSequence, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = false): Unit {
    Toasty.error(this, message, duration, withIcon).styling().show()
}

fun Context.successToast(@NonNull message: CharSequence, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = false): Unit {
    Toasty.success(this, message, duration, withIcon).styling().show()
}

fun Context.infoToast(@NonNull message: CharSequence, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = false): Unit {
    Toasty.info(this, message, duration, withIcon).styling().show()
}

fun Context.warningToast(@NonNull message: CharSequence, duration: Int = Toast.LENGTH_SHORT, withIcon: Boolean = false): Unit {
    Toasty.warning(this, message, duration, withIcon).styling().show()
}

private fun Toast.styling(): Toast {
    val textView = this.view.findViewById<TextView>(es.dmoral.toasty.R.id.toast_text)
    textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
    textView.typeface = Typeface.createFromAsset(appContext.assets, appFont.defaultFontPath)
    return this
}

//endregion

//region Snackbar
@JvmOverloads
inline fun View.snack(
        @StringRes messageRes: Int,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar {
    return snack(resources.getString(messageRes), length, backgroundColor, textColor, f)
}

@JvmOverloads
inline fun View.snack(
        message: String,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar {
    val snack = com.google.android.material.snackbar.Snackbar.make(this, message, length)

    backgroundColor?.let {
        val snackBarView = snack.view
        snackBarView.setBackgroundColor(it)
    }

    textColor?.let {
        val snackBarView = snack.view
        val textView = snackBarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.setTextColor(it)
    }

    snack.f()
    snack.show()
    return snack
}

@JvmOverloads
fun com.google.android.material.snackbar.Snackbar.action(@StringRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

@JvmOverloads
fun com.google.android.material.snackbar.Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(it) }
}

@JvmOverloads
inline fun Activity.snack(
        @StringRes messageRes: Int,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,

        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar {
    return snack(resources.getString(messageRes), length, backgroundColor, textColor, f)
}

@JvmOverloads
inline fun Activity.snack(
        message: String,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar {
    return getRootView().snack(message, length, backgroundColor, textColor, f)
}

@JvmOverloads
inline fun Fragment.snack(
        @StringRes messageRes: Int,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar? {
    return snack(resources.getString(messageRes), length, backgroundColor, textColor, f)
}

@JvmOverloads
inline fun Fragment.snack(
        message: String,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar? {
    return view?.snack(message, length, backgroundColor, textColor, f)
}

@Suppress("DEPRECATION")
@JvmOverloads
inline fun android.app.Fragment.snack(
        @StringRes messageRes: Int,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar? {
    return snack(resources.getString(messageRes), length, backgroundColor, textColor, f)
}

@JvmOverloads
inline fun android.app.Fragment.snack(
        message: String,
        length: Int = com.google.android.material.snackbar.Snackbar.LENGTH_LONG,
        backgroundColor: Int? = null,
        textColor: Int? = null,
        f: com.google.android.material.snackbar.Snackbar.() -> Unit
): com.google.android.material.snackbar.Snackbar? {
    return view?.snack(message, length, backgroundColor, textColor, f)
}
//endregion