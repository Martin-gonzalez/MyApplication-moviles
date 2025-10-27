package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Model.Juego
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.viewmodel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    viewModel: CatalogoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    onVerDetalles: (juego: Juego) -> Unit = {},
    onVerCarrito: () -> Unit = {}
) {
    val lista by viewModel.catalogo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de Videojuegos") },
                actions = {
                    // 🛒 Botón para abrir el carrito
                    IconButton(onClick = onVerCarrito) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver carrito"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(lista) { juego ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(juego.titulo, style = MaterialTheme.typography.titleLarge)
                        Spacer(Modifier.height(4.dp))
                        Text(juego.genero, style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(4.dp))
                        Text(juego.descripcion, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Text("Precio: ${juego.precio} CLP", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 🔍 Ver detalles del juego
                            Button(
                                onClick = { onVerDetalles(juego) }
                            ) {
                                Text("Ver detalles")
                            }

                            // 🛒 Agregar al carrito
                            Button(
                                onClick = {
                                    carritoViewModel.agregarAlCarrito(juego)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}
