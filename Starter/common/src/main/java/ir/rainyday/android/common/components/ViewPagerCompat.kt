@file:Suppress("DEPRECATION", "NAME_SHADOWING", "OverridingDeprecatedMember", "unused")

package ir.rainyday.android.common.components

import android.annotation.SuppressLint
import android.content.Context
import android.database.DataSetObserver
import android.os.Parcel
import android.os.Parcelable
import androidx.core.os.ParcelableCompat
import androidx.core.os.ParcelableCompatCreatorCallbacks
import androidx.core.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import java.lang.ref.WeakReference
import java.util.*



/**
 * `ViewPagerCompat` is an API-compatible implemetation of `ViewPager` which
 * orders paged views according to the layout direction of the view.  In left to right mode, the
 * first view is at the left side of the carousel, and in right to left mode it is at the right
 * side.
 *
 * It accomplishes this by wrapping the provided `PagerAdapter` and any provided
 * `OnPageChangeListener`s so that clients can be agnostic to layout direction and
 * modifications are kept internal to `ViewPagerCompat`.
 */
class ViewPagerCompat : androidx.viewpager.widget.ViewPager {
    private var mLayoutDirection = ViewCompat.LAYOUT_DIRECTION_LTR
    private val mPageChangeListeners = HashMap<androidx.viewpager.widget.ViewPager.OnPageChangeListener, ReversingOnPageChangeListener>()

    private val isRtl: Boolean
        get() = mLayoutDirection == ViewCompat.LAYOUT_DIRECTION_RTL

    val itemCount: Int
        get() = adapter?.count ?: 0

    var swipingEnabled: Boolean = true
    var animationEnabled: Boolean = true
    var repeat: Boolean = false


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onRtlPropertiesChanged(layoutDirection: Int) {
        super.onRtlPropertiesChanged(layoutDirection)
        val viewCompatLayoutDirection = if (layoutDirection == View.LAYOUT_DIRECTION_RTL) ViewCompat.LAYOUT_DIRECTION_RTL else ViewCompat.LAYOUT_DIRECTION_LTR
        if (viewCompatLayoutDirection != mLayoutDirection) {
            val adapter = super.getAdapter()
            var position = 0
            if (adapter != null) {
                position = currentItem
            }
            mLayoutDirection = viewCompatLayoutDirection
            if (adapter != null) {
                adapter.notifyDataSetChanged()
                currentItem = position
            }
        }
    }

    override fun setAdapter(adapter: androidx.viewpager.widget.PagerAdapter?) {
        var adapter = adapter
        if (adapter != null) {
            adapter = ReversingAdapter(adapter)
        }
        super.setAdapter(adapter)
        currentItem = 0
    }

    override fun getAdapter(): androidx.viewpager.widget.PagerAdapter? {
        val adapter = super.getAdapter() as ReversingAdapter?
        return adapter?.delegate
    }

    override fun getCurrentItem(): Int {
        var item = super.getCurrentItem()
        val adapter = super.getAdapter()
        if (adapter != null && isRtl) {
            item = itemCount - item - 1
        }
        return item
    }

    override fun setCurrentItem(position: Int, smoothScroll: Boolean) {
        var position = position
        val adapter = super.getAdapter()
        if (adapter != null && isRtl) {
            position = itemCount - position - 1
        }
        super.setCurrentItem(position, animationEnabled )
    }

    override fun setCurrentItem(position: Int) {
        var position = position
        val adapter = super.getAdapter()
        if (adapter != null && isRtl) {
            position = itemCount - position - 1
        }
        super.setCurrentItem(position,animationEnabled)
    }

    class SavedState : Parcelable {
        internal val mViewPagerSavedState: Parcelable
        internal val mLayoutDirection: Int

        internal constructor(viewPagerSavedState: Parcelable, layoutDirection: Int) {
            mViewPagerSavedState = viewPagerSavedState
            mLayoutDirection = layoutDirection
        }

        private constructor(`in`: Parcel, loader: ClassLoader?) {
            var loader = loader
            if (loader == null) {
                loader = javaClass.classLoader
            }
            mViewPagerSavedState = `in`.readParcelable(loader)!!
            mLayoutDirection = `in`.readInt()
        }

        override fun describeContents(): Int {
            return 0
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            out.writeParcelable(mViewPagerSavedState, flags)
            out.writeInt(mLayoutDirection)
        }

        companion object {

            // The `CREATOR` field is used to create the parcelable from a parcel, even though it is never referenced directly.
            val CREATOR: Parcelable.Creator<SavedState> = ParcelableCompat.newCreator(object : ParcelableCompatCreatorCallbacks<SavedState> {
                override fun createFromParcel(`in`: Parcel, loader: ClassLoader): SavedState {
                    return SavedState(`in`, loader)
                }

                override fun newArray(size: Int): Array<SavedState> {
                    return newArray(size)
                }
            })
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState!!, mLayoutDirection)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state !is SavedState) {
            super.onRestoreInstanceState(state)
            return
        }

        mLayoutDirection = state.mLayoutDirection
        super.onRestoreInstanceState(state.mViewPagerSavedState)
    }

    override fun setOnPageChangeListener(listener: androidx.viewpager.widget.ViewPager.OnPageChangeListener) {
        super.setOnPageChangeListener(ReversingOnPageChangeListener(listener))
    }

    override fun addOnPageChangeListener(listener: androidx.viewpager.widget.ViewPager.OnPageChangeListener) {
        val reversingListener = ReversingOnPageChangeListener(listener)
        mPageChangeListeners.put(listener, reversingListener)
        super.addOnPageChangeListener(reversingListener)
    }

    override fun removeOnPageChangeListener(listener: androidx.viewpager.widget.ViewPager.OnPageChangeListener) {
        super.removeOnPageChangeListener(mPageChangeListeners[listener]!!)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.UNSPECIFIED) {
            var height = 0
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                val h = child.measuredHeight
                if (h > height) {
                    height = h
                }
            }
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


    //auto scroll
    private var timer: WeakReference<Timer>? = null

    fun enableAutoScroll(timeInterval: Long = 5000) {
        if (timer?.get() != null)
            return

        repeat = true

        val timerTask = object : TimerTask() {
            override fun run() {
                post {

                    if (repeat) {
                        currentItem = (currentItem + 1) % itemCount
                    } else {
                        if (currentItem + 1 == itemCount)
                            return@post

                        currentItem += 1
                    }
                }
            }
        }
        timer = WeakReference(Timer())
        timer?.get()?.schedule(timerTask, timeInterval, timeInterval)
    }

    fun disableAutoScroll() {
        timer?.get()?.cancel()
        timer = null
    }


    //swipe
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (this.swipingEnabled) {
            return super.onTouchEvent(event)
        }

        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (this.swipingEnabled) {
            return super.onInterceptTouchEvent(event)
        }

        return false
    }


    private inner class ReversingOnPageChangeListener(private val mListener: androidx.viewpager.widget.ViewPager.OnPageChangeListener) : androidx.viewpager.widget.ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            var position = position
            var positionOffset = positionOffset
            var positionOffsetPixels = positionOffsetPixels
            // The documentation says that `getPageWidth(...)` returns the fraction of the _measured_ width that that page takes up.  However, the code seems to
            // use the width so we will here too.
            val width = width
            val adapter = super@ViewPagerCompat.getAdapter()
            if (isRtl && adapter != null) {
                val count = adapter.count
                var remainingWidth = (width * (1 - adapter.getPageWidth(position))).toInt() + positionOffsetPixels
                while (position < count && remainingWidth > 0) {
                    position += 1
                    remainingWidth -= (width * adapter.getPageWidth(position)).toInt()
                }
                position = count - position - 1
                positionOffsetPixels = -remainingWidth
                positionOffset = positionOffsetPixels / (width * adapter.getPageWidth(position))
            }
            mListener.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            var position = position
            val adapter = super@ViewPagerCompat.getAdapter()
            if (isRtl && adapter != null) {
                position = adapter.count - position - 1
            }
            mListener.onPageSelected(position)
        }

        override fun onPageScrollStateChanged(state: Int) {
           mListener.onPageScrollStateChanged(state)
        }
    }

    private inner class ReversingAdapter(adapter: androidx.viewpager.widget.PagerAdapter) : DelegatingPagerAdapter(adapter) {

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            super.destroyItem(container, position, `object`)
        }

        override fun destroyItem(container: View, position: Int, `object`: Any) {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            super.destroyItem(container, position, `object`)
        }

        override fun getItemPosition(`object`: Any): Int {
            var position = super.getItemPosition(`object`)
            if (isRtl) {
                if (position == androidx.viewpager.widget.PagerAdapter.POSITION_UNCHANGED || position == androidx.viewpager.widget.PagerAdapter.POSITION_NONE) {
                    // We can't accept POSITION_UNCHANGED when in RTL mode because adding items to the end of the collection adds them to the beginning of the
                    // ViewPager.  Items whose positions do not change from the perspective of the wrapped adapter actually do change from the perspective of
                    // the ViewPager.
                    position = androidx.viewpager.widget.PagerAdapter.POSITION_NONE
                } else {
                    position = count - position - 1
                }
            }
            return position
        }

        override fun getPageTitle(position: Int): CharSequence? {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            return super.getPageTitle(position)
        }

        override fun getPageWidth(position: Int): Float {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            return super.getPageWidth(position)
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            return super.instantiateItem(container, position)
        }

        override fun instantiateItem(container: View, position: Int): Any {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            return super.instantiateItem(container, position)
        }

        override fun setPrimaryItem(container: View, position: Int, `object`: Any) {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            super.setPrimaryItem(container, position, `object`)
        }

        override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
            var position = position
            if (isRtl) {
                position = count - position - 1
            }
            super.setPrimaryItem(container, position, `object`)
        }
    }
}



internal open class DelegatingPagerAdapter(val delegate: androidx.viewpager.widget.PagerAdapter) : androidx.viewpager.widget.PagerAdapter() {

    init {
        delegate.registerDataSetObserver(MyDataSetObserver(this))
    }

    override fun getCount(): Int {
        return delegate.count
    }

    override fun startUpdate(container: ViewGroup) {
        delegate.startUpdate(container)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return delegate.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        delegate.destroyItem(container, position, `object`)
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        delegate.setPrimaryItem(container, position, `object`)
    }

    override fun finishUpdate(container: ViewGroup) {
        delegate.finishUpdate(container)
    }

    @Deprecated("")
    override fun startUpdate(container: View) {
        delegate.startUpdate(container)
    }

    @Deprecated("")
    override fun instantiateItem(container: View, position: Int): Any {
        return delegate.instantiateItem(container, position)
    }

    @Deprecated("")
    override fun destroyItem(container: View, position: Int, `object`: Any) {
        delegate.destroyItem(container, position, `object`)
    }

    @Deprecated("")
    override fun setPrimaryItem(container: View, position: Int, `object`: Any) {
        delegate.setPrimaryItem(container, position, `object`)
    }

    @Deprecated("")
    override fun finishUpdate(container: View) {
        delegate.finishUpdate(container)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return delegate.isViewFromObject(view, `object`)
    }

    override fun saveState(): Parcelable? {
        return delegate.saveState()
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        delegate.restoreState(state, loader)
    }

    override fun getItemPosition(`object`: Any): Int {
        return delegate.getItemPosition(`object`)
    }

    override fun notifyDataSetChanged() {
        delegate.notifyDataSetChanged()
    }

    internal fun superNotifyDataSetChanged() {
        super.notifyDataSetChanged()
    }

    override fun registerDataSetObserver(observer: DataSetObserver) {
        delegate.registerDataSetObserver(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        delegate.unregisterDataSetObserver(observer)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return delegate.getPageTitle(position)
    }

    override fun getPageWidth(position: Int): Float {
        return delegate.getPageWidth(position)
    }

    private class MyDataSetObserver internal constructor(internal val mParent: DelegatingPagerAdapter?) : DataSetObserver() {

        override fun onChanged() {
            mParent?.superNotifyDataSetChanged()
        }

        override fun onInvalidated() {
            onChanged()
        }
    }
}