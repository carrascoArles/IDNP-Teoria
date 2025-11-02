package com.example.turismoapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * 🎨 DATASTORE: Gestión moderna de preferencias de tema
 *
 * ¿QUÉ ES DATASTORE?
 * - Reemplazo moderno de SharedPreferences
 * - Usa Kotlin Coroutines y Flow (asíncrono)
 * - Type-safe (seguro de tipos)
 * - Más eficiente y robusto
 *
 * ¿CÓMO FUNCIONA?
 * - Guarda datos en archivos usando Protocol Buffers
 * - Lee/escribe de forma asíncrona (no bloquea la UI)
 * - Los cambios se propagan automáticamente con Flow
 */

// Extension property para crear el DataStore
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

class ThemeDataStore(private val context: Context) {

    companion object {
        // Key para guardar la preferencia de tema oscuro
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme")
    }

    /**
     * 📖 LEER el estado del tema
     * Retorna un Flow que emite true (oscuro) o false (claro)
     */
    val isDarkThemeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            // Por defecto: false (tema claro)
            preferences[DARK_THEME_KEY] ?: false
        }

    /**
     * 💾 GUARDAR la preferencia de tema
     * @param isDarkTheme true = oscuro, false = claro
     */
    suspend fun setDarkTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_THEME_KEY] = isDarkTheme
        }
    }

    /**
     * 🔄 ALTERNAR entre tema claro y oscuro
     */
    suspend fun toggleTheme() {
        context.dataStore.edit { preferences ->
            val currentTheme = preferences[DARK_THEME_KEY] ?: false
            preferences[DARK_THEME_KEY] = !currentTheme
        }
    }
}