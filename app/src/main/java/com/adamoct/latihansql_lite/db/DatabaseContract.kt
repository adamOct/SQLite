package com.adamoct.latihansql_lite.db

import android.icu.text.CaseMap

object DatabaseContract {
    class NoteColumns{
        companion object{
            const val TABLE_NOTE = "note"
            const val ID = "_id"
            const val TITLE = "title"
            const val DESCRIPTION = "description"
        }
    }
}