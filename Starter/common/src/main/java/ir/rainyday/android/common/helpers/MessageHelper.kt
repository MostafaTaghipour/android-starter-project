@file:Suppress("DEPRECATION")

package ir.rainyday.android.common.helpers

import android.app.Fragment
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.database.Cursor
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment as SupportFragment


/**
 * Created by taghipour on 15/10/2017.
 */


//region Toast
@JvmOverloads
fun Context.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT, textGravity: Int=Gravity.CENTER): Unit {
    val toast = Toast.makeText(this, text, duration)
    val textView = (toast.view as ViewGroup).getChildAt(0) as TextView
    textView.gravity = textGravity
    toast.show()
}

fun Context.longToast(text: CharSequence): Unit = this.toast(text, Toast.LENGTH_LONG, Gravity.CENTER)

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Unit = this.toast(this.getString(resId), duration, Gravity.CENTER)

fun Context.longToast(@StringRes resId: Int): Unit = this.longToast(this.getString(resId))

fun Fragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = activity.toast(text, duration, Gravity.CENTER)

fun Fragment.longToast(text: CharSequence) = activity.longToast(text)

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT): Unit = activity.toast(resId, duration)

fun Fragment.longToast(@StringRes resId: Int): Unit = activity.longToast(resId)

fun SupportFragment.toast(text: CharSequence, duration: Int = Toast.LENGTH_SHORT) = activity?.toast(text, duration, Gravity.CENTER)

fun SupportFragment.longToast(text: CharSequence) = activity?.longToast(text)

fun SupportFragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) = activity?.toast(resId, duration)

fun SupportFragment.longToast(@StringRes resId: Int) = activity?.longToast(resId)
//endregion

//region Log
fun Any.v(msg: () -> String) {
    if (Log.isLoggable(tag, Log.VERBOSE)) v(msg())
}

fun Any.d(msg: () -> String) {
    if (Log.isLoggable(tag, Log.DEBUG)) d(msg())
}

fun Any.i(msg: () -> String) {
    if (Log.isLoggable(tag, Log.INFO)) i(msg())
}

fun Any.e(msg: () -> String) {
    if (Log.isLoggable(tag, Log.ERROR)) e(msg())
}

fun Any.wtf(msg: () -> String) = w(msg())

fun Any.v(msg: String) = v(tag, msg)

fun Any.d(msg: String) = d(tag, msg)

fun Any.i(msg: String) = i(tag, msg)

fun Any.w(msg: String) = w(tag, msg)

fun Any.e(msg: String) = e(tag, msg)

fun Any.wtf(msg: String) = wtf(tag, msg)

fun v(tag: String, msg: String) = Log.v(tag, msg)

fun d(tag: String, msg: String) = Log.d(tag, msg)

fun i(tag: String, msg: String) = Log.i(tag, msg)

fun w(tag: String, msg: String) = Log.w(tag, msg)

fun e(tag: String, msg: String) = Log.e(tag, msg)

fun wtf(tag: String, msg: String) = Log.wtf(tag, msg)

private val Any.tag: String
    get() = javaClass.simpleName

fun log(msg: String, tag: String = "TAG") {
    if (CurrentApp.isDebugMode)
        Log.d(tag, msg)
}

fun log(msg: Any, tag: String = "TAG") {
    if (CurrentApp.isDebugMode)
        Log.d(tag, msg.toString())
}
//endregion

//region Alert
fun Fragment.alert(
        message: String,
        title: String? = null,
        init: (AlertDialogBuilder.() -> Unit)? = null
) = activity.alert(message, title, init)

fun Context.alert(
        message: String,
        title: String? = null,
        init: (AlertDialogBuilder.() -> Unit)? = null
) = AlertDialogBuilder(this).apply {
    if (title != null) title(title)
    message(message)
    if (init != null) init()
}

fun Fragment.alert(
        @StringRes message: Int,
        @StringRes title: Int? = null,
        init: (AlertDialogBuilder.() -> Unit)? = null
) = activity.alert(message, title, init)

fun Context.alert(
        @StringRes message: Int,
        @StringRes title: Int? = null,
        init: (AlertDialogBuilder.() -> Unit)? = null
) = AlertDialogBuilder(this).apply {
    if (title != null) title(title)
    message(message)
    if (init != null) init()
}

fun Fragment.alert(init: AlertDialogBuilder.() -> Unit): AlertDialogBuilder = activity.alert(init)

fun Context.alert(init: AlertDialogBuilder.() -> Unit) = AlertDialogBuilder(this).apply { init() }

