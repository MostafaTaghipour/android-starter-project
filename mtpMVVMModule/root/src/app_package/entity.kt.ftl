package ${appPackage}.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "${moduleNameLower}_table")
data class ${moduleName}Entity(
        @PrimaryKey
        val id: Long ,
        val title: String
)
