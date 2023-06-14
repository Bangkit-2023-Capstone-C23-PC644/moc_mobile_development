package com.dicoding.medicaloversee.model


data class UserModel(
    val nik: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val lintang: String,
    val bujur: String,
    val isLogin: Boolean,
    val token: String
)

