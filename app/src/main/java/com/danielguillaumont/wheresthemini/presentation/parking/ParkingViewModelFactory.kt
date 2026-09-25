package com.danielguillaumont.wheresthemini.presentation.parking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.danielguillaumont.wheresthemini.data.notification.ParkingReminderScheduler
import com.danielguillaumont.wheresthemini.data.repository.ParkingRepository

class ParkingViewModelFactory(
    private val repository:
    ParkingRepository,

    private val reminderScheduler:
    ParkingReminderScheduler
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (
            modelClass.isAssignableFrom(
                ParkingViewModel::class.java
            )
        ) {
            return ParkingViewModel(
                repository =
                    repository,

                reminderScheduler =
                    reminderScheduler
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}