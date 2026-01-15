package com.example.cocktailfinder.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.cocktailfinder.model.Cocktail

@Composable
fun HomeScreen(
    cocktailUiState: CocktailUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (cocktailUiState) {
        is CocktailUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
        is CocktailUiState.Success -> ResultGridScreen(
            cocktailUiState.cocktails, contentPadding = contentPadding, modifier = modifier.fillMaxWidth()
        )
        is CocktailUiState.Error -> ErrorScreen(retryAction, modifier = modifier.fillMaxSize())
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(contentAlignment = Alignment.Center, modifier = modifier) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Error de conexión", modifier = Modifier.padding(16.dp))
        Button(onClick = retryAction) {
            Text("Reintentar")
        }
    }
}

@Composable
fun ResultGridScreen(
    cocktails: List<Cocktail>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding,
    ) {
        items(items = cocktails, key = { cocktail -> cocktail.id }) { cocktail ->
            CocktailCard(
                cocktail,
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.0f)
            )
        }
    }
}

@Composable
fun CocktailCard(cocktail: Cocktail, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(cocktail.image)
                    .crossfade(true)
                    .build(),
                contentDescription = cocktail.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Text(
                text = cocktail.name,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}