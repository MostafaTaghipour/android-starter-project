@file:Suppress("INTEGER_OVERFLOW", "DEPRECATED_IDENTITY_EQUALS")

package ir.rainyday.android.common.helpers

import android.content.Context
import ir.rainyday.android.common.R
import java.text.DateFormat
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*


/**
 * Created by taghipour on 15/10/2017.
 */


enum class StandardDateFormat(val value: String) {
    RFC_1123("EEE, dd MMM yyyy HH:mm:ss z"),
    ISO_8601_DATE("yyyy-MM-dd"),
    ISO_8601_DATE_TIME("yyyy-MM-dd'T'HH:mm:ssZ"),
    ISO_8601_DATE_TIME_MILLIS("yyyy-MM-dd'T'HH:mm:ss.SSSZ"),
    ISO_8601_DATE_TIME_MILLIS_NO_TIMEZONE("yyyy-MM-dd'T'HH:mm:ss.SSS")
}

object DateFormatter {
    private fun fixedDateFormatter(format: StandardDateFormat): DateFormat {
        return SimpleDateFormat(format.value, Locale.ROOT).apply {
            timeZone = TimeZone.getTimeZone("GMT+00:00")
        }
    }

    val iso8601Date: DateFormat
        get() = fixedDateFormatter(StandardDateFormat.ISO_8601_DATE)


    val iso8601DateTime: DateFormat
        get() = fixedDateFormatter(StandardDateFormat.ISO_8601_DATE_TIME)


    val iso8601DateTimeMillis: DateFormat
        get() = fixedDateFormatter(StandardDateFormat.ISO_8601_DATE_TIME_MILLIS)

    val rfc1123: DateFormat
        get() = fixedDateFormatter(StandardDateFormat.RFC_1123)
}


object DateHelper {

    private val SUPPORTED_ISO_8601_PATTERNS = arrayOf(StandardDateFormat.ISO_8601_DATE_TIME.value, StandardDateFormat.ISO_8601_DATE_TIME_MILLIS.value)
    private val TICK_MARK_COUNT = 2
    private val COLON_PREFIX_COUNT = "+00".length
    private val COLON_INDEX = 22


    @JvmOverloads
    fun stringToDate(dateStr: String, format: String = "dd-MM-yyyy", locale: Locale = Locale.ENGLISH): Date? {
        val frmt = SimpleDateFormat(format, locale)
        return try {
            frmt.parse(dateStr)
        } catch (e: ParseException) {
            null
        }
    }

