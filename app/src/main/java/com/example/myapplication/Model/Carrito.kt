package com.example.myapplication.Model

data class Carrito(

    val juego: Juego,
    var cantidad: Int = 1
) {
    val PrecioTotal: Int
        get() = juego.precio * cantidad
}
