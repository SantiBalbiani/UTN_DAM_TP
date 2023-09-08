package ar.edu.utn.frba.placesify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.placesify.view.DetailListScreen
import ar.edu.utn.frba.placesify.view.DetailPlacesScreen
import ar.edu.utn.frba.placesify.view.DiscoverPlacesScreen
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel
import ar.edu.utn.frba.placesify.view.theme.PlacesifyTheme
import ar.edu.utn.frba.placesify.view.HomeScreen
import ar.edu.utn.frba.placesify.view.LoginScreen
import ar.edu.utn.frba.placesify.view.MyListsScreen
import ar.edu.utn.frba.placesify.view.NewPlacesScreen
import ar.edu.utn.frba.placesify.view.ProfileScreen
import ar.edu.utn.frba.placesify.view.RegisterScreen
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel
import ar.edu.utn.frba.placesify.viewmodel.DetailPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.LoginViewModel
import ar.edu.utn.frba.placesify.viewmodel.MyListsViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.ProfileViewModel
import ar.edu.utn.frba.placesify.viewmodel.RegisterViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlacesifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}
@Composable
private fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {

        // Armo las Rutas de Navegación
        composable("login") {LoginScreen( LoginViewModel(), navController = navController); }
        composable("home") { HomeScreen(HomeViewModel(), navController = navController); }
        composable("register") { RegisterScreen(RegisterViewModel(), navController = navController); }
        composable("discover_places") { DiscoverPlacesScreen(DiscoverPlacesViewModel(), navController = navController); }
        composable("new_places") { NewPlacesScreen(NewPlacesViewModel(), navController = navController); }
        composable("detail_places") { DetailPlacesScreen(DetailPlacesViewModel(), navController = navController); }
        composable("detail_list") { DetailListScreen(DetailListViewModel(), navController = navController); }
        composable("profile") { ProfileScreen(ProfileViewModel(), navController = navController); }
        composable("my_lists") { MyListsScreen(MyListsViewModel(), navController = navController); }
    }
}