package ${appPackage}.modules.shared.base

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import androidx.lifecycle.Observer
import ${appPackage}.R
import ${appPackage}.helpers.*
import ${appPackage}.modules.shared.events.*
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.toast


abstract class BaseMvvmActivity<VM : BaseViewModel> : BaseActivity() {

    protected var viewModel: VM? = null
    protected val disposable by lazy { CompositeDisposable() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = generateViewModel()
        if (viewModel != null) {
            subscribeEvents()
            onViewModelReady(viewModel!!)
        }

    }


    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


    //region Events
    private fun subscribeEvents() {
        viewModel?.observeEvent(this, NoInternetAccessEvent::class.java, Observer { event -> onLostInternetConnection(event) })
        viewModel?.observeEvent(this, HttpErrorEvent::class.java, Observer { event -> onHttpError(event) })
        viewModel?.observeEvent(this, MessageEvent::class.java, Observer { event -> onMessageEvent(event) })
        viewModel?.observeEvent(this, NavigationEvent::class.java, Observer { event -> onNavigationRequest(event) })
        viewModel?.observeEvent(this, FinishEvent::class.java, Observer { event -> onFinishEvent(event) })

    }

    private fun onLostInternetConnection(event: NoInternetAccessEvent?) {
        event?.let {
            snack(R.string.error_internet_connection) {}
        }
    }

    private fun onFinishEvent(event: FinishEvent?) {
        event?.let {
            event.handled = true
            finish()
        }
    }

    protected open fun onHttpError(event: HttpErrorEvent?) {
        event?.error?.localizedDescription?.let {
            errorToast(it, duration = Toast.LENGTH_LONG)
            event.handled = true
        }
    }

    protected open fun onMessageEvent(event: MessageEvent?) {
        event?.let {
            when (event.type) {
                MessageType.SUCCESS -> successToast(event.message, event.duration)
                MessageType.ERROR -> errorToast(event.message, event.duration)
                MessageType.INFO -> infoToast(event.message, event.duration)
                MessageType.WARNING -> warningToast(event.message, event.duration)
                else -> toast(event.message, event.duration, Gravity.CENTER)
            }
            event.handled = true
        }
    }

    protected open fun onNavigationRequest(event: NavigationEvent?) {
        //todo unimplemented
//        if (event?.destination == AuthActivity::class.java) {
//            Navigator.logout(googleSignInHelper)
//        }
    }
    //endregion

    open fun generateViewModel(): VM? = null
    open fun onViewModelReady(viewModel: VM) {}
}
