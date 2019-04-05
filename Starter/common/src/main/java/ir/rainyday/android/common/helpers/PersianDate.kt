package ir.rainyday.android.common.helpers


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*



/**
 * Created by Saman on 3/29/2017 AD.
 */

@Suppress("NAME_SHADOWING", "MemberVisibilityCanPrivate", "LocalVariableName", "unused")

class PersianDate {
    /*----- Define Variable ---*/
    /**
     * return time in long value
     *
     * @return Value of time in mile
     */
    var time: Long? = null
        private set
    private var _persianYear: Int = 0
    var persianYear: Int
        get() = _persianYear
        set(value) = setPersianDate(value, _persianMonth, _persianDay, _hour, _minute, _second)

    private var _persianMonth: Int = 0
    var persianMonth: Int
        get() = _persianMonth
        set(value) = setPersianDate(_persianYear, value, _persianDay, _hour, _minute, _second)

    private var _persianDay: Int = 0
    var persianDay: Int
        get() = _persianDay
        set(value) = setPersianDate(_persianYear, _persianMonth, value, _hour, _minute, _second)

    private var _hour: Int = 0
    var hour: Int
        get() = _hour
        set(value) = setPersianDate(_persianYear, _persianMonth, _persianDay, value, _minute, _second)

    private var _minute: Int = 0
    var minute: Int
        get() = _minute
        set(value) = setPersianDate(_persianYear, _persianMonth, _persianDay, _hour, value, _second)

    private var _second: Int = 0
    var second: Int
        get() = _second
        set(value) = setPersianDate(_persianYear, _persianMonth, _persianDay, _hour, _minute, value)


    /**
     * ---- Dont change---
     */
    private val grgSumOfDays = arrayOf(intArrayOf(0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365), intArrayOf(0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366))
    private val hshSumOfDays = arrayOf(intArrayOf(0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 365), intArrayOf(0, 31, 62, 93, 124, 155, 186, 216, 246, 276, 306, 336, 366))
    private val dayNames = arrayOf("شنبه", "یک‌شنبه", "دوشنبه", "سه‌شنبه", "چهارشنبه", "پنج‌شنبه", "جمعه")
    private val monthNames = arrayOf("فروردین", "اردیبهشت", "خرداد", "تیر", "مرداد", "شهریور", "مهر", "آبان", "آذر", "دی", "بهمن", "اسفند")

    /**
     * Check year in Leap
     *
     * @return true or false
     */
    val isLeap: Boolean
        get() = this.isLeap(this.persianYear)

    /**
     * Number days of month
     *
     * @return return days
     */
    val monthDays: Int
        get() = this.getMonthDays(this.persianYear, this.persianMonth)

    /**
     * calcute day in year
     *
     * @return
     */
    val dayInYear: Int
        get() = this.getDayInYear(this.persianMonth, persianDay)
    /**
     * Return Day in difreent date
     *
     * @return
     */
    val dayuntilToday: Long
        get() = this.getDayUntilToday(PersianDate())

    /**
     * Constractou
     */
    constructor() {
        this.time = Date().time
        this.changeTime()
    }

    /**
     * Constractou
     */
    constructor(timeInMilliseconds: Long?) {
        this.time = timeInMilliseconds
        this.changeTime()
    }

    /**
     * Constractou
     */
    constructor(gregorianDate: Date) {
        this.time = gregorianDate.time
        this.changeTime()
    }

    /**
     * initilize date from jallai date
     *
     * @param persianYear  Year in jallali date
     * @param persianMonth Month in Jallali date
     * @param persianDay   daye in Jalalli date
     * @return
     */
    constructor(persianYear: Int, persianMonth: Int, persianDay: Int) : this(persianYear, persianMonth, persianDay, 0, 0, 0)


    /**
     * initilize date from jallai date
     *
     * @param persianYear   Year in jallali date
     * @param persianMonth  Month in Jallali date
     * @param persianDay    daye in Jalalli date
     * @param hour   Hour
     * @param minute Minute
     * @param second Second
     * @return
     */
    constructor(persianYear: Int, persianMonth: Int, persianDay: Int, hour: Int, minute: Int, second: Int) {
        setPersianDate(persianYear, persianMonth, persianDay, hour, minute, second)
    }

