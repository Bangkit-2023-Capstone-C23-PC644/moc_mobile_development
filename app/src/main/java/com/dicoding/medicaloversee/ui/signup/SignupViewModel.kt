package com.dicoding.medicaloversee.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.medicaloversee.data.remote.response.RequestResponse
import com.dicoding.medicaloversee.data.remote.response.RegisterRequest
import com.dicoding.medicaloversee.data.remote.retrofit.ApiConfig
import com.dicoding.medicaloversee.model.UserModel
import com.dicoding.medicaloversee.model.UserPreference
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel(private val pref: UserPreference) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _isSuccess = MutableLiveData<String>()
    val isSuccess: LiveData<String> = _isSuccess

    fun saveUser(user: UserModel) {
        viewModelScope.launch {
            pref.saveUser(user)
        }
    }

    fun registerUser(nik: String, name: String, email: String, phone: String, password: String, lintang: String, bujur:String) {
        _isLoading.value = true
        if (!isInputValid(nik, name, email, phone, password, lintang, bujur)) {
            _isError.value = true
            _isLoading.value = false
            return
        }
        val apiService = ApiConfig().getApiService()
        val request = RegisterRequest(nik, name, email, phone, password, lintang, bujur)
        val addUserRequest = apiService.addUser(request)
        addUserRequest.enqueue(object : Callback<RequestResponse> {
            override fun onResponse(
                call: Call<RequestResponse>,
                response: Response<RequestResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        val responseBody = response.body()
                        if (responseBody != null && !responseBody.error) {
                            saveUser(UserModel(nik, name, email, phone, password, lintang, bujur, false,""))
                            _isSuccess.value = responseBody.message
                        } else {
                            _isError.value = true
                        }
                    } else {
                        _isError.value = true
                    }
                }
            }

            override fun onFailure(call: Call<RequestResponse>, t: Throwable) {
                _isLoading.value = false
                _isError.value = true
            }
        })
    }

    private fun isInputValid(nik: String, name: String, email: String, phone: String, password: String, lintang: String, bujur:String): Boolean {
        if (nik.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || lintang.isEmpty() || bujur.isEmpty()) {
            return false
        }
        return true
    }
}