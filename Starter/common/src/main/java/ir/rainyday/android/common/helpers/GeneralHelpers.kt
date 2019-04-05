package ir.rainyday.android.common.helpers

import java.lang.ref.WeakReference

/**
 * Created by taghipour on 16/10/2017.
 */


//multi variable nullable check
fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (List<T>) -> R) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}

fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (List<T>) -> R) {
    if (this.any { it != null }) {
        block(this.filterNotNull())
    }
}


fun <K, V> Map<K, V>.getKey(value: V): K? {
    for (entry in entries) {
        if (value === entry.value) {
            return entry.key
        }
    }
    return null
}

fun <T> Collection<T>?.isNullOrEmpty(): Boolean {
    return this == null || isEmpty()
}

fun <T> Collection<T>?.isNotNullAndEmpty(): Boolean {
    return !isNullOrEmpty()
}

val <T : Any>T.weak: WeakReference<T>?
    get() {
        return WeakReference(this)
    }

infix fun <T> Boolean.then(value: T?)
        = if (this) value else null


fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> (Unit)) {
    if (value1 != null && value2 != null) {
        bothNotNull(value1, value2)
    }
}

fun <T1, T2, T3 > ifNotNull(value1: T1?, value2: T2?, value3: T3?, bothNotNull: (T1, T2, T3 ) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null ) {
        bothNotNull(value1, value2, value3)
    }
}

fun <T1, T2, T3, T4> ifNotNull(value1: T1?, value2: T2?, value3: T3?, value4: T4?,  bothNotNull: (T1, T2, T3, T4) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null && value4 != null ) {
        bothNotNull(value1, value2, value3, value4)
    }
}

fun <T1, T2, T3, T4, T5> ifNotNull(value1: T1?, value2: T2?, value3: T3?, value4: T4?, value5: T5?, bothNotNull: (T1, T2, T3, T4, T5) -> (Unit)) {
    if (value1 != null && value2 != null && value3 != null && value4 != null && value5 != null) {
        bothNotNull(value1, value2, value3, value4, value5)
    }
}