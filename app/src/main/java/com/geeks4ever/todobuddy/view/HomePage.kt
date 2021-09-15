package com.geeks4ever.todobuddy.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.todobuddy.R
import com.geeks4ever.todobuddy.view.adaptor.NoteAdapter
import com.geeks4ever.todobuddy.view.adaptor.NoteClickDeleteInterface
import com.geeks4ever.todobuddy.view.adaptor.NoteClickEditInterface
import com.geeks4ever.todobuddy.viewModel.NoteViewModal
import kotlinx.android.synthetic.main.home_page.*
import kotlinx.android.synthetic.main.note_item.*

class HomePage : AppCompatActivity(), NoteClickEditInterface, NoteClickDeleteInterface {

    lateinit var viewModal: NoteViewModal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page)

        applicationContext.getSharedPreferences("log", 0).edit()
        .putInt("currentLogName", applicationContext.getSharedPreferences("log", 0)
            .getInt("currentLogName", 1)+1).apply()

        notesRV.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteAdapter(this, this)
        notesRV.adapter = noteRVAdapter
        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)
       viewModal.allNotes.observe(this, Observer { list ->
            list?.let {
                noteRVAdapter.updateList(it)
            }
        })
        idFAB.setOnClickListener {
            val intent = Intent(this@HomePage, AddEditNotePage::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    override fun onEditButtonClick(noteModel: NoteModel) {
        val intent = Intent(this@HomePage, AddEditNotePage::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", noteModel.noteTitle)
        intent.putExtra("noteDescription", noteModel.noteDescription)
        intent.putExtra("noteId", noteModel.id)
        startActivity(intent)
    }

    override fun onDeleteIconClick(noteModel: NoteModel) {
        viewModal.deleteNote(noteModel)
        Toast.makeText(this, "${noteModel.noteTitle} Deleted", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        viewModal.encryptFile()
        super.onDestroy()
    }

}