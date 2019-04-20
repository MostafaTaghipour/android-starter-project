package  ${appPackage}.data.model.json

import com.google.gson.annotations.SerializedName

data class ${moduleName}Json(
        @SerializedName("Id") val id: Long,
        @SerializedName("Title") val title: String
)
