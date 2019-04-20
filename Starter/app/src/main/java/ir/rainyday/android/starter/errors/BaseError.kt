package ir.rainyday.android.starter.errors

import android.content.Context
import ir.rainyday.android.common.helpers.GlobalAppContext


abstract class BaseException(throwable: Throwable) : RuntimeException(throwable){
    abstract val localizedDescription: String?

    val context: Context by lazy {
        GlobalAppContext.instance.applicationContext!!
    }
}