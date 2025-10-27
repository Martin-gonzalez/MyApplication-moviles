package com.example.myapplication.Model

data class Carrito(

    val juego: Juego,
    var cantidad: Double = 1.0
) {
    val PrecioTotal: Double
        get() = juego.precio * cantidad
}
