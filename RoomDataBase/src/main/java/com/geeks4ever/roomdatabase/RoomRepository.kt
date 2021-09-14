package com.geeks4ever.roomdatabase

import android.app.Application
import androidx.lifecycle.LiveData

class RoomRepository(application: Application) {

    private val notesDao = NoteDatabase.getDatabase(application).getNotesDao()

    val allNotes: LiveData<List<NoteModel>> = notesDao.getAllNotes()

    suspend fun insert(note: NoteModel) {
        notesDao.insert(note)
    }

    suspend fun delete(note: NoteModel) {
        notesDao.delete(note)
    }

    suspend fun update(note: NoteModel) {
        notesDao.update(note)
    }

}