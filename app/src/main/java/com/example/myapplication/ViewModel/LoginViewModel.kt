package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val userData: usuario? = null
)

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsuarioChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(usuario = nuevo, error = null)
    }

    fun onContrasenaChange(nueva: String) {
        _uiState.value = _uiState.value.copy(contrasena = nueva, error = null)
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }

    fun onLoginClick() {
        val current = _uiState.value

        if (current.usuario.isBlank() || current.contrasena.isBlank()) {
            _uiState.value = current.copy(error = "Completa ambos campos antes de continuar")
            return
        }

        if (current.usuario == "martin" && current.contrasena == "1234") {
            val user = usuario(
                nombre = "Martin Gonzalez",
                usuario = current.usuario,
                direccion = "Santiago, Chile",
                mail = "martin@mail.com",
                contrasena = current.contrasena
            )
            _uiState.value = current.copy(isLoading = false, success = true, userData = user)
        } else {
            _uiState.value = current.copy(isLoading = false, error = "Usuario o contraseña incorrectos", success = false)
        }
    }
}
