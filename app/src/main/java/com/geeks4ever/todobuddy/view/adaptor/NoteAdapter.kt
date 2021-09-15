package com.geeks4ever.todobuddy.view.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.geeks4ever.roomdatabase.NoteModel
import com.geeks4ever.todobuddy.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView

class NoteAdapter(
    val noteClickDeleteInterface: NoteClickDeleteInterface,
    val noteClickEditInterface: NoteClickEditInterface
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val allNotes = ArrayList<NoteModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTV = itemView.findViewById<MaterialTextView>(R.id.idTVNote)
        val descTv = itemView.findViewById<MaterialTextView>(R.id.idTVDesc)
        val dateTV = itemView.findViewById<MaterialTextView>(R.id.idTVDate)
        val deleteIV = itemView.findViewById<MaterialButton>(R.id.deleteButton)
        val editTv = itemView.findViewById<MaterialButton>(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.noteTV.setText(allNotes.get(position).noteTitle)
        holder.dateTV.setText("Last Updated : "+allNotes.get(position).timeStamp)
        holder.deleteIV.setOnClickListener {

            noteClickDeleteInterface.onDeleteIconClick(allNotes.get(position))
        }

        holder.editTv.setOnClickListener {
            noteClickEditInterface.onEditButtonClick(allNotes[position])
        }

        holder.itemView.setOnClickListener{
            if(holder.descTv.visibility == View.GONE){
                holder.descTv.visibility = View.VISIBLE
                holder.editTv.visibility = View.VISIBLE
                holder.deleteIV.visibility = View.VISIBLE
            }
            else{
                holder.descTv.visibility = View.GONE
                holder.editTv.visibility = View.GONE
                holder.deleteIV.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {

        return allNotes.size
    }

    fun updateList(newList: List<NoteModel>) {

        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }

}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(noteModel: NoteModel)
}

interface NoteClickEditInterface {
    fun onEditButtonClick(noteModel: NoteModel)
}