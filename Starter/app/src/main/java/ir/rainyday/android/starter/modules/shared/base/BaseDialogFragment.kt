package ir.rainyday.android.starter.modules.shared.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.rainyday.android.common.helpers.dismissKeyboard
import ir.rainyday.android.common.helpers.inflateLayout
import ir.rainyday.android.common.supers.SuperDialogFragment


abstract class BaseDialogFragment : SuperDialogFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return context!!.inflateLayout(getContentView(), container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onReady(savedInstanceState)
    }

    override fun onDismiss(dialog: DialogInterface) {
        activity?.dismissKeyboard()
        super.onDismiss(dialog)
    }

    abstract fun getContentView(): Int
    open fun onReady(savedInstanceState: Bundle?) {}
}
