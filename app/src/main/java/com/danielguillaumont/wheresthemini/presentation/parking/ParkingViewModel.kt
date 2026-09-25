package com.danielguillaumont.wheresthemini.presentation.parking

import androidx.lifecycle.ViewModel
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ParkingFormState(
    val parkingLevel: String = "",
    val spotNumber: String = "",
    val note: String = "",
    val parkingExpiry: String = ""
)

data class ParkingUiState(
    val form: ParkingFormState = ParkingFormState(),
    val currentParking: ParkingSession? = null
)

class ParkingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ParkingUiState())

    val uiState: StateFlow<ParkingUiState> =
        _uiState.asStateFlow()

    fun updateParkingLevel(value: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                parkingLevel = value
            )
        )
    }

    fun updateSpotNumber(value: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                spotNumber = value
            )
        )
    }

    fun updateNote(value: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                note = value
            )
        )
    }

    fun updateParkingExpiry(value: String) {
        _uiState.value = _uiState.value.copy(
            form = _uiState.value.form.copy(
                parkingExpiry = value
            )
        )
    }

    fun saveParking() {
        val form = _uiState.value.form

        val parkingSession = ParkingSession(
            id = System.currentTimeMillis(),
            parkingLevel = form.parkingLevel.trim(),
            spotNumber = form.spotNumber.trim(),
            note = form.note.trim(),
            parkingExpiry = form.parkingExpiry.trim(),
            parkedAtMillis = System.currentTimeMillis()
        )

        _uiState.value = _uiState.value.copy(
            currentParking = parkingSession,
            form = ParkingFormState()
        )
    }

    fun clearCurrentParking() {
        _uiState.value = _uiState.value.copy(
            currentParking = null
        )
    }
}