package com.example.cocktailfinder.data

import com.example.cocktailfinder.model.Cocktail
import com.example.cocktailfinder.network.CocktailApiService

interface CocktailRepository {
    suspend fun getCocktails(): List<Cocktail>
}

class NetworkCocktailRepository(
    private val cocktailApiService: CocktailApiService
) : CocktailRepository {
    override suspend fun getCocktails(): List<Cocktail> {
        return cocktailApiService.getCocktails().drinks
    }
}