package ir.rainyday.android.starter.helpers


import androidx.appcompat.widget.SearchView
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject




/**
* Created by taghipour on 11/10/2017.
*/


fun Disposable.disposedBy(disposable: CompositeDisposable?) {
    disposable?.add(this)
}


fun <T> Flowable<T>.composeForIoTasks(): Flowable<T> = compose<T>({
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
})


fun <T> Observable<T>.composeForIoTasks(): Observable<T> = compose<T>({
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
})


fun <T> Single<T>.composeForIoTasks(): Single<T> =  compose<T>({
    it.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
})


object RxSearchObservable {

    fun fromView(searchView: SearchView): Observable<String> {

        val subject = PublishSubject.create<String>()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(text: String): Boolean {
                subject.onNext(text)
                return true
            }
        })

        return subject
    }
}

