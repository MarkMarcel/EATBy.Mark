package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.core.IODispatcher
import com.marcel.eatbymark.places.models.Place
import com.marcel.eatbymark.places.placesaround.data.models.GenericItemAPIResponse
import com.marcel.eatbymark.places.placesaround.data.models.GetPlacesAroundError
import com.marcel.eatbymark.places.placesaround.data.models.PlacesAPIResponse
import com.marcel.eatbymark.places.placesaround.data.models.SectionAPIResponse
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import jakarta.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class PlacesAroundRemoteDataSource @Inject constructor(
    private val api: PlacesAroundAPI,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : PlacesAroundDataSource {
    override suspend fun getPlacesAround(
        latitude: Double,
        longitude: Double
    ): Result<List<Place>> {
        return withContext(ioDispatcher) {
            try {
                val response = api.getPlacesAround(latitude, longitude)
                Result.success(mapAndTakeFirst15Places(response))

            } catch (e: HttpException) {
                when (e.code()) {
                    in 400..499 -> Result.failure(GetPlacesAroundError.ClientError(e.message()))
                    in 500..599 -> Result.failure(GetPlacesAroundError.ServerError(e.message()))
                    else -> Result.failure(GetPlacesAroundError.UnknownError(e.message()))
                }
            } catch (e: IOException) {
                Result.failure(GetPlacesAroundError.NetworkError(e.message))
            } catch (e: JsonDataException) {
                Result.failure(GetPlacesAroundError.ServerError(e.message))
            } catch (e: JsonEncodingException) {
                Result.failure(GetPlacesAroundError.ServerError(e.message))
            } catch (e: Exception) {
                Result.failure(GetPlacesAroundError.UnknownError(e.message))
            }
        }
    }

    private fun mapAndTakeFirst15Places(response: PlacesAPIResponse): List<Place> {
        return response.sections
            .asSequence() // Sequence for potentially better performance on large lists
            .flatMap { section: SectionAPIResponse ->
                section.items?.asSequence() ?: emptySequence()
            } // Flatten items from all sections
            .mapNotNull { item: GenericItemAPIResponse ->
                item.toPlace()
            } // Get venue if item has one, filter out nulls
            .take(15) // Take the first 15
            .toList()
    }
}