package com.adamoct.latihansql_lite.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Note(
    var id: Int? = null,
    var title: String? = null,
    var description: String? = null

): Parcelable