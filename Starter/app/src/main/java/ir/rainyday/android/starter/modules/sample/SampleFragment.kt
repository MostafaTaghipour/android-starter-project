package ir.rainyday.android.starter.modules.sample

import android.os.Bundle
import ir.rainyday.android.starter.R
import ir.rainyday.android.starter.helpers.twoWay
import ir.rainyday.android.starter.modules.shared.base.BaseMvvmFragment
import kotlinx.android.synthetic.main.fragment_sample.*
import org.koin.android.viewmodel.ext.android.getViewModel


class SampleFragment : BaseMvvmFragment<SampleViewModel>() {

    override fun getContentView(): Int {
        return R.layout.fragment_sample
    }

    override fun generateViewModel(): SampleViewModel? {
        return getViewModel()
    }


    override fun onReady(savedInstanceState: Bundle?, viewModel: SampleViewModel) {
        editText twoWay viewModel.text
        submitButton.setOnClickListener { viewModel.doSomething() }
    }

}


