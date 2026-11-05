package com.app.plenka.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * 2 -> 3: у кэша списков составной ключ (id, category).
 * Кэш можно пересоздать, а избранное (favorite_movies) остаётся нетронутым.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `movies_cache`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `movies_cache` (" +
                "`id` INTEGER NOT NULL, `title` TEXT NOT NULL, `originalTitle` TEXT, " +
                "`posterUrl` TEXT NOT NULL, `posterUrlPreview` TEXT NOT NULL, `year` INTEGER, " +
                "`rating` REAL, `genres` TEXT NOT NULL, `category` TEXT NOT NULL, " +
                "`page` INTEGER NOT NULL, PRIMARY KEY(`id`, `category`))"
        )
    }
}
