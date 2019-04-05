package ir.rainyday.android.common.components

import android.view.View
import android.view.ViewGroup
import ir.rainyday.android.common.helpers.inflateLayout


/**
 * Created by taghipour on 11/11/2017.
 */
abstract class LayoutPagerAdapter<T>() : androidx.viewpager.widget.PagerAdapter() {

private var _items: ArrayList<T>? = null
    var items: ArrayList<T>? = null
        set(value) {
            _items=value
            notifyDataSetChanged()
        }

    override fun getCount(): Int {
        return if (_items != null) {
            _items!!.size
        } else 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ViewGroup
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = container.context.inflateLayout(getLayout(), container)

        if (_items != null) {
            bindView(itemView, _items!![position], position)
        }

        container.addView(itemView)

        return itemView
    }

    abstract fun getLayout(): Int

    abstract fun bindView(itemView: View,item:T, position: Int)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ViewGroup)
    }

}