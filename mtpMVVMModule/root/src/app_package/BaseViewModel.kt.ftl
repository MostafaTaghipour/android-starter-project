package ${appPackage}.modules.shared.base

import android.content.Intent
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ${appPackage}.errors.HttpException
import ${appPackage}.extras.Event
import ${appPackage}.extras.LiveBus
import ${appPackage}.helpers.inject
import ${appPackage}.modules.shared.events.HttpErrorEvent
import ${appPackage}.modules.shared.events.NoInternetAccessEvent
import ${appPackage}.net.CredentialsKeysRepo
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.NetworkInfo



abstract class BaseViewModel : ViewModel() {
    val disposable by lazy { CompositeDisposable() }
    private val liveBus by lazy { LiveBus() }

    var noInternetAccess: Boolean = false
        get() {
            val connectedToInternet = inject<NetworkInfo>().isConnectedToInternet
            if (!connectedToInternet)
                sendEvent(NoInternetAccessEvent())
            return !connectedToInternet
        }

    var isUserSessionValid: Boolean = true
        get() = inject<CredentialsKeysRepo>().isSessionValid

    init {
        @Suppress("LeakingThis")
        validateSession()
    }

     open protected fun validateSession() {
         //todo implement
//        if (!isUserSessionValid) {
//            sendEvent(NavigationEvent(AuthActivity::class.java))
//        }
    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {}
    open fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {}

    //event methods
    fun <T : Event> observeEvent(lifecycleOwner: LifecycleOwner, eventClass: Class<T>, observer: Observer<T>) {
        liveBus.observe(lifecycleOwner, eventClass, observer)
    }

    fun <T : Event> sendEvent(event: T) {
        liveBus.send(event)
    }


    protected fun hasError(exception: Throwable?): Boolean {
        if (exception != null) {
            sendEvent(HttpErrorEvent(exception as? HttpException))
            return true
        }
        return false
    }


    @CallSuper
    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}

