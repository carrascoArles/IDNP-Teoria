package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import java.net.URLEncoder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                HotelApp()
            }
        }
    }
}

@Composable
fun HotelApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "busqueda") {
        composable("busqueda") {
            PantallaBusqueda(
                onBuscar = { query ->
                    // Codificamos el texto para pasarlo en la ruta
                    val encoded = URLEncoder.encode(query, StandardCharsets.UTF_8.toString())
                    navController.navigate("resultados/$encoded")
                }
            )
        }
        composable(
            route = "resultados/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val decoded = URLDecoder.decode(query, StandardCharsets.UTF_8.toString())
            PantallaResultados(decoded)
        }
    }
}

@Composable
fun PantallaBusqueda(onBuscar: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            label = { Text("Buscar hotel por nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { if (query.isNotEmpty()) onBuscar(query) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Buscar")
        }
    }
}

@Composable
fun PantallaResultados(query: String) {
    // Lista ficticia de hoteles
    val hoteles = listOf(
        "Hotel Plaza - 3⭐ - S/ 150",
        "Hotel Sol - 4⭐ - S/ 250",
        "Hotel Real - 5⭐ - S/ 400",
        "Hostal Económico - 2⭐ - S/ 80"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(text = "Resultados para: \"$query\"", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(16.dp))

        LazyColumn {
            items(hoteles) { hotel ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text = hotel,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
