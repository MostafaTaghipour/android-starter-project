package ir.rainyday.android.common.helpers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.ArrayMap


/**
 * Created by taghipour on 15/10/2017.
 */
object CurrentApp {
    val versionCode: Int?
        get() {
            GlobalAppContext.instance.applicationContext?.let {
                return getBuildConfigValue( it,"VERSION_CODE") as? Int
            }
           return null
        }
    val versionName: String?
        get() {
            GlobalAppContext.instance.applicationContext?.let {
                return getBuildConfigValue( it,"VERSION_NAME") as? String
            }
            return null
        }
    val Id: String?
    get() {
        GlobalAppContext.instance.applicationContext?.let {
            return getBuildConfigValue( it,"APPLICATION_ID") as? String
        }
        return null
    }
    val packageName: String? = GlobalAppContext.instance.applicationContext?.packageName

    val name: String?
        get() {
            GlobalAppContext.instance.applicationContext?.let {
                return  it.applicationInfo.loadLabel(it.packageManager).toString()
            }
            return null
        }

    val isDebugMode: Boolean
        get() {
            GlobalAppContext.instance.applicationContext?.let {
                return  (getBuildConfigValue( it,"DEBUG") as? Boolean) ?: true
            }
            return true
        }

    val currentActivity: Activity?
        @SuppressLint("PrivateApi")
        get() {
            val activityThreadClass = Class.forName("android.app.ActivityThread")
            val activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null)
            val activitiesField = activityThreadClass.getDeclaredField("mActivities")
            activitiesField.isAccessible = true
            val activities = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) activitiesField.get(activityThread) as HashMap<*, *> else activitiesField.get(activityThread) as ArrayMap<*, *>
            for (activityRecord in activities.values) {
                val activityRecordClass = activityRecord.javaClass
                val pausedField = activityRecordClass.getDeclaredField("paused")
                pausedField.isAccessible = true
                if (!pausedField.getBoolean(activityRecord)) {
                    val activityField = activityRecordClass.getDeclaredField("activity")
                    activityField.isAccessible = true
                    return activityField.get(activityRecord) as? Activity
                }
            }
            return null
        }


    /**
     * Gets a field from the project's BuildConfig. This is useful when, for example, flavors
     * are used at the project level to set custom fields.
     * @param context       Used to find the correct file
     * @param fieldName     The name of the field-to-access
     * @return              The value of the field, or `null` if the field is not found.
     */
    private fun getBuildConfigValue(context: Context, fieldName: String): Any? {
        try {
            val clazz = Class.forName(context.packageName + ".BuildConfig")
            val field = clazz.getField(fieldName)
            return field.get(null)
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

        return null
    }
}

fun Context.isAppAvailable(packageName:String) : Boolean{
    return try {
        packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
        true
    } catch (e: Exception) {
        false
    }

}

class GlobalAppContext {
    var applicationContext: Context? = null
        private set

    var application: Application? = null
        private set

    internal fun initialize(application: Application, context: Context?) {
        this.application = application
        this.applicationContext = context
    }

    companion object {
        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            GlobalAppContext()
        }
    }
}