fun Fragment.progressDialog(
        @StringRes message: Int? = null,
        @StringRes title: Int? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = activity.progressDialog(message, title, init)

fun Context.progressDialog(
        @StringRes message: Int? = null,
        @StringRes title: Int? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = progressDialog(false, message?.let { getString(it) }, title?.let { getString(it) }, init)

fun Fragment.indeterminateProgressDialog(
        @StringRes message: Int? = null,
        @StringRes title: Int? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = activity.progressDialog(message, title, init)

fun Context.indeterminateProgressDialog(
        @StringRes message: Int? = null,
        @StringRes title: Int? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = progressDialog(true, message?.let { getString(it) }, title?.let { getString(it) }, init)

fun Fragment.progressDialog(
        message: String? = null,
        title: String? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = activity.progressDialog(message, title, init)

fun Context.progressDialog(
        message: String? = null,
        title: String? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = progressDialog(false, message, title, init)

fun Fragment.indeterminateProgressDialog(
        message: String? = null,
        title: String? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = activity.indeterminateProgressDialog(message, title, init)

fun Context.indeterminateProgressDialog(
        message: String? = null,
        title: String? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = progressDialog(true, message, title, init)

private fun Context.progressDialog(
        indeterminate: Boolean,
        message: String? = null,
        title: String? = null,
        init: (ProgressDialog.() -> Unit)? = null
) = ProgressDialog(this).apply {
    isIndeterminate = indeterminate
    if (!indeterminate) setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    if (message != null) setMessage(message)
    if (title != null) setTitle(title)
    if (init != null) init()
    show()
}

fun Fragment.selector(
        title: CharSequence? = null,
        items: List<CharSequence>,
        onClick: (Int) -> Unit
) = activity.selector(title, items, onClick)

fun Context.selector(
        title: CharSequence? = null,
        items: List<CharSequence>,
        onClick: (Int) -> Unit
) {
    with(AlertDialogBuilder(this)) {
        if (title != null) title(title)
        items(items, onClick)
        show()
    }
}

class AlertDialogBuilder(val ctx: Context) {

    val builder: AlertDialog.Builder = AlertDialog.Builder(ctx)
    var dialog: AlertDialog? = null

    fun dismiss() = dialog?.dismiss()

    fun show(): AlertDialogBuilder {
        dialog = builder.create()
        dialog!!.show()
        return this
    }

    fun title(title: CharSequence) {
        builder.setTitle(title)
    }

    fun title(@StringRes resource: Int) {
        builder.setTitle(resource)
    }

    fun message(title: CharSequence) {
        builder.setMessage(title)
    }

    fun message(@StringRes resource: Int) {
        builder.setMessage(resource)
    }

    fun icon(@DrawableRes icon: Int) {
        builder.setIcon(icon)
    }

    fun icon(icon: Drawable) {
        builder.setIcon(icon)
    }

    fun customTitle(title: View) {
        builder.setCustomTitle(title)
    }

    fun customView(view: View) {
        builder.setView(view)
    }

    fun cancellable(value: Boolean = true) {
        builder.setCancelable(value)
    }

    fun onCancel(f: () -> Unit) {
        builder.setOnCancelListener { f() }
    }

    fun onKey(f: (keyCode: Int, e: KeyEvent) -> Boolean) {
        builder.setOnKeyListener({ _, keyCode, event -> f(keyCode, event) })
    }

    fun neutralButton(@StringRes textResource: Int = android.R.string.ok, f: DialogInterface.() -> Unit = { dismiss() }) {
        neutralButton(ctx.getString(textResource), f)
    }

    fun neutralButton(title: String, f: DialogInterface.() -> Unit = { dismiss() }) {
        builder.setNeutralButton(title, { dialog, _ -> dialog.f() })
    }

    fun positiveButton(@StringRes textResource: Int = android.R.string.ok, f: DialogInterface.() -> Unit) {
        positiveButton(ctx.getString(textResource), f)
    }

    fun positiveButton(title: String, f: DialogInterface.() -> Unit) {
        builder.setPositiveButton(title, { dialog, _ -> dialog.f() })
    }

    fun negativeButton(@StringRes textResource: Int = android.R.string.cancel, f: DialogInterface.() -> Unit = { dismiss() }) {
        negativeButton(ctx.getString(textResource), f)
    }

    fun negativeButton(title: String, f: DialogInterface.() -> Unit = { dismiss() }) {
        builder.setNegativeButton(title, { dialog, _ -> dialog.f() })
    }

    fun items(@ArrayRes itemsId: Int, f: (which: Int) -> Unit) {
        items(ctx.resources!!.getTextArray(itemsId), f)
    }

    fun items(items: List<CharSequence>, f: (which: Int) -> Unit) {
        items(items.toTypedArray(), f)
    }

    fun items(items: Array<CharSequence>, f: (which: Int) -> Unit) {
        builder.setItems(items, { _, which -> f(which) })
    }

    fun adapter(adapter: ListAdapter, f: (which: Int) -> Unit) {
        builder.setAdapter(adapter, { _, which -> f(which) })
    }

    fun adapter(cursor: Cursor, labelColumn: String, f: (which: Int) -> Unit) {
        builder.setCursor(cursor, { _, which -> f(which) }, labelColumn)
    }
}
//endregion