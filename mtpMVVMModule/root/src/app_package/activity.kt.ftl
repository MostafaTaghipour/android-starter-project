package ${appPackage}.modules.${moduleNameLower}

import android.os.Bundle
import ${appPackage}.R
import ${appPackage}.helpers.twoWay
import ${appPackage}.modules.shared.base.BaseMvvmActivity
import ir.rainyday.android.common.helpers.displayBackButton
import kotlinx.android.synthetic.main.content_${moduleNameLower}.*
import org.koin.android.viewmodel.ext.android.getViewModel


class ${moduleName}Activity : BaseMvvmActivity<${moduleName}ViewModel>() {

    override fun getContentView(): Int {
        return R.layout.activity_${moduleNameLower}
    }

    override fun generateViewModel(): ${moduleName}ViewModel? {
        return getViewModel()
    }


    override fun onReady(savedInstanceState: Bundle?) {
       // supportActionBar?.displayBackButton()
        title = getString(R.string.activity_${moduleNameLower}_title)
    }

    override fun onViewModelReady(viewModel: ${moduleName}ViewModel) {
        editText twoWay viewModel.text
        submitButton.setOnClickListener { viewModel.doSomething() }
    }

}