    fun newDateInstance(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DATE, day)
        return calendar.getTime()
    }


    /**
     * Parses a date from the specified RFC 1123-compliant string.
     *
     * @param string the string to parsePersian
     * @return the [Date] resulting from the parsing, or null if the string could not be
     * parsed
     */
    fun parseRfc1123DateTime(string: String): Date? {
        try {
            return SimpleDateFormat(StandardDateFormat.RFC_1123.value, Locale.US).parse(string)
        } catch (e: ParseException) {
            return null
        }

    }

    /**
     * Formats the specified date to an RFC 1123-compliant string.
     *
     * @param date     the date to format
     * @param timeZone the [TimeZone] to use when formatting the date
     * @return the formatted string
     */
    fun formatRfc1123DateTime(date: Date, timeZone: TimeZone?): String {
        val dateFormat = SimpleDateFormat(StandardDateFormat.RFC_1123.value, Locale.US)
        if (timeZone != null) {
            dateFormat.timeZone = timeZone
        }
        return dateFormat.format(date)
    }

    /**
     * Parses a date from the specified ISO 8601-compliant string.
     *
     * @param string the string to parsePersian
     * @return the [Date] resulting from the parsing, or null if the string could not be
     * parsed
     */
    fun parseIso8601DateTime(string: String?): Date? {
        if (string == null) {
            return null
        }
        val s = string.replace("Z", "+00:00")
        for (pattern in SUPPORTED_ISO_8601_PATTERNS) {
            var str = s
            val colonPosition = pattern.lastIndexOf('Z') - TICK_MARK_COUNT + COLON_PREFIX_COUNT
            if (str.length > colonPosition) {
                str = str.substring(0, colonPosition) + str.substring(colonPosition + 1)
            }
            try {
                return SimpleDateFormat(pattern, Locale.US).parse(str)
            } catch (e: ParseException) {
                // try the next one
            }

        }
        return null
    }

    /**
     * Formats the specified date to an ISO 8601-compliant string.
     *
     * @param date     the date to format
     * @param timeZone the [TimeZone] to use when formatting the date
     * @return the formatted string
     */
    fun formatIso8601DateTime(date: Date, timeZone: TimeZone?): String? {
        val dateFormat = SimpleDateFormat(StandardDateFormat.ISO_8601_DATE_TIME.value, Locale.US)
        if (timeZone != null) {
            dateFormat.timeZone = timeZone
        }
        var formatted: String? = dateFormat.format(date)
        if (formatted != null && formatted.length > COLON_INDEX) {
            formatted = formatted.substring(0, 22) + ":" + formatted.substring(22)
        }
        return formatted
    }


    //-----------------------------------------------------------------------
    /**
     *
     * Checks if two date objects are on the same day ignoring time.
     *
     *
     * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     *
     *
     * @param date1  the first date, not altered, not null
     * @param date2  the second date, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either date is `null`
     * @since 2.1
     */
    fun isSameDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isSameDay(cal1, cal2)
    }

    /**
     *
     * Checks if two calendar objects are on the same day ignoring time.
     *
     *
     * 28 Mar 2002 13:45 and 28 Mar 2002 06:01 would return true.
     * 28 Mar 2002 13:45 and 12 Mar 2002 13:45 would return false.
     *
     *
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same day
     * @throws IllegalArgumentException if either calendar is `null`
     * @since 2.1
     */
    fun isSameDay(cal1: Calendar, cal2: Calendar): Boolean {
        return cal1.get(Calendar.ERA) === cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) === cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     *
     * Checks if a date is today.
     * @param date the date, not altered, not null.
     * @return true if the date is today.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isToday(date: Date): Boolean {
        return isSameDay(date, Calendar.getInstance().time)
    }

    /**
     *
     * Checks if a calendar date is today.
     * @param cal  the calendar, not altered, not null
     * @return true if cal date is today
     * @throws IllegalArgumentException if the calendar is `null`
     */
    fun isToday(cal: Calendar): Boolean {
        return isSameDay(cal, Calendar.getInstance())
    }


    /**
     *
     * Checks if the first date is before the second date ignoring time.
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is before the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isBeforeDay(date1: Date, date2: Date): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isBeforeDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is before the second calendar date ignoring time.
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is before cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isBeforeDay(cal1: Calendar, cal2: Calendar): Boolean {
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return true
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) false else cal1.get(Calendar.DAY_OF_YEAR) < cal2.get(Calendar.DAY_OF_YEAR)
    }

    /**
     *
     * Checks if the first date is after the second date ignoring time.
     * @param date1 the first date, not altered, not null
     * @param date2 the second date, not altered, not null
     * @return true if the first date day is after the second date day.
     * @throws IllegalArgumentException if the date is `null`
     */
    fun isAfterDay(date1: Date?, date2: Date?): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return isAfterDay(cal1, cal2)
    }

    /**
     *
     * Checks if the first calendar date is after the second calendar date ignoring time.
     * @param cal1 the first calendar, not altered, not null.
     * @param cal2 the second calendar, not altered, not null.
     * @return true if cal1 date is after cal2 date ignoring time.
     * @throws IllegalArgumentException if either of the calendars are `null`
     */
    fun isAfterDay(cal1: Calendar, cal2: Calendar): Boolean {
        if (cal1.get(Calendar.ERA) < cal2.get(Calendar.ERA)) return false
        if (cal1.get(Calendar.ERA) > cal2.get(Calendar.ERA)) return true
        if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) return false
        return if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) true else cal1.get(Calendar.DAY_OF_YEAR) > cal2.get(Calendar.DAY_OF_YEAR)
    }

    //-----------------------------------------------------------------------
    /**
     *
     * Checks if two calendar objects represent the same local time.
     *
     *
     * This method compares the values of the fields of the two objects.
     * In addition, both calendars must be the same of the same type.
     *
     * @param cal1  the first calendar, not altered, not null
     * @param cal2  the second calendar, not altered, not null
     * @return true if they represent the same millisecond instant
     * @throws IllegalArgumentException if either date is `null`
     * @since 2.1
     */
    fun isSameLocalTime(cal1: Calendar?, cal2: Calendar?): Boolean {
        if (cal1 == null || cal2 == null) {
            throw IllegalArgumentException("The date must not be null")
        }
        return cal1.get(Calendar.MILLISECOND) === cal2.get(Calendar.MILLISECOND) &&
                cal1.get(Calendar.SECOND) === cal2.get(Calendar.SECOND) &&
                cal1.get(Calendar.MINUTE) === cal2.get(Calendar.MINUTE) &&
                cal1.get(Calendar.HOUR_OF_DAY) === cal2.get(Calendar.HOUR_OF_DAY) &&
                cal1.get(Calendar.DAY_OF_YEAR) === cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) === cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.ERA) === cal2.get(Calendar.ERA) &&
                cal1.javaClass === cal2.javaClass
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of years to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addYears(date: Date, amount: Int): Date {
        return add(date, Calendar.YEAR, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of months to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addMonths(date: Date, amount: Int): Date {
        return add(date, Calendar.MONTH, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of weeks to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addWeeks(date: Date, amount: Int): Date {
        return add(date, Calendar.WEEK_OF_YEAR, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of days to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addDays(date: Date, amount: Int): Date {
        return add(date, Calendar.DAY_OF_MONTH, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of hours to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addHours(date: Date, amount: Int): Date {
        return add(date, Calendar.HOUR_OF_DAY, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of minutes to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addMinutes(date: Date, amount: Int): Date {
        return add(date, Calendar.MINUTE, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of seconds to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addSeconds(date: Date, amount: Int): Date {
        return add(date, Calendar.SECOND, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun addMilliseconds(date: Date, amount: Int): Date {
        return add(date, Calendar.MILLISECOND, amount)
    }

    //-----------------------------------------------------------------------
    /**
     * Adds to a date returning a new object.
     * The original `Date` is unchanged.
     *
     * @param date  the date, not null
     * @param calendarField  the calendar field to add to
     * @param amount  the amount to add, may be negative
     * @return the new `Date` with the amount added
     * @throws IllegalArgumentException if the date is null
     */
    fun add(date: Date, calendarField: Int, amount: Int): Date {
        val c = Calendar.getInstance()
        c.time = date
        c.add(calendarField, amount)
        return c.time
    }


//    private val SECOND_MILLIS = 1000
//    private val MINUTE_MILLIS = 60 * SECOND_MILLIS
//    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
//    private val DAY_MILLIS = 24 * HOUR_MILLIS
//    private val WEEK_MILLIS = 7 * DAY_MILLIS
//    private val MONTH_MILLIS = 30 * DAY_MILLIS
//    private val YEAR_MILLIS = 365 * DAY_MILLIS
//
//    fun getTimeAgo(context: Context, date: Date): String {
//        var time = date.time
//        if (time < 1000000000000L) {
//            // if timestamp given in seconds, convert to millis
//            time *= 1000
//        }
//
//        val now = Calendar.getInstance().time.time
//        if (time > now || time <= 0) {
//            return time.toString()
//        }
//
//        val diff = now - time
//        return if (diff < 10 * SECOND_MILLIS) {
//            context.getString(R.string.just_now)
//        } else if (diff < MINUTE_MILLIS) {
//            context.getString(R.string.moments_ago)
//        } else if (diff < 2 * MINUTE_MILLIS) {
//            context.getString(R.string.a_min_ago)
//        } else if (diff < 1 * HOUR_MILLIS) {
//          context.getString(R.string.min_ago,  (diff / MINUTE_MILLIS).toString())
//        }
//        else if (diff < 2 * HOUR_MILLIS) {
//            context.getString(R.string.an_hour_ago)
//        } else if (diff < 1 * DAY_MILLIS) {
//            context.getString(R.string.hour_ago,  (diff / HOUR_MILLIS).toString())
//        }
//
//        else if (diff < 2 * DAY_MILLIS) {
//            context.getString(R.string.yesterday)
//        } else if (diff < 1 * WEEK_MILLIS) {
//            context.getString(R.string.day_ago,  (diff / DAY_MILLIS).toString())
//        }
//
//        else if (diff < 2 * WEEK_MILLIS) {
//            context.getString(R.string.last_week)
//        } else if (diff < 1 * MONTH_MILLIS) {
//            context.getString(R.string.week_ago,  (diff / WEEK_MILLIS).toString())
//        }
//        else if (diff < 2 * MONTH_MILLIS) {
//            context.getString(R.string.last_month)
//        } else if (diff < 1 * YEAR_MILLIS) {
//            context.getString(R.string.month_ago,  (diff / MONTH_MILLIS).toString())
//        }
//        else if (diff < 2 * YEAR_MILLIS) {
//            context.getString(R.string.last_year)
//        } else  {
//            context.getString(R.string.year_ago,  (diff / YEAR_MILLIS).toString())
//        }
//
//    }
}


class TimeAgo(protected var context: Context) {

    fun timeAgo(date: Date): String {
        return timeAgo(date.time)
    }

    fun timeAgo(millis: Long): String {
        val diff = Date().time - millis

        val r = context.resources

        val prefix = r.getString(R.string.time_ago_prefix)
        val suffix = r.getString(R.string.time_ago_suffix)

        val seconds = (Math.abs(diff) / 1000).toDouble()
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val years = days / 365

        val words: String

        if (seconds < 10) {
            words = r.getString(R.string.time_ago_moment)
        }
        else if (seconds < 45) {
            words = r.getString(R.string.time_ago_seconds)
        } else if (seconds < 90) {
            words = r.getString(R.string.time_ago_minute)
        } else if (minutes < 45) {
            words = r.getString(R.string.time_ago_minutes, Math.round(minutes))
        } else if (minutes < 90) {
            words = r.getString(R.string.time_ago_hour)
        } else if (hours < 24) {
            words = r.getString(R.string.time_ago_hours, Math.round(hours))
        } else if (hours < 42) {
            words = r.getString(R.string.time_ago_day)
        } else if (days < 30) {
            words = r.getString(R.string.time_ago_days, Math.round(days))
        } else if (days < 45) {
            words = r.getString(R.string.time_ago_month)
        } else if (days < 365) {
            words = r.getString(R.string.time_ago_months, Math.round(days / 30))
        } else if (years < 1.5) {
            words = r.getString(R.string.time_ago_year)
        } else {
            words = r.getString(R.string.time_ago_years, Math.round(years))
        }

        val sb = StringBuilder()

        if (prefix.isNotEmpty()) {
            sb.append(prefix).append(" ")
        }

        sb.append(words)

        if (suffix.isNotEmpty()) {
            sb.append(" ").append(suffix)
        }

        return sb.toString().trim { it <= ' ' }
    }
}








fun String.toDate(format: String = "dd-MM-yyyy", locale: Locale = Locale.ENGLISH): Date? {
    return DateHelper.stringToDate(this, format, locale)
}

fun String.toRfc1123Date(): Date? {
    return DateHelper.parseRfc1123DateTime(string = this)
}

fun String.toIso8601Date(): Date? {
    return DateHelper.parseIso8601DateTime(this)
}

fun String.toPersianDate(format: String = "YYYY/MM/dd"): PersianDate {
    return PersianDateFormat().parsePersian(this, format)
}

fun Date.toPersianDate(): PersianDate {
    return PersianDate(this)
}


fun Date.toFormattedString(format: String = "MM/dd/yyyy HH:mm:ss", locale: Locale = Locale.ENGLISH): String {
    return SimpleDateFormat(format, locale).format(this)
}

fun PersianDate.toFormattedString(format: String = "Y/m/d"): String? {
    return PersianDateFormat(format).format(this)
}


fun yesterday(): Date {
    return DateHelper.addDays(Date(), -1)
}

fun tomorrow(): Date {
    return DateHelper.addDays(Date(), 1)
}

fun today(): Date {
    return Date()
}


val Date.isToday: Boolean
    get() = DateHelper.isToday(this)


val Date.isPassed: Boolean
    get() {
        return Date().after(this)
    }


val Date.notPassed: Boolean
    get() {
        return !isPassed
    }

fun Date.isSameDay(otherDate: Date): Boolean {
    return DateHelper.isSameDay(this, otherDate)
}

fun Date.addYears(amount: Int): Date {
    return DateHelper.add(this, Calendar.YEAR, amount)
}

fun Date.addMonths(amount: Int): Date {
    return DateHelper.add(this, Calendar.MONTH, amount)
}

fun Date.addWeeks(amount: Int): Date {
    return DateHelper.add(this, Calendar.WEEK_OF_YEAR, amount)
}

fun Date.addDays(amount: Int): Date {
    return DateHelper.add(this, Calendar.DAY_OF_MONTH, amount)
}

fun Date.addHours(amount: Int): Date {
    return DateHelper.add(this, Calendar.HOUR_OF_DAY, amount)
}

fun Date.addMinutes(amount: Int): Date {
    return DateHelper.add(this, Calendar.MINUTE, amount)
}

fun Date.addSeconds(amount: Int): Date {
    return DateHelper.add(this, Calendar.SECOND, amount)
}

fun Date.addMilliseconds(amount: Int): Date {
    return DateHelper.add(this, Calendar.MILLISECOND, amount)
}
