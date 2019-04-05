@file:Suppress("DEPRECATION")

package ir.rainyday.android.common.helpers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.telephony.TelephonyManager


/**
 * Created by m_taghipour on 5/12/2016.
 */


enum class ConnectivityType {
    WIFI,
    MOBILE,
    NOT_CONNECTED,
    UNKNOWN
}

enum class ConnectivitySpeed {
    FAST,
    MEDIUM,
    SLOW,
    NOT_CONNECTED,
    UNKNOWN
}

enum class NetworkType {
    WIFI,
    FOUR_G,
    THREE_G,
    TWO_G,
    NOT_CONNECTED,
    UNKNOWN
}



object NetworkHelper {

    val isConnectedToInternet: Boolean
        get() {
            val status = connectivityStatus
            return status != ConnectivityType.NOT_CONNECTED
        }


    val connectivityStatus: ConnectivityType
        @SuppressLint("MissingPermission")
        get() {
            val context = GlobalAppContext.instance.applicationContext

            if (context == null || !context.isPermissionGranted(android.Manifest.permission.ACCESS_NETWORK_STATE))
                return ConnectivityType.UNKNOWN

            val cm = context.connectivityManager


            val activeNetwork = cm?.activeNetworkInfo
            if (null != activeNetwork) {
                if (activeNetwork.type == ConnectivityManager.TYPE_WIFI)
                    return ConnectivityType.WIFI

                if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE)
                    return ConnectivityType.MOBILE
            }
            return ConnectivityType.NOT_CONNECTED
        }


    val networkType: NetworkType
        get() {
            val context = GlobalAppContext.instance.applicationContext!!
            val conn = connectivityStatus
            if (conn == ConnectivityType.NOT_CONNECTED)
                return NetworkType.NOT_CONNECTED

            if (conn == ConnectivityType.WIFI)
                return NetworkType.WIFI

            val mTelephonyManager = context.telephonyManager
            val networkType = mTelephonyManager?.networkType
            when (networkType) {
                TelephonyManager.NETWORK_TYPE_GPRS, TelephonyManager.NETWORK_TYPE_EDGE, TelephonyManager.NETWORK_TYPE_CDMA, TelephonyManager.NETWORK_TYPE_1xRTT, TelephonyManager.NETWORK_TYPE_IDEN -> return NetworkType.TWO_G
                TelephonyManager.NETWORK_TYPE_UMTS, TelephonyManager.NETWORK_TYPE_EVDO_0, TelephonyManager.NETWORK_TYPE_EVDO_A, TelephonyManager.NETWORK_TYPE_HSDPA, TelephonyManager.NETWORK_TYPE_HSUPA, TelephonyManager.NETWORK_TYPE_HSPA, TelephonyManager.NETWORK_TYPE_EVDO_B, TelephonyManager.NETWORK_TYPE_EHRPD, TelephonyManager.NETWORK_TYPE_HSPAP -> return NetworkType.THREE_G
                TelephonyManager.NETWORK_TYPE_LTE -> return NetworkType.FOUR_G
                else -> return NetworkType.UNKNOWN
            }
        }

    val connectivitySpeed: ConnectivitySpeed
        get() {
            val context = GlobalAppContext.instance.applicationContext!!
            val type = connectivityStatus

            if (type == ConnectivityType.WIFI) {
                return ConnectivitySpeed.FAST
            } else if (type == ConnectivityType.MOBILE) {
                val mTelephonyManager = context.telephonyManager
                val subType = mTelephonyManager?.networkType


                when (subType) {
                    TelephonyManager.NETWORK_TYPE_1xRTT -> return ConnectivitySpeed.SLOW // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_CDMA -> return ConnectivitySpeed.SLOW // ~ 14-64 kbps
                    TelephonyManager.NETWORK_TYPE_EDGE -> return ConnectivitySpeed.SLOW // ~ 50-100 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_0 -> return ConnectivitySpeed.MEDIUM // ~ 400-1000 kbps
                    TelephonyManager.NETWORK_TYPE_EVDO_A -> return ConnectivitySpeed.MEDIUM // ~ 600-1400 kbps
                    TelephonyManager.NETWORK_TYPE_GPRS -> return ConnectivitySpeed.SLOW // ~ 100 kbps
                    TelephonyManager.NETWORK_TYPE_HSDPA -> return ConnectivitySpeed.FAST // ~ 2-14 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPA -> return ConnectivitySpeed.MEDIUM // ~ 700-1700 kbps
                    TelephonyManager.NETWORK_TYPE_HSUPA -> return ConnectivitySpeed.FAST // ~ 1-23 Mbps
                    TelephonyManager.NETWORK_TYPE_UMTS -> return ConnectivitySpeed.FAST // ~ 400-7000 kbps
                /*
                 * Above API level 7, make sure to setValue android:targetSdkVersion
                 * to appropriate level to use these
                 */
                    TelephonyManager.NETWORK_TYPE_EHRPD // API level 11
                    -> return ConnectivitySpeed.FAST // ~ 1-2 Mbps
                    TelephonyManager.NETWORK_TYPE_EVDO_B // API level 9
                    -> return ConnectivitySpeed.FAST // ~ 5 Mbps
                    TelephonyManager.NETWORK_TYPE_HSPAP // API level 13
                    -> return ConnectivitySpeed.FAST // ~ 10-20 Mbps
                    TelephonyManager.NETWORK_TYPE_IDEN // API level 8
                    -> return ConnectivitySpeed.SLOW // ~25 kbps
                    TelephonyManager.NETWORK_TYPE_LTE // API level 11
                    -> return ConnectivitySpeed.FAST // ~ 10+ Mbps
                // Unknown
                    TelephonyManager.NETWORK_TYPE_UNKNOWN -> return ConnectivitySpeed.UNKNOWN
                }
            }
            return ConnectivitySpeed.NOT_CONNECTED

        }



    fun registerNetworkStateListener(context: Context, listener: NetworkStateReceiver.Listener?): NetworkStateReceiver {
        val networkStateChangeReceiver = NetworkStateReceiver(listener)
        @Suppress("DEPRECATION")
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkStateChangeReceiver, intentFilter)
        return networkStateChangeReceiver
    }

}


class NetworkInfo {

    val isConnectedToInternet: Boolean
    val connectivityType: ConnectivityType
    val networkType: NetworkType
    val connectivitySpeedStatus: ConnectivitySpeed


    init {
        this.isConnectedToInternet = NetworkHelper.isConnectedToInternet
        this.connectivityType = NetworkHelper.connectivityStatus
        this.networkType = NetworkHelper.networkType
        this.connectivitySpeedStatus = NetworkHelper.connectivitySpeed
    }

}


class NetworkStateReceiver(private var listener: Listener?) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        listener?.onNetworkStateChange(NetworkInfo())
    }

    interface Listener{
        fun onNetworkStateChange(info : NetworkInfo)
    }
}
