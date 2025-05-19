package com.marcel.eatbymark.places.placesaround.data

import com.marcel.eatbymark.places.currentlocation.data.DefaultCurrentLocationDataSource
import com.marcel.eatbymark.places.favouriteplaces.data.FavouritePlacesDataSource
import com.marcel.eatbymark.places.favouriteplaces.domain.models.GetFavouritePlacesIdsResult
import com.marcel.eatbymark.places.placesTestData
import com.marcel.eatbymark.places.placesaround.PlacesAroundConfig
import com.marcel.eatbymark.places.placesaround.data.models.GetPlacesAroundError
import com.marcel.eatbymark.places.placesaround.domain.models.PlacesAroundUpdate
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultPlacesAroundRepositoryTest {

    @Test
    fun `When API fails, Then emit error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val applicationScope = CoroutineScope(dispatcher + SupervisorJob())
        val placesAroundConfig = PlacesAroundConfig(updatesIntervalSeconds = 10)
        // Return empty favourite places list
        val mockFavouritePlacesDataSource: FavouritePlacesDataSource = mockk {
            coEvery { getAllFavouritePlacesIds() } returns flowOf(
                GetFavouritePlacesIdsResult.Success(
                    emptyList()
                )
            )
        }
        // Return error from DataSource
        val mockPlacesAroundDataSource: PlacesAroundDataSource = mockk {
            coEvery { getPlacesAround(any(), any()) } returns Result.failure(
                GetPlacesAroundError.UnknownError("Simulated failure")
            )
        }
        val repository = DefaultPlacesAroundRepository(
            applicationScope = applicationScope,
            currentLocationDataSource = DefaultCurrentLocationDataSource(dispatcher),
            placesAroundConfig = placesAroundConfig,
            favouritePlacesDataSource = mockFavouritePlacesDataSource,
            placesAroundDataSource = mockPlacesAroundDataSource,
        )
        val emittedResults = mutableListOf<PlacesAroundUpdate>()
        runCurrent() // Starts location updates from CurrentLocationDataSource
        val collectJob = launch(dispatcher) {
            repository.getPlacesAroundUpdates().collect {
                emittedResults.add(it)
            }
        }
        runCurrent() // Starts collect job
        collectJob.cancelAndJoin()
        applicationScope.cancel()
        Assert.assertEquals(2, emittedResults.size)
        assert(emittedResults[0] is PlacesAroundUpdate.Loading)
        assert(emittedResults.last() is PlacesAroundUpdate.Error)
    }

    @Test
    fun `When API succeeds, Then emit success with places`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val applicationScope = CoroutineScope(dispatcher + SupervisorJob())
        val placesAroundConfig = PlacesAroundConfig(updatesIntervalSeconds = 10)
        // Return empty favourite places list
        val mockFavouritePlacesDataSource: FavouritePlacesDataSource = mockk {
            coEvery { getAllFavouritePlacesIds() } returns flowOf(
                GetFavouritePlacesIdsResult.Success(
                    emptyList()
                )
            )
        }
        // Return success from DataSource
        val mockPlacesAroundDataSource: PlacesAroundDataSource = mockk {
            coEvery { getPlacesAround(any(), any()) } returns Result.success(placesTestData)
        }
        val repository = DefaultPlacesAroundRepository(
            applicationScope = applicationScope,
            currentLocationDataSource = DefaultCurrentLocationDataSource(dispatcher),
            placesAroundConfig = placesAroundConfig,
            favouritePlacesDataSource = mockFavouritePlacesDataSource,
            placesAroundDataSource = mockPlacesAroundDataSource,
        )
        val emittedResults = mutableListOf<PlacesAroundUpdate>()
        runCurrent() // Starts location updates from CurrentLocationDataSource
        val collectJob = launch(dispatcher) {
            repository.getPlacesAroundUpdates().collect {
                emittedResults.add(it)
            }
        }
        runCurrent() // Starts collect job
        collectJob.cancelAndJoin()
        applicationScope.cancel()
        Assert.assertEquals(2, emittedResults.size)
        assert(emittedResults[0] is PlacesAroundUpdate.Loading)
        assert(emittedResults.last() is PlacesAroundUpdate.Success)
        Assert.assertEquals(
            (emittedResults.last() as PlacesAroundUpdate.Success).places,
            placesTestData
        )
    }

    @Test
    fun `Given API succeeded, When item id is in favourites, Then item isFavourite and otherwise`() =
        runTest {
            val dispatcher = StandardTestDispatcher(testScheduler)
            val applicationScope = CoroutineScope(dispatcher + SupervisorJob())
            val placesAroundConfig = PlacesAroundConfig(updatesIntervalSeconds = 10)
            // Return success from DataSource
            val mockPlacesAroundDataSource: PlacesAroundDataSource = mockk {
                coEvery { getPlacesAround(any(), any()) } returns Result.success(placesTestData)
            }
            val mockFavouritePlacesDataSource: FavouritePlacesDataSource = mockk {
                coEvery { getAllFavouritePlacesIds() } returns flowOf(
                    GetFavouritePlacesIdsResult.Success(
                        placesTestData.mapIndexed { index, place ->
                            // Add all even indexed places ids to favourites
                            if (index % 2 == 0) place.id else null
                        }.filterNotNull()
                    )
                )
            }
            val repository = DefaultPlacesAroundRepository(
                applicationScope = applicationScope,
                currentLocationDataSource = DefaultCurrentLocationDataSource(dispatcher),
                placesAroundConfig = placesAroundConfig,
                favouritePlacesDataSource = mockFavouritePlacesDataSource,
                placesAroundDataSource = mockPlacesAroundDataSource,
            )
            val emittedResults = mutableListOf<PlacesAroundUpdate>()
            runCurrent() // Starts location updates from CurrentLocationDataSource
            val collectJob = launch(dispatcher) {
                repository.getPlacesAroundUpdates().collect {
                    emittedResults.add(it)
                }
            }
            runCurrent() // Starts collect job
            collectJob.cancelAndJoin()
            applicationScope.cancel()
            val expectedPlaces = placesTestData.mapIndexed { index, place ->
                place.copy(isFavourite = index % 2 == 0)
            }
            Assert.assertEquals(2, emittedResults.size)
            assert(emittedResults[0] is PlacesAroundUpdate.Loading)
            assert(emittedResults.last() is PlacesAroundUpdate.Success)
            Assert.assertEquals(
                (emittedResults.last() as PlacesAroundUpdate.Success).places,
                expectedPlaces
            )
        }
}