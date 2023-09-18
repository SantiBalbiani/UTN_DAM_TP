package ar.edu.utn.frba.placesify.model

import com.squareup.moshi.Json
import com.google.gson.annotations.SerializedName

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

data class OpenStreetmapResponse(
    @SerializedName("place_id") var placeId: Long? = null,
    @SerializedName("licence") var licence: String? = null,
    @SerializedName("osm_type") var osmType: String? = null,
    @SerializedName("osm_id") var osmId: Long? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("class") var nombreclass: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("place_rank") var placeRank: Int? = null,
    @SerializedName("importance") var importance: Double? = null,
    @SerializedName("addresstype") var addresstype: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("boundingbox") var boundingbox: ArrayList<String> = arrayListOf()
)

