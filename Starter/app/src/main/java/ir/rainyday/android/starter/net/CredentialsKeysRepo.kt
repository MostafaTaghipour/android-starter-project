package ir.rainyday.android.starter.net

import android.content.SharedPreferences
import ir.rainyday.android.common.helpers.isPassed
import ir.rainyday.android.common.helpers.toIso8601Date

interface  CredentialsKeysRepo{
    val isSessionValid: Boolean
    var accessToken: String
    var notificationClientId: String
     var expires: String

    fun sync()
    fun clear()
}

class CredentialsKeysRepoImp(private val preferences: SharedPreferences) :CredentialsKeysRepo {

    companion object {
        private const val DEFAULT_STRING = ""
        private const val ACCESS_TOKEN_KEY = "access_token"
        private const val EXPIRE_KEY = "expire_token"
        private const val NOTIFICATION_CLIENT_KEY: String = "notification_client_id"
    }


    private var _expires: String = DEFAULT_STRING
    override var expires: String
        get() = _expires
        set(value) {
            _expires = value
        }

    val tokenExpired: Boolean
        get() = _expires.toIso8601Date()?.isPassed ?: true


    override val isSessionValid: Boolean
        get() = true // _accessToken.isNotNullAndEmpty() && !tokenExpired

    private var _accessToken: String = DEFAULT_STRING
    override var accessToken: String
        get() = _accessToken
        set(value) {
            _accessToken = value
        }


    private var _notificationClientId: String = DEFAULT_STRING
    override var notificationClientId: String
        get() = _notificationClientId
        set(value) {
            _notificationClientId = value
        }

    init {
        load()

    }

    private fun load() {
        expires = preferences.getString(EXPIRE_KEY, DEFAULT_STRING) ?: ""
        accessToken = preferences.getString(ACCESS_TOKEN_KEY, DEFAULT_STRING) ?: ""
        notificationClientId = preferences.getString(NOTIFICATION_CLIENT_KEY, DEFAULT_STRING) ?: ""
    }

    override fun sync() {
        val editor = preferences.edit()
        editor.putString(EXPIRE_KEY, expires)
        editor.putString(ACCESS_TOKEN_KEY, accessToken)
        editor.putString(NOTIFICATION_CLIENT_KEY, notificationClientId)
        editor.apply()
    }

    override fun clear() {
        accessToken = DEFAULT_STRING
        expires = DEFAULT_STRING
        notificationClientId = DEFAULT_STRING
        sync()
    }

}