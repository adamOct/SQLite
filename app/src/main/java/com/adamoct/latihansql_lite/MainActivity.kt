package com.adamoct.latihansql_lite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.adamoct.latihansql_lite.adapter.NoteAdapter
import com.adamoct.latihansql_lite.db.NoteHelper
import com.adamoct.latihansql_lite.helper.MappingHelper
import com.adamoct.latihansql_lite.model.Note
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.nio.channels.NonReadableChannelException

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: NoteAdapter
    private lateinit var noteHelper: NoteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNotHelper()
        initAdapter()
        onClick()
        loadNote()

    }

    private fun loadNote() {
        GlobalScope.launch ( Dispatchers.Main ) {
            progressBar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = noteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressBar.visibility = View.INVISIBLE
            val notes = deferredNotes.await()
            if (notes.size > 0) {
                adapter.listNote = notes
            }else  {
                adapter.listNote = ArrayList()
                Toast.makeText(this@MainActivity
                    , "Tidak ada saat ini"
                    , Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun initNotHelper() {
        noteHelper = NoteHelper.getIntance(this)
        noteHelper.open()
    }

    private fun initAdapter() {
        adapter = NoteAdapter { note, position ->
            val intent = Intent(this, NoteActivityUpdate::class.java)
            intent.putExtra(NoteActivityUpdate.EXTRA_POSITION, position)
            intent.putExtra(NoteActivityUpdate.EXTRA_NOTE, note)
            startActivityForResult(intent, NoteActivityUpdate.REQUEST_UPDATE)

        }
        rvNote.layoutManager = LinearLayoutManager(this)
        rvNote.adapter = adapter
    }

    private fun onClick() {
        fabAdd.setOnClickListener() {
            val intent = Intent(this, NoteActivityUpdate::class.java)
            startActivityForResult(intent, NoteActivityUpdate.REQUEST_ADD)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(resultCode){
            NoteActivityUpdate.REQUEST_ADD -> {
                if (resultCode == NoteActivityUpdate.RESULT_ADD){
                    val note = data?.getParcelableExtra<Note>(NoteActivityUpdate.EXTRA_NOTE)
                    if (note != null) {
                        adapter.addItem(note)
                    }
                    rvNote.smoothScrollToPosition(adapter.itemCount - 1)
                    Toast.makeText(this, "Satu Item Berhasil di Tambahkan",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
            NoteActivityUpdate.REQUEST_UPDATE -> {
                val position = data?.getIntExtra(NoteActivityUpdate.EXTRA_POSITION, 0)
                if (position != null) {
                    adapter.removeItem(position)
                    Toast.makeText(this, "Satu Item Berhasil Di hapus",
                    Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
}

