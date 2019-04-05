package ir.rainyday.android.starter.modules.sample

import com.google.gson.annotations.SerializedName

data class SampleJson(
        @SerializedName("Id") val id: Long,
        @SerializedName("Title") val title: String
)