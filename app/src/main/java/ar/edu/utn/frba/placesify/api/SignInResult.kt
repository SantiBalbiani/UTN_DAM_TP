package ar.edu.utn.frba.placesify.api

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)
data class UserData(
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?
)
data class SignInState(
    var isSignInSuccessful: Boolean = false,
    var signInError: String? = null
)