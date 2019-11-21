package com.adamoct.latihansql_lite.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adamoct.latihansql_lite.R
import com.adamoct.latihansql_lite.model.Note
import kotlinx.android.synthetic.main.item_note.view.*
import kotlin.collections.ArrayList

class NoteAdapter (private val listener: (Note, Int) -> Unit)
    :RecyclerView.Adapter<NoteAdapter.ViewHolder>(){
    var listNote = ArrayList<Note>()
        set (listNote) {
            if (this.listNote.size > 0){
                this.listNote.clear()
            }
                this.listNote.addAll(listNote)
                notifyDataSetChanged()
        }


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        fun bindItem(note: Note, listener: (Note, Int) -> Unit) {
            itemView.tvTitle.text = note.title
            itemView.tvDescripsion.text = note.description
            itemView.setOnClickListener{
                listener(note, adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_note,  parent, false))

    override fun getItemCount(): Int = this.listNote.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(this.listNote[position],listener)
    }

    fun addItem(note: Note){
        this.listNote.add(note)
        notifyItemInserted(this.listNote.size - 1)
    }

    fun updateItem(position: Int, note: Note) {
        this.listNote[position] = note
        notifyItemChanged(position, note)
    }

    fun removeItem(position: Int) {
        this.listNote.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listNote.size)
    }


}