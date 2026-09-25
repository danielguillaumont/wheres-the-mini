package com.danielguillaumont.wheresthemini.presentation.parking

import com.danielguillaumont.wheresthemini.data.notification.ParkingReminderScheduler
import com.danielguillaumont.wheresthemini.data.repository.ParkingRepository
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.ZonedDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class ParkingViewModelTest {

    private val testDispatcher =
        StandardTestDispatcher()

    private lateinit var repository:
            ParkingRepository

    private lateinit var reminderScheduler:
            ParkingReminderScheduler

    private lateinit var activeParkingFlow:
            MutableStateFlow<ParkingSession?>

    private lateinit var parkingHistoryFlow:
            MutableStateFlow<List<ParkingSession>>

    private lateinit var viewModel:
            ParkingViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(
            testDispatcher
        )

        activeParkingFlow =
            MutableStateFlow(
                null
            )

        parkingHistoryFlow =
            MutableStateFlow(
                emptyList()
            )

        repository =
            mockk(
                relaxed = true
            )

        reminderScheduler =
            mockk(
                relaxed = true
            )

        every {
            repository.activeParking
        } returns activeParkingFlow

        every {
            repository.parkingHistory
        } returns parkingHistoryFlow

        viewModel =
            ParkingViewModel(
                repository =
                    repository,
                reminderScheduler =
                    reminderScheduler
            )

        testDispatcher
            .scheduler
            .runCurrent()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `database state is reflected in UI state`() =
        runTest(
            testDispatcher
        ) {
            val activeParking =
                sampleParkingSession(
                    id = 100L,
                    parkingLevel = "P2",
                    spotNumber = "134"
                )

            val historyItem =
                sampleParkingSession(
                    id = 99L,
                    parkingLevel = "P1",
                    spotNumber = "12",
                    recoveredAtMillis =
                        2_000L
                )

            activeParkingFlow.value =
                activeParking

            parkingHistoryFlow.value =
                listOf(
                    historyItem
                )

            testDispatcher
                .scheduler
                .runCurrent()

            assertEquals(
                activeParking,
                viewModel
                    .uiState
                    .value
                    .currentParking
            )

            assertEquals(
                listOf(
                    historyItem
                ),
                viewModel
                    .uiState
                    .value
                    .parkingHistory
            )
        }

    @Test
    fun `begin editing loads current parking into form`() =
        runTest(
            testDispatcher
        ) {
            val location =
                ParkingLocation(
                    latitude =
                        37.42200,
                    longitude =
                        -122.08400,
                    accuracyMeters =
                        5f
                )

            val currentParking =
                sampleParkingSession(
                    id = 101L,
                    parkingLevel = "P5",
                    spotNumber = "555",
                    note = "Evidence test",
                    parkingExpiry = "6:30 PM",
                    parkingExpiryMillis =
                        10_000_000L,
                    reminderEnabled = true,
                    location = location
                )

            activeParkingFlow.value =
                currentParking

            testDispatcher
                .scheduler
                .runCurrent()

            viewModel
                .beginEditingCurrentParking()

            val form =
                viewModel
                    .uiState
                    .value
                    .form

            assertEquals(
                "P5",
                form.parkingLevel
            )

            assertEquals(
                "555",
                form.spotNumber
            )

            assertEquals(
                "Evidence test",
                form.note
            )

            assertEquals(
                "6:30 PM",
                form.parkingExpiry
            )

            assertEquals(
                10_000_000L,
                form.parkingExpiryMillis
            )

            assertTrue(
                form.reminderEnabled
            )

            assertEquals(
                location,
                form.location
            )
        }

    @Test
    fun `saving parking trims form values and persists session`() =
        runTest(
            testDispatcher
        ) {
            val location =
                ParkingLocation(
                    latitude =
                        37.42200,
                    longitude =
                        -122.08400,
                    accuracyMeters =
                        5f
                )

            viewModel
                .beginNewParking()

            viewModel
                .updateParkingLevel(
                    "  P3  "
                )

            viewModel
                .updateSpotNumber(
                    "  127  "
                )

            viewModel
                .updateNote(
                    "  Beside the suspicious red car  "
                )

            viewModel
                .setCapturedLocation(
                    location
                )

            viewModel
                .saveParking()

            testDispatcher
                .scheduler
                .runCurrent()

            val savedParking =
                slot<ParkingSession>()

            coVerify(
                exactly = 1
            ) {
                repository
                    .saveParking(
                        capture(
                            savedParking
                        )
                    )
            }

            assertEquals(
                "P3",
                savedParking
                    .captured
                    .parkingLevel
            )

            assertEquals(
                "127",
                savedParking
                    .captured
                    .spotNumber
            )

            assertEquals(
                "Beside the suspicious red car",
                savedParking
                    .captured
                    .note
            )

            assertEquals(
                location,
                savedParking
                    .captured
                    .location
            )

            assertEquals(
                savedParking.captured,
                viewModel
                    .uiState
                    .value
                    .currentParking
            )

            assertEquals(
                ParkingFormState(),
                viewModel
                    .uiState
                    .value
                    .form
            )

            verify(
                exactly = 1
            ) {
                reminderScheduler
                    .cancelReminder(
                        savedParking
                            .captured
                            .id
                    )
            }
        }

    @Test
    fun `expiry reminder schedules fifteen minutes before expiry`() =
        runTest(
            testDispatcher
        ) {
            val oneHourFromNow =
                ZonedDateTime
                    .now()
                    .plusHours(
                        1
                    )

            viewModel
                .updateParkingLevel(
                    "P4"
                )

            viewModel
                .updateSpotNumber(
                    "301"
                )

            viewModel
                .setParkingExpiry(
                    hour =
                        oneHourFromNow.hour,
                    minute =
                        oneHourFromNow.minute
                )

            viewModel
                .setReminderEnabled(
                    true
                )

            val expiryMillis =
                viewModel
                    .uiState
                    .value
                    .form
                    .parkingExpiryMillis

            assertTrue(
                expiryMillis != null
            )

            viewModel
                .saveParking()

            testDispatcher
                .scheduler
                .runCurrent()

            val savedParking =
                slot<ParkingSession>()

            coVerify(
                exactly = 1
            ) {
                repository
                    .saveParking(
                        capture(
                            savedParking
                        )
                    )
            }

            val reminderTime =
                slot<Long>()

            verify(
                exactly = 1
            ) {
                reminderScheduler
                    .scheduleReminder(
                        parkingId =
                            savedParking
                                .captured
                                .id,
                        reminderAtMillis =
                            capture(
                                reminderTime
                            ),
                        parkingLevel =
                            "P4",
                        spotNumber =
                            "301"
                    )
            }

            val expectedReminderTime =
                requireNotNull(
                    savedParking
                        .captured
                        .parkingExpiryMillis
                ) -
                        15L *
                        60L *
                        1000L

            assertEquals(
                expectedReminderTime,
                reminderTime.captured
            )

            assertTrue(
                savedParking
                    .captured
                    .reminderEnabled
            )
        }

    @Test
    fun `clearing expiry also disables reminder`() =
        runTest(
            testDispatcher
        ) {
            val oneHourFromNow =
                ZonedDateTime
                    .now()
                    .plusHours(
                        1
                    )

            viewModel
                .setParkingExpiry(
                    hour =
                        oneHourFromNow.hour,
                    minute =
                        oneHourFromNow.minute
                )

            viewModel
                .setReminderEnabled(
                    true
                )

            assertTrue(
                viewModel
                    .uiState
                    .value
                    .form
                    .reminderEnabled
            )

            viewModel
                .clearParkingExpiry()

            val form =
                viewModel
                    .uiState
                    .value
                    .form

            assertEquals(
                "",
                form.parkingExpiry
            )

            assertNull(
                form.parkingExpiryMillis
            )

            assertFalse(
                form.reminderEnabled
            )

            assertNull(
                form.reminderError
            )
        }

    @Test
    fun `notification permission denial disables reminder and shows error`() =
        runTest(
            testDispatcher
        ) {
            val oneHourFromNow =
                ZonedDateTime
                    .now()
                    .plusHours(
                        1
                    )

            viewModel
                .setParkingExpiry(
                    hour =
                        oneHourFromNow.hour,
                    minute =
                        oneHourFromNow.minute
                )

            viewModel
                .setReminderEnabled(
                    true
                )

            viewModel
                .setNotificationPermissionDenied()

            val form =
                viewModel
                    .uiState
                    .value
                    .form

            assertFalse(
                form.reminderEnabled
            )

            assertEquals(
                "Notifications are disabled, so the reminder cannot be scheduled.",
                form.reminderError
            )
        }

    @Test
    fun `finding the Mini clears active parking and marks it recovered`() =
        runTest(
            testDispatcher
        ) {
            val currentParking =
                sampleParkingSession(
                    id = 729L,
                    parkingLevel = "P7",
                    spotNumber = "729",
                    note = "Near garbage room"
                )

            activeParkingFlow.value =
                currentParking

            testDispatcher
                .scheduler
                .runCurrent()

            assertEquals(
                currentParking,
                viewModel
                    .uiState
                    .value
                    .currentParking
            )

            viewModel
                .clearCurrentParking()

            testDispatcher
                .scheduler
                .runCurrent()

            assertNull(
                viewModel
                    .uiState
                    .value
                    .currentParking
            )

            assertEquals(
                ParkingFormState(),
                viewModel
                    .uiState
                    .value
                    .form
            )

            verify(
                exactly = 1
            ) {
                reminderScheduler
                    .cancelReminder(
                        729L
                    )
            }

            coVerify(
                exactly = 1
            ) {
                repository
                    .markParkingRecovered(
                        729L
                    )
            }
        }

    private fun sampleParkingSession(
        id: Long,
        parkingLevel: String,
        spotNumber: String,
        note: String = "",
        parkingExpiry: String = "",
        parkedAtMillis: Long = 1_000L,
        location: ParkingLocation? = null,
        recoveredAtMillis: Long? = null,
        parkingExpiryMillis: Long? = null,
        reminderEnabled: Boolean = false,
        photoPath: String? = null
    ): ParkingSession {

        return ParkingSession(
            id = id,
            parkingLevel = parkingLevel,
            spotNumber = spotNumber,
            note = note,
            parkingExpiry = parkingExpiry,
            parkedAtMillis = parkedAtMillis,
            location = location,
            recoveredAtMillis = recoveredAtMillis,
            parkingExpiryMillis = parkingExpiryMillis,
            reminderEnabled = reminderEnabled,
            photoPath = photoPath
        )
    }
}