package com.danielguillaumont.wheresthemini.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.danielguillaumont.wheresthemini.data.local.WheresTheMiniDatabase
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ParkingRepositoryInstrumentedTest {

    private lateinit var database:
            WheresTheMiniDatabase

    private lateinit var repository:
            ParkingRepository

    @Before
    fun setUp() {
        val context =
            ApplicationProvider.getApplicationContext<Context>()

        database =
            Room.inMemoryDatabaseBuilder(
                context,
                WheresTheMiniDatabase::class.java
            )
                .allowMainThreadQueries()
                .build()

        repository =
            ParkingRepository(
                database.parkingDao()
            )
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun savingParkingPersistsCompleteActiveRecord() =
        runBlocking {
            val location =
                ParkingLocation(
                    latitude = 37.42200,
                    longitude = -122.08400,
                    accuracyMeters = 5f
                )

            val parking =
                ParkingSession(
                    id = 101L,
                    parkingLevel = "P5",
                    spotNumber = "555",
                    note = "Evidence test",
                    parkingExpiry = "7:00 PM",
                    parkedAtMillis = 1_000L,
                    location = location,
                    parkingExpiryMillis = 5_000L,
                    reminderEnabled = true,
                    photoPath = "/test/photos/mini.jpg"
                )

            repository.saveParking(
                parking
            )

            val savedParking =
                repository
                    .activeParking
                    .first()

            assertNotNull(
                savedParking
            )

            assertEquals(
                101L,
                savedParking?.id
            )

            assertEquals(
                "P5",
                savedParking?.parkingLevel
            )

            assertEquals(
                "555",
                savedParking?.spotNumber
            )

            assertEquals(
                "Evidence test",
                savedParking?.note
            )

            assertEquals(
                "7:00 PM",
                savedParking?.parkingExpiry
            )

            assertEquals(
                1_000L,
                savedParking?.parkedAtMillis
            )

            assertEquals(
                location,
                savedParking?.location
            )

            assertEquals(
                5_000L,
                savedParking?.parkingExpiryMillis
            )

            assertTrue(
                savedParking?.reminderEnabled == true
            )

            assertEquals(
                "/test/photos/mini.jpg",
                savedParking?.photoPath
            )

            assertNull(
                savedParking?.recoveredAtMillis
            )
        }

    @Test
    fun recoveringParkingMovesItFromActiveParkingToHistory() =
        runBlocking {
            val parking =
                ParkingSession(
                    id = 202L,
                    parkingLevel = "P2",
                    spotNumber = "134",
                    note = "Close by parking entrance",
                    parkingExpiry = "",
                    parkedAtMillis = 2_000L
                )

            repository.saveParking(
                parking
            )

            val activeBeforeRecovery =
                repository
                    .activeParking
                    .first()

            assertNotNull(
                activeBeforeRecovery
            )

            repository.markParkingRecovered(
                parkingId = 202L
            )

            val activeAfterRecovery =
                repository
                    .activeParking
                    .first()

            val history =
                repository
                    .parkingHistory
                    .first()

            assertNull(
                activeAfterRecovery
            )

            assertEquals(
                1,
                history.size
            )

            val recoveredParking =
                history.first()

            assertEquals(
                202L,
                recoveredParking.id
            )

            assertEquals(
                "P2",
                recoveredParking.parkingLevel
            )

            assertEquals(
                "134",
                recoveredParking.spotNumber
            )

            assertNotNull(
                recoveredParking.recoveredAtMillis
            )
        }

    @Test
    fun updatingSameParkingIdReplacesSavedParkingData() =
        runBlocking {
            val originalParking =
                ParkingSession(
                    id = 303L,
                    parkingLevel = "P1",
                    spotNumber = "10",
                    note = "Original note",
                    parkingExpiry = "",
                    parkedAtMillis = 3_000L,
                    reminderEnabled = false
                )

            repository.saveParking(
                originalParking
            )

            val updatedParking =
                originalParking.copy(
                    parkingLevel = "P4",
                    spotNumber = "301",
                    note = "Near the elevator",
                    parkingExpiry = "6:30 PM",
                    parkingExpiryMillis = 9_000L,
                    reminderEnabled = true
                )

            repository.saveParking(
                updatedParking
            )

            val savedParking =
                repository
                    .activeParking
                    .first()

            assertNotNull(
                savedParking
            )

            assertEquals(
                303L,
                savedParking?.id
            )

            assertEquals(
                "P4",
                savedParking?.parkingLevel
            )

            assertEquals(
                "301",
                savedParking?.spotNumber
            )

            assertEquals(
                "Near the elevator",
                savedParking?.note
            )

            assertEquals(
                "6:30 PM",
                savedParking?.parkingExpiry
            )

            assertEquals(
                9_000L,
                savedParking?.parkingExpiryMillis
            )

            assertTrue(
                savedParking?.reminderEnabled == true
            )

            assertFalse(
                repository
                    .parkingHistory
                    .first()
                    .any {
                        it.id == 303L
                    }
            )
        }
}