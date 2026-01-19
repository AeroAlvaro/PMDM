package com.example.nombresapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.nombresapp.NombresApplication
import com.example.nombresapp.data.NameEntity
import com.example.nombresapp.data.NameDao
import com.example.nombresapp.data.UserPreferencesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(
    private val nameDao: NameDao,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val names: StateFlow<List<NameEntity>> = userPreferencesRepository.isSortAsc
        .flatMapLatest { isAsc ->
            if (isAsc) nameDao.getNamesAsc() else nameDao.getNamesDesc()
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val isSortAsc = userPreferencesRepository.isSortAsc.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    fun addName(name: String) {
        viewModelScope.launch {
            nameDao.insert(NameEntity(name = name))
        }
    }

    fun toggleSort() {
        viewModelScope.launch {
            userPreferencesRepository.saveSortPreference(!isSortAsc.value)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as NombresApplication)
                AppViewModel(
                    application.database.nameDao(),
                    application.userPreferencesRepository
                )
            }
        }
    }
}