package ir.rainyday.android.common.supers

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import ir.rainyday.android.common.helpers.GlobalAppContext

open class SuperApplication : Application(), LifecycleDelegate {

    override fun onCreate() {
        super.onCreate()

        val lifeCycleHandler = AppLifecycleHandler(this)
        registerLifecycleHandler(lifeCycleHandler)

        val context = init()

        GlobalAppContext.instance.initialize(this,context)
    }

    open fun init(): Context {
        return applicationContext
    }

    override fun onAppBackgrounded() {

    }

    override fun onAppForegrounded() {

    }

    private fun registerLifecycleHandler(lifeCycleHandler: AppLifecycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler)
        registerComponentCallbacks(lifeCycleHandler)
    }

}


//region App Lifecycle
interface LifecycleDelegate {
    fun onAppBackgrounded()
    fun onAppForegrounded()
}

class AppLifecycleHandler(private val lifecycleDelegate: LifecycleDelegate)
    : Application.ActivityLifecycleCallbacks, ComponentCallbacks2 // <-- Implement these
{

    private var appInForeground = false


    // Override from Application.ActivityLifecycleCallbacks
    override fun onActivityResumed(p0: Activity?) {
        if (!appInForeground) {
            appInForeground = true
            lifecycleDelegate.onAppForegrounded()
        }
    }

    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }

    override fun onLowMemory() {

    }

    override fun onConfigurationChanged(newConfig: Configuration?) {

    }


    // Override from ComponentCallbacks2
    override fun onTrimMemory(level: Int) {
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            appInForeground = false
            lifecycleDelegate.onAppBackgrounded()
        }
    }

}
//endregion