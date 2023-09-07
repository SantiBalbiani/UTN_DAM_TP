package ar.edu.utn.frba.placesify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.placesify.data.HomeViewModel
import ar.edu.utn.frba.placesify.ui.theme.PlacesifyTheme
import ar.edu.utn.frba.placesify.ui.HomeScreen
import ar.edu.utn.frba.placesify.ui.theme.login.ui.LoginScreen
import ar.edu.utn.frba.placesify.ui.RegisterScreen
import ar.edu.utn.frba.placesify.data.LoginViewModel
import ar.edu.utn.frba.placesify.data.RegisterViewModel

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
    NavHost(navController = navController, startDestination = "register") {
        composable("login") {
            LoginScreen(
                LoginViewModel(),
                navController = navController
            );
        }
        composable("home") {
            HomeScreen(
                HomeViewModel(),
                navController = navController
            );
        }
        composable("register") {
            RegisterScreen(
                RegisterViewModel(),
                navController = navController
            );
        }
    }
}