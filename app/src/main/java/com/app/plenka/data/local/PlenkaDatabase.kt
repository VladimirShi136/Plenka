package com.app.plenka.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.app.plenka.data.local.entity.MovieEntity
import com.app.plenka.data.local.entity.FavoriteEntity

@Database(entities = [MovieEntity::class, FavoriteEntity::class], version = 4, exportSchema = false)
@TypeConverters(StringListConverters::class)
abstract class PlenkaDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
