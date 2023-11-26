package ar.edu.utn.frba.placesify.view

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.view.componentes.ConnectionState
import ar.edu.utn.frba.placesify.view.componentes.connectivityState
import ar.edu.utn.frba.placesify.view.componentes.noInternet
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController? = null) {

    val alpha = remember {
        Animatable(0f)
    }
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available
    LaunchedEffect(key1 = true) {
        alpha.animateTo(1f, animationSpec = tween(1500))
        delay(2000)

        if (navController != null) {
            navController.navigate(route = "login") {
                popUpTo(route = "splash") {
                    inclusive = true
                }
            }
        }
    }

    // Parametros del Degrade Radial
    val colorStops = arrayOf(
        0.0f to Color.Yellow,
        0.2f to Color.Red,
        1f to Color.Blue,
    )
    // Obtengo la LocalConfiguration
    val configuration = LocalConfiguration.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors = listOf(Color.White, Color.Blue),
                    center = Offset.Unspecified,
                    radius = configuration.screenHeightDp.toFloat() / 1.5f,
                )
            ),
        contentAlignment = Alignment.Center
    ) {if(isConnected) {
        Image(
            painter = painterResource(id = R.drawable.splash_placesify),
            contentDescription = "",
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 50.dp)
                .alpha(alpha.value)
        )
    }else{
        noInternet()
    }
    }
}

