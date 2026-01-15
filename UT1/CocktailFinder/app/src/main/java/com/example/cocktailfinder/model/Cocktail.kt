package com.example.cocktailfinder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CocktailResponse(
    val drinks: List<Cocktail>
)

@Serializable
data class Cocktail(
    @SerialName("idDrink")
    val id: String,
    @SerialName("strDrink")
    val name: String,
    @SerialName("strDrinkThumb")
    val image: String
)