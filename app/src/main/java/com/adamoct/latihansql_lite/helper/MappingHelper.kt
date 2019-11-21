package com.adamoct.latihansql_lite.helper

import android.database.Cursor
import android.provider.ContactsContract
import com.adamoct.latihansql_lite.db.DatabaseContract.NoteColumns.Companion.DESCRIPTION
import com.adamoct.latihansql_lite.db.DatabaseContract.NoteColumns.Companion.ID
import com.adamoct.latihansql_lite.db.DatabaseContract.NoteColumns.Companion.TITLE
import com.adamoct.latihansql_lite.model.Note

object MappingHelper {
    fun mapCursorToArrayList(noteCursor: Cursor)
    : ArrayList<Note> {
        val noteList = ArrayList<Note>()
        noteCursor.moveToFirst()
        while (noteCursor.moveToNext()) {
            val id = noteCursor
                .getInt(noteCursor.getColumnIndexOrThrow(ID))
            val title = noteCursor
                .getString(noteCursor.getColumnIndexOrThrow(TITLE))
            val description = noteCursor
                .getString(noteCursor.getColumnIndexOrThrow(DESCRIPTION))
            noteList.add(Note(id, title, description))
        }
        return noteList
    }

}