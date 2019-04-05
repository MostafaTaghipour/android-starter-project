package ir.rainyday.android.common.supers

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.TaskStackBuilder
import ir.rainyday.android.common.helpers.GlobalAppContext

interface SuperRouter {
    val applicationContext: Context
        get() = GlobalAppContext.instance.applicationContext!!


    fun restart(context: Context?=null, nextIntent: Intent?=null) {

        val ctx = context ?: applicationContext

        val launcherIntent = ctx.packageManager.getLaunchIntentForPackage(ctx.packageName)
        launcherIntent!!.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)

        var stack = TaskStackBuilder.create(ctx)
                .addNextIntent(launcherIntent)

        nextIntent?.let {
            stack = stack.addNextIntentWithParentStack(it)
        }

        (ctx as? Activity)?.finish()

        stack.startActivities()

    }

    fun logout()
}