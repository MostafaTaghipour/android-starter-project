package ir.rainyday.android.starter.modules.sample

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ir.rainyday.android.starter.modules.shared.base.BaseDao

/**
 * Created by taghipour on 20/11/2017.
 */


@Dao
interface SampleDao : BaseDao<SampleEntity>  {

    @Query("select * from sample_table")
    fun getAll(): List<SampleEntity>


    @Query("select * from sample_table " +
            "WHERE id = :id " )
    fun get(id:Long): SampleEntity?


    @Query("select * from sample_table")
    fun getAllLive(): LiveData<List<SampleEntity>>


    @Query("DELETE FROM sample_table " +
            "WHERE id = :id")
    fun delete(id: Long)


    @Query("DELETE FROM sample_table")
    fun clear()
}