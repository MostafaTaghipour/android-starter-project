package ir.rainyday.android.starter.net

import ir.rainyday.android.starter.BuildConfig
import ir.rainyday.android.starter.helpers.BuildConfigEx
import ir.rainyday.android.starter.helpers.ProductFlavor


object URLs {
     val baseUrl: String
    get() {
        return when(BuildConfigEx.productFlavor){
            ProductFlavor.DEVELOPMENT -> "https://url.dev"
            ProductFlavor.STAGING -> "https://url.stage"
            else -> "https://url.com"
        }
    }


    const val TIME_OUT: Long = 40 
}

