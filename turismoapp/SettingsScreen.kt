package com.example.turismoapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val context = LocalContext.current
    val themeDataStore = remember { ThemeDataStore(context) }
    val coroutineScope = rememberCoroutineScope()

    val primaryColor = if (isDarkTheme) Color(0xFF66BB6A) else Color(0xFF2E7D32)
    val backgroundColor = if (isDarkTheme) Color(0xFF121212) else Color(0xFFE8F5E9)
    val cardColor = if (isDarkTheme) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkTheme) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                if (isDarkTheme) {
                    Brush.verticalGradient(colors = listOf(Color(0xFF1A1A1A), Color(0xFF121212)))
                } else {
                    Brush.verticalGradient(colors = listOf(Color(0xFFE8F5E9), Color.White))
                }
            )
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = if (isDarkTheme) {
                            listOf(Color(0xFF1B5E20), Color(0xFF2E7D32))
                        } else {
                            listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))
                        }
                    )
                )
                .padding(20.dp)
        ) {
            Column {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Configuración",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Personaliza tu experiencia",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de Tema
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Default.DarkMode else Icons.Default.LightMode,
                        contentDescription = null,
                        tint = primaryColor,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Tema de la App",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                        Text(
                            text = if (isDarkTheme) "Modo Oscuro activado" else "Modo Claro activado",
                            fontSize = 14.sp,
                            color = textColor.copy(alpha = 0.7f)
                        )
                    }
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { newValue ->
                            coroutineScope.launch {
                                themeDataStore.setDarkTheme(newValue)
                            }
                            onThemeChange(newValue)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = primaryColor,
                            uncheckedThumbColor = Color.White,
                            uncheckedTrackColor = Color.Gray
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Divider(color = textColor.copy(alpha = 0.1f))

                Spacer(modifier = Modifier.height(16.dp))

                // Opciones de tema con botones
                Text(
                    text = "Selecciona tu preferencia:",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Botón Tema Claro
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                themeDataStore.setDarkTheme(false)
                            }
                            onThemeChange(false)
                        },
                        modifier = Modifier.weight(1f).height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (!isDarkTheme) primaryColor.copy(alpha = 0.1f) else Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = if (!isDarkTheme) 2.dp else 1.dp,
                            brush = SolidColor(if (!isDarkTheme) primaryColor else textColor.copy(alpha = 0.3f))
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LightMode,
                                contentDescription = null,
                                tint = if (!isDarkTheme) primaryColor else textColor.copy(alpha = 0.6f),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Claro",
                                color = if (!isDarkTheme) primaryColor else textColor.copy(alpha = 0.8f),
                                fontWeight = if (!isDarkTheme) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }

                    // Botón Tema Oscuro
                    OutlinedButton(
                        onClick = {
                            coroutineScope.launch {
                                themeDataStore.setDarkTheme(true)
                            }
                            onThemeChange(true)
                        },
                        modifier = Modifier.weight(1f).height(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = if (isDarkTheme) primaryColor.copy(alpha = 0.1f) else Color.Transparent
                        ),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = if (isDarkTheme) 2.dp else 1.dp,
                            brush = SolidColor(if (isDarkTheme) primaryColor else textColor.copy(alpha = 0.3f))
                        )
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DarkMode,
                                contentDescription = null,
                                tint = if (isDarkTheme) primaryColor else textColor.copy(alpha = 0.6f),
                                modifier = Modifier.size(28.dp)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Oscuro",
                                color = if (isDarkTheme) primaryColor else textColor.copy(alpha = 0.8f),
                                fontWeight = if (isDarkTheme) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card de información
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = primaryColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Persistencia con DataStore",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Text(
                        text = "Tu preferencia se guarda automáticamente y se mantiene al cerrar la app",
                        fontSize = 12.sp,
                        color = textColor.copy(alpha = 0.7f)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estadísticas
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📊 Información del Sistema",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(12.dp))
                InfoRow("Método de almacenamiento:", "DataStore Preferences", textColor)
                InfoRow("Estado actual:", if (isDarkTheme) "Modo Oscuro" else "Modo Claro", textColor)
                InfoRow("Persistencia:", "✅ Activada", textColor)
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, textColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = textColor.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = textColor
        )
    }
}