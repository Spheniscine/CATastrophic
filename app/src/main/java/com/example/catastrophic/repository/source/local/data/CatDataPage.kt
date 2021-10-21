package com.example.catastrophic.repository.data

import androidx.room.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types

@Entity(tableName = CatDataPage.TABLE_NAME)
data class CatDataPage(
    @PrimaryKey val id: Int,

    @ColumnInfo(name = "data")
    val data: List<CatData>
) {
    companion object {
        const val TABLE_NAME = "cat_pages"
    }
}

class CatDataListConverter {
    private val moshi = Moshi.Builder().build()

    private val type = Types.newParameterizedType(List::class.java, CatData::class.java)
    private val adapter = moshi.adapter<List<CatData>>(type)

    @TypeConverter
    fun fromJson(string: String): List<CatData>? {
        return runCatching { adapter.fromJson(string) }.getOrNull()
    }

    @TypeConverter
    fun toJson(data: List<CatData>): String {
        return adapter.toJson(data)
    }
}

