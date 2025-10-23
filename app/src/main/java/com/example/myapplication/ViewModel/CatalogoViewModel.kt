package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.Juego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CatalogoViewModel : ViewModel() {

    private val _catalogo = MutableStateFlow<List<Juego>>(emptyList())
    val catalogo: StateFlow<List<Juego>> = _catalogo

    init {
        cargarJuegos()
    }

    private fun cargarJuegos() {
        _catalogo.value = listOf(
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
