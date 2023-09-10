package ar.edu.utn.frba.placesify.model

data class Lugares(val name: String, val description: String)
data class Listas(val name: String, val description: String, val lstPlaces: List<Lugares>)
data class Usuarios(val name: String, val lastname: String, val email: String)