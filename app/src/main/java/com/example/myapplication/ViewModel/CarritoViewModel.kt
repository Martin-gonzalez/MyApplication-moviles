package com.example.myapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.Carrito
import com.example.myapplication.Model.Juego

class CarritoViewModel : ViewModel() {

    private val _carrito = MutableLiveData<MutableList<Carrito>>(mutableListOf())
    val carrito: LiveData<MutableList<Carrito>> = _carrito

    val totalPrecio: LiveData<Double> = MutableLiveData<Double>().apply {
        value = 0.0
    }

    fun agregarAlCarrito(juego: Juego) {
        val lista = _carrito.value ?: mutableListOf()
        val existente = lista.find { it.juego.id == juego.id }

        if (existente != null) {
            existente.cantidad++
        } else {
            lista.add(Carrito(juego))
        }
        _carrito.value = lista
        actualizarTotal()
    }

    fun eliminarDelCarrito(juego: Juego) {
        val lista = _carrito.value ?: mutableListOf()
        lista.removeIf { it.juego.id == juego.id }
        _carrito.value = lista
        actualizarTotal()
    }

    fun modificarCantidad(juego: Juego, cantidad: Int) {
        val lista = _carrito.value ?: mutableListOf()
        val item = lista.find { it.juego.id == juego.id }
        if (item != null && cantidad > 0) {
            item.cantidad = cantidad
        } else if (item != null && cantidad <= 0) {
            lista.remove(item)
        }
        _carrito.value = lista
        actualizarTotal()
    }

    private fun actualizarTotal() {
        val total = _carrito.value?.sumOf { it.PrecioTotal } ?: 0.0
        (totalPrecio as MutableLiveData).value = total as Double?
    }
}

