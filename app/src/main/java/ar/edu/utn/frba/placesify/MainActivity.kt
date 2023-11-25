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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambda
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ar.edu.utn.frba.placesify.api.OpenStreetmapService
import ar.edu.utn.frba.placesify.api.BackendService
import ar.edu.utn.frba.placesify.api.GoogleAuthUiClient
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.view.DetailListScreen
import ar.edu.utn.frba.placesify.view.DetailPlacesScreen
import ar.edu.utn.frba.placesify.view.DiscoverPlacesScreen
import ar.edu.utn.frba.placesify.view.DiscoverCategoryScreen
import ar.edu.utn.frba.placesify.view.FavoritesScreen
import ar.edu.utn.frba.placesify.view.HomeScreen
import ar.edu.utn.frba.placesify.view.InternetStatusComponent
import ar.edu.utn.frba.placesify.view.LoginScreen
import ar.edu.utn.frba.placesify.view.MyListsScreen
import ar.edu.utn.frba.placesify.view.NewListScreen
import ar.edu.utn.frba.placesify.view.NewPlacesPrincipalScreen
import ar.edu.utn.frba.placesify.view.NewPlacesScreen
import ar.edu.utn.frba.placesify.view.SearchedListsScreen
import ar.edu.utn.frba.placesify.view.SplashScreen
import ar.edu.utn.frba.placesify.view.theme.PlacesifyTheme
import ar.edu.utn.frba.placesify.viewmodel.DetailListViewModel
import ar.edu.utn.frba.placesify.viewmodel.DetailPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.DiscoverCategoryViewModel
import ar.edu.utn.frba.placesify.viewmodel.FavoritesViewModel
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel
import ar.edu.utn.frba.placesify.viewmodel.LoginViewModel
import ar.edu.utn.frba.placesify.viewmodel.MyListsViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewListViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesPrincipalViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewPlacesViewModel
import ar.edu.utn.frba.placesify.viewmodel.SearchListsViewModel
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var preferencesManager: PreferencesManager

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferencesManager = PreferencesManager(this)
        setContent {
            PlacesifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "splash") {


                        // Armo las Rutas de Navegación
                        composable("splash") {
                            InternetStatusComponent()
                            SplashScreen(
                                navController = navController
                            )
                        }

                        composable("login") {

                            val viewModel = viewModel<LoginViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()

                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = { result ->
                                    if (result.resultCode == RESULT_OK) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.signInWithIntent(
                                                intent = result.data ?: return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )

                            LaunchedEffect(key1 = state.isSignInSuccessful) {

                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "Acceso exitoso...",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    // Direcciono al Home
                                    navController.navigate("home")
                                    viewModel.resetState()
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
                                navController = navController
                            )
                        }
                        composable("home") {
                            HomeScreen(
                                HomeViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                        composable("searched_lists") {
                            SearchedListsScreen(
                                SearchListsViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                        composable("detail_places/{id_place}",
                            arguments = listOf(navArgument("id_place") { type = NavType.StringType })) {
                            DetailPlacesScreen(
                                DetailPlacesViewModel(
                                    BackendService.instance
                                    , it.arguments?.getString("id_place")
                                ),
                                FavoritesViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                        composable("discover_places") {
                            DiscoverPlacesScreen(
                                DiscoverPlacesViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                        composable("discover_category/{id_category}",
                            arguments = listOf(navArgument("id_category") { type = NavType.StringType })
                            ){

                            DiscoverCategoryScreen(
                                DiscoverCategoryViewModel(
                                    BackendService.instance,
                                    it.arguments?.getString("id_category")
                                    ),
                                navController = navController
                                )
                        }

                        composable("new_places_principal") {
                            NewPlacesPrincipalScreen(
                                NewPlacesPrincipalViewModel(
                                    application,
                                    activityResultRegistry,
                                    LocalContext.current,
                                    OpenStreetmapService.instance),
                                navController = navController
                            )
                        }

                        composable("new_places") {
                            NewPlacesScreen(
                                NewPlacesViewModel(OpenStreetmapService.instance, ),
                                navController = navController
                            )
                        }

                        composable("new_list") {
                            NewListScreen(
                                NewListViewModel(OpenStreetmapService.instance,
                                    BackendService.instance,
                                    LocalContext.current),
                                navController = navController
                            )
                        }


                        composable(
                            "detail_list/{id_list}",
                            arguments = listOf(navArgument("id_list") { type = NavType.StringType })
                        ) {
                            DetailListScreen(
                                DetailListViewModel(
                                    application,
                                    activityResultRegistry,
                                    BackendService.instance,
                                    it.arguments?.getString("id_list"),
                                    OpenStreetmapService.instance,
                                    LocalContext.current
                                ),
                                navController = navController
                            )
                        }
                        composable("favorites") {
                            FavoritesScreen(
                                FavoritesViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                        composable("my_lists") {
                            MyListsScreen(
                                MyListsViewModel(BackendService.instance),
                                navController = navController
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("ON STOP", "")
        preferencesManager.saveList(
            "nuevaLista", null
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ON DESTROY", "")
        preferencesManager.saveList(
            "nuevaLista", null
        )

    }
}

