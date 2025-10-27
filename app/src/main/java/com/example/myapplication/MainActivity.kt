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
import com.example.myapplication.View.BackOfficeScreen
import com.example.myapplication.View.CatalogoScreen
import com.example.myapplication.View.DetalleJuegoScreen
import com.example.myapplication.view.AgregarProductoScreen
import com.example.myapplication.view.LoginScreen
import com.example.myapplication.view.RegisterScreen
import com.example.myapplication.viewmodel.CatalogoViewModel
import com.example.myapplication.viewmodel.LoginViewModel
import com.example.myapplication.viewmodel.RegisterViewModel

// --- CORRECCIÓN PRINCIPAL: Todas las importaciones usan paquetes en minúsculas ---
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            // NOTA: Como tus ViewModels ahora son AndroidViewModel (para usar el repositorio),
            // la función viewModel() se encarga de crearlos correctamente de forma automática.
            // No necesitas crear una "Factory" manualmente si sigues esta estructura simple.
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
                                // La lógica de navegación ahora es más limpia y depende del callback del ViewModel
                                onLoginSuccess = { isAdmin ->
                                    val destination = if (isAdmin) "backoffice" else "catalogo"
                                    navController.navigate(destination) {
                                        // Limpia el historial para que el usuario no pueda volver atrás
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
                            // Obtenemos la lista de juegos del StateFlow del ViewModel
                            val catalogo by catalogoViewModel.catalogo.collectAsState()
                            val juego = catalogo.find { it.id == id }

                            if (juego != null) {
                                DetalleJuegoScreen(
                                    juego = juego,
                                    onVolver = { navController.popBackStack() }
                                )
                            } else {
                                // Muestra un mensaje mientras carga o si el ID no es válido
                                Text("Juego no encontrado o cargando...")
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
                                viewModel = catalogoViewModel,
                                onGuardar = { navController.popBackStack() },
                                onCancelar = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}