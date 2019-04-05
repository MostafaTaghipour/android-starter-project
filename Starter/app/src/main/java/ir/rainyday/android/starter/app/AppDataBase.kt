package ir.rainyday.android.starter.app

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ir.rainyday.android.starter.modules.sample.SampleDao
import ir.rainyday.android.starter.modules.sample.SampleEntity
import ir.rainyday.android.common.helpers.ioThread
import java.util.*


/**
 * Created by taghipour on 20/11/2017.
 */
@Database(entities = arrayOf(
        SampleEntity::class
        //todo add entities here
), version = 1, exportSchema = false)
@TypeConverters(
        CommonConverter::class
)
abstract class AppDataBase : RoomDatabase() {
    fun clean() = ioThread {
        sampleDao().clear()
        //todo clear dao here
    }

    //DAOs
    abstract fun sampleDao(): SampleDao
    //todo declare dao here
}


object CommonConverter {

    @TypeConverter
    @JvmStatic
    fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter
    @JvmStatic
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }


    @TypeConverter
    @JvmStatic
    fun fromJsonString(value: String): ArrayList<Long> {
        val listType = object : TypeToken<ArrayList<Long>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    @JvmStatic
    fun fromLongArrayList(list: ArrayList<Long>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}