package com.example.myapplication.View

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.Model.Juego
import com.example.myapplication.ViewModel.CarritoViewModel




@Composable
fun CarritoScreen(
    carritoViewModel: CarritoViewModel = viewModel(),
    onCompraExitosa: () -> Unit,
    onCompraRechazada: () -> Unit
) {
    val carrito by carritoViewModel.carrito.observeAsState(mutableListOf())
    val total by carritoViewModel.totalPrecio.observeAsState(0.0)

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("🛒 Carrito de Compras", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))

        if (carrito.isEmpty()) {
            Text("Tu carrito está vacío.")
        } else {
            LazyColumn {
                items(carrito) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${item.juego.titulo} (${item.cantidad})")
                        Text("$${item.PrecioTotal}")
                        Row {
                            Button(onClick = { carritoViewModel.modificarCantidad(item.juego, item.cantidad - 1) }) {
                                Text("-")
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(onClick = { carritoViewModel.modificarCantidad(item.juego, item.cantidad + 1) }) {
                                Text("+")
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))
            Text("Total: $${String.format("%.2f", total)}", style = MaterialTheme.typography.headlineSmall)

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    if (carrito.isNotEmpty()) onCompraExitosa() else onCompraRechazada()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar Compra")
            }
        }
    }
}
