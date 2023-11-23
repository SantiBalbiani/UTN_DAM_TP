package ar.edu.utn.frba.placesify.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ar.edu.utn.frba.placesify.R

@Composable
fun noInternet(){
    Image(
        painter = painterResource(id = R.drawable.nointernet),
        contentDescription = "",
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 50.dp)
    )
}