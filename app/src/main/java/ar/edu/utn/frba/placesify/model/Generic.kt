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
@Serializable
data class Listas(
    @field:Json(name = "_created") // Usado por Retrofit
    val created: String? = "",
    @field:Json(name = "_uuid") // Usado por Retrofit
    val id: String? = "",
    @field:Json(name = "name") // Usado por Retrofit
    var name: String = "",
    @field:Json(name = "description") // Usado por Retrofit
    var description: String? = "",
    val review: Double? = 0.0,
    val email_owner: String = "",
    var lstPlaces: List<Lugares>?,
    var lstCategories: List<Int>?,
    var likes: Int? = 0
)

data class Usuarios(
    @field:Json(name = "_uuid") // Usado por Retrofit
    val id: String? = "",
    val email: String? = "",
    val fullname: String? = "",
    val favoritesLists: MutableList<String>?
)

data class NuevoUsuario(
    val email: String? = "",
    val fullname: String? = "",
    val favoritesLists: MutableList<String>?
)

data class NuevaLista(
    var name: String = "",
    var description: String? = "",
    var review: Double? = 0.0,
    var email_owner: String = "",
    var lstPlaces: List<Lugares>?,
    var lstCategories: List<Int>?,
    var likes: Int? = 0
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

data class ApiPutUserResponse(
    val items: List<Usuarios>
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
