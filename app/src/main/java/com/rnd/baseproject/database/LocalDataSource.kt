package com.rnd.baseproject.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rnd.baseproject.database.dao.NoteDao
import com.rnd.baseproject.database.entity.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = true,
    autoMigrations = []
)
abstract class LocalDataSource:RoomDatabase() {
    abstract fun getNoteDao():NoteDao
}