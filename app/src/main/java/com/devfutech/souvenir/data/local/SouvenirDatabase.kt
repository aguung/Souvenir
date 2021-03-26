package com.devfutech.souvenir.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.devfutech.souvenir.data.local.entity.Souvenir

@Database(entities = [Souvenir::class], version = 1, exportSchema = false)
abstract class SouvenirDatabase : RoomDatabase() {
    abstract fun souvenirDao(): SouvenirDao
}