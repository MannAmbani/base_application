package com.rnd.baseproject.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.rnd.baseproject.database.entity.Note

@Dao
interface NoteDao {

    /**This will insert the note into database*/
    @Insert
    suspend fun addNote(note: Note)
    /**Suspend a function means we cannot use it directly .. we need coroutine scope to call this function*/
    /**Show All Notes */
    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes():List<Note>

    /**Insert multiple notes to db*/
    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)
}