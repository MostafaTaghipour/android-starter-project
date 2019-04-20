package ${appPackage}.modules.shared.base

import androidx.lifecycle.Observer
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ${appPackage}.helpers.*
import ${appPackage}.modules.shared.events.*
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.*



abstract class BaseMvvmDialogFragment<VM : BaseViewModel> : androidx.fragment.app.DialogFragment() {


    protected var viewModel: VM? = null
    protected val disposable by lazy { CompositeDisposable() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context!!.inflateLayout(getContentView(), container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = generateViewModel()
        setupView()

        if (viewModel != null)
            onReady(savedInstanceState, viewModel!!)
        else
            onReady(savedInstanceState)
    }

    private fun setupView() {
        viewModel?.observeEvent(this, HttpErrorEvent::class.java, Observer { errorEvent -> onHttpError(errorEvent) })
        viewModel?.observeEvent(this, MessageEvent::class.java, Observer { event -> onMessageEvent(event) })
        viewModel?.observeEvent(this, FinishEvent::class.java, Observer { event -> onFinishEvent(event) })
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
                else -> toast(event.message,event.duration)
            }
            event.handled = true
        }
    }

    private fun onFinishEvent(event: FinishEvent?) {
        event?.let {
            event.handled = true
            dismiss()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.dismissKeyboard()
        super.onDismiss(dialog)
    }

    abstract fun getContentView(): Int
    open fun generateViewModel(): VM? = null
    open fun onReady(savedInstanceState: Bundle?) {}
    open fun onReady(savedInstanceState: Bundle?, viewModel: VM) {}
}
