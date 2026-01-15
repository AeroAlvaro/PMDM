package com.example.cocktailfinder

import android.app.Application
import com.example.cocktailfinder.data.AppContainer
import com.example.cocktailfinder.data.DefaultAppContainer

class CocktailApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}