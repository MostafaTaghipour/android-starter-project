package ir.rainyday.android.starter.modules.shared.base

import androidx.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ir.rainyday.android.starter.helpers.*
import ir.rainyday.android.starter.modules.shared.events.HttpErrorEvent
import ir.rainyday.android.starter.modules.shared.events.MessageEvent
import ir.rainyday.android.starter.modules.shared.events.MessageType
import io.reactivex.disposables.CompositeDisposable
import ir.rainyday.android.common.helpers.*


/**
* Created by taghipour on 09/10/2017.
*/
abstract class BaseMvvmFragment<VM : BaseViewModel> : androidx.fragment.app.Fragment() {

    protected val disposable by lazy { CompositeDisposable() }
    protected var viewModel: VM? = null

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel?.onRequestPermissionsResult(requestCode,permissions, grantResults)
    }


    override fun onPause() {
        activity?.dismissKeyboard()
        super.onPause()
    }

    override fun onDestroy() {
        disposable.clear()
        super.onDestroy()
    }

    protected open fun onHttpError(event: HttpErrorEvent?) {
        event?.error?.localizedDescription?.let {
            context?.errorToast(it,duration = Toast.LENGTH_LONG)
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

    abstract fun getContentView(): Int
    open fun generateViewModel(): VM? = null
    open fun onReady(savedInstanceState: Bundle?){}
    open fun onReady(savedInstanceState: Bundle?, viewModel: VM) {}
}
