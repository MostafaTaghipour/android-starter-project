package ir.rainyday.android.starter.extras

/**
 * Created by taghipour on 18/10/2017.
 */
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.annotation.MainThread
import ir.rainyday.android.common.helpers.runOnUiThread
import java.util.concurrent.atomic.AtomicBoolean


@Suppress("UNCHECKED_CAST")
class LiveBus {
    private val mEventMap: MutableMap<Class<out Event>, LiveEvent<out Event>>

    init {
        mEventMap = androidx.collection.ArrayMap<Class<out Event>, LiveEvent<out Event>>()
    }


    fun <T : Event> observe(lifecycleOwner: LifecycleOwner, eventClass: Class<T>, observer: Observer<T>) {
        var liveEvent: LiveEvent<T>? = mEventMap[eventClass] as? LiveEvent<T>
        if (liveEvent == null) {
            liveEvent = initLiveEvent(eventClass)
        }
        liveEvent.observe(lifecycleOwner, observer)
    }


    fun <T : Event> send(event: T) {
        runOnUiThread {
            var liveEvent: LiveEvent<T>? = mEventMap[event.javaClass] as? LiveEvent<T>
            if (liveEvent == null) {
                liveEvent = initLiveEvent(event.javaClass)
            }
            liveEvent.value = event
        }
    }


    private fun <T : Event> initLiveEvent(eventClass: Class<T>): LiveEvent<T> {
        val liveEvent = LiveEvent<T>()
        mEventMap.put(eventClass, liveEvent)
        return liveEvent
    }
}


public abstract class Event
{
    var handled=false
}


// source: https://github.com/googlesamples/android-architecture-components/issues/63
class LiveEvent<T> : MutableLiveData<T>() {
    private val mPending = AtomicBoolean(false)


    @MainThread
    override fun observe(owner: androidx.lifecycle.LifecycleOwner, observer: androidx.lifecycle.Observer<in T>) {
        super.observe(owner,  Observer { value ->
            if (mPending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        })
    }

    @MainThread
    override fun setValue(value: T?) {
        mPending.set(true)
        super.setValue(value)
    }


    @MainThread
    fun call() {
        value = null
    }
}