package ar.edu.utn.frba.placesify.api

import ar.edu.utn.frba.placesify.model.OpenStreetmapResponse
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface OpenStreetmapService {
    companion object {

        var gson = GsonBuilder()
            .setLenient()
            .create()

        val instance = Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient.Builder().addInterceptor { chain ->

                val resp = chain.proceed(chain.request())
                // Deal with the response code
                if (resp.code == 200) {
                    try {
                        val myJson =
                            resp.peekBody(2048).string() // peekBody() will not close the response
                        println(myJson)
                    } catch (e: Exception) {
                        println("Error parse json from intercept..............")
                    }
                } else {
                    println(resp)
                }
                resp

            }.build())
            .build().create(OpenStreetmapService::class.java)
    }

    @GET("search?")
    @Headers(
        "Accept: application/json",
        "Accept-Language: es-ES"
    )
    suspend fun getLugares(
        @Query("q") q: String,
        @Query("format") format: String = "jsonv2",
        @Query("limit") limit: String = "50",
        @Query("countrycodes") countrycodes: String = "AR"
    ): List<OpenStreetmapResponse>
}
