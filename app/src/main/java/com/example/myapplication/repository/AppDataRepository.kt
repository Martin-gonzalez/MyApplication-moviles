package com.example.myapplication.repository

import android.content.Context
import com.example.myapplication.Model.Juego
import com.example.myapplication.Model.usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class AppDataRepository(private val context: Context) {

    private val json = Json { prettyPrint = true; ignoreUnknownKeys = true }
    private val dataFile = File(context.filesDir, "app_data.json")

    private val _appData = MutableStateFlow(AppData())
    val appData: StateFlow<AppData> = _appData

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        if (dataFile.exists()) {
            val data = dataFile.readText()
            try {
                _appData.value = json.decodeFromString<AppData>(data)
            } catch (e: Exception) {
                // Si el archivo está corrupto o es inválido, creamos uno por defecto
                crearDatosPorDefecto()
            }
        } else {
            crearDatosPorDefecto()
        }
    }

    private fun crearDatosPorDefecto() {
        val adminUser = usuario("admin", "admin", "", "admin@admin.com", "admin1234")
        val testUser = usuario("Test User", "testuser", "123 Test St", "user@test.com", "password123")
        val defaultData = AppData(usuarios = listOf(adminUser, testUser), juegos = obtenerJuegosIniciales())
        _appData.value = defaultData
        guardarDatos(defaultData)
    }

    private fun guardarDatos(data: AppData) {
        val dataString = json.encodeToString(data)
        dataFile.writeText(dataString)
    }

    suspend fun agregarUsuario(nuevoUsuario: usuario) {
        val currentData = _appData.value
        val usuariosActualizados = currentData.usuarios + nuevoUsuario
        val newData = currentData.copy(usuarios = usuariosActualizados)
        _appData.value = newData
        guardarDatos(newData)
    }

    suspend fun agregarJuego(nuevoJuego: Juego) {
        val currentData = _appData.value
        // Asignamos un ID único basado en el tamaño actual de la lista
        val juegoConId = nuevoJuego.copy(id = (currentData.juegos.size + 1))
        val juegosActualizados = currentData.juegos + juegoConId
        val newData = currentData.copy(juegos = juegosActualizados)
        _appData.value = newData
        guardarDatos(newData)
    }

    private fun obtenerJuegosIniciales(): List<Juego> {
        return listOf(
            Juego(
                id = 1,
                titulo = "CyberQuest 2077",
                descripcion = "RPG futurista ambientado en una megaciudad llena de peligros.",
                precio = 29990,
                genero = "RPG",
                imagen = "https://via.placeholder.com/150"
            ),
            Juego(
                id = 2,
                titulo = "Dark Souls IV",
                descripcion = "Un desafiante juego de acción con combates épicos y atmósfera oscura.",
                precio = 45990,
                genero = "Acción",
                imagen = "https://via.placeholder.com/150"
            ),
            Juego(
                id = 3,
                titulo = "Pixel Adventure",
                descripcion = "Un divertido plataformas de estilo retro y niveles coloridos.",
                precio = 9990,
                genero = "Plataformas",
                imagen = "https://via.placeholder.com/150"
            )
        )
    }
}
