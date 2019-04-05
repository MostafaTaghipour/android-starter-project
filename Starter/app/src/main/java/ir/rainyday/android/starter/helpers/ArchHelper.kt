package ir.rainyday.android.starter.helpers

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.lifecycle.*


/**
 * Created by taghipour on 17/10/2017.
 */

fun <T> MutableLiveData(default: T): MutableLiveData<T> = MutableLiveData<T>().apply {
    value = default
}


infix fun EditText.twoWay(liveData: MutableLiveData<CharSequence?>?) {

    liveData?.let {
        //---->
        val txtWatcher = object : TextWatcher {
            override fun afterTextChanged(newText: Editable?) {
                val value = liveData.value

                if (value == null || value.toString() != newText.toString()) {
                    liveData.value = newText
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

        }

        this.addTextChangedListener(txtWatcher)

        //<----
        val lifecycleOwner: LifecycleOwner? = this.context as? LifecycleOwner
        lifecycleOwner?.let {
            liveData.observe(lifecycleOwner, Observer { newText ->

                if (newText != null && !this.text.toString().equals(newText.toString())) {
                    setText(newText)
                }

            })

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroy() {
                    removeTextChangedListener(txtWatcher)
                }
            })
        }
    }

}

infix fun androidx.viewpager.widget.ViewPager.twoWay(liveData: MutableLiveData<Int>?) {

    liveData?.let {
        //---->
        val pageChangeListener = object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                val value = liveData.value

                if (value == null || value != position) {
                    liveData.value = position
                }
            }
        }

        this.addOnPageChangeListener(pageChangeListener)

        //<----
        val lifecycleOwner: LifecycleOwner? = this.context as? LifecycleOwner
        lifecycleOwner?.let {
            liveData.observe(lifecycleOwner, Observer { newIndex ->

                if (newIndex != null && currentItem != newIndex) {
                    setCurrentItem(newIndex, true)
                }

            })

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroy() {
                    removeOnPageChangeListener(pageChangeListener)
                }
            })
        }
    }

}


infix fun CheckBox.twoWay(liveData: MutableLiveData<Boolean>?) {

    liveData?.let {
        //---->
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, checked ->
            val value = liveData.value

            if (value == null || value != checked) {
                liveData.value = checked
            }
        }

        this.setOnCheckedChangeListener(checkedChangeListener)

        //<----
        val lifecycleOwner: LifecycleOwner? = this.context as? LifecycleOwner
        lifecycleOwner?.let {
            liveData.observe(lifecycleOwner, Observer { checked ->

                if (checked != null && isChecked != checked) {
                    isChecked = checked
                }

            })

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroy() {
                    setOnCheckedChangeListener(null)
                }
            })
        }
    }

}


infix fun Switch.twoWay(liveData: MutableLiveData<Boolean>?) {

    liveData?.let { _ ->
        //---->
        val checkedChangeListener = CompoundButton.OnCheckedChangeListener { _, checked ->
            val value = liveData.value

            if (value == null || value != checked) {
                liveData.value = checked
            }
        }

        this.setOnCheckedChangeListener(checkedChangeListener)

        //<----
        val lifecycleOwner: LifecycleOwner? = this.context as? LifecycleOwner
        lifecycleOwner?.let {
            liveData.observe(lifecycleOwner, Observer { checked ->

                if (checked != null && isChecked != checked) {
                    isChecked = checked
                }

            })

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroy() {
                    setOnCheckedChangeListener(null)
                }
            })
        }
    }

}


infix fun Spinner.twoWay(liveData: MutableLiveData<Int>?) {

    liveData?.let {
        //---->
        val selectedChangeListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val value = liveData.value

                if (value == null || value != position) {
                    liveData.value = position
                }
            }
        }

        this.onItemSelectedListener = selectedChangeListener

        //<----
        val lifecycleOwner: LifecycleOwner? = this.context as? LifecycleOwner
        lifecycleOwner?.let {
            liveData.observe(lifecycleOwner, Observer { position ->

                if (position != null && selectedItemPosition != position) {
                    setSelection(position)
                }

            })

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun destroy() {
                    onItemSelectedListener = null
                }
            })
        }
    }

}