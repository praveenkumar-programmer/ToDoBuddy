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
    private val noteClickDeleteInterface: NoteClickDeleteInterface,
    private val noteClickEditInterface: NoteClickEditInterface
) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private val allNotes = ArrayList<NoteModel>()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val noteTV: MaterialTextView = itemView.findViewById(R.id.idTVNote)
        val descTv: MaterialTextView = itemView.findViewById(R.id.idTVDesc)
        val dateTV: MaterialTextView = itemView.findViewById(R.id.idTVDate)
        val deleteIV: MaterialButton = itemView.findViewById(R.id.deleteButton)
        val editTv: MaterialButton = itemView.findViewById(R.id.editButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.note_item,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.noteTV.text = allNotes[position].noteTitle
        holder.descTv.text = allNotes[position].noteDescription
        holder.dateTV.text = "Last Updated : "+allNotes.get(position).timeStamp
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