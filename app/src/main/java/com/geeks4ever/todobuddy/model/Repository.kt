package com.geeks4ever.todobuddy.model

import android.app.Application
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.roomdatabase.RoomRepository

class Repository(application: Application) {

    private val RoomRepository = RoomRepository(application)

    val allNotes = RoomRepository.allNotes

    suspend fun deleteNote(noteModel: NoteModel) =
        RoomRepository.delete(noteModel)

    suspend fun updateNote(noteModel: NoteModel) =
        RoomRepository.update(noteModel)

    suspend fun addNote(noteModel: NoteModel) =
        RoomRepository.insert(noteModel)

}
