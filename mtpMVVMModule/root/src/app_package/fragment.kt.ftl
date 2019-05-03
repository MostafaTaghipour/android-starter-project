package ${appPackage}.modules.${moduleNameLower}

import android.os.Bundle
import ${appPackage}.R
import ${appPackage}.helpers.twoWay
import ${appPackage}.modules.shared.base.BaseMvvmFragment
import kotlinx.android.synthetic.main.fragment_${moduleNameLower}.*
import org.koin.android.viewmodel.ext.android.getViewModel


class ${moduleName}Fragment : BaseMvvmFragment<${moduleName}ViewModel>() {

    override fun getContentView(): Int {
        return R.layout.fragment_${moduleNameLower}
    }

    override fun generateViewModel(): ${moduleName}ViewModel? {
        return getViewModel()
    }


    override fun onReady(savedInstanceState: Bundle?) {
    }

    override fun onViewModelReady(viewModel: ${moduleName}ViewModel) {
        editText twoWay viewModel.text
        submitButton.setOnClickListener { viewModel.doSomething() }
    }

}


