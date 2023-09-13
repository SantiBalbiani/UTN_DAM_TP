package ar.edu.utn.frba.placesify.model

import com.squareup.moshi.Json

data class Lugares(val id: Int, val name: String, val description: String)
data class Listas(
    @field:Json(name = "_id") // Usado por Retrofit
    val id: Int,
    @field:Json(name = "name") // Usado por Retrofit
    val name: String,
    @field:Json(name = "description") // Usado por Retrofit
    val description: String,
    val lstPlaces: List<Lugares>
)
// Para usarlo en el POST, ya que no necesita ID
data class ListasDTO(
    @field:Json(name = "name") // Usado por Retrofit
    val name: String,
    @field:Json(name = "description") // Usado por Retrofit
    val description: String,
    val lstPlaces: List<Lugares>
)