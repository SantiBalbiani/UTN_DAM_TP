package ar.edu.utn.frba.placesify.model

import com.squareup.moshi.Json

data class Lugares(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    @field:Json(name = "latitud") // Usado por Retrofit
    val latitud: Double = 0.0,
    @field:Json(name = "longitud") // Usado por Retrofit
    val longitud: Double = 0.0
)

data class Listas(
    @field:Json(name = "_uuid") // Usado por Retrofit
    val id: String = "",
    @field:Json(name = "name") // Usado por Retrofit
    val name: String = "",
    @field:Json(name = "description") // Usado por Retrofit
    val description: String = "",
    val review: Double = 0.0,
    val email_owner: String = "",
    val lstPlaces: List<Lugares>?
)

data class Usuarios(
    val email: String = "",
    val fullname: String = "",
    val favoritesLists: List<String>?
)

data class ApiListResponse(
    val cursor: String?,
    val items: List<Listas>,
    val next_page: String?
)
data class ApiUserResponse(
    val cursor: String?,
    val items: List<Usuarios>,
    val next_page: String?
)
