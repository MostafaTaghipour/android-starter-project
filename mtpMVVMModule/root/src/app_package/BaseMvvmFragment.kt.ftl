package ${appPackage}.modules.shared.base

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ${appPackage}.helpers.*
import ${appPackage}.modules.shared.events.HttpErrorEvent
import ${appPackage}.modules.shared.events.MessageEvent
import ${appPackage}.modules.shared.events.MessageType
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.*


abstract class BaseMvvmFragment<VM : BaseViewModel> : BaseFragment() {

    protected val disposable by lazy { CompositeDisposable() }
    protected var viewModel: VM? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        viewModel?.observeEvent(this, HttpErrorEvent::class.java, Observer { errorEvent -> onHttpError(errorEvent) })
        viewModel?.observeEvent(this, MessageEvent::class.java, Observer { event -> onMessageEvent(event) })
    }

    protected open fun onHttpError(event: HttpErrorEvent?) {
        event?.error?.localizedDescription?.let {
            context?.errorToast(it, duration = Toast.LENGTH_LONG)
            event.handled = true
        }
    }

    protected open fun onMessageEvent(event: MessageEvent?) {
        event?.let {
            when (event.type) {
                MessageType.SUCCESS -> context?.successToast(event.message, event.duration)
                MessageType.ERROR -> context?.errorToast(event.message, event.duration)
                MessageType.INFO -> context?.infoToast(event.message, event.duration)
                MessageType.WARNING -> context?.warningToast(event.message, event.duration)
                else -> toast(event.message)
            }
            event.handled = true
        }
    }
    //endregion

    open fun generateViewModel(): VM? = null
    open fun onViewModelReady(viewModel: VM) {}
}
