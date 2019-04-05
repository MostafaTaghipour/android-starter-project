package ir.rainyday.android.starter.modules.sample

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ir.rainyday.android.starter.net.composeForHttpTasks
import io.reactivex.Single
import ir.rainyday.android.common.helpers.ioThread
import okhttp3.ResponseBody

/**
 * Created by taghipour on 16/10/2017.
 */

interface SampleRepo {
    fun getFromDB() : List<SampleDTO>
    fun getFromDB(id:Long) : SampleDTO?
    fun getFromDBLive() : LiveData<List<SampleDTO>>
    fun getFromServer(): Single<List<SampleDTO>>
    fun getFromServer(id:Long): Single<SampleDTO>
    fun upsertIntoDB(item: SampleEntity)
    fun upsertIntoDB(items: List<SampleEntity>)
    fun insertIntoServer(item: SampleJson) : Single<ResponseBody>
    fun updateInServer(id:Long,item: SampleJson) : Single<ResponseBody>
    fun deleteFromDB(id:Long)
    fun deleteFromServer(id:Long) : Single<ResponseBody>
}


class SampleRepoImp(  private val api: SampleApi ,  private val dao: SampleDao) : SampleRepo {
    override fun getFromDB(): List<SampleDTO> {
        return dao.getAll().map { it.toDTO() }
    }

    override fun getFromDB(id: Long): SampleDTO? {
        return dao.get(id)?.toDTO()
    }

    override fun getFromDBLive(): LiveData<List<SampleDTO>> {
       return Transformations.map(dao.getAllLive()){data ->
           return@map data.map { it.toDTO() }
       }
    }

    override fun getFromServer(): Single<List<SampleDTO>> {
       return api.getAll().composeForHttpTasks().map { data ->  data.map { it.toDTO()  } }
    }

    override fun getFromServer(id: Long): Single<SampleDTO> {
        return api.get(id).composeForHttpTasks().map { data ->  data.toDTO() }
    }

    override fun upsertIntoDB(item: SampleEntity) = ioThread {
       dao.upsert(item)
    }

    override fun upsertIntoDB(items: List<SampleEntity>) {
        dao.upsert(items.map { it })
    }

    override fun insertIntoServer(item: SampleJson): Single<ResponseBody> {
       return api.insert(item).composeForHttpTasks()
    }


    override fun updateInServer(id: Long, item: SampleJson): Single<ResponseBody> {
        return api.update(id,item).composeForHttpTasks()
    }

    override fun deleteFromDB(id: Long) = ioThread {
       dao.delete(id)
    }

    override fun deleteFromServer(id: Long): Single<ResponseBody> {
        return api.delete(id).composeForHttpTasks()
    }
}