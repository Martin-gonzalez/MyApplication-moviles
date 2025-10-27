package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.usuario
import com.example.myapplication.repository.AppDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

class RegisterViewModel(private val repository: AppDataRepository) : ViewModel() {

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

    private fun validateInputs(): String? {
        val current = _uiState.value
        return when {
            current.nombre.isBlank() || current.usuario.isBlank() || current.direccion.isBlank()
                    || current.mail.isBlank() || current.contrasena.isBlank() ->
                "Por favor, completa todos los campos."

            current.usuario.length < 4 ->
                "El nombre de usuario debe tener al menos 4 caracteres."

            !android.util.Patterns.EMAIL_ADDRESS.matcher(current.mail).matches() ->
                "El formato del correo electrónico no es válido."

            current.contrasena.length < 8 ->
                "La contraseña debe tener al menos 8 caracteres."

            else -> null
        }
    }

    fun onRegisterClick() {
        val validationError = validateInputs()
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val current = _uiState.value
            val existingUsers = repository.appData.value.usuarios

            if (existingUsers.any { it.usuario.equals(current.usuario, ignoreCase = true) }) {
                _uiState.update { it.copy(isLoading = false, error = "El nombre de usuario ya está en uso.") }
                return@launch
            }

            if (existingUsers.any { it.mail.equals(current.mail, ignoreCase = true) }) {
                _uiState.update { it.copy(isLoading = false, error = "El correo electrónico ya está registrado.") }
                return@launch
            }

            val nuevoUsuario = usuario(
                nombre = current.nombre,
                usuario = current.usuario,
                direccion = current.direccion,
                mail = current.mail,
                contrasena = current.contrasena
            )

            repository.agregarUsuario(nuevoUsuario)

            _uiState.update { it.copy(isLoading = false, success = true) }
        }
    }
}