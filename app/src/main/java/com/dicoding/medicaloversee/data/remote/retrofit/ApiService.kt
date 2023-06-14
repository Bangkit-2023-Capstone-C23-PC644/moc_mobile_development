package com.dicoding.medicaloversee.data.remote.retrofit

import com.dicoding.medicaloversee.data.remote.response.HospitalResponse
import com.dicoding.medicaloversee.data.remote.response.RequestResponse
import com.dicoding.medicaloversee.data.remote.response.LoginRequest
import com.dicoding.medicaloversee.data.remote.response.LoginResponse
import com.dicoding.medicaloversee.data.remote.response.NearestHospital
import com.dicoding.medicaloversee.data.remote.response.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("register")
    fun addUser(
        @Body request: RegisterRequest
    ) : Call<RequestResponse>

    @Headers("Content-Type: application/json")
    @POST("login")
    fun loginUser(
        @Body request: LoginRequest
    ): Call<LoginResponse>

    @GET("nearest")
    fun getNearestHospital(
        @Header("authorization") token: String,
    ): Call<NearestHospital>

    @GET("hospitals/{id}")
    fun getDetailHospital(
        @Header("authorization") token: String,
        @Path("id") hospitalID: String?
    ): Call<HospitalResponse>
}