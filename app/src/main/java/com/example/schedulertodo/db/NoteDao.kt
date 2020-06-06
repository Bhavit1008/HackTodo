package com.example.schedulertodo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {

    @Insert
    suspend fun addNote(note: Note)

    @Query("SELECT * FROM note ORDER BY id DESC")
    suspend fun getAllNotes() : List<Note>

    @Query("SELECT * FROM note WHERE cat = 'Personal'")
    suspend fun getPersonalTask() : List<Note>

    @Query("SELECT * FROM note WHERE cat = 'Work'")
    suspend fun getWorkTask() : List<Note>

    @Insert
    suspend fun addMultipleNotes(vararg note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    @Query("SELECT COUNT(id) FROM note")
    fun countPersonalTask() : Int
}