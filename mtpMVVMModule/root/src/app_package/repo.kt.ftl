package ${appPackage}.modules.${moduleNameLower}


<#if includeLocale>
import ${appPackage}.data.local.${moduleName}Dao
import ${appPackage}.data.model.entity.${moduleName}Entity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import ir.rainyday.android.common.helpers.ioThread
</#if>
<#if includeWebApi>
import ${appPackage}.data.remote.${moduleName}Api
import ${appPackage}.data.model.json.${moduleName}Json
import ${appPackage}.net.composeForHttpTasks
import io.reactivex.Single
import okhttp3.ResponseBody
</#if>

import ${appPackage}.data.model.dto.${moduleName}DTO
import ${appPackage}.data.model.transformer.toDTO


interface ${moduleName}Repo {
    <#if includeLocale>
    fun getFromDB() : List<${moduleName}DTO>
    fun getFromDB(id:Long) : ${moduleName}DTO?
    fun getFromDBLive() : LiveData<List<${moduleName}DTO>>
    fun upsertIntoDB(item: ${moduleName}Entity)
    fun upsertIntoDB(items: List<${moduleName}Entity>)
    fun deleteFromDB(id:Long)
    </#if>
    <#if includeWebApi>
    fun getFromServer(): Single<List<${moduleName}DTO>>
    fun getFromServer(id:Long): Single<${moduleName}DTO>
    fun insertIntoServer(item: ${moduleName}Json) : Single<ResponseBody>
    fun updateInServer(id:Long,item: ${moduleName}Json) : Single<ResponseBody>
    fun deleteFromServer(id:Long) : Single<ResponseBody>
    </#if>
}

<#if includeLocale && includeWebApi>
class ${moduleName}RepoImp(private val api: ${moduleName}Api, private val dao: ${moduleName}Dao) : ${moduleName}Repo {
<#elseif includeLocale>
class ${moduleName}RepoImp( private val dao: ${moduleName}Dao) : ${moduleName}Repo {
<#else>
class ${moduleName}RepoImp(private val api: ${moduleName}Api) : ${moduleName}Repo {
</#if>

    <#if includeLocale>
    override fun getFromDB(): List<${moduleName}DTO> {
        return dao.getAll().map { it.toDTO() }
    }

    override fun getFromDB(id: Long): ${moduleName}DTO? {
        return dao.get(id)?.toDTO()
    }

    override fun getFromDBLive(): LiveData<List<${moduleName}DTO>> {
       return Transformations.map(dao.getAllLive()){data ->
           return@map data.map { it.toDTO() }
       }
    }

    override fun upsertIntoDB(item: ${moduleName}Entity) = ioThread {
       dao.upsert(item)
    }

    override fun upsertIntoDB(items: List<${moduleName}Entity>) {
        dao.upsert(items.map { it })
    }

     override fun deleteFromDB(id: Long) = ioThread {
       dao.delete(id)
    }
    </#if>

    <#if includeWebApi>
    override fun getFromServer(): Single<List<${moduleName}DTO>> {
       return api.getAll().composeForHttpTasks().map { data ->  data.map { it.toDTO()  } }
    }

    override fun getFromServer(id: Long): Single<${moduleName}DTO> {
        return api.get(id).composeForHttpTasks().map { data ->  data.toDTO() }
    }

    override fun insertIntoServer(item: ${moduleName}Json): Single<ResponseBody> {
       return api.insert(item).composeForHttpTasks()
    }


    override fun updateInServer(id: Long, item: ${moduleName}Json): Single<ResponseBody> {
        return api.update(id,item).composeForHttpTasks()
    }

    override fun deleteFromServer(id: Long): Single<ResponseBody> {
        return api.delete(id).composeForHttpTasks()
    }
    </#if>

    

   
}
