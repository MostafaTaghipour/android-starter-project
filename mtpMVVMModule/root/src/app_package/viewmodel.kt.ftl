package ${appPackage}.modules.${moduleNameLower}

import ${appPackage}.R
import ${appPackage}.helpers.MutableLiveData
import ${appPackage}.helpers.appContext
import ${appPackage}.helpers.successToast
import ${appPackage}.modules.shared.base.BaseViewModel
import ${appPackage}.modules.shared.events.MessageEvent
import ${appPackage}.modules.shared.events.MessageType


<#if includeLocale || includeWebApi>
class ${moduleName}ViewModel(private var repo: ${moduleName}Repo) : BaseViewModel() {
<#else>
class ${moduleName}ViewModel : BaseViewModel() {
</#if>
    val text = MutableLiveData<CharSequence?>(null)

    fun doSomething() {
        if (text.value.isNullOrEmpty()) {
            sendEvent(MessageEvent(appContext.getString(R.string.text_is_empty), MessageType.ERROR))
            return
        }

      appContext.successToast(text.value!!)
    }
}
