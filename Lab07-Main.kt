package com.example.turismoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.turismoapp.models.RegisteredTourist
import com.example.turismoapp.models.TouristData
import com.example.turismoapp.models.TouristDestination
import com.example.turismoapp.repository.TouristRepository
import com.example.turismoapp.ui.theme.TurismoAppTheme

sealed class Screen(val route: String, val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Destinos", Icons.Default.Place)
    object Register : Screen("register", "Registro", Icons.Default.Add)
    object Tourists : Screen("tourists", "Turistas", Icons.Default.Person)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TurismoAppTheme {
                TouristGuideApp()
            }
        }
    }
}

@Composable
fun TouristGuideApp() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val repository = remember { TouristRepository(context) }

    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Home.route) { DestinationsListScreen() }
            composable(Screen.Register.route) { RegisterTouristScreen(repository, navController) }
            composable(Screen.Tourists.route) { TouristsListScreen(repository) }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(Screen.Home, Screen.Register, Screen.Tourists)
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = Color(0xFF2E7D32),
        contentColor = Color.White
    ) {
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.title) },
                label = { Text(screen.title) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.White,
                    selectedTextColor = Color.White,
                    unselectedIconColor = Color.White.copy(alpha = 0.6f),
                    unselectedTextColor = Color.White.copy(alpha = 0.6f),
                    indicatorColor = Color(0xFF66BB6A)
                )
            )
        }
    }
}

@Composable
fun DestinationsListScreen() {
    val destinations = TouristData.destinations

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(colors = listOf(Color(0xFFE8F5E9), Color.White)))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.horizontalGradient(colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A))))
                .padding(20.dp)
        ) {
            Column {
                Icon(imageVector = Icons.Default.Place, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Guía Turística", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "${destinations.size} destinos disponibles", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(destinations) { destination -> DestinationCard(destination) }
        }
    }
}

