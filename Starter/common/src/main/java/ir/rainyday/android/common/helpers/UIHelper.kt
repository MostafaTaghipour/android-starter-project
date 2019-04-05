package ir.rainyday.android.common.helpers

import android.annotation.TargetApi

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.os.Build
import androidx.annotation.*
import androidx.fragment.app.Fragment as SupportFragment
import androidx.appcompat.widget.SearchView as SupportSearchView
import android.text.Editable
import android.text.TextWatcher
import androidx.core.widget.ImageViewCompat
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import android.view.*
import android.view.View.*
import android.widget.*
import ir.rainyday.android.common.R
import java.util.ArrayList


/**
* Created by taghipour on 15/10/2017.
*/

fun Context.inflateLayout(@LayoutRes layoutResId: Int, parent: ViewGroup? = null, attachToRoot: Boolean = false)
        = LayoutInflater.from(this).inflate(layoutResId, parent, attachToRoot)

//region View
@Deprecated("Use findViewById() instead", ReplaceWith("findViewById()"))
inline fun <reified T : View> View.find(@IdRes id: Int): T = findViewById(id)

var View.isVisible
    get() = visibility == VISIBLE
    set(value) {
        visibility = if (value) VISIBLE else GONE
    }

fun View.setVisibility(visible: Boolean, withAnimation: Boolean = false, animationDuration: Long = 200) {
    if (visible)
        show(withAnimation, animationDuration)
    else
        hide(true, withAnimation, animationDuration)
}

fun View.hide(gone: Boolean = true, withAnimation: Boolean = false, animationDuration: Long = 200) {

    if (!isVisible)
        return

    if (withAnimation) {
        fadeOut(animationDuration)
                .animListener {
                    onAnimationEnd {
                        visibility = if (gone) GONE else INVISIBLE
                    }
                }
    } else {
        visibility = if (gone) GONE else INVISIBLE
    }
}

fun View.show(withAnimation: Boolean = false, animationDuration: Long = 200) {

    if (isVisible)
        return

    visibility = VISIBLE
    if (withAnimation) {
        fadeIn(animationDuration)
    }
}


fun View.setWidth(width: Int) {
    val params = layoutParams
    params.width = width
    layoutParams = params
}

fun View.setHeight(height: Int) {
    val params = layoutParams
    params.height = height
    layoutParams = params
}

fun View.setSize(width: Int, height: Int) {
    val params = layoutParams
    params.width = width
    params.height = height
    layoutParams = params
}


fun View.setMargin(left: Int, top: Int, right: Int, bottom: Int) {
    val marginLayoutParams = layoutParams as? ViewGroup.MarginLayoutParams ?: return
    marginLayoutParams.setMargins(left, top, right, bottom)
    layoutParams = marginLayoutParams
}


inline val ViewGroup.views
    get() = (0..childCount - 1).map { getChildAt(it) }


fun View.isPointInsideThis(x: Float, y: Float): Boolean {
    val location = IntArray(2)
    getLocationOnScreen(location)
    val viewX = location[0]
    val viewY = location[1]

    //point is inside view bounds
    return if (x > viewX && x < viewX + width && y > viewY && y < viewY + height) {
        true
    } else {
        false
    }
}


const val OVERLAY_TAG: Int = 1352
fun ViewGroup.removeOverlay(tag: Int = OVERLAY_TAG, withAnimation: Boolean = true) {
    val overlayView = findViewById<ViewGroup?>(tag)

    if (overlayView != null) {
        if (withAnimation) {
            overlayView.fadeOut()
                    .animListener {
                        onAnimationEnd { removeView(overlayView) }
                    }
        } else {
            removeView(overlayView)
        }
    }
}


