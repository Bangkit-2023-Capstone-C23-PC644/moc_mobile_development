package com.dicoding.medicaloversee.data.remote.response

data class RegisterRequest(
    val nik: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val lintang: String,
    val bujur: String
)

data class LoginRequest(
    val nik: String,
    val password: String
)

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult
)


data class LoginResult(
    val nik: String,
    val name: String,
    val email: String,
    val phone: String,
    val lintang: String,
    val bujur: String,
    val token: String
)
