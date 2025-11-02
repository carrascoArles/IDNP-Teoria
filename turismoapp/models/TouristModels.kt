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