package com.github.spheniscine.catastrophic.repository.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.github.spheniscine.catastrophic.repository.data.CatDataPage

@Dao
interface CatPageDao {
    @Query("SELECT * FROM ${CatDataPage.TABLE_NAME} WHERE id=:id")
    suspend fun loadSingle(id: Int): CatDataPage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CatDataPage)
}