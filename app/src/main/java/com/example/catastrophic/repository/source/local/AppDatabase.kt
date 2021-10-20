package com.example.catastrophic.repository.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.catastrophic.repository.data.CatDataListConverter
import com.example.catastrophic.repository.data.CatDataPage

@Database(entities = [CatDataPage::class], version = 1)
@TypeConverters(CatDataListConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun catPageDao(): CatPageDao
}