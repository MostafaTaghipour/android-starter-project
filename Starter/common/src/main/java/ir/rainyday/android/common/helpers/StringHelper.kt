package ir.rainyday.android.common.helpers

/**
* Created by m_taghipour on 5/25/2016.
*/


fun <T> List<T>.toSeparatedString(separator: String, trimEnd: Boolean): String {
    return StringHelper.toSeparatedString(this, separator, trimEnd)
}

fun CharSequence.toPascalCase(): String {
    return substring(0, 1).toUpperCase() + substring(1)
}

fun CharSequence.toCamelCase(): String {
    return substring(0, 1).toLowerCase() + substring(1)
}


fun CharSequence?.isNotNullAndEmpty(): Boolean {
    return !isNullOrEmpty()
}

fun CharSequence.transformToPersian(): String {
    return StringHelper.transformToPersian(this.toString())
}

fun CharSequence.transformToLatin(): String {
    return StringHelper.transformToLatin(this.toString())
}

fun Long.thousandSeparator(): String {
    return StringHelper.thousandSeparator(this)
}