package ar.edu.utn.frba.placesify

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.edu.utn.frba.placesify.api.ApiService
import ar.edu.utn.frba.placesify.api.GoogleAuthUiClient
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
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlacesifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "login") {

                        // Armo las Rutas de Navegación
                        composable("login") {

                            val state by LoginViewModel().state.collectAsState()

                            val isSignInSuccessful: Boolean by LoginViewModel().isSignInSuccessful.observeAsState(
                                initial = false
                            )


// Si ya estoy logueado...
                            /*
                                                        LaunchedEffect(key1 = Unit) {
                                                            if (googleAuthUiClient.getSignedInUSer() != null) {
                                                                navController.navigate("profile")
                                                            }
                                                        }
                            */
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->

                                    Log.i(
                                        result.toString(),
                                        "GOOGLE SIGN IN2"
                                    )

                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            LoginViewModel().onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = isSignInSuccessful) {

                                Log.d(isSignInSuccessful.toString(), "GOOGLE SIGN IN1")

                                if (isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Sign in successful",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController?.navigate("home")
                                }
                            }

                            LoginScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender ?: return@launch

                                            ).build()
                                        )
                                    }
                                },
                                LoginViewModel(),
                                navController = navController
                            );
                        }
                        composable("home") {
                            HomeScreen(
                                HomeViewModel(ApiService.instance),
                                navController = navController
                            );
                        }
                        composable("register") {
                            RegisterScreen(
                                RegisterViewModel(),
                                navController = navController
                            );
                        }
                        composable("discover_places") {
                            DiscoverPlacesScreen(
                                DiscoverPlacesViewModel(),
                                navController = navController
                            );
                        }
                        composable("new_places") {
                            NewPlacesScreen(
                                NewPlacesViewModel(),
                                navController = navController
                            );
                        }
                        composable("detail_places") {
                            DetailPlacesScreen(
                                DetailPlacesViewModel(),
                                navController = navController
                            );
                        }
                        composable(
                            "detail_list/{id_list}/{name_list}",
                            arguments = listOf(navArgument("id_list") { type = NavType.IntType })
                        ) {
                            DetailListScreen(
                                DetailListViewModel(),
                                navController = navController,
                                it.arguments?.getInt("id_list") ?: 0,
                                it.arguments?.getString("name_list") ?: ""
                            );
                        }
                        composable("profile") {
                            ProfileScreen(
                                ProfileViewModel(),
                                navController = navController
                            );
                        }
                        composable("my_lists") {
                            MyListsScreen(
                                MyListsViewModel(),
                                navController = navController
                            );
                        }
                    }
                }
            }
        }
    }
}

