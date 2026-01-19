package com.example.nombresapp

import android.app.Application
import com.example.nombresapp.data.AppDatabase
import com.example.nombresapp.data.UserPreferencesRepository
import com.example.nombresapp.data.dataStore

class NombresApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        userPreferencesRepository = UserPreferencesRepository(dataStore)
    }
}