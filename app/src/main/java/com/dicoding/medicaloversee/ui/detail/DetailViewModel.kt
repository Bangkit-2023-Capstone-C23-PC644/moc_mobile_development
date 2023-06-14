package com.dicoding.medicaloversee.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.medicaloversee.data.remote.response.HospitalDetail
import com.dicoding.medicaloversee.data.remote.response.HospitalResponse
import com.dicoding.medicaloversee.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<String>()
    val isSuccess: LiveData<String> = _isSuccess

    private val _hospital = MutableLiveData<HospitalDetail>()
    val hospital: LiveData<HospitalDetail> = _hospital

    fun getHospital(token: String, hospitalID: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val hospitalRequest = apiService.getDetailHospital(token, hospitalID)

        hospitalRequest.enqueue(object : Callback<HospitalResponse> {
            override fun onResponse(
                call: Call<HospitalResponse>,
                response: Response<HospitalResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val detailHospital = responseBody.result
                        val newHospital = HospitalDetail(
                            hospitalID = detailHospital.hospitalID,
                            namaRS = detailHospital.namaRS,
                            alamat = detailHospital.alamat,
                            kemampuan_penyelenggaraan = detailHospital.kemampuan_penyelenggaraan,
                            status_akreditasi = detailHospital.status_akreditasi,
                            jumlah_tempat_tidur_perawatan_umum = detailHospital.jumlah_tempat_tidur_perawatan_umum,
                            jumlah_tempat_tidur_perawatan_persalinan = detailHospital.jumlah_tempat_tidur_perawatan_persalinan,
                            jml_dokter_umum = detailHospital.jml_dokter_umum,
                            jml_dokter_gigi = detailHospital.jml_dokter_gigi,
                            jml_perawat = detailHospital.jml_perawat,
                            jml_bidan = detailHospital.jml_bidan,
                            jml_ahli_gizi = detailHospital.jml_ahli_gizi,
                            status = detailHospital.status,
                            pplestimate = detailHospital.pplestimate,
                            timeadded = detailHospital.timeadded
                        )
                        Log.d("tes", newHospital.toString())
                        _hospital.value = newHospital
                        _isSuccess.value = responseBody.message
                    } else {
                        _isError.value = true
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<HospitalResponse>, t: Throwable) {
                t.localizedMessage?.let { Log.d("tes", it) }
                _isLoading.value = false
                _isError.value = true
            }

        })
    }
}