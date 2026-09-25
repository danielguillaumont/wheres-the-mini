package com.danielguillaumont.wheresthemini.presentation.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielguillaumont.wheresthemini.data.notification.ParkingReminderScheduler
import com.danielguillaumont.wheresthemini.data.repository.ParkingRepository
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private const val REMINDER_OFFSET_MILLIS =
    15L * 60L * 1000L

data class ParkingFormState(
    val parkingLevel: String = "",
    val spotNumber: String = "",
    val note: String = "",
    val parkingExpiry: String = "",
    val parkingExpiryMillis: Long? = null,
    val reminderEnabled: Boolean = false,
    val reminderError: String? = null,
    val location: ParkingLocation? = null,
    val isLocating: Boolean = false,
    val locationError: String? = null
)

data class ParkingUiState(
    val form: ParkingFormState =
        ParkingFormState(),

    val currentParking:
    ParkingSession? = null,

    val parkingHistory:
    List<ParkingSession> =
        emptyList()
)

class ParkingViewModel(
    private val repository:
    ParkingRepository,

    private val reminderScheduler:
    ParkingReminderScheduler
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(
            ParkingUiState()
        )

    val uiState:
            StateFlow<ParkingUiState> =
        _uiState.asStateFlow()

    init {
        observeParkingDatabase()
    }

    private fun observeParkingDatabase() {
        viewModelScope.launch {
            combine(
                repository.activeParking,
                repository.parkingHistory
            ) { activeParking, history ->

                activeParking to history

            }.collect {
                    (activeParking, history) ->

                _uiState.value =
                    _uiState.value.copy(
                        currentParking =
                            activeParking,

                        parkingHistory =
                            history
                    )
            }
        }
    }

    fun beginNewParking() {
        _uiState.value =
            _uiState.value.copy(
                form =
                    ParkingFormState()
            )
    }

    fun beginEditingCurrentParking() {
        val currentParking =
            _uiState.value
                .currentParking
                ?: return

        _uiState.value =
            _uiState.value.copy(
                form =
                    ParkingFormState(
                        parkingLevel =
                            currentParking
                                .parkingLevel,

                        spotNumber =
                            currentParking
                                .spotNumber,

                        note =
                            currentParking
                                .note,

                        parkingExpiry =
                            currentParking
                                .parkingExpiry,

                        parkingExpiryMillis =
                            currentParking
                                .parkingExpiryMillis,

                        reminderEnabled =
                            currentParking
                                .reminderEnabled,

                        location =
                            currentParking
                                .location
                    )
            )
    }

    fun updateParkingLevel(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            parkingLevel =
                                value
                        )
            )
    }

    fun updateSpotNumber(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            spotNumber =
                                value
                        )
            )
    }

    fun updateNote(
        value: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            note =
                                value
                        )
            )
    }

    fun setParkingExpiry(
        hour: Int,
        minute: Int
    ) {
        val now =
            ZonedDateTime.now()

        var expiry =
            now
                .withHour(
                    hour
                )
                .withMinute(
                    minute
                )
                .withSecond(
                    0
                )
                .withNano(
                    0
                )

        if (
            !expiry.isAfter(
                now
            )
        ) {
            expiry =
                expiry.plusDays(
                    1
                )
        }

        val formatter =
            DateTimeFormatter.ofPattern(
                "h:mm a",
                Locale.US
            )

        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            parkingExpiry =
                                expiry.format(
                                    formatter
                                ),

                            parkingExpiryMillis =
                                expiry
                                    .toInstant()
                                    .toEpochMilli(),

                            reminderError =
                                null
                        )
            )
    }

    fun clearParkingExpiry() {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            parkingExpiry =
                                "",

                            parkingExpiryMillis =
                                null,

                            reminderEnabled =
                                false,

                            reminderError =
                                null
                        )
            )
    }

    fun setReminderEnabled(
        enabled: Boolean
    ) {
        val form =
            _uiState.value.form

        if (
            enabled &&
            form.parkingExpiryMillis ==
            null
        ) {
            return
        }

        _uiState.value =
            _uiState.value.copy(
                form =
                    form.copy(
                        reminderEnabled =
                            enabled,

                        reminderError =
                            null
                    )
            )
    }

    fun setNotificationPermissionDenied() {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            reminderEnabled =
                                false,

                            reminderError =
                                "Notifications are disabled, so the reminder cannot be scheduled."
                        )
            )
    }

    fun beginLocationCapture() {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            isLocating =
                                true,

                            locationError =
                                null
                        )
            )
    }

    fun setCapturedLocation(
        location: ParkingLocation
    ) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            location =
                                location,

                            isLocating =
                                false,

                            locationError =
                                null
                        )
            )
    }

    fun setLocationError(
        message: String
    ) {
        _uiState.value =
            _uiState.value.copy(
                form =
                    _uiState.value
                        .form
                        .copy(
                            isLocating =
                                false,

                            locationError =
                                message
                        )
            )
    }

    fun setLocationPermissionDenied() {
        setLocationError(
            "Location permission was denied. You can still save the parking details manually."
        )
    }

    fun saveParking() {
        val form =
            _uiState.value.form

        val existingParking =
            _uiState.value
                .currentParking

        val parkingSession =
            ParkingSession(
                id =
                    existingParking?.id
                        ?: System
                            .currentTimeMillis(),

                parkingLevel =
                    form.parkingLevel
                        .trim(),

                spotNumber =
                    form.spotNumber
                        .trim(),

                note =
                    form.note
                        .trim(),

                parkingExpiry =
                    form.parkingExpiry
                        .trim(),

                parkedAtMillis =
                    existingParking
                        ?.parkedAtMillis
                        ?: System
                            .currentTimeMillis(),

                location =
                    form.location,

                parkingExpiryMillis =
                    form.parkingExpiryMillis,

                reminderEnabled =
                    form.reminderEnabled
            )

        _uiState.value =
            _uiState.value.copy(
                currentParking =
                    parkingSession,

                form =
                    ParkingFormState()
            )

        viewModelScope.launch {
            repository.saveParking(
                parkingSession
            )

            reminderScheduler
                .cancelReminder(
                    parkingSession.id
                )

            val expiryMillis =
                parkingSession
                    .parkingExpiryMillis

            if (
                parkingSession
                    .reminderEnabled &&
                expiryMillis != null
            ) {
                val normalReminderTime =
                    expiryMillis -
                            REMINDER_OFFSET_MILLIS

                val earliestAllowedTime =
                    System
                        .currentTimeMillis() +
                            1_000L

                val reminderTime =
                    maxOf(
                        normalReminderTime,
                        earliestAllowedTime
                    )

                reminderScheduler
                    .scheduleReminder(
                        parkingId =
                            parkingSession.id,

                        reminderAtMillis =
                            reminderTime,

                        parkingLevel =
                            parkingSession
                                .parkingLevel,

                        spotNumber =
                            parkingSession
                                .spotNumber
                    )
            }
        }
    }

    fun clearCurrentParking() {
        val parkingId =
            _uiState.value
                .currentParking
                ?.id
                ?: return

        reminderScheduler
            .cancelReminder(
                parkingId
            )

        _uiState.value =
            _uiState.value.copy(
                currentParking =
                    null,

                form =
                    ParkingFormState()
            )

        viewModelScope.launch {
            repository
                .markParkingRecovered(
                    parkingId
                )
        }
    }
}