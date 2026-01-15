package com.example.cocktailfinder.network

import com.example.cocktailfinder.model.CocktailResponse
import retrofit2.http.GET

interface CocktailApiService {
    @GET("search.php?s=margarita")
    suspend fun getCocktails(): CocktailResponse
}