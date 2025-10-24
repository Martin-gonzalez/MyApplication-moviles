package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.usuario
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    fun onNombreChange(nuevo: String) {
        _uiState.update { it.copy(nombre = nuevo, error = null) }
    }

    fun onUsuarioChange(nuevo: String) {
        _uiState.update { it.copy(usuario = nuevo, error = null) }
    }

    fun onDireccionChange(nueva: String) {
        _uiState.update { it.copy(direccion = nueva, error = null) }
    }

    fun onMailChange(nuevo: String) {
        _uiState.update { it.copy(mail = nuevo.trim(), error = null) }
    }

    fun onContrasenaChange(nueva: String) {
        _uiState.update { it.copy(contrasena = nueva, error = null) }
    }

    //validacion de campos
    private fun validateInputs(): String? {
        val current = _uiState.value
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

        return when {
            current.nombre.isBlank() || current.usuario.isBlank() || current.direccion.isBlank()
                    || current.mail.isBlank() || current.contrasena.isBlank() ->
                "Por favor, completa todos los campos."

            current.usuario.length < 4 ->
                "El nombre de usuario debe tener al menos 4 caracteres."

            !current.mail.matches(emailRegex) ->
                "El formato del correo electrónico no es válido."

            current.contrasena.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres."

            else -> null // Todas las validaciones pasaron
        }
    }

    // Acción al presionar "Registrar"
    fun onRegisterClick() {
        // 1. Primero, validar todos los campos.
        val validationError = validateInputs()
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        // 2. Si la validación es exitosa, iniciar el proceso asíncrono.
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            delay(1500) // Simula la espera de una llamada a la API o base de datos.

            val current = _uiState.value


            //  éxito.
            val nuevoUsuario = usuario(
                nombre = current.nombre,
                usuario = current.usuario,
                direccion = current.direccion,
                mail = current.mail,
                contrasena = current.contrasena //
            )

            _uiState.update { it.copy(isLoading = false, success = true) }
            println("Usuario registrado (simulado): $nuevoUsuario")
        }
    }
}