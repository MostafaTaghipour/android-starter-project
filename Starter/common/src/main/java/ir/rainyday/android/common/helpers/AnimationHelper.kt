@file:Suppress("NOTHING_TO_INLINE")

package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 15/10/2017.
 */
import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import androidx.annotation.AnimRes
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.AlphaAnimation


inline fun View.fadeIn(duration: Long = 200): Animation {
    val fade_in = AlphaAnimation(0.0f, 1.0f)
    fade_in.duration = duration
    startAnimation(fade_in)
    return fade_in
}

inline fun View.fadeOut(duration: Long = 200): Animation {
    val fade_out = AlphaAnimation(1.0f, 0.0f)
    fade_out.duration = duration
    startAnimation(fade_out)
    return fade_out
}


enum class ShakeDirection(val title: String) {
    HORIZONTAL("translationX"),
    VERTICAL("translationY")
}

inline fun View.shake(duration: Long = 500, shakeDirection: ShakeDirection = ShakeDirection.HORIZONTAL): Animator {
    val shakeAnim = ObjectAnimator
            .ofFloat(this, shakeDirection.title, 0f, 25f, -25f, 23f, -23f, 19f, -19f, 17f, -17f, 15f, -15f, 10f, -10f, 5f, -5f, 0f)
            .setDuration(duration)
    shakeAnim.start()
    return shakeAnim
}


inline fun Context.loadAnimation(@AnimRes id: Int) = AnimationUtils.loadAnimation(applicationContext, id)


//Animation listener
inline fun Animation.animListener(init: AnimListener.() -> Unit) = setAnimationListener(AnimListener().apply(init))
class AnimListener : Animation.AnimationListener {

    private var _onAnimationRepeat: ((Animation?) -> Unit)? = null
    private var _onAnimationEnd: ((Animation?) -> Unit)? = null
    private var _onAnimationStart: ((Animation?) -> Unit)? = null

    override fun onAnimationRepeat(animation: Animation?) {
        _onAnimationRepeat?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animation?) {
        _onAnimationEnd?.invoke(animation)
    }

    override fun onAnimationStart(animation: Animation?) {
        _onAnimationStart?.invoke(animation)
    }

    fun onAnimationRepeat(listener: (Animation?) -> Unit) {
        _onAnimationRepeat = listener
    }

    fun onAnimationEnd(listener: (Animation?) -> Unit) {
        _onAnimationEnd = listener
    }

    fun onAnimationStart(listener: (Animation?) -> Unit) {
        _onAnimationStart = listener
    }
}


//Animator listener
inline fun Animator.animListener(init: AnimtrListener.() -> Unit) = addListener(AnimtrListener().apply(init))
class AnimtrListener : Animator.AnimatorListener {

    private var _onAnimationRepeat: ((Animator?) -> Unit)? = null
    private var _onAnimationEnd: ((Animator?) -> Unit)? = null
    private var _onAnimationStart: ((Animator?) -> Unit)? = null
    private var _onAnimationCancel: ((Animator?) -> Unit)? = null

    override fun onAnimationRepeat(animation: Animator?) {
        _onAnimationRepeat?.invoke(animation)
    }

    override fun onAnimationEnd(animation: Animator?) {
        _onAnimationEnd?.invoke(animation)
    }

    override fun onAnimationStart(animation: Animator?) {
        _onAnimationStart?.invoke(animation)
    }

    override fun onAnimationCancel(animation: Animator?) {
        _onAnimationCancel?.invoke(animation)
    }


    fun onAnimationRepeat(listener: (Animator?) -> Unit) {
        _onAnimationRepeat = listener
    }

    fun onAnimationEnd(listener: (Animator?) -> Unit) {
        _onAnimationEnd = listener
    }

    fun onAnimationCancel(listener: (Animator?) -> Unit) {
        _onAnimationCancel = listener
    }

    fun onAnimationStart(listener: (Animator?) -> Unit) {
        _onAnimationStart = listener
    }
}