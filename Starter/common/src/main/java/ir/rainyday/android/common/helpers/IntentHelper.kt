@file:Suppress("DEPRECATION")

package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 15/10/2017.
 */
import android.Manifest
import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment as SupportFragment
import android.content.ComponentName
import android.provider.Telephony
import android.os.Build
import androidx.annotation.RequiresApi


inline fun <reified T : Parcelable> Bundle.putParcelableCollection(key: String, value: Collection<T>) = putParcelableArray(key, value.toTypedArray())

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Bundle.getParcelableMutableList(key: String): MutableList<T> = (getParcelableArray(key) as Array<T>).toMutableList()

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Parcelable> Bundle.getParcelableMutableSet(key: String): MutableSet<T> = (getParcelableArray(key) as Array<T>).toMutableSet()


inline fun <reified T : Any> Context.startActivity(extras: Bundle? = null) {
    val intent = IntentFor<T>(this)
    extras?.let {
        intent.putExtras(extras)
    }
    startActivity(intent)
}

@RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
inline fun <reified T : Any> Activity.startActivityForResult(requestCode: Int, extras: Bundle? = null, options: Bundle? = null, action: String? = null) {
    val intent = IntentFor<T>(this)
    intent.action = action
    extras?.let {
        intent.putExtras(extras)
    }
    startActivityForResult(intent, requestCode, options)
}


inline fun <reified T : Any> IntentFor(context: Context): Intent = Intent(context, T::class.java)

fun Intent.start(context: Context) = context.startActivity(this)

fun Intent.startForResult(activity: Activity, requestCode: Int) = activity.startActivityForResult(this, requestCode)

fun Intent.startForResult(fragment: Fragment, requestCode: Int) = fragment.startActivityForResult(this, requestCode)


fun Context.launchUrl(url: String) {
    if (url.isEmpty())
        return
    this.launchUrl(Uri.parse(url))
}


fun Context.launchUrl(url: Uri?) {

    if (url == null)
        return

    val i = Intent(Intent.ACTION_VIEW)
    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    i.data = url
    this.startActivity(i)
}


@RequiresPermission(Manifest.permission.CALL_PHONE)
fun Context.call(phoneNumber: Any?) {
    if (phoneNumber != null) {
        val num = phoneNumber.toString()
        if (num != "") {
            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:" + num)
            startActivity(intent)
        }
    }
}

fun Context.dial(phoneNumber: Any?) {
    if (phoneNumber != null) {
        val num = phoneNumber.toString()
        if (num != "") {

            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}

fun Context.launchInternetSetting() {
    try {
        startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
    } catch (ex: android.content.ActivityNotFoundException) {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings")
        startActivity(intent)
    }

}

fun Context.launchAppMarket(packageName: String = this.packageName) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)))
    } catch (ex: android.content.ActivityNotFoundException) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("bazaar://details?id=" + packageName)
            intent.`package` = "com.farsitel.bazaar"
            startActivity(intent)
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)))
        }

    }

}

fun Context.launchApp(appPackageName: String, openMarketIfNotFound: Boolean = true) {

    if (appPackageName.isNullOrEmpty())
        return

    val intent = packageManager.getLaunchIntentForPackage(appPackageName)
    if (intent != null) {
        // We found the activity now start the activity
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    } else if (openMarketIfNotFound) {
        launchAppMarket(packageName)
    }
}


@Throws
fun Context.share(text: CharSequence?, subject: CharSequence? = null, imageUri: Uri? = null, chooserTitle: CharSequence = "Share via ...") {
    // Intent to share info
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND


    // Image
    if (imageUri != null) {
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.type = "image/*"
    } else {
        shareIntent.type = "text/html"
    }

    text?.let { shareIntent.putExtra(Intent.EXTRA_TEXT, text) }
    subject?.let { shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject) }


    shareIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

    startActivity(Intent.createChooser(shareIntent, chooserTitle))
}


@Throws
fun Context.shareVia(packageName: CharSequence, text: CharSequence?, subject: CharSequence? = null, imageUri: Uri? = null) {
    // Intent to share info
    val shareIntent = Intent()
    shareIntent.action = Intent.ACTION_SEND

    // Image
    if (imageUri != null) {
        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
        shareIntent.type = "image/*"
    } else {
        shareIntent.type = "text/plain"
    }

    text?.let { shareIntent.putExtra(Intent.EXTRA_TEXT, text) }
    subject?.let { shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject) }


    val pm = packageManager
    val activityList = pm.queryIntentActivities(shareIntent, 0)
    for (app in activityList) {
        if (app.activityInfo.packageName.contains(packageName)) {
            val activityInfo = app.activityInfo
            val name = ComponentName(activityInfo.applicationInfo.packageName, activityInfo.name)
            shareIntent.addCategory(Intent.CATEGORY_LAUNCHER)
            shareIntent.component = name
            startActivity(shareIntent)
            break
        }
    }
}

@Throws
fun Context.sendSMS(text: CharSequence, to: Array<String>? = null) {
    if ((to == null || to.isEmpty()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        val defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this) // Need to change the build to API 19

        val sendIntent = Intent(Intent.ACTION_SEND)
        sendIntent.type = "text/plain"
        sendIntent.putExtra(Intent.EXTRA_TEXT, text)


        if (defaultSmsPackageName != null)
        // Can be null in case that there is no default, then the user would be able to choose
        // any app that support this intent.
        {
            sendIntent.`package` = defaultSmsPackageName
        }
        startActivity(sendIntent)
    } else {
        var toStr = ""
        if (to != null && to.isNotEmpty()) {
            toStr = to.joinToString()
        }

        val uri = Uri.parse("smsto:${toStr}")
        val it = Intent(Intent.ACTION_SENDTO, uri)
        it.putExtra("sms_body", text)
        startActivity(it)
    }
}

@Throws
fun Context.sendMail(subject: CharSequence, body: CharSequence, imageUri: Uri? = null, to: Array<String>? = null) {
    val intent = Intent(Intent.ACTION_SENDTO)

    if (imageUri != null) {
        intent.putExtra(Intent.EXTRA_STREAM, imageUri)
        intent.type = "image/*"
    } else {
        intent.type = "text/html"
    }

    var toStr = ""
    if (to != null && to.isNotEmpty()) {
        toStr = to.joinToString()
    }

    intent.data = Uri.parse("mailto:${toStr}")

    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
    intent.putExtra(Intent.EXTRA_TEXT, body)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

    startActivity(intent)
}

