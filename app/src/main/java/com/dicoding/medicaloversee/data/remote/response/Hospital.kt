package com.dicoding.medicaloversee.data.remote.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class NearestHospital(

    @field:SerializedName("result")
    val result: List<Hospital>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

)

@Parcelize
data class Hospital(

    @field:SerializedName("hospitalID")
    val hospitalID: String,

    @field:SerializedName("namaRS")
    val namaRS: String,

    @field:SerializedName("lintang")
    val lintang: Double,

    @field:SerializedName("bujur")
    val bujur: Double,

    @field:SerializedName("distance")
    val distance: Double,

):Parcelable

@Parcelize
data class HospitalDetail(

    @field:SerializedName("hospitalID")
    val hospitalID: String,

    @field:SerializedName("namaRS")
    val namaRS: String,

    @field:SerializedName("alamat")
    val alamat: String,

    @field:SerializedName("kemampuan_penyelenggaraan")
    val kemampuan_penyelenggaraan: Int,

    @field:SerializedName("status_akreditasi")
    val status_akreditasi: Int,

    @field:SerializedName("jumlah_tempat_tidur_perawatan_umum")
    val jumlah_tempat_tidur_perawatan_umum: Int,

    @field:SerializedName("jumlah_tempat_tidur_perawatan_persalinan")
    val jumlah_tempat_tidur_perawatan_persalinan: Int,

    @field:SerializedName("jml_dokter_umum")
    val jml_dokter_umum: Int,

    @field:SerializedName("jml_dokter_gigi")
    val jml_dokter_gigi: Int,

    @field:SerializedName("jml_perawat")
    val jml_perawat: Int,

    @field:SerializedName("jml_bidan")
    val jml_bidan: Int,

    @field:SerializedName("jml_ahli_gizi")
    val jml_ahli_gizi: Int,

    @field:SerializedName("status")
    val status: Int,

    @field:SerializedName("pplestimate")
    val pplestimate: Int,

    @field:SerializedName("timeadded")
    val timeadded: String,

):Parcelable

data class HospitalResponse(
    val error: Boolean,
    val message: String,
    val result: DetailHospital
)

data class DetailHospital(
    val hospitalID: String,
    val namaRS: String,
    val alamat: String,
    val kemampuan_penyelenggaraan: Int,
    val status_akreditasi: Int,
    val jumlah_tempat_tidur_perawatan_umum: Int,
    val jumlah_tempat_tidur_perawatan_persalinan: Int,
    val jml_dokter_umum: Int,
    val jml_dokter_gigi: Int,
    val jml_perawat: Int,
    val jml_bidan: Int,
    val jml_ahli_gizi: Int,
    val status: Int,
    val pplestimate: Int,
    val timeadded: String

)