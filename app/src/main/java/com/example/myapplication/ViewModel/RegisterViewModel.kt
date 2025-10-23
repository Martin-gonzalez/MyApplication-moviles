package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estado de la pantalla de registro
data class RegisterUiState(
    val nombre: String = "",
    val usuario: String = "",
    val direccion: String = "",
    val mail: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

// ViewModel que maneja la lógica del registro
class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    // Actualizaciones de cada campo
    fun onNombreChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(nombre = nuevo, error = null)
    }

    fun onUsuarioChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(usuario = nuevo, error = null)
    }

    fun onDireccionChange(nueva: String) {
        _uiState.value = _uiState.value.copy(direccion = nueva, error = null)
    }

    fun onMailChange(nuevo: String) {
        _uiState.value = _uiState.value.copy(mail = nuevo, error = null)
    }

    fun onContrasenaChange(nueva: String) {
        _uiState.value = _uiState.value.copy(contrasena = nueva, error = null)
    }

    // Acción al presionar "Registrar"
    fun onRegisterClick() {
        val current = _uiState.value

        if (current.nombre.isBlank() || current.usuario.isBlank() || current.direccion.isBlank()
            || current.mail.isBlank() || current.contrasena.isBlank()
        ) {
            _uiState.value = current.copy(error = "Por favor, completa todos los campos.")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(current.mail).matches()) {
            _uiState.value = current.copy(error = "El formato del correo no es válido.")
            return
        }

        viewModelScope.launch {
            _uiState.value = current.copy(isLoading = true)

            // Simulación de registro exitoso
            val nuevoUsuario = usuario(
                nombre = current.nombre,
                usuario = current.usuario,
                direccion = current.direccion,
                mail = current.mail,
                contrasena = current.contrasena
            )

            _uiState.value = current.copy(isLoading = false, success = true)
            println("Usuario registrado: $nuevoUsuario")
        }
    }
}