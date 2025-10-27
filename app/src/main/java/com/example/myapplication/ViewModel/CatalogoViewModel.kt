package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.Juego
import com.example.myapplication.repository.AppDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CatalogoViewModel(private val repository: AppDataRepository) : ViewModel() {

    val catalogo: StateFlow<List<Juego>> = repository.appData
        .map { it.juegos }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun agregarJuego(juego: Juego) {
        viewModelScope.launch {
            repository.agregarJuego(juego)
        }
    }
}
