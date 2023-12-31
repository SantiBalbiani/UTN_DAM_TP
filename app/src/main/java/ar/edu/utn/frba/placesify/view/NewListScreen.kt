package ar.edu.utn.frba.placesify.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.animation.OvershootInterpolator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.placesify.R
import ar.edu.utn.frba.placesify.model.Categorias
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.Lugares
import ar.edu.utn.frba.placesify.model.PreferencesManager
import ar.edu.utn.frba.placesify.view.componentes.ShowLoading
import ar.edu.utn.frba.placesify.view.theme.Purple80
import ar.edu.utn.frba.placesify.viewmodel.HomeViewModel
import ar.edu.utn.frba.placesify.viewmodel.NewListViewModel
import coil.compose.AsyncImage
import com.google.android.material.card.MaterialCardView
import com.google.firebase.auth.ktx.*
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.delay

@Composable
fun NewListScreen(
    viewModel: NewListViewModel,
    navController: NavController? = null
) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        NewList(
            Modifier
                .align(Alignment.TopStart)
                .padding(16.dp),
            viewModel,
            navController
        )
    }
}


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NewList(
    modifier: Modifier,
    viewModel: NewListViewModel,
    navController: NavController?
) {
    //Contexto
    val context = LocalContext.current
    // Instancio al PreferencesManager
    val preferencesManager = remember { PreferencesManager(context) }

    // Declaro los viewData
    val categorias: List<Categorias>? by viewModel.categorias.observeAsState(initial = null)
    val categoriasActualizada: Boolean by viewModel.categoriasActualizada.observeAsState(
        initial = false
    )

    val isRefreshing: Boolean by viewModel.isRefreshing.observeAsState(
        initial = false
    )

    val nuevaLista: Listas? by viewModel.nuevaLista.observeAsState(initial = null)

    var pullRefreshState = rememberPullRefreshState(isRefreshing, { viewModel.refresh() })

    var name = rememberSaveable {
        mutableStateOf("")
    }

    var descripcion = rememberSaveable {
        mutableStateOf("")
    }

    var esta_abierto by remember {
        mutableStateOf(false)
    }
    var categoriaPredeterminada by remember {
        mutableStateOf("Seleccionar categoria")
    }

    val categoriasSeleccionadas by viewModel.categoriasSeleccionadas.observeAsState()
    val showConfirmationDialog = viewModel.showConfirmationDialog.value

    if (showConfirmationDialog) {
        Confirmacion(
            onDismissRequest = {
                viewModel.setShowConfirmationDialog(false)
            },
            onConfirmation = {
                viewModel.setShowConfirmationDialog(false)
                viewModel.limpiarNuevaLista()
                navController?.navigateUp()
            },
            dialogTitle = "Confirmación",
            dialogText = "¿Desea cancelar la creación de la lista? Se perderán los datos no guardados",
            icon = Icons.Default.Info
        )
    }

    BackHandler(onBack = {
        viewModel.setShowConfirmationDialog(true)
    })

    Scaffold(
        topBar = { BarraNavegacionSuperior("Crear Lista", navController, viewModel1 = viewModel) },
    ) { innerPadding ->

        if (categoriasSeleccionadas == null || !categoriasActualizada) {
            ShowLoading("Actualizando...")
        } else {


            LazyColumn(
                modifier = modifier.padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {

                    Text(
                        text = "Crear lista nueva",
                        fontSize = dimensionResource(id = R.dimen.font_size_titulo).value.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        label = { Text(text = "Nombre de la lista") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Purple80,
                            focusedLabelColor = Purple80,
                            cursorColor = Purple80,
                            textColor = Color.Black
                        ),
                        value = name.value,
                        onValueChange = {
                            name.value = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    OutlinedTextField(
                        label = { Text(text = "Descripcion de la lista") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Purple80,
                            focusedLabelColor = Purple80,
                            cursorColor = Purple80,
                            textColor = Color.Black
                        ),
                        value = descripcion.value,
                        onValueChange = {
                            descripcion.value = it
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ExposedDropdownMenuBox(
                            expanded = esta_abierto,
                            onExpandedChange = { esta_abierto = it }) {
                            TextField(
                                value = categoriaPredeterminada,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = esta_abierto)
                                },
                                colors = ExposedDropdownMenuDefaults.textFieldColors(
                                    textColor = Color.Black
                                )
                            )

                            ExposedDropdownMenu(
                                expanded = esta_abierto,
                                onDismissRequest = { esta_abierto = false }
                            ) {

                                categorias?.forEach { categoria ->
                                    androidx.compose.material3.DropdownMenuItem(
                                        text = { Text(categoria.name, color = Color.Black) },
                                        onClick = {
                                            viewModel.agregarCat(categoria)
                                            esta_abierto = false

                                        }
                                    )

                                }

                            }

                        }
                    }

                    Spacer(modifier = Modifier.padding(12.dp))

                    if (!categoriasSeleccionadas.isNullOrEmpty()) {

                        Text(
                            text = "Categorias seleccionadas",
                            fontSize = dimensionResource(id = R.dimen.font_size_normal).value.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )

                        categoriasSeleccionadas?.forEach { cat ->

                            Card(
                                modifier = Modifier
                                    .padding(7.dp)
                                    .fillMaxWidth()
                                    .background(color = MaterialTheme.colorScheme.surfaceVariant)
                                //.clickable { onCategoryClick(categoria) }//,
                                //elevation = 6.dp
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        AsyncImage(
                                            model = cat.icono,
                                            contentDescription = "",
                                            modifier = Modifier
                                                .padding(5.dp)
                                                .size(width = 40.dp, height = 40.dp)
                                        )
                                        Text(
                                            text = cat.name,
                                            modifier = Modifier.padding(5.dp)
                                        )
                                    }

                                    AsyncImage(
                                        model = com.google.android.material.R.drawable.mtrl_ic_cancel,
                                        contentDescription = "Cerrar",
                                        modifier = Modifier
                                            .size(width = 40.dp, height = 40.dp)
                                            .padding(5.dp)
                                            .clickable { viewModel.quitarCat(cat) }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(24.dp))

                    Button(
                        onClick = {
                            if (categoriasSeleccionadas!!.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Debe seleccionar al menos una categoría",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                nuevaLista?.name = name.value
                                nuevaLista?.description = descripcion.value
                                val lstCategories: List<Int> =
                                    categoriasSeleccionadas!!.map { cat -> cat.id }
                                nuevaLista?.lstCategories = lstCategories

                                nuevaLista?.let { preferencesManager.saveList("nuevaLista", it) }
                                navController?.navigate("new_places_principal")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(text = "Agregar Lugares")
                    }

                    // Para debug muestro por pantalla la lista en todo momento
                    /*Text(
                        text = preferencesManager.getList(
                            "nuevaLista",
                            Listas(
                                lstPlaces = emptyList(),
                                lstCategories = emptyList()
                            )
                        ).toString(),
                        modifier = Modifier.padding(5.dp)
                    )*/

                }
            }

            PullRefreshIndicator(
                refreshing = isRefreshing,
                state = pullRefreshState,
            )

        }
    }

}







