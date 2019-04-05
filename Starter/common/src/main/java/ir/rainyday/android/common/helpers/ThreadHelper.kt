package ir.rainyday.android.common.helpers

/**
 * Created by taghipour on 15/10/2017.
 */
import android.os.Handler
import android.os.Looper
import android.view.View
import java.util.concurrent.Executors


private val IO_EXECUTOR = Executors.newSingleThreadExecutor()

/**
 * Utility method to run blocks on a dedicated background thread, used for io/database work.
 */
fun ioThread(f : () -> Unit) {
    IO_EXECUTOR.execute(f)
}


fun runAsync(action: () -> Unit) = Thread(Runnable(action)).start()

fun runOnUiThread(action: () -> Unit) {
    if (isMainLooperAlive()) {
        action()
    } else {
        Handler(Looper.getMainLooper()).post(Runnable(action))
    }
}

fun runDelayed(delayMillis: Long, action: () -> Unit) = Handler().postDelayed(Runnable(action), delayMillis)

fun runDelayedOnUiThread(delayMillis: Long, action: () -> Unit) = Handler(Looper.getMainLooper()).postDelayed(Runnable(action), delayMillis)

fun View.whenReady(action:()->Unit){
    post(Runnable(action))
}

private fun isMainLooperAlive() = Looper.myLooper() == Looper.getMainLooper()


