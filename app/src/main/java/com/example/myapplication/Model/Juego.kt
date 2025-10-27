package com.example.myapplication.Model

import kotlinx.serialization.Serializable

@Serializable
data class Juego(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val precio: Int,
    val genero: String,
    val imagen: String
)
