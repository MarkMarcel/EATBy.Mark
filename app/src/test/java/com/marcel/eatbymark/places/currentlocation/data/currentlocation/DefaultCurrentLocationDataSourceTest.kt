package com.marcel.eatbymark.places.currentlocation.data.currentlocation

import com.marcel.eatbymark.places.currentlocation.data.DefaultCurrentLocationDataSource
import com.marcel.eatbymark.places.models.Coordinate
import com.marcel.eatbymark.places.models.coordinates
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test


class DefaultCurrentLocationDataSourceTest {
    private val updatesIntervalMillis = 10 * 1000L

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given the timer is running, when 10s pass, then emit the next coordinate`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler) // Use the testScheduler by runTest
        val dataSource = DefaultCurrentLocationDataSource(
            coroutineDispatcher = dispatcher,
        )
        val emittedCoordinates = mutableListOf<Coordinate>() // List to collect emitted coordinates
        val collectJob = launch {
            dataSource.getCurrentLocation(
                updatesIntervalSeconds = 10
            ).collect { coordinate ->
                emittedCoordinates.add(coordinate)
            }
        }
        runCurrent() // Run pending tasks
        assertEquals(
            "Should emit the first coordinate immediately",
            1,
            emittedCoordinates.size
        )
        assertEquals(
            "First emitted coordinate should be the first in the coordinates list",
            coordinates[0],
            emittedCoordinates.last()
        )
        // Time advanced by less than the interval
        advanceTimeBy(updatesIntervalMillis - 1)
        assertEquals(
            "No new emission before the interval",
            1,
            emittedCoordinates.size
        )
        advanceTimeBy(2) // Time advanced up to interval (Total: 10000ms)
        assertEquals(
            "Should emit the second coordinate after first interval",
            2,
            emittedCoordinates.size
        )
        assertEquals(
            "Second emitted coordinate should be the second in the list",
            coordinates[1],
            emittedCoordinates.last()
        )
        val intervalToAdvance = (coordinates.size - 1)
        advanceTimeBy(updatesIntervalMillis * intervalToAdvance) // Time advanced many intervals
        assertEquals(
            "Should emit correct number of coordinates after multiple intervals and a loop",
            intervalToAdvance + 2,
            emittedCoordinates.size
        )

        // Verifiers the last few emitted coordinates show the loop
        val expectedLoopStart = coordinates[0]
        val expectedPenultimate = coordinates[coordinates.lastIndex]

        if (coordinates.size > 1) {
            assertEquals(
                "Should emit the last coordinate before looping",
                expectedPenultimate,
                emittedCoordinates[emittedCoordinates.lastIndex - 1]
            )
            assertEquals(
                "Should loop back to the first coordinate",
                expectedLoopStart,
                emittedCoordinates.last()
            )
        }
        collectJob.cancel()
        runCurrent() // ensures cancellation is propagated
        advanceTimeBy(updatesIntervalMillis * 5)
        assertEquals(
            "No emissions after cancellation",
            1 + coordinates.size,
            emittedCoordinates.size
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `Given the timer is running, when cancelled, then stop emitting`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        val dataSource = DefaultCurrentLocationDataSource(
            coroutineDispatcher = dispatcher,
        )
        val emittedCoordinates = mutableListOf<Coordinate>()
        launch {
            dataSource.getCurrentLocation(
                updatesIntervalSeconds = 10
            ).collect { coordinate ->
                emittedCoordinates.add(coordinate)
                if (emittedCoordinates.size == 3) {
                    cancel() // Cancel the collection job
                }
            }
        }
        runCurrent()
        advanceTimeBy(updatesIntervalMillis)
        advanceTimeBy(updatesIntervalMillis)
        runCurrent()
        assertEquals(
            "Should stop emitting after cancellation",
            3,
            emittedCoordinates.size,
        )
        advanceTimeBy(updatesIntervalMillis * 10) // Advance time beyond cancellation
        assertEquals(
            "No emissions after cancellation and time advancement",
            3,
            emittedCoordinates.size
        )
    }
}