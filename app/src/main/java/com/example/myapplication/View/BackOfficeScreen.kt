package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(
    viewModel: CatalogoViewModel = viewModel(),
    onAgregarProducto: () -> Unit = {}
) {
    val lista by viewModel.catalogo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Back Office - Gestión de Productos") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarProducto) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(lista) { juego ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(juego.titulo, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text("Género: ${juego.genero}")
                        Text("Precio: ${juego.precio} CLP")
                        Spacer(Modifier.height(4.dp))
                        Text("Descripción: ${juego.descripcion}")
                    }
                }
            }
        }
    }
}