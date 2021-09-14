package com.geeks4ever.todobuddy.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.todobuddy.model.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoteViewModal(application: Application) : AndroidViewModel(application) {

    val allNotes: LiveData<List<NoteModel>>
    private val repository: Repository = Repository(application)

    init {
        allNotes = repository.allNotes
    }

    fun deleteNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(noteModel)
    }

    fun updateNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(noteModel)
    }

    fun addNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(noteModel)
    }
}