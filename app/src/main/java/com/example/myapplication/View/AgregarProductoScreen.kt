package com.example.myapplication.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.myapplication.Model.Juego
import com.example.myapplication.viewmodel.CatalogoViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(
    viewModel: CatalogoViewModel,
    onGuardar: () -> Unit = {},
    onCancelar: () -> Unit = {}
) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agregar Producto") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título del juego") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Género") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    // Lógica para guardar el nuevo juego
                    val precioInt = precio.toIntOrNull() ?: 0
                    val nuevoJuego = Juego(
                        id = 0, // El repositorio se encargará de asignar el ID correcto
                        titulo = titulo,
                        descripcion = descripcion,
                        genero = genero,
                        precio = precioInt,
                        imagen = "https://via.placeholder.com/150" // URL de imagen por defecto
                    )
                    viewModel.agregarJuego(nuevoJuego)
                    onGuardar() // Navegar hacia atrás después de guardar
                }) {
                    Text("Guardar")
                }
                OutlinedButton(onClick = onCancelar) {
                    Text("Cancelar")
                }
            }
        }
    }
}