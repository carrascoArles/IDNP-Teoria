package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TouristCircleWithMap()
                }
            }
        }
    }
}

@Composable
fun TouristCircleWithMap() {
    var expanded by remember { mutableStateOf(false) }

    // Animación de tamaño del círculo
    val circleSize by animateDpAsState(
        targetValue = if (expanded) 220.dp else 90.dp,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ), label = "circleAnimation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondo del mapa
        Image(
            painter = painterResource(id = R.drawable.map_background), // ← tu imagen del mapa
            contentDescription = "Mapa turístico de fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Contenido central
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Círculo verde con posible imagen interior
            Box(
                modifier = Modifier
                    .size(circleSize)
                    .clip(CircleShape)
                    .background(Color(0xFF4CAF50)), // verde turístico
                contentAlignment = Alignment.Center
            ) {
                if (expanded) {
                    // Imagen del museo dentro del círculo
                    Image(
                        painter = painterResource(id = R.drawable.museum_inside), // ← imagen del museo
                        contentDescription = "Museo descubierto",
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = { expanded = !expanded }) {
                Text(
                    text = if (expanded) "Ocultar Museo" else "Descubrir Museo",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}