    /*---- Setter And getter ----*/

    /**
     * initilize date from jallai date
     *
     * @param persianYear  Year in jallali date
     * @param persianMonth Month in Jallali date
     * @param persianDay   daye in Jalalli date
     * @return
     */
    fun setPersianDate(persianYear: Int, persianMonth: Int, persianDay: Int) {
        setPersianDate(persianYear, persianMonth, persianDay, 0, 0, 0)
    }


    /**
     * initilize date from jallai date
     *
     * @param persianYear   Year in jallali date
     * @param persianMonth  Month in Jallali date
     * @param persianDay    daye in Jalalli date
     * @param hour   Hour
     * @param minute Minute
     * @param second Second
     * @return
     */
    fun setPersianDate(persianYear: Int, persianMonth: Int, persianDay: Int, hour: Int, minute: Int, second: Int) {

        this._persianYear = persianYear
        this._persianMonth = persianMonth
        this._persianDay = persianDay
        this._hour = hour
        this._minute = minute
        this._second = second

        val convert = this.toGregorian(persianYear, persianMonth, persianDay)


        val dtStart = ("" + this.textNumberFilter("" + convert[0]) + "-" + this.textNumberFilter("" + convert[1]) + "-" + this.textNumberFilter("" + convert[2])
                + "T" + this.textNumberFilter("" + hour) + ":" + this.textNumberFilter("" + minute) + ":" + this.textNumberFilter("" + second) + "Z")
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date?
        try {
            date = format.parse(dtStart)
            this.time = date!!.time
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    /**
     * init without time
     *
     * @param gregorianYear  Yera in Grg
     * @param gregorianMonth Month in Grg
     * @param gregorianDay   Day in Grg
     * @return
     */
    fun setGregorianDate(gregorianYear: Int, gregorianMonth: Int, gregorianDay: Int) {
        setGregorianDate(gregorianYear, gregorianMonth, gregorianDay, 0, 0, 0)
    }

    /**
     * init with Grg data
     *
     * @param gregorianYear   Year in Grg
     * @param gregorianMonth  Month in Grg
     * @param gregorianDay    day in Grg
     * @param hour   hour
     * @param minute min
     * @param second secon
     * @return
     */
    fun setGregorianDate(gregorianYear: Int, gregorianMonth: Int, gregorianDay: Int, hour: Int, minute: Int, second: Int) {
        val convert = this.toPersian(gregorianYear, gregorianMonth, gregorianDay)

        this._persianYear = convert[0]
        this._persianMonth = convert[1]
        this._persianDay = convert[2]

        val dtStart = ("" + this.textNumberFilter("" + gregorianYear) + "-" + this.textNumberFilter("" + gregorianMonth) + "-" + this.textNumberFilter("" + gregorianDay)
                + "T" + this.textNumberFilter("" + hour) + ":" + this.textNumberFilter("" + minute) + ":" + this.textNumberFilter("" + second) + "Z")
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val date: Date?
        try {
            date = format.parse(dtStart)
            this.time = date!!.time

            val calendar = Calendar.getInstance()
            calendar.time = date
            this._hour = calendar.get(Calendar.HOUR_OF_DAY)
            this._minute = calendar.get(Calendar.MINUTE)
            this._second = calendar.get(Calendar.SECOND)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

    }

    /**
     * Check Grg year is leap
     *
     * @param gregorianYear
     * @return
     */
    private fun gregorianIsLeap(gregorianYear: Int): Boolean {
        return gregorianYear % 4 == 0 && (gregorianYear % 100 != 0 || gregorianYear % 400 == 0)
    }

    /**
     * Check custome year is leap
     *
     * @param persianYear int year
     * @return true or false
     */
    fun isLeap(persianYear: Int): Boolean {
        var Year = persianYear.toDouble()
        Year = (Year - 474) % 128
        Year += (if (Year >= 30) 0 else 29)
        Year = Year - Math.floor(Year / 33) - 1.0
        return Year % 4 == 0.0
    }

    /**
     * Convert Grg date to jalali date
     *
     * @param gregorianYear  year in Grg date
     * @param gregorianMonth month in Grg date
     * @param gregorianDay   day in Grg date
     * @return a int[year][month][gregorianDay] in jalali date
     */
    private fun toPersian(gregorianYear: Int, gregorianMonth: Int, gregorianDay: Int): IntArray {
        var hpersianDay = 0
        var hpersianMonth = 0
        var hshElapsed: Int
        var hpersianYear = gregorianYear - 621
        val grgLeap = this.gregorianIsLeap(gregorianYear)
        var hshLeap = this.isLeap(hpersianYear - 1)
        val grgElapsed = grgSumOfDays[if (grgLeap) 1 else 0][gregorianMonth - 1] + gregorianDay
        val XmasToNorooz = if (hshLeap && grgLeap) 80 else 79
        if (grgElapsed <= XmasToNorooz) {
            hshElapsed = grgElapsed + 286
            hpersianYear--
            if (hshLeap && !grgLeap)
                hshElapsed++
        } else {
            hshElapsed = grgElapsed - XmasToNorooz
            hshLeap = this.isLeap(hpersianYear)
        }
        for (i in 1..12) {
            if (hshSumOfDays[if (hshLeap) 1 else 0][i] >= hshElapsed) {
                hpersianMonth = i
                hpersianDay = hshElapsed - hshSumOfDays[if (hshLeap) 1 else 0][i - 1]
                break
            }
        }
        return intArrayOf(hpersianYear, hpersianMonth, hpersianDay)
    }

    /**
     * Convert Jalali date to Grg
     *
     * @param persianYear  Year in jalali
     * @param persianMonth Month in Jalali
     * @param persianDay   Day in Jalali
     * @return int[year][month][persianDay]
     */
    private fun toGregorian(persianYear: Int, persianMonth: Int, persianDay: Int): IntArray {
        var grgYear = persianYear + 621
        var grgDay = 0
        var grgMonth = 0
        val grgElapsed: Int

        var hshLeap = this.isLeap(persianYear)
        var grgLeap = this.gregorianIsLeap(grgYear)

        val hshElapsed = hshSumOfDays[if (hshLeap) 1 else 0][persianMonth - 1] + persianDay

        if (persianMonth > 10 || persianMonth == 10 && hshElapsed > 286 + if (grgLeap) 1 else 0) {
            grgElapsed = hshElapsed - (286 + if (grgLeap) 1 else 0)
            grgLeap = gregorianIsLeap(++grgYear)
        } else {
            hshLeap = this.isLeap(persianYear - 1)
            grgElapsed = hshElapsed + 79 + (if (hshLeap) 1 else 0) - if (gregorianIsLeap(grgYear - 1)) 1 else 0
        }

        for (i in 1..12) {
            if (grgSumOfDays[if (grgLeap) 1 else 0][i] >= grgElapsed) {
                grgMonth = i
                grgDay = grgElapsed - grgSumOfDays[if (grgLeap) 1 else 0][i - 1]
                break
            }
        }

        return intArrayOf(grgYear, grgMonth, grgDay)
    }

    /**
     * calc day of week
     *
     * @return
     */
    val dayOfWeek: Int
        get() = this.dayOfWeek(this.persianYear, this.persianMonth, this.persianDay)


    /**
     * cal day of the week
     *
     * @param persianYear
     * @param persianMonth
     * @param persianDay
     * @return
     */
    fun dayOfWeek(persianYear: Int, persianMonth: Int, persianDay: Int): Int {
        var value: Int
        value = persianYear - 1376 + hshSumOfDays[0][persianMonth - 1] + persianDay - 1

        for (i in 1380 until persianYear)
            if (this.isLeap(i)) value++
        for (i in persianYear..1379)
            if (this.isLeap(i)) value--

        value %= 7
        if (value < 0) value += 7

        return value
    }

    /**
     * return month name
     *
     * @return
     */
    val monthName: String
        get() = this.monthName(this.persianMonth)


    /**
     * Return month name
     *
     * @param month Month
     * @return
     */
    fun monthName(month: Int): String {
        return this.monthNames[month - 1]
    }

    /**
     * get day name
     *
     * @return
     */
    val dayName: String
        get() = this.dayName(this.persianYear, this.persianMonth, this.persianDay)


    /**
     * Return day name
     *
     * @param persianYear  Year
     * @param persianMonth Month
     * @param persianDay   Day
     * @return
     */
    fun dayName(persianYear: Int, persianMonth: Int, persianDay: Int): String {
        return this.dayNames[this.dayOfWeek(persianYear, persianMonth, persianDay)]
    }

    /**
     * calc count of day in month
     *
     * @param persianYear
     * @param persianMonth
     * @return
     */
    fun getMonthDays(persianYear: Int, persianMonth: Int): Int {
        if (persianMonth == 12 && !this.isLeap(persianYear)) {
            return 29
        }
        return if (persianMonth <= 6) {
            31
        } else {
            30
        }
    }

    /**
     * Calc day of the year
     *
     * @param persianMonth Month
     * @param persianDay   Day
     * @return
     */
    fun getDayInYear(persianMonth: Int, persianDay: Int): Int {
        var day = persianDay
        for (i in 1 until persianMonth) {
            if (i <= 6) {
                day += 31
            } else {
                day += 30
            }
        }
        return day
    }

    /**
     * add to date
     *
     * @param year Number of Year you want add
     * @param month Number of month you want add
     * @param day Number of day you want add
     * @param hour Number of hour you want add
     * @param minute Number of minute you want add
     * @param second Number of second you want add
     * @return
     */
    fun addDate(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int): PersianDate {
        this.time = this.time!! + ((year * 365 + month * 30 + day) * 24 * 3600 * 1000).toLong()
        this.time = this.time!! + ((second + hour * 3600 + minute * 60) * 1000).toLong()
        this.changeTime()
        return this
    }

    /**
     * add to date
     *
     * @param year Number of Year you want add
     * @param month Number of month you want add
     * @param day Number of day you want add
     * @return
     */
    fun addDate(year: Int, month: Int, day: Int): PersianDate {
        return addDate(year, month, day, 0, 0, 0)
    }

    fun addYear(year: Int): PersianDate {
        return addDate(year, 0, 0, 0, 0, 0)
    }

    fun addMonth(month: Int): PersianDate {
        return addDate(0, month, 0, 0, 0, 0)
    }

    fun addWeek(week: Int): PersianDate {
        return addDate(0, 0, week * 7, 0, 0, 0)
    }

    fun addDay(day: Int): PersianDate {
        return addDate(0, 0, day, 0, 0, 0)
    }

    /**
     * Compare 2 date
     *
     * @param dateInput PersianDate type
     *
     * @return
     */
    fun after(dateInput: PersianDate): Boolean {
        return this.time!! < dateInput.time!!
    }

    /**
     * copare to data
     *
     * @param dateInput Input
     * @return
     */
    fun before(dateInput: PersianDate): Boolean {
        return !this.after(dateInput)
    }

    /**
     * Check date equals
     *
     * @param dateInput
     * @return
     */
    fun equals(dateInput: PersianDate): Boolean {
        return this.time === dateInput.time
    }

    /**
     * compare 2 data
     *
     * @param anotherDate
     * @return  0 = equal,1=data1 > anotherDate,-1=data1 > anotherDate
     */
    operator fun compareTo(anotherDate: PersianDate): Int {
        return if (this.time!! < anotherDate.time!!) -1 else if (this.time === anotherDate.time) 0 else 1
    }

    /**
     * Return difreent just day in copare 2 date
     *
     * @param date date for compare
     * @return
     */
    fun getDayUntilToday(date: PersianDate): Long {
        val ret = this.untilToday(date)
        return ret[0]
    }

    /**
     * Calc difreent date until now
     *
     * @return
     */
    fun untilToday(): LongArray {
        return this.untilToday(PersianDate())
    }

    /**
     * calcute diffrent between 2 date
     *
     * @param date Date 1
     * @return
     */
    fun untilToday(date: PersianDate): LongArray {
        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24
        var different = Math.abs(this.time!! - date.time!!)

        val elapsedDays = different / daysInMilli
        different %= daysInMilli

        val elapsedHours = different / hoursInMilli
        different %= hoursInMilli
        val elapsedMinutes = different / minutesInMilli
        different %= minutesInMilli
        val elapsedSeconds = different / secondsInMilli
        return longArrayOf(elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds)
    }
    /*----- Helper Function-----*/

    /**
     * convert PersianDate class to date
     *
     * @return
     */
    fun toGregorian(): Date {
        val offsetInMillis = 0
        val timeInMillis = this.time!! + offsetInMillis
        return Date(timeInMillis)
    }


    /**
     * Helper function
     *
     * @param date
     * @return
     */
    private fun textNumberFilter(date: String): String {
        return if (date.length < 2) {
            "0" + date
        } else date
    }

    /**
     * initi with time in milesecond
     */
    private fun changeTime() {
//        val tz = TimeZone.getTimeZone("Iran")
//        var sdf = SimpleDateFormat("HH")
//        sdf.timeZone = tz
//        val hour = Integer.parseInt(sdf.format(this.time))
//
//        sdf = SimpleDateFormat("mm")
//        val min = Integer.parseInt(sdf.format(this.time))
//
//        sdf = SimpleDateFormat("ss")
//        val sec = Integer.parseInt(sdf.format(this.time))
//       // val localTime = sdf.format(Date(this.time!!)) // I assume your timestamp is in seconds and you're converting to milliseconds?
//
////        this._hour = SimpleDateFormat("this.time")
////        this._minute = calendar.get(Calendar.MINUTE)
////        this._second = calendar.get(Calendar.SECOND)
////
//
//
//        Log.d("Time: ", hour.toString())
//

        this.setGregorianDate(Integer.parseInt(SimpleDateFormat("yyyy").format(this.time)), Integer.parseInt(SimpleDateFormat("MM").format(this.time)),
                Integer.parseInt(SimpleDateFormat("dd").format(this.time)), Integer.parseInt(SimpleDateFormat("HH").format(this.time)),
                Integer.parseInt(SimpleDateFormat("mm").format(this.time)), Integer.parseInt(SimpleDateFormat("ss").format(this.time)))
    }

    companion object {
        val FARVARDIN = 1
        val ORDIBEHEST = 2
        val KHORDAD = 3
        val TIR = 4
        val MORDAD = 5
        val SHAHRIVAR = 6
        val MEHR = 7
        val ABAN = 8
        val AZAR = 9
        val DAY = 10
        val BAHMAN = 11
        val ESFAND = 12
    }
}


class PersianDateFormat {
    //variable
    /**
     * Key for convert Date to String
     *
     * l	Day name in week (شنبه و ....)
     * j	Day number in month(1-10-20...)
     * F	Month name (فروردین)
     * Y	Year (1396...)
     * H	Hour in day
     * i	Minutes in hour
     * s	Second in minute
     * d	day in month (01-02-21...)
     * g	Hour in day 1-12
     * n	Month of year 1-12
     * m	Month of year 01-12
     * t	number day of month
     * w	day in week 0-6
     * y	year with 2 digits
     * z	Number of days (full) past the year
     * L	Is leap year (0-1)
     *
     */
    private val key = arrayOf("l", "j", "F", "Y", "H", "i", "s", "d", "g", "n", "m", "t", "w", "y", "z", "L")
    private var pattern: String? = null
    /**
     * key_parse for convert String to PersianDate
     *
     * yyyy = Year (1396)
     * MM = month (02-12-...)
     * dd = day (13-02-15-...)
     * HH = Hour (13-02-15-...)
     * mm = minutes (13-02-15-...)
     * ss = second (13-02-15-...)
     */
    private val key_parse = arrayOf("yyyy", "MM", "dd", "HH", "mm", "ss")

    /**
     * Constracutor
     *
     * @param pattern
     */
    constructor(pattern: String) {
        this.pattern = pattern
    }

    /**
     * initilize pattern
     */
    constructor() {
        pattern = "l j F Y H:i:s"
    }


    /**
     *
     * l	Day name in week (شنبه و ....)
     * j	Day number in month(1-10-20...)
     * F	Month name (فروردین)
     * Y	Year (1396...)
     * H	Hour in day
     * i	Minutes in hour
     * s	Second in minute
     * d	day in month (01-02-21...)
     * g	Hour in day 1-12
     * n	Month of year 1-12
     * m	Month of year 01-12
     * t	number day of month
     * w	day in week 0-6
     * y	year with 2 digits
     * z	Number of days (full) past the year
     * L	Is leap year (0-1)
     *
     */
    fun format(date: PersianDate): String? {
        val year2: String?
        if (("" + date.persianYear).length == 2) {
            year2 = "" + date.persianYear
        } else if (("" + date.persianYear).length == 3) {
            year2 = ("" + date.persianYear).substring(2, 3)
        } else {
            year2 = ("" + date.persianYear).substring(2, 4)
        }
        val values = arrayOf(date.dayName, "" + date.persianDay, date.monthName, "" + date.persianYear, this.textNumberFilter("" + date.hour), this.textNumberFilter("" + date.minute), this.textNumberFilter("" + date.second), this.textNumberFilter("" + date.persianDay), "" + date.hour, "" + date.persianMonth, this.textNumberFilter("" + date.persianMonth), "" + date.monthDays, "" + date.dayOfWeek, year2, "" + date.dayInYear, if (date.isLeap) "1" else "0")
        return this.stringUtils(this.pattern, this.key, values)
    }

    /**
     * Parse jalli date from String
     *
     * @param date date in string
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parsePersian(date: String): PersianDate {
        return this.parsePersian(date, this.pattern)
    }

    /**
     * Parse jalli date from String
     *
     * @param date date in string
     * @param pattern pattern
     * @return
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parsePersian(date: String, pattern: String?): PersianDate {
        val JalaliDate = object : ArrayList<Int>() {
            init {
                add(0)
                add(0)
                add(0)
                add(0)
                add(0)
                add(0)
            }
        }
        for (i in key_parse.indices) {
            if (pattern!!.indexOf(key_parse[i]) >= 0) {
                val start_temp = pattern.indexOf(key_parse[i])
                val end_temp = start_temp + key_parse[i].length
                val date_replcae = date.substring(start_temp, end_temp)
                if (date_replcae.matches("[-+]?\\d*\\.?\\d+".toRegex())) {
                    JalaliDate[i] = Integer.parseInt(date_replcae)
                } else {
                    throw ParseException("Parse Exeption", 10)
                }

            }
        }
        return PersianDate(JalaliDate[0], JalaliDate[1], JalaliDate[2], JalaliDate[3], JalaliDate[4], JalaliDate[5])
    }

    /**
     * Convert String Grg date to persiand date object
     *
     * @param date date in String
     * @return PersianDate object
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parseGregorian(date: String): PersianDate {
        return this.parseGregorian(date, this.pattern)
    }

    /**
     * Convert String Grg date to persiand date object
     *
     * @param date date String
     * @param pattern pattern
     * @return PersianDate object
     * @throws ParseException
     */
    @Throws(ParseException::class)
    fun parseGregorian(date: String, pattern: String?): PersianDate {
        val dateInGrg = SimpleDateFormat(pattern!!).parse(date)
        return PersianDate(dateInGrg.time)
    }

    /**
     * Replace String
     *
     * @param text   String
     * @param key    Loking for
     * @param values Replace with
     * @return
     */
    private fun stringUtils(text: String?, key: Array<String>, values: Array<String>): String? {
        var res = text
        for (i in key.indices) {
            res = res!!.replace(key[i], values[i])
        }
        return res
    }

    /**
     * add zero to start
     *
     * @param date data
     * @return return string with 0 in start
     */
    private fun textNumberFilter(date: String): String {
        return if (date.length < 2) {
            "0" + date
        } else date
    }

}