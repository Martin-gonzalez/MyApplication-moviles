package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel



@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToRegister: () -> Unit = {},
    onLoginSuccess: () -> Unit // Callback para notificar el éxito
) {
    val state by viewModel.uiState.collectAsState()

    // Efecto para navegar cuando el login es exitoso
    LaunchedEffect(state.success) {
        if (state.success) {
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        // 🔹 Campo de usuario
        OutlinedTextField(
            value = state.usuario,
            onValueChange = viewModel::onUsuarioChange,
            label = { Text("Usuario") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(Modifier.height(12.dp))

        // 🔹 Campo de contraseña
        OutlinedTextField(
            value = state.contrasena,
            onValueChange = viewModel::onContrasenaChange,
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default,
            modifier = Modifier.fillMaxWidth(0.9f)
        )

        Spacer(Modifier.height(20.dp))

        // 🔹 Botón de inicio de sesión
        Button(
            onClick = viewModel::onLoginClick,
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth(0.9f)
        ) {
            if (state.isLoading)
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            else
                Text("Ingresar")
        }

        Spacer(Modifier.height(16.dp))

        // 🔹 Mensaje de error
        state.error?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(12.dp))

        // 🔹 Enlace para ir al registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes cuenta? Regístrate aquí")
        }
    }
}
