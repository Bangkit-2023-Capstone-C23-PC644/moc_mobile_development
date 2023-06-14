package com.dicoding.medicaloversee.model

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUser(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[NIK_KEY] ?:"",
                preferences[NAME_KEY] ?:"",
                preferences[EMAIL_KEY] ?:"",
                preferences[PHONE_KEY] ?:"",
                preferences[PASSWORD_KEY] ?:"",
                preferences[LINTANG_KEY] ?:"",
                preferences[BUJUR_KEY] ?:"",
                preferences[STATE_KEY] ?: false,
                preferences[TOKEN_KEY] ?: ""
            )
        }
    }

    suspend fun saveUser(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NIK_KEY] = user.nik
            preferences[NAME_KEY] = user.name
            preferences[EMAIL_KEY] = user.email
            preferences[PHONE_KEY] = user.phone
            preferences[PASSWORD_KEY] = user.password
            preferences[LINTANG_KEY] = user.lintang
            preferences[BUJUR_KEY] = user.bujur
            preferences[STATE_KEY] = user.isLogin
            preferences[TOKEN_KEY] = user.token
        }
    }

    suspend fun updateLocation(lintang: String, bujur: String) {
        dataStore.edit { preferences ->
            preferences[LINTANG_KEY] = lintang
            preferences[BUJUR_KEY] = bujur
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NIK_KEY = stringPreferencesKey("nik")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PHONE_KEY = stringPreferencesKey("phone")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val LINTANG_KEY = stringPreferencesKey("lintang")
        private val BUJUR_KEY = stringPreferencesKey("bujur")
        private val STATE_KEY = booleanPreferencesKey("state")
        private val TOKEN_KEY = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}