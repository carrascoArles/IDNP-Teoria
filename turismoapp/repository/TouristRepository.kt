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