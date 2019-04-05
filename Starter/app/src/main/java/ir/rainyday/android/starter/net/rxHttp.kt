package ir.rainyday.android.starter.net

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Publisher
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

/**
 * Created by taghipour on 06/11/2017.
 */

fun <T> Flowable<T>.composeForHttpTasks(): Flowable<T> = compose<T>({
    it.subscribeOn(Schedulers.io()).retryWhen(RetryAfterTimeoutWithDelay(3, 2)).mapHttpException().observeOn(AndroidSchedulers.mainThread())
})




fun <T> Single<T>.composeForHttpTasks(): Single<T> =  compose<T>({
    it.subscribeOn(Schedulers.io()).retryWhen(RetryAfterTimeoutWithDelay(3, 2)).mapHttpException().observeOn(AndroidSchedulers.mainThread())
})



 fun <T> Flowable<T>.mapHttpException(): Flowable<T> {

   return  onErrorResumeNext({e:Throwable->
         Flowable.error(ir.rainyday.android.starter.errors.HttpException(e))
     })

}


fun <T> Single<T>.mapHttpException(): Single<T> {

    return  onErrorResumeNext({e:Throwable->
        e.printStackTrace()
        Single.error(ir.rainyday.android.starter.errors.HttpException(e))
    })

}


class  RetryAfterTimeoutWithDelay(val maxRetries: Int, var delay: Long, val delayAmount: Long = 100): Function<Flowable<Throwable>, Publisher<*>> {

    internal var retryCount = 0

    override fun apply(attempts: Flowable<Throwable>): Publisher<*> {
        return attempts.flatMap({
            if (++retryCount < maxRetries && (it is SocketTimeoutException /*|| it is IOException*/)) {
                delay += delayAmount
                Flowable.timer(delay, TimeUnit.MILLISECONDS)
            } else {
                Flowable.error(it)
            }
        })
    }

}



//class RetryAfterTimeoutWithDelay(val maxRetries: Int, var delay: Long, val delayAmount: Long = 100)
//    : Func1<Observable<out Throwable>, Observable<*>> {
//
//    internal var retryCount = 0
//
//    override fun call(attempts: Observable<out Throwable>): Observable<*> {
//        return attempts.flatMap({
//            if (++retryCount < maxRetries && (it is SocketTimeoutException /*|| it is IOException*/)) {
//                delay += delayAmount
//                Observable.timer(delay, TimeUnit.MILLISECONDS)
//            } else {
//                Observable.error(it as Throwable)
//            }
//        })
//    }
//}
//
//fun <T> Observable<T>.composeForHttpTasks(): Observable<T> = compose<T>(Observable.Transformer {
//    it.subscribeOn(io()).retryWhen(RetryAfterTimeoutWithDelay(3, 2)).observeOn(mainThread())
//})
