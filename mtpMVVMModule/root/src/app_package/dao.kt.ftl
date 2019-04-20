package ${appPackage}.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import ${appPackage}.data.model.entity.${moduleName}Entity

//todo add dao to AppDataBase
@Dao
interface ${moduleName}Dao : BaseDao<${moduleName}Entity>  {

    @Query("select * from ${moduleNameLower}_table")
    fun getAll(): List<${moduleName}Entity>


    @Query("select * from ${moduleNameLower}_table " +
            "WHERE id = :id " )
    fun get(id:Long): ${moduleName}Entity?


    @Query("select * from ${moduleNameLower}_table")
    fun getAllLive(): LiveData<List<${moduleName}Entity>>


    @Query("DELETE FROM ${moduleNameLower}_table " +
            "WHERE id = :id")
    fun delete(id: Long)


    @Query("DELETE FROM ${moduleNameLower}_table")
    fun clear()
}
