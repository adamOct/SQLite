package com.adamoct.latihansql_lite

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.adamoct.latihansql_lite.db.DatabaseContract
import com.adamoct.latihansql_lite.db.NoteHelper
import com.adamoct.latihansql_lite.model.Note
import kotlinx.android.synthetic.main.activity_note_update.*

class NoteActivityUpdate : AppCompatActivity() {

    private var isEdit  = false
    private var note: Note? = null
    private var position: Int = 0
    private lateinit var noteHelper: NoteHelper

    companion object{
        const val EXTRA_NOTE = "extra_note"
        const val EXTRA_POSITION = "extra_position"
        const val REQUEST_ADD = 100
        const val RESULT_ADD = 101
        const val REQUEST_UPDATE = 200
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_update)

        initNoteHelper()
        getDataIntent()
        initView()
        initActionBar()
        onClick()

    }

    private fun onClick() {
        btnSubmit.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Tidak Boleh Kosong"
            }

            note?.title = title
            note?.description = description

            val intent = Intent()
            intent.putExtra(EXTRA_NOTE, note)
            intent.putExtra(EXTRA_POSITION, position)

            val values = ContentValues()
            values.put(DatabaseContract.NoteColumns.TITLE, title)
            values.put(DatabaseContract.NoteColumns.DESCRIPTION, description)

            if (isEdit){
                val result = noteHelper.update(note?.id.toString(), values)
                if (result > 0){
                    setResult(RESULT_UPDATE, intent)
                    finish()
                }else {
                    Toast.makeText(this,
                        "Gagal Update",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                val result = noteHelper.insert(values)
                if (result > 0) {
                    note?.id = result.toInt()
                    setResult(RESULT_ADD, intent)
                    finish()
                }else{
                    Toast.makeText(this,
                        "Gagal Input",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun initView() {
        val actionBarTitle: String
        val btnTitle: String

        if (isEdit) {
            actionBarTitle = "Ubah"
            btnTitle = "Update"
            note?.let { etTitle.setText(it.title) }
            note?.let { etDescription.setText(it.description) }
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Input"
        }

        supportActionBar?.title = actionBarTitle
        btnSubmit.text = btnTitle
    }

    private fun getDataIntent() {
        note = intent.getParcelableExtra(EXTRA_NOTE)
        if (note != null) {
            position = intent.getIntExtra(EXTRA_POSITION, 0)
            isEdit = true

        }else {
            note = Note()
        }
    }

    private fun initNoteHelper() {
        noteHelper = NoteHelper.getIntance(this)
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        if (isEdit){
            menuInflater.inflate(R.menu.menu_forum, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete ->deleteItem()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun deleteItem(){
        val result = noteHelper.deleteBy(note?.id.toString())
        if (result > 0){
            val intent = Intent()
            intent.putExtra(EXTRA_POSITION, position)
            setResult(RESULT_DELETE, intent)
            finish()
        }else  {
            Toast.makeText(this,
                "Gagal menghapus",
                Toast.LENGTH_SHORT)
                .show()
        }
    }

}
