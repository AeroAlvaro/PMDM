package com.example.cocktailfinder.data

import com.example.cocktailfinder.network.CocktailApiService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val cocktailRepository: CocktailRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://www.thecocktaildb.com/api/json/v1/1/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: CocktailApiService by lazy {
        retrofit.create(CocktailApiService::class.java)
    }

    override val cocktailRepository: CocktailRepository by lazy {
        NetworkCocktailRepository(retrofitService)
    }
}