package ar.edu.utn.frba.placesify.api

import android.util.Log
import ar.edu.utn.frba.placesify.model.ApiCategoriesResponse
import ar.edu.utn.frba.placesify.model.ApiListResponse
import ar.edu.utn.frba.placesify.model.ApiPutUserResponse
import ar.edu.utn.frba.placesify.model.ApiUserResponse
import ar.edu.utn.frba.placesify.model.Listas
import ar.edu.utn.frba.placesify.model.NuevaLista
import ar.edu.utn.frba.placesify.model.NuevoUsuario
import ar.edu.utn.frba.placesify.model.Usuarios
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface BackendService {
    companion object {

        // Configuro el log level. Poner Level.NONE en PRODUCTION
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        val instance = Retrofit.Builder()
            .baseUrl("https://crudapi.co.uk/api/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor(logger)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    "Bearer mJtzfkgldCK080hZjWQ8hh2hj_4HoS2qvQeh7C1Ve5P5uEeZTw"
                ).addHeader(
                    "Content-Type",
                    "application/json"
                ).build()
                Log.d("RETROFIT REQUEST", "${request.toString()}")
                val resp = chain.proceed(request)
                // Deal with the response code
                if (resp.code == 200 || resp.code == 400) {
                    try {
                        val myJson =
                            resp.peekBody(2048).string() // peekBody() will not close the response
                        println(myJson)
                        println(resp.request)
                    } catch (e: Exception) {
                        println("Error parse json from intercept..............")
                    }
                } else {
                    println(resp.body)
                }
                resp
            }.build())
            .build().create(BackendService::class.java)
    }

   // @GET("searched_lists")
   // suspend fun getListas(): ApiListResponse
    @GET("listas")
    suspend fun getListas(): ApiListResponse

    @GET("listas/{id}")
    suspend fun getLista(@Path("id") id: String): Listas

    @POST("listas")
    suspend fun postLista(@Body listas: List<NuevaLista>): Listas

    @PUT("listas/{id}")
    suspend fun putLista(
        @Path("id") id:String,
        @Body lista:Listas
    ):Listas

    @GET("categorias")
    suspend fun getCategorias(): ApiCategoriesResponse

    @GET("usuarios")
    suspend fun getUsuarios(): ApiUserResponse

    @POST("usuarios")
    suspend fun postUsuario(@Body usuarios: List<NuevoUsuario>): Usuarios

    @PUT("usuarios/{id}")
    suspend fun putUsuario(
        @Path("id") id:String,
        @Body usuario: Usuarios
    ):Usuarios

    @DELETE("listas/{id}")
    suspend fun deleteLista(@Path("id") id:String):Listas

}