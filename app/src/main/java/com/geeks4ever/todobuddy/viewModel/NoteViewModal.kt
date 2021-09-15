package com.geeks4ever.todobuddy.viewModel

import android.Manifest
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Environment
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.todobuddy.background.EncryptWorker
import com.geeks4ever.todobuddy.model.Repository
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class NoteViewModal(application: Application) : AndroidViewModel(application) {

    val allNotes: LiveData<List<NoteModel>>
    private val repository: Repository = Repository(application)
    private val context = application

    init {
        allNotes = repository.allNotes
        allNotes.observeForever{
            it?.let {
                viewModelScope.launch(Dispatchers.IO) {
                    var data: String = ""
                    for(node in it){
                        data = "$data${node.noteTitle}, "
                    }
                    writeToDisk(context, "Read : $data\n")
                }
            }
        }
    }

    fun deleteNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteNote(noteModel)
        writeToDisk(context, "Delete : ${noteModel.noteTitle}\n")
    }

    fun updateNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateNote(noteModel)
        writeToDisk(context, "Update : ${noteModel.noteTitle}\n")
    }

    fun addNote(noteModel: NoteModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.addNote(noteModel)
        writeToDisk(context, "Insert : ${noteModel.noteTitle}\n")
    }

    fun encryptFile(){
        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(EncryptWorker::class.java).build()
        WorkManager.getInstance(context).enqueue(oneTimeWorkRequest)
    }

    private fun writeToDisk(context: Context, data: String){

        var permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            permissions = listOf(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }


        Dexter.withContext(context)
            .withPermissions(permissions)
            .withListener(object : MultiplePermissionsListener {

                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {

                        try {

                            val filename = "Log_${context.getSharedPreferences("log", 0)
                                .getInt("currentLogName", 1)}.txt"

                            val dir: File? = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                            val fileWithinMyDir = File(dir, filename)
                            val out = FileOutputStream(fileWithinMyDir, true)

                            out.write(data.toByteArray())
                            out.close()


                        } catch (e: IOException) {
                            Log.e("Exception", "File write failed: $e")
                        }

                    }

                    if (report.isAnyPermissionPermanentlyDenied) {

//                            showSettingsDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            })
            .onSameThread()
            .check()

    }

}