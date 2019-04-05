package ir.rainyday.android.starter.modules.sample

import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.helpers.MutableLiveData
import ir.rainyday.android.starter.helpers.appContext
import ir.rainyday.android.starter.helpers.successToast
import ir.rainyday.android.starter.modules.shared.base.BaseViewModel
import ir.rainyday.android.starter.modules.shared.events.MessageEvent
import ir.rainyday.android.starter.modules.shared.events.MessageType

/**
 * Created by taghipour on 10/10/2017.
 */

class SampleViewModel(private var repo: SampleRepo) : BaseViewModel() {

    val text = MutableLiveData<CharSequence?>(null)

    fun doSomething() {
        if (text.value.isNullOrEmpty()) {
            sendEvent(MessageEvent(appContext.getString(R.string.text_is_empty), MessageType.ERROR))
            return
        }

      appContext.successToast(text.value!!)
    }
}