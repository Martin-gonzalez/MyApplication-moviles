package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.Model.usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val userData: usuario? = null
)

class LoginViewModel : ViewModel() {

    //base de datos
    private val knownUsers = listOf(
        usuario(
            nombre = "Martin Gonzalez",
            usuario = "martin@mail.com",
            direccion = "Santiago, Chile",
            mail = "martin@mail.com",
            contrasena = "contrasena123"
        ),
        usuario(
            nombre = "Usuario de Prueba",
            usuario = "test@example.com",
            direccion = "Valparaíso, Chile",
            mail = "test@example.com",
            contrasena = "testing456"
        ),
        usuario( // Admin
            nombre = "Admin",
            usuario = "admin@admin.com",
            direccion = "Chile",
            mail = "admin@admin.com",
            contrasena = "admin1234"
        )
    )

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    var usuarioActual: usuario? = null
        private set

    fun onUsuarioChange(nuevo: String) {
        _uiState.update { it.copy(usuario = nuevo.trim(), error = null) }
    }

    fun onContrasenaChange(nueva: String) {
        _uiState.update { it.copy(contrasena = nueva, error = null) }
    }


    private fun validateInputs(): String? {
        val emailRegex = Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
        val current = _uiState.value

        return when {
            current.usuario.isBlank() || current.contrasena.isBlank() ->
                "El correo y la contraseña no pueden estar vacíos."

            !current.usuario.matches(emailRegex) ->
                "Por favor, ingresa un formato de correo válido."

            current.contrasena.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres."

            else -> null // Todas las validaciones pasaron
        }
    }

    fun onLoginClick() {
        // 1. Validar las entradas del usuario primero.
        val validationError = validateInputs()
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        // --- Lógica de Login Síncrona ---
        _uiState.update { it.copy(isLoading = true, error = null) }
        val current = _uiState.value

        // 2. Buscar al usuario en nuestra "base de datos" simulada.
        val foundUser = knownUsers.find { it.usuario.equals(current.usuario, ignoreCase = true) }

        // 3. Verificar si el usuario existe y la contraseña coincide.
        if (foundUser != null && foundUser.contrasena == current.contrasena) {
            // Éxito
            usuarioActual = foundUser
            _uiState.update {
                it.copy(
                    isLoading = false,
                    success = true,
                    userData = foundUser
                )
            }
        } else {
            // Fallo
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "El correo o la contraseña son incorrectos."
                )
            }
        }
    }
}