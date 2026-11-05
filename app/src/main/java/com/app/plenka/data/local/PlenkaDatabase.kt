package com.app.plenka.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.app.plenka.data.local.entity.MovieEntity

@Database(entities = [MovieEntity::class, com.app.plenka.data.local.entity.FavoriteEntity::class], version = 3, exportSchema = false)
abstract class PlenkaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
