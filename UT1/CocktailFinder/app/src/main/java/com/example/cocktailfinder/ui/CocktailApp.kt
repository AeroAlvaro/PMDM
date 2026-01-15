package com.example.cocktailfinder.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cocktailfinder.ui.screens.CocktailViewModel
import com.example.cocktailfinder.ui.screens.HomeScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CocktailApp() {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Cocktail Finder") },
                scrollBehavior = scrollBehavior
            )
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            val cocktailViewModel: CocktailViewModel =
                viewModel(factory = CocktailViewModel.Factory)
            HomeScreen(
                cocktailUiState = cocktailViewModel.cocktailUiState,
                retryAction = cocktailViewModel::getCocktails
            )
        }
    }
}