package ar.edu.utn.frba.placesify.model

import com.squareup.moshi.Json

data class Lugares(val id: Int, val name: String, val description: String)
data class Listas(
    @field:Json(name = "_uuid") // Usado por Retrofit
    val id: String,
    @field:Json(name = "name") // Usado por Retrofit
    val name: String,
    @field:Json(name = "description") // Usado por Retrofit
    val description: String,
    val lstPlaces: List<Lugares>?
)

data class ApiResponse(
    val cursor: String?,
    val items: List<Listas>,
    val next_page: String?
)
