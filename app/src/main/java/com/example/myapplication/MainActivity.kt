package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Model.Juego
import com.example.myapplication.View.*
import com.example.myapplication.viewmodel.CatalogoViewModel
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.viewmodel.RegisterViewModel
import com.example.myapplication.ViewModel.CarritoViewModel
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.RegisterScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // ✅ ViewModels
            val loginViewModel: LoginViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
            val catalogoViewModel: CatalogoViewModel = viewModel()
            val carritoViewModel: CarritoViewModel = viewModel()

            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {

                        // 🔐 LOGIN
                        composable("login") {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    val usuario = loginViewModel.usuarioActual
                                    if (usuario?.mail == "admin@admin.com") {
                                        navController.navigate("backoffice") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate("catalogo") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                }
                            )
                        }


                        composable("register") {
                            RegisterScreen(
                                viewModel = registerViewModel,
                                onRegisterSuccess = {
                                    navController.navigate("login") {
                                        popUpTo("register") { inclusive = true }
                                    }
                                },
                                onBackToLogin = { navController.popBackStack() }
                            )
                        }


                        composable("catalogo") {
                            CatalogoScreen(
                                viewModel = catalogoViewModel,
                                carritoViewModel = carritoViewModel,
                                onVerDetalles = { juego: Juego ->
                                    navController.navigate("detalle/${juego.id}")
                                },
                                onVerCarrito = { navController.navigate("carrito") }
                            )
                        }


                        composable(
                            route = "detalle/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id")
                            val catalogo by catalogoViewModel.catalogo.collectAsState()
                            val juego = catalogo.find { it.id == id }

                            if (juego != null) {
                                DetalleJuegoScreen(
                                    juego = juego,
                                    onVolver = { navController.popBackStack() }
                                )
                            } else {
                                Text("Juego no encontrado")
                            }
                        }


                        composable("backoffice") {
                            BackOfficeScreen(
                                viewModel = catalogoViewModel,
                                onAgregarProducto = { navController.navigate("agregarProducto") }
                            )
                        }

                        composable("agregarProducto") {
                            AgregarProductoScreen(
                                onGuardar = { navController.popBackStack() },
                                onCancelar = { navController.popBackStack() }
                            )
                        }


                        composable("carrito") {
                            CarritoScreen(
                                carritoViewModel = carritoViewModel,
                                onCompraExitosa = {
                                    navController.navigate("compraExitosa") {
                                        popUpTo("carrito") { inclusive = true }
                                    }
                                },
                                onCompraRechazada = {
                                    navController.navigate("compraRechazada") {
                                        popUpTo("carrito") { inclusive = true }
                                    }
                                }
                            )
                        }


                        composable("compraExitosa") {
                            CompraExitosaScreen(
                                onVolverInicio = {
                                    navController.navigate("catalogo") {
                                        popUpTo("compraExitosa") { inclusive = true }
                                    }
                                }
                            )
                        }


                        composable("compraRechazada") {
                            CompraRechazadaScreen(
                                onVolverCarrito = {
                                    navController.navigate("carrito") {
                                        popUpTo("compraRechazada") { inclusive = true }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

