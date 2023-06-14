package com.dicoding.medicaloversee.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "hospital")
class HospitalEntity (

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "hospitalID")
    var hospitalID: String,

    @ColumnInfo(name = "namaRS")
    var namaRS: String,

    @ColumnInfo(name = "alamat")
    var alamat: String,

    @ColumnInfo(name = "favorited")
    var isFavorited: Boolean

): Parcelable