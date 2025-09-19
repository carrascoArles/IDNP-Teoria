package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                EnvioScreen()
            }
        }
    }
}

@Composable
fun EnvioScreen() {
    // Estados para los campos
    var nombre by remember { mutableStateOf("") }
    var montoEnviar by remember { mutableStateOf("") }
    var montoActual by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre destinatario") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = montoEnviar,
            onValueChange = { montoEnviar = it },
            label = { Text("Monto a enviar S/") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = montoActual,
            onValueChange = { montoActual = it },
            label = { Text("Monto actual S/") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                // Convertimos los montos a Double, manejando entradas no válidas
                val enviar = montoEnviar.toDoubleOrNull() ?: 0.0
                val actual = montoActual.toDoubleOrNull() ?: 0.0

                val nuevoSaldo = actual - enviar
                montoActual = nuevoSaldo.toString()

                mensaje = "Se envió a $nombre: S/ $enviar\nSaldo restante: S/ $nuevoSaldo"
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("REGISTRAR", fontSize = 18.sp)
        }

        Spacer(Modifier.height(24.dp))

        if (mensaje.isNotEmpty()) {
            Text(
                text = mensaje,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
