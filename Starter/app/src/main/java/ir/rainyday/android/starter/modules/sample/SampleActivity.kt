package ir.rainyday.android.starter.modules.sample

import android.os.Bundle
import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.helpers.twoWay
import ir.rainyday.android.starter.modules.shared.base.BaseMvvmActivity
import ir.rainyday.android.common.helpers.displayBackButton
import kotlinx.android.synthetic.main.content_sample.*
import org.koin.android.viewmodel.ext.android.getViewModel

class SampleActivity : BaseMvvmActivity<SampleViewModel>() {

    override fun getContentView(): Int {
        return R.layout.activity_sample
    }

    override fun generateViewModel(): SampleViewModel? {
        return getViewModel()
    }


    override fun onReady(savedInstanceState: Bundle?, viewModel: SampleViewModel) {
       // supportActionBar?.displayBackButton()
        title = getString(R.string.activity_sample_title)

        editText twoWay viewModel.text
        submitButton.setOnClickListener { viewModel.doSomething() }
    }

}