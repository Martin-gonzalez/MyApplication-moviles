package com.example.myapplication.Model

import kotlinx.serialization.Serializable

@Serializable
data class usuario(
    val nombre: String,
    val usuario: String,
    val direccion: String,
    val mail: String,
    val contrasena: String
)
