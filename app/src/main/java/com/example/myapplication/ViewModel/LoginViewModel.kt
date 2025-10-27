package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Model.usuario
import com.example.myapplication.repository.AppDataRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val usuario: String = "",
    val contrasena: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
    val userData: usuario? = null
)

class LoginViewModel(private val repository: AppDataRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun onUsuarioChange(nuevo: String) {
        _uiState.update { it.copy(usuario = nuevo.trim(), error = null) }
    }

    fun onContrasenaChange(nueva: String) {
        _uiState.update { it.copy(contrasena = nueva, error = null) }
    }

    private fun validateInputs(): String? {
        val current = _uiState.value
        return when {
            current.usuario.isBlank() || current.contrasena.isBlank() ->
                "El correo y la contraseña no pueden estar vacíos."

            !android.util.Patterns.EMAIL_ADDRESS.matcher(current.usuario).matches() ->
                "Por favor, ingresa un formato de correo válido."

            else -> null
        }
    }

    fun onLoginClick() {
        val validationError = validateInputs()
        if (validationError != null) {
            _uiState.update { it.copy(error = validationError) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val current = _uiState.value
            val users = repository.appData.value.usuarios

            val foundUser = users.find { it.mail.equals(current.usuario, ignoreCase = true) }

            if (foundUser != null && foundUser.contrasena == current.contrasena) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = true,
                        userData = foundUser
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "El correo o la contraseña son incorrectos."
                    )
                }
            }
        }
    }

    // 3. CORRECCIÓN: Nueva función para resetear el estado de éxito.
    fun onLoginSuccessConsumed() {
        _uiState.update { it.copy(success = false) }
    }
}