@Composable
fun DestinationCard(destination: TouristDestination) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(60.dp).clip(CircleShape).background(Color(0xFFE8F5E9)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = destination.avatar, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = destination.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = destination.country, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = destination.rating.toString(), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    AssistChip(onClick = {}, label = { Text(destination.category, fontSize = 11.sp) }, modifier = Modifier.height(24.dp))
                }
            }

            Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterTouristScreen(repository: TouristRepository, navController: NavHostController) {
    var dni by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var selectedDestination by remember { mutableStateOf("") }
    var expandedDestination by remember { mutableStateOf(false) }
    var selectedTripType by remember { mutableStateOf("") }
    var expandedTripType by remember { mutableStateOf(false) }
    var selectedAvatar by remember { mutableStateOf(TouristData.avatars[0]) }
    var expandedAvatar by remember { mutableStateOf(false) }
    var travelDate by remember { mutableStateOf("") }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val primaryColor = Color(0xFF2E7D32)

    fun validateAndSave() {
        when {
            dni.isBlank() || dni.length < 8 -> { errorMessage = "DNI debe tener al menos 8 dígitos"; showErrorDialog = true }
            firstName.isBlank() -> { errorMessage = "Ingresa tu nombre"; showErrorDialog = true }
            lastName.isBlank() -> { errorMessage = "Ingresa tu apellido"; showErrorDialog = true }
            email.isBlank() || !email.contains("@") -> { errorMessage = "Email inválido"; showErrorDialog = true }
            phone.isBlank() || phone.length < 9 -> { errorMessage = "Teléfono debe tener al menos 9 dígitos"; showErrorDialog = true }
            selectedDestination.isBlank() -> { errorMessage = "Selecciona un destino"; showErrorDialog = true }
            selectedTripType.isBlank() -> { errorMessage = "Selecciona tipo de viaje"; showErrorDialog = true }
            travelDate.isBlank() -> { errorMessage = "Ingresa fecha de viaje"; showErrorDialog = true }
            repository.findTouristByDni(dni) != null -> { errorMessage = "DNI ya registrado"; showErrorDialog = true }
            else -> {
                repository.addTourist(RegisteredTourist(dni, firstName, lastName, email, phone, selectedDestination, selectedTripType, travelDate, selectedAvatar))
                showSuccessDialog = true
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color(0xFFE8F5E9), Color.White)))) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(colors = listOf(primaryColor, Color(0xFF66BB6A)))).padding(20.dp)) {
            Column {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Registro de Turista", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = "Completa tus datos para el viaje", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
            }
        }

        LazyColumn(modifier = Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item { OutlinedTextField(value = dni, onValueChange = { if (it.length <= 12) dni = it }, label = { Text("DNI / Cédula") }, leadingIcon = { Icon(Icons.Default.AccountBox, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
            item { OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Nombres") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
            item { OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Apellidos") }, leadingIcon = { Icon(Icons.Default.Person, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
            item { OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, leadingIcon = { Icon(Icons.Default.Email, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }
            item { OutlinedTextField(value = phone, onValueChange = { if (it.length <= 15) phone = it }, label = { Text("Teléfono") }, leadingIcon = { Icon(Icons.Default.Phone, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) }

            item {
                ExposedDropdownMenuBox(expanded = expandedDestination, onExpandedChange = { expandedDestination = it }) {
                    OutlinedTextField(value = selectedDestination, onValueChange = {}, readOnly = true, label = { Text("Destino") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedDestination) }, leadingIcon = { Icon(Icons.Default.Place, null) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expandedDestination, onDismissRequest = { expandedDestination = false }) {
                        TouristData.destinations.forEach { destination ->
                            DropdownMenuItem(text = { Text("${destination.avatar} ${destination.name}") }, onClick = { selectedDestination = destination.name; expandedDestination = false })
                        }
                    }
                }
            }

            item {
                ExposedDropdownMenuBox(expanded = expandedTripType, onExpandedChange = { expandedTripType = it }) {
                    OutlinedTextField(value = selectedTripType, onValueChange = {}, readOnly = true, label = { Text("Tipo de Viaje") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedTripType) }, leadingIcon = { Icon(Icons.Default.Star, null) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expandedTripType, onDismissRequest = { expandedTripType = false }) {
                        TouristData.tripTypes.forEach { type ->
                            DropdownMenuItem(text = { Text(type) }, onClick = { selectedTripType = type; expandedTripType = false })
                        }
                    }
                }
            }

            item {
                ExposedDropdownMenuBox(expanded = expandedAvatar, onExpandedChange = { expandedAvatar = it }) {
                    OutlinedTextField(value = selectedAvatar, onValueChange = {}, readOnly = true, label = { Text("Avatar") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expandedAvatar) }, modifier = Modifier.fillMaxWidth().menuAnchor(), shape = RoundedCornerShape(12.dp))
                    ExposedDropdownMenu(expanded = expandedAvatar, onDismissRequest = { expandedAvatar = false }) {
                        TouristData.avatars.forEach { avatar ->
                            DropdownMenuItem(text = { Text(avatar, fontSize = 24.sp) }, onClick = { selectedAvatar = avatar; expandedAvatar = false })
                        }
                    }
                }
            }

            item { OutlinedTextField(value = travelDate, onValueChange = { travelDate = it }, label = { Text("Fecha de Viaje (dd/mm/yyyy)") }, leadingIcon = { Icon(Icons.Default.DateRange, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), placeholder = { Text("01/12/2025") }) }

            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Button(onClick = { validateAndSave() }, modifier = Modifier.weight(1f).height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = primaryColor), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.Check, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar")
                    }
                    OutlinedButton(onClick = { dni = ""; firstName = ""; lastName = ""; email = ""; phone = ""; selectedDestination = ""; selectedTripType = ""; travelDate = "" }, modifier = Modifier.weight(1f).height(56.dp), shape = RoundedCornerShape(12.dp)) {
                        Icon(Icons.Default.Clear, null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Limpiar")
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false; navController.navigate(Screen.Tourists.route) },
            icon = { Icon(Icons.Default.Check, null, tint = Color(0xFF4CAF50), modifier = Modifier.size(48.dp)) },
            title = { Text("¡Registro Exitoso!", fontWeight = FontWeight.Bold) },
            text = { Text("Turista registrado correctamente. Los datos están guardados permanentemente.") },
            confirmButton = { Button(onClick = { showSuccessDialog = false; navController.navigate(Screen.Tourists.route) }, colors = ButtonDefaults.buttonColors(containerColor = primaryColor)) { Text("Ver Turistas") } },
            dismissButton = { TextButton(onClick = { showSuccessDialog = false }) { Text("Cerrar") } }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            icon = { Icon(Icons.Default.Warning, null, tint = Color.Red, modifier = Modifier.size(48.dp)) },
            title = { Text("Error de Validación") },
            text = { Text(errorMessage) },
            confirmButton = { Button(onClick = { showErrorDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Entendido") } }
        )
    }
}

@Composable
fun TouristsListScreen(repository: TouristRepository) {
    var tourists by remember { mutableStateOf(repository.getTourists()) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var touristToDelete by remember { mutableStateOf<RegisteredTourist?>(null) }
    var showClearAllDialog by remember { mutableStateOf(false) }

    val stats = repository.getStatistics()

    Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color(0xFFE8F5E9), Color.White)))) {
        Box(modifier = Modifier.fillMaxWidth().background(Brush.horizontalGradient(colors = listOf(Color(0xFF2E7D32), Color(0xFF66BB6A)))).padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Turistas Registrados", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    Text(text = "Total: ${stats["total"]} | Este mes: ${stats["thisMonth"]}", fontSize = 14.sp, color = Color.White.copy(alpha = 0.9f))
                }
                if (tourists.isNotEmpty()) {
                    IconButton(onClick = { showClearAllDialog = true }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar todos", tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        if (tourists.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(32.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(80.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "No hay turistas registrados", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                Text(text = "Ve a la sección de Registro para agregar turistas", fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center)
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(tourists, key = { it.dni }) { tourist ->
                    TouristCard(tourist = tourist, onDelete = { touristToDelete = tourist; showDeleteDialog = true })
                }
            }
        }
    }

    if (showDeleteDialog && touristToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            icon = { Icon(Icons.Default.Warning, null, tint = Color(0xFFF44336)) },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Deseas eliminar a ${touristToDelete!!.firstName} ${touristToDelete!!.lastName}?") },
            confirmButton = { Button(onClick = { repository.deleteTourist(touristToDelete!!.dni); tourists = repository.getTourists(); showDeleteDialog = false; touristToDelete = null }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false; touristToDelete = null }) { Text("Cancelar") } }
        )
    }

    if (showClearAllDialog) {
        AlertDialog(
            onDismissRequest = { showClearAllDialog = false },
            icon = { Icon(Icons.Default.Delete, null, tint = Color.Red, modifier = Modifier.size(48.dp)) },
            title = { Text("¡Cuidado!", fontWeight = FontWeight.Bold) },
            text = { Text("¿Seguro que deseas eliminar TODOS los turistas registrados? Esta acción no se puede deshacer.") },
            confirmButton = { Button(onClick = { repository.clearAll(); tourists = emptyList(); showClearAllDialog = false }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) { Text("Eliminar Todo") } },
            dismissButton = { TextButton(onClick = { showClearAllDialog = false }) { Text("Cancelar") } }
        )
    }
}

@Composable
fun TouristCard(tourist: RegisteredTourist, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(70.dp).clip(CircleShape).background(Color(0xFFE8F5E9)), contentAlignment = Alignment.Center) {
                Text(text = tourist.avatar, fontSize = 36.sp)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${tourist.firstName} ${tourist.lastName}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "DNI: ${tourist.dni}", fontSize = 13.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Email, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = tourist.email, fontSize = 13.sp, color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Gray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = tourist.phone, fontSize = 13.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    AssistChip(onClick = {}, label = { Text(tourist.selectedDestination, fontSize = 11.sp) }, leadingIcon = { Icon(Icons.Default.Place, contentDescription = null, modifier = Modifier.size(14.dp)) }, modifier = Modifier.height(28.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    AssistChip(onClick = {}, label = { Text(tourist.tripType, fontSize = 11.sp) }, modifier = Modifier.height(28.dp))
                }

                Text(text = "📅 Viaje: ${tourist.travelDate}", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium, modifier = Modifier.padding(top = 4.dp))
            }

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

package com.example.turismoapp.models

data class TouristDestination(
    val id: String,
    val name: String,
    val country: String,
    val category: String,
    val description: String,
    val rating: Float,
    val avatar: String
)

data class RegisteredTourist(
    val dni: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val selectedDestination: String,
    val tripType: String,
    val travelDate: String,
    val avatar: String,
    val registrationDate: Long = System.currentTimeMillis()
)

object TouristData {
    val destinations = listOf(
        TouristDestination("1", "Machu Picchu", "Perú", "Histórico", "Antigua ciudad inca en las montañas", 4.9f, "🏛️"),
        TouristDestination("2", "Cristo Redentor", "Brasil", "Cultural", "Icónica estatua en Río de Janeiro", 4.8f, "✝️"),
        TouristDestination("3", "Cataratas del Iguazú", "Argentina/Brasil", "Natural", "Impresionantes cascadas fronterizas", 4.9f, "💧"),
        TouristDestination("4", "Galápagos", "Ecuador", "Ecológico", "Islas con fauna única en el mundo", 5.0f, "🐢"),
        TouristDestination("5", "Cartagena", "Colombia", "Histórico", "Ciudad amurallada colonial caribeña", 4.7f, "🏰"),
        TouristDestination("6", "Patagonia", "Argentina", "Aventura", "Glaciares y paisajes extremos del sur", 4.8f, "🏔️"),
        TouristDestination("7", "Salar de Uyuni", "Bolivia", "Natural", "El desierto de sal más grande del mundo", 4.9f, "🧂"),
        TouristDestination("8", "Cusco", "Perú", "Cultural", "Capital histórica del Imperio Inca", 4.7f, "🎭"),
        TouristDestination("9", "Amazonas", "Brasil/Perú", "Ecológico", "La selva tropical más grande del planeta", 4.8f, "🌴"),
        TouristDestination("10", "Parque Tayrona", "Colombia", "Playa", "Playas vírgenes y selva tropical", 4.6f, "🏖️"),
        TouristDestination("11", "Atacama", "Chile", "Aventura", "Desierto más árido del mundo con géiseres", 4.8f, "🏜️"),
        TouristDestination("12", "Buenos Aires", "Argentina", "Cultural", "Capital del tango y la gastronomía", 4.7f, "💃"),
        TouristDestination("13", "Bariloche", "Argentina", "Aventura", "Lagos y montañas en la Patagonia norte", 4.6f, "⛷️"),
        TouristDestination("14", "Titicaca", "Perú/Bolivia", "Cultural", "Lago navegable más alto del mundo", 4.5f, "🚤"),
        TouristDestination("15", "Montevideo", "Uruguay", "Cultural", "Capital cosmopolita con playas urbanas", 4.4f, "🌆"),
        TouristDestination("16", "Punta del Este", "Uruguay", "Playa", "Balneario de lujo y vida nocturna", 4.5f, "🌊"),
        TouristDestination("17", "Quito", "Ecuador", "Histórico", "Centro histórico mejor conservado de América", 4.6f, "⛪"),
        TouristDestination("18", "Valle de Cocora", "Colombia", "Natural", "Palmas de cera más altas del mundo", 4.8f, "🌴"),
        TouristDestination("19", "Mendoza", "Argentina", "Gastronómico", "Región vitivinícola con los Andes de fondo", 4.7f, "🍷"),
        TouristDestination("20", "Islas de San Blas", "Panamá", "Playa", "Archipiélago paradisíaco caribeño", 4.9f, "🏝️"),
        TouristDestination("21", "Península Valdés", "Argentina", "Ecológico", "Avistamiento de ballenas y pingüinos", 4.7f, "🐋"),
        TouristDestination("22", "Lima", "Perú", "Gastronómico", "Capital gastronómica de Sudamérica", 4.6f, "🍽️"),
        TouristDestination("23", "Salto Ángel", "Venezuela", "Natural", "Cascada más alta del mundo", 4.9f, "🌈"),
        TouristDestination("24", "Pantanal", "Brasil", "Ecológico", "Humedal tropical con fauna diversa", 4.7f, "🦜")
    )

    val tripTypes = listOf(
        "🎒 Aventura", "🎨 Cultural", "😌 Relajación", "🍴 Gastronómico",
        "📚 Histórico", "🌿 Ecológico", "🏖️ Playa", "🏔️ Montaña"
    )

    val avatars = listOf(
        "👨", "👩", "👦", "👧", "🧑", "👴", "👵",
        "👨‍💼", "👩‍💼", "🧔", "👱", "👨‍🎓", "👩‍🎓"
    )
} 

package com.example.turismoapp.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.turismoapp.models.RegisteredTourist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TouristRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "tourist_prefs",
        Context.MODE_PRIVATE
    )

    private val gson = Gson()

    companion object {
        private const val KEY_TOURISTS = "registered_tourists"
    }

    fun saveTourists(tourists: List<RegisteredTourist>) {
        val json = gson.toJson(tourists)
        prefs.edit().putString(KEY_TOURISTS, json).apply()
    }

    fun getTourists(): List<RegisteredTourist> {
        val json = prefs.getString(KEY_TOURISTS, null) ?: return emptyList()
        val type = object : TypeToken<List<RegisteredTourist>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun addTourist(tourist: RegisteredTourist) {
        val currentList = getTourists().toMutableList()
        currentList.add(tourist)
        saveTourists(currentList)
    }

    fun deleteTourist(dni: String) {
        val currentList = getTourists().toMutableList()
        currentList.removeAll { it.dni == dni }
        saveTourists(currentList)
    }

    fun findTouristByDni(dni: String): RegisteredTourist? {
        return getTourists().find { it.dni == dni }
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    fun getStatistics(): Map<String, Int> {
        val tourists = getTourists()
        return mapOf(
            "total" to tourists.size,
            "thisMonth" to tourists.count {
                System.currentTimeMillis() - it.registrationDate < 30L * 24 * 60 * 60 * 1000
            }
        )
    }
} 
