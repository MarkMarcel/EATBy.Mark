package com.marcel.eatbymark.core

import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.data.PlacesAroundAPI
import com.marcel.eatbymark.places.placesaround.data.models.GenericItemAPIResponse
import com.marcel.eatbymark.places.placesaround.data.models.PlacesAPIResponse
import com.marcel.eatbymark.places.placesaround.data.models.SectionAPIResponse
import com.marcel.eatbymark.places.placesaround.data.toPlace
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

data class NetworkClientConfig(
    val baseUrl: String,
    val connectTimeoutSeconds: Long,
    val readTimeoutSeconds: Long
)

// Benchmarking
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY // Logs headers and body
}
private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(60, TimeUnit.SECONDS)
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl("https://restaurant-api.wolt.com/v1/pages/")
    .client(okHttpClient)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

fun main() = runBlocking {
    val apiService = retrofit.create(PlacesAroundAPI::class.java)
    val startTime = System.currentTimeMillis()
    var endTime = System.currentTimeMillis()
    try {
        val response: PlacesAPIResponse = apiService.getPlacesAround(60.170187, 24.930599)

        val venues = response.sections
            ?.asSequence() // Sequence for potentially better performance on large lists
            ?.flatMap { section: SectionAPIResponse ->
                section.items?.asSequence() ?: emptySequence()
            } // Flatten items from all sections
            ?.mapNotNull { item: GenericItemAPIResponse -> item.toPlace() } // Get venue if item has one, filter out nulls
            ?.take(5)
            ?.toList()// Take the first venue found, or null if none
        println("Venues: $venues")
        endTime = System.currentTimeMillis()
    } catch (e: Exception) {
        println("Error fetching or processing data: ${e.message}")
        e.printStackTrace()
        endTime = System.currentTimeMillis()
    }
    //println("Time taken - gson: ${endTime - startTime} ms")
}
