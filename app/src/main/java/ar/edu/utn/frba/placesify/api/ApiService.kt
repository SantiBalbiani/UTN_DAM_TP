package ar.edu.utn.frba.placesify.api

import ar.edu.utn.frba.placesify.model.ApiListResponse
import ar.edu.utn.frba.placesify.model.ApiUserResponse
import ar.edu.utn.frba.placesify.model.Listas
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    companion object {
        val instance = Retrofit.Builder()
            .baseUrl("https://crudapi.co.uk/api/v1/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader(
                    "Authorization",
                    "Bearer s6A42K8fhYhBeQ7QZD-yhfj6zVAQpWkYPws_ucD_aGKkbJxc9A"
                ).build()
                chain.proceed(request)
            }.build())
            .build().create(ApiService::class.java)
    }

    @GET("listas")
    suspend fun getListas(): ApiListResponse

    @GET("listas/{id}")
    suspend fun getLista(@Path("id") id: String): ApiListResponse

    @POST("lista")
    suspend fun addLista(@Body lista: Listas): Listas

    @GET("usuarios")
    suspend fun getUsuarios(): ApiUserResponse

}