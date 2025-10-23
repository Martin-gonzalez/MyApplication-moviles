package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.Model.Juego
import com.example.myapplication.view.CatalogoScreen
import com.example.myapplication.view.DetalleJuegoScreen
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.RegisterScreen
import com.example.myapplication.viewmodel.CatalogoViewModel
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            val loginViewModel: LoginViewModel = viewModel()
            val registerViewModel: RegisterViewModel = viewModel()
            val catalogoViewModel: CatalogoViewModel = viewModel()

            MaterialTheme {
                Surface(color = MaterialTheme.colorScheme.background) {

                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {

                        composable("login") {
                            LoginScreen(
                                viewModel = loginViewModel,
                                onNavigateToRegister = { navController.navigate("register") },
                                onLoginSuccess = {
                                    navController.navigate("catalogo") {
                                        popUpTo("login") { inclusive = true }
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
                                onVerDetalles = { juego: Juego ->
                                    navController.navigate("detalle/${juego.id}")
                                }
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
                    }
                }
            }
        }
    }
}
