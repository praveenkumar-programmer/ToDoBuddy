package com.geeks4ever.todobuddy.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.todobuddy.R
import com.geeks4ever.todobuddy.view.adaptor.NoteAdapter
import com.geeks4ever.todobuddy.view.adaptor.NoteClickDeleteInterface
import com.geeks4ever.todobuddy.view.adaptor.NoteClickInterface
import com.geeks4ever.todobuddy.viewModel.NoteViewModal
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomePage : AppCompatActivity(), NoteClickInterface, NoteClickDeleteInterface {

    //on below line we are creating a variable for our recycler view, exit text, button and viewmodal.
    lateinit var viewModal: NoteViewModal
    lateinit var notesRV: RecyclerView
    lateinit var addFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)
        //on below line we are initializing all our variables.
        notesRV = findViewById(R.id.notesRV)
        addFAB = findViewById(R.id.idFAB)
        //on below line we are setting layout manager to our recycler view.
        notesRV.layoutManager = LinearLayoutManager(this)
        //on below line we are initializing our adapter class.
        val noteRVAdapter = NoteAdapter(this, this, this)
        //on below line we are setting adapter to our recycler view.
        notesRV.adapter = noteRVAdapter
        //on below line we are initializing our view modal.
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
        //on below line we are calling all notes methof from our view modal class to observer the changes on list.
        viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                //on below line we are updating our list.
                noteRVAdapter.updateList(it)
            }
        })
        addFAB.setOnClickListener {
            //adding a click listner for fab button and opening a new intent to add a new note.
            val intent = Intent(this@HomePage, AddEditNotePage::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onNoteClick(noteModel: NoteModel) {
        //opening a new intent and passing a data to it.
        val intent = Intent(this@HomePage, AddEditNotePage::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", noteModel.noteTitle)
        intent.putExtra("noteDescription", noteModel.noteDescription)
        intent.putExtra("noteId", noteModel.id)
        startActivity(intent)
        this.finish()
    }

    override fun onDeleteIconClick(noteModel: NoteModel) {
        //in on note click method we are calling delete method from our viw modal to delete our not.
        viewModal.deleteNote(noteModel)
        //displaying a toast message
        Toast.makeText(this, "${noteModel.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }
}