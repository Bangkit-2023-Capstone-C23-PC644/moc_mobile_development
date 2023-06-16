package com.dicoding.medicaloversee.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.medicaloversee.adapter.ListHospitalAdapter
import com.dicoding.medicaloversee.data.remote.response.Hospital
import com.dicoding.medicaloversee.data.remote.response.NearestHospital
import com.dicoding.medicaloversee.data.remote.retrofit.ApiConfig
import com.dicoding.medicaloversee.model.UserModel
import com.dicoding.medicaloversee.model.UserPreference
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _hospitalAdapter = MutableLiveData<ListHospitalAdapter>()
    val hospitalAdapter: LiveData<ListHospitalAdapter> = _hospitalAdapter

    private val _listHospital= MutableLiveData<List<Hospital>>()
    val listHospital: LiveData<List<Hospital>> = _listHospital

    private val _originalListHospital = MutableLiveData<List<Hospital>>()
    val originalListHospital: LiveData<List<Hospital>> = _originalListHospital

    fun getUser(): LiveData<UserModel> {
        return pref.getUser().asLiveData()
    }

    fun getAllHospitals(token: String) {
        _isLoading.value = true
        val apiService = ApiConfig().getApiService()
        val hospitalRequest = apiService.getNearestHospital(token)

        hospitalRequest.enqueue(object : Callback<NearestHospital> {
            override fun onResponse(
                call: Call<NearestHospital>,
                response: Response<NearestHospital>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _originalListHospital.value = responseBody.result
                        val listStoryAdapter = ListHospitalAdapter()
                        listStoryAdapter.submitList(responseBody.result)
                        _hospitalAdapter.value = listStoryAdapter
                        _listHospital.value = responseBody.result
                    } else {
                        _isError.value = true
                    }
                } else {
                    _isError.value = true
                }
            }

            override fun onFailure(call: Call<NearestHospital>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }
}