fun ViewGroup.addOverlayView(overlayView: View, tag: Int = OVERLAY_TAG, withAnimation: Boolean = true,cancelable:Boolean=false) {
    val params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    overlayView.layoutParams = params
    overlayView.id = tag
    overlayView.isClickable = true

    addView(overlayView)


    if(cancelable)
        overlayView.setOnClickListener {
            removeOverlay(tag,withAnimation)
        }

    if (withAnimation) {
        overlayView.fadeIn()
    } else {
        overlayView.alpha = 1.0f
    }
}


fun ViewGroup.addOverlayWithProgressBarAndText(bgColor: Int = 0x7f000000, tag: Int = OVERLAY_TAG, withAnimation: Boolean = true, cancelable: Boolean = false, showProgressbar: Boolean = false, progressBarStyle: Int = android.R.attr.progressBarStyle, progressBarColor: Int? = Color.WHITE, text: CharSequence? = null, textColor: Int = Color.WHITE, @StyleRes textStyle: Int = R.style.TextAppearance_AppCompat_Subhead, typeface: Typeface? = null) {
    val overlayView = RelativeLayout(context)
    overlayView.setBackgroundColor(bgColor)



    if (showProgressbar || (text != null && text.isNotEmpty())) {
        val container = LinearLayout(context).apply {
            @Suppress("NAME_SHADOWING")
            val params = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
            layoutParams = params
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        }


        //progressBar
        if (showProgressbar) {
            val progressBar = ProgressBar(context, null, progressBarStyle).apply {
                if (progressBarColor != null)
                    indeterminateDrawable?.setColorFilter(progressBarColor, PorterDuff.Mode.SRC_IN)
            }

            container.addView(progressBar)
        }


        //label
        if (text != null && text.isNotEmpty()) {

            val label = TextView(GlobalAppContext.instance.applicationContext).apply {
                val params1 = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                params1.setMargins(16.dp, 16.dp, 16.dp, 16.dp)
                layoutParams = params1
                this.text = text
                setTextAppearanceCompat(textStyle)
                setTextColor(textColor)
                this.typeface = typeface
            }

            container.addView(label)
        }

        overlayView.addView(container)
    }

    addOverlayView(overlayView, tag, withAnimation,cancelable)

}

fun ViewGroup.addOverlay(bgColor: Int = 0x7f000000, tag: Int = OVERLAY_TAG, withAnimation: Boolean = true,cancelable:Boolean=false) {
    addOverlayWithProgressBarAndText(
            bgColor = bgColor,
            tag = tag,
            withAnimation = withAnimation,
            cancelable = cancelable
    )
}

fun ViewGroup.addOverlayWithProgressBar(bgColor: Int = 0x7f000000, tag: Int = OVERLAY_TAG, withAnimation: Boolean = true, cancelable:Boolean=false, showProgressbar: Boolean = true, progressBarStyle: Int = android.R.attr.progressBarStyle, progressBarColor: Int? = Color.WHITE) {
    addOverlayWithProgressBarAndText(
            bgColor = bgColor,
            tag = tag,
            withAnimation = withAnimation,
            cancelable = cancelable,
            showProgressbar = showProgressbar,
            progressBarStyle = progressBarStyle,
            progressBarColor = progressBarColor
    )
}

fun ViewGroup.addOverlayWithText(bgColor: Int = 0x7f000000, tag: Int = OVERLAY_TAG, withAnimation: Boolean = true, cancelable:Boolean=false, text: CharSequence? = null, textColor: Int = Color.WHITE, @StyleRes textStyle: Int = R.style.TextAppearance_AppCompat_Subhead, typeface: Typeface? = null) {
    addOverlayWithProgressBarAndText(
            bgColor = bgColor,
            tag = tag,
            withAnimation = withAnimation,
            cancelable = cancelable,
            text = text,
            textColor = textColor,
            textStyle = textStyle,
            typeface = typeface
    )
}
//endregion

//region SearchView
fun SearchView.onQueryChange(query: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(q: String): Boolean {
            query(q)
            return false
        }

        override fun onQueryTextSubmit(q: String): Boolean {
            return false
        }
    })
}

fun SearchView.onQuerySubmit(query: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextChange(q: String): Boolean {
            return false
        }

        override fun onQueryTextSubmit(q: String): Boolean {
            query(q)
            return false
        }
    })
}

fun SupportSearchView.onQueryChange(query: (String) -> Unit) {
    setOnQueryTextListener(object : SupportSearchView.OnQueryTextListener {
        override fun onQueryTextChange(q: String): Boolean {
            query(q)
            return false
        }

        override fun onQueryTextSubmit(q: String): Boolean {
            return false
        }
    })
}

fun SupportSearchView.onQuerySubmit(query: (String) -> Unit) {
    setOnQueryTextListener(object : SupportSearchView.OnQueryTextListener {
        override fun onQueryTextChange(q: String): Boolean {
            return false
        }

        override fun onQueryTextSubmit(q: String): Boolean {
            query(q)
            return false
        }
    })
}
//endregion

//region  TextView
inline fun TextView.textWatcher(init: TxtWatcher.() -> Unit) = addTextChangedListener(TxtWatcher().apply(init))

class TxtWatcher : TextWatcher {

    val TextView.isEmpty
        get() = text.isEmpty()

    val TextView.isNotEmpty
        get() = text.isNotEmpty()

    val TextView.isBlank
        get() = text.isBlank()

    val TextView.isNotBlank
        get() = text.isNotBlank()

    private var _beforeTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null
    private var _afterTextChanged: ((Editable?) -> Unit)? = null

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        _beforeTextChanged?.invoke(s, start, count, after)
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        _onTextChanged?.invoke(s, start, before, count)
    }

    override fun afterTextChanged(s: Editable?) {
        _afterTextChanged?.invoke(s)
    }

    fun beforeTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _beforeTextChanged = listener
    }

    fun onTextChanged(listener: (CharSequence?, Int, Int, Int) -> Unit) {
        _onTextChanged = listener
    }

    fun afterTextChanged(listener: (Editable?) -> Unit) {
        _afterTextChanged = listener
    }
}


fun TextView.setTextAppearanceCompat(@StyleRes resId: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        setTextAppearance(resId)
    } else {
        @Suppress("DEPRECATION")
        setTextAppearance(context, resId)
    }
}
//endregion


//region Toolbar
val Toolbar.navImageButton: ImageButton?
    get() {
        try {
            val toolbarClass = Toolbar::class.java
            val navButtonField = toolbarClass.getDeclaredField("mNavButtonView")
            navButtonField.isAccessible = true

            return navButtonField.get(this) as ImageButton
        } catch (e: Exception) {
        }

        return null
    }

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
fun Activity.getOverflowButtonView(@StringRes overflowDescriptionRes: Int, listener: getOverflowMenuListener?) {
    try {
        val overflowDescription = getString(overflowDescriptionRes)
        val decorView = window.decorView as ViewGroup
        val viewTreeObserver = decorView.viewTreeObserver
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val outViews = ArrayList<View>()
                decorView.findViewsWithText(outViews, overflowDescription,
                        View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION)
                if (outViews.isEmpty()) {
                    decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    listener?.onFailed()
                    return
                }
                val overflow = outViews[0] as AppCompatImageView
                decorView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                listener?.onOverflowMenuAvailable(overflow)
            }
        })
    } catch (ex: Exception) {
        listener?.onFailed()
    }

}

interface getOverflowMenuListener {
    fun onOverflowMenuAvailable(overflow: AppCompatImageView)

    fun onFailed()
}


fun ActionBar.displayBackButton(@DrawableRes icon: Int? = null) {
    setDisplayHomeAsUpEnabled(true)
    setDisplayShowHomeEnabled(true)
    if (icon != null) {
        setHomeAsUpIndicator(icon)
    }
}
//endregion

//region ImageView
fun ImageView.setTintColor(color:Int){
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color));
}

fun ImageView.setTintColorRes(@ColorRes colorId:Int){
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(context.getColorCompat(colorId)));
}
//endregion