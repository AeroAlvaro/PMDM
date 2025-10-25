package com.example.proyectopersonal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val colorScheme = if (isDarkMode) darkColorScheme() else lightColorScheme()
            MaterialTheme(colorScheme = colorScheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GreetingSection(
                        isDarkMode = isDarkMode,
                        onToggleTheme = { isDarkMode = !isDarkMode }
                    )
                }
            }
        }
    }
}

@Composable
fun GreetingSection(isDarkMode: Boolean, onToggleTheme: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.mi_foto),
            contentDescription = "Foto de perfil",
            modifier = Modifier
                .size(180.dp)
                .padding(8.dp),
            contentScale = ContentScale.Crop
        )

        val greetingText = if (isDarkMode) "Bona nit" else "Bon dia"
        Text(text = greetingText, fontSize = 28.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Proyecto personal de Álvaro", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onToggleTheme) {
            Text(if (isDarkMode) "Cambiar a modo día" else "Cambiar a modo noche")
        }
    }
}
