package com.example.myapplication.Model
//data class para una estructura de guardado facil de informacion
//si es class se le puede agregar logica y aca no nos interesa poner logica
data class usuario(
    var nombre: String,
    var usuario: String,
    var direccion: String,
    var mail: String,
    var contrasena: String
)
