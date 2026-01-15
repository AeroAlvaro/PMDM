package com.example.cocktailfinder.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.cocktailfinder.CocktailApplication
import com.example.cocktailfinder.data.CocktailRepository
import com.example.cocktailfinder.model.Cocktail
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface CocktailUiState {
    data class Success(val cocktails: List<Cocktail>) : CocktailUiState
    object Error : CocktailUiState
    object Loading : CocktailUiState
}

class CocktailViewModel(private val cocktailRepository: CocktailRepository) : ViewModel() {

    var cocktailUiState: CocktailUiState by mutableStateOf(CocktailUiState.Loading)
        private set

    init {
        getCocktails()
    }

    fun getCocktails() {
        viewModelScope.launch {
            cocktailUiState = CocktailUiState.Loading
            cocktailUiState = try {
                val list = cocktailRepository.getCocktails()
                CocktailUiState.Success(list)
            } catch (e: IOException) {
                CocktailUiState.Error
            } catch (e: HttpException) {
                CocktailUiState.Error
            } catch (e: Exception) {
                CocktailUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as CocktailApplication)
                val repository = application.container.cocktailRepository
                CocktailViewModel(repository)
            }
        }
    }
}