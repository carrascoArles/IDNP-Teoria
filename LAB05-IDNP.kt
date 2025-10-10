package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ModernForm()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModernForm() {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var showDialog by remember { mutableStateOf(false) }

    val cafe = Color(0xFF6F4E37)

    // 🔹 COLUMN: organiza los elementos de forma vertical
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 36.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título del formulario
        Text(
            text = "Registro de Usuario",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = cafe,
            modifier = Modifier.padding(bottom = 30.dp)
        )

        // 🔹 CAMPOS DE TEXTO
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            shape = RoundedCornerShape(16.dp)
        )

        // 🔹 SPACER: agrega espacio entre los campos y los botones
        Spacer(modifier = Modifier.height(30.dp))

        // 🔹 ROW: organiza los botones en una misma línea
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Botón Guardar
            Button(
                onClick = {
                    showDialog = true
                    name = TextFieldValue("")
                    email = TextFieldValue("")
                    password = TextFieldValue("")
                },
                colors = ButtonDefaults.buttonColors(containerColor = cafe),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
            ) {
                Text("Guardar", fontSize = 18.sp, color = Color.White)
            }

            Spacer(modifier = Modifier.width(16.dp)) // Espacio entre botones

            // Botón Cancelar
            Button(
                onClick = {
                    name = TextFieldValue("")
                    email = TextFieldValue("")
                    password = TextFieldValue("")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(55.dp)
            ) {
                Text("Cancelar", fontSize = 18.sp, color = Color.White)
            }
        }
    }

    // 🔹 DIÁLOGO DE CONFIRMACIÓN
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .width(250.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "Usuario guardado",
                        color = cafe,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { showDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = cafe)
                    ) {
                        Text("Aceptar", color = Color.White)
                    }
                }
            }
        }
    }
}
