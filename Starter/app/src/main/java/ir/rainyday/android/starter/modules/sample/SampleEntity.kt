package ir.rainyday.android.starter.modules.sample

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sample_table")
data class SampleEntity(
        @PrimaryKey
        val id: Long ,
        val title: String
)