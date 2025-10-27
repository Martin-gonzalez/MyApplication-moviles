package com.example.myapplication.repository

import com.example.myapplication.Model.Juego
import com.example.myapplication.Model.usuario
import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    val usuarios: List<usuario> = emptyList(),
    val juegos: List<Juego> = emptyList()
)
