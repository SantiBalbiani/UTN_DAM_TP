package ar.edu.utn.frba.placesify.api

import ar.edu.utn.frba.placesify.model.Listas
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ListService {
    companion object {
        val instance = Retrofit.Builder()
            .baseUrl("https://crudcrud.com/api/e7fdaf7a61cc4d93a9114dd52671f5e0/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().build())
            .build().create(ListService::class.java)
    }

    @GET("listas")
    suspend fun getListas(): List<Listas>

    @POST("lista")
    suspend fun addLista(@Body lista: Listas): Listas
}