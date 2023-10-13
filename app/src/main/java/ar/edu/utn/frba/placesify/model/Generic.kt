package ar.edu.utn.frba.placesify.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import kotlinx.serialization.Serializable

data class Categorias(
    @field:Json(name = "id") // Usado por Retrofit
    val id: Int = 0,
    @field:Json(name = "name") // Usado por Retrofit
    val name: String = "",
    @field:Json(name = "icono") // Usado por Retrofit
    val icono: String = "",
)

@Serializable
data class Lugares(
    val id: Long? = 0,
    val name: String = "",
    val description: String = "",
    @field:Json(name = "latitud") // Usado por Retrofit
    val latitud: Double = 0.0,
    @field:Json(name = "longitud") // Usado por Retrofit
    val longitud: Double = 0.0
)

data class Listas(
    @field:Json(name = "_created") // Usado por Retrofit
    val created: Double = 0.00,
    @field:Json(name = "_uuid") // Usado por Retrofit
    val id: String = "",
    @field:Json(name = "name") // Usado por Retrofit
    val name: String = "",
    @field:Json(name = "description") // Usado por Retrofit
    val description: String = "",
    val review: Double = 0.0,
    val email_owner: String = "",
    val lstPlaces: List<Lugares>?,
    val lstCategories: List<Int>?,
    val likes: Int = 0
)

data class Usuarios(
    val email: String = "",
    val fullname: String = "",
    val favoritesLists: List<String>?
)

data class ApiListResponse(
    val cursor: String? = "",
    val items: List<Listas>,
    val next_page: String? = ""
)

data class ApiCategoriesResponse(
    val cursor: String? = "",
    val items: List<Categorias>,
    val next_page: String? = ""
)

data class ApiUserResponse(
    val cursor: String? = "",
    val items: List<Usuarios>,
    val next_page: String? = ""
)

data class OpenStreetmapResponse(
    @SerializedName("place_id") var placeId: Long? = -1,
    @SerializedName("licence") var licence: String? = "",
    @SerializedName("osm_type") var osmType: String? = "",
    @SerializedName("osm_id") var osmId: Long? = -1,
    @SerializedName("lat") var lat: String? = "",
    @SerializedName("lon") var lon: String? = "",
    @SerializedName("category") var category: String? = "",
    @SerializedName("type") var type: String? = "",
    @SerializedName("place_rank") var placeRank: Int? = -1,
    @SerializedName("importance") var importance: Double? = -1.0,
    @SerializedName("addresstype") var addresstype: String? = "",
    @SerializedName("name") var name: String? = "",
    @SerializedName("display_name") var displayName: String? = "",
    @SerializedName("boundingbox") var boundingbox: ArrayList<String> = arrayListOf("")
)
