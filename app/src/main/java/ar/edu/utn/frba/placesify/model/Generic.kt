package ar.edu.utn.frba.placesify.model

data class Lugares(val id: Int, val name: String, val description: String)
data class Listas(val id: Int, val name: String, val description: String, val lstPlaces: List<Lugares>)
data class Usuarios(val id: Int, val name: String, val lastname: String, val email: String)