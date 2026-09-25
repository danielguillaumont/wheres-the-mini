package com.danielguillaumont.wheresthemini.presentation.parking

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.danielguillaumont.wheresthemini.data.photo.ParkingPhotoManager
import com.danielguillaumont.wheresthemini.data.photo.PendingParkingPhoto
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.ui.theme.AsphaltGrey
import com.danielguillaumont.wheresthemini.ui.theme.BonnetBlack
import com.danielguillaumont.wheresthemini.ui.theme.BritishRed
import com.danielguillaumont.wheresthemini.ui.theme.MiniCitron
import com.danielguillaumont.wheresthemini.ui.theme.MutedGrey
import com.danielguillaumont.wheresthemini.ui.theme.TicketPaper
import com.danielguillaumont.wheresthemini.ui.theme.WarmCream
import com.danielguillaumont.wheresthemini.ui.theme.WheresTheMiniTheme
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun SaveParkingScreen(
    formState: ParkingFormState,
    onParkingLevelChange: (String) -> Unit,
    onSpotNumberChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onParkingExpirySelected: (Int, Int) -> Unit,
    onClearParkingExpiry: () -> Unit,
    onReminderEnabledChange: (Boolean) -> Unit,
    onNotificationPermissionDenied: () -> Unit,
    onPhotoCaptured: (String) -> Unit,
    onRemovePhoto: () -> Unit,
    onCaptureLocation: () -> Unit,
    onLocationPermissionDenied: () -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context =
        LocalContext.current

    val photoManager =
        remember(context) {
            ParkingPhotoManager(
                context.applicationContext
            )
        }

    var pendingPhoto by
    remember {
        mutableStateOf<
                PendingParkingPhoto?
                >(null)
    }

    val locationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .RequestMultiplePermissions()
        ) { permissions ->

            val fineLocationGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_FINE_LOCATION
                ] == true

            val coarseLocationGranted =
                permissions[
                    Manifest.permission
                        .ACCESS_COARSE_LOCATION
                ] == true

            if (
                fineLocationGranted ||
                coarseLocationGranted
            ) {
                onCaptureLocation()
            } else {
                onLocationPermissionDenied()
            }
        }

    val notificationPermissionLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .RequestPermission()
        ) { granted ->

            if (granted) {
                onReminderEnabledChange(
                    true
                )
            } else {
                onNotificationPermissionDenied()
            }
        }

    val takePictureLauncher =
        rememberLauncherForActivityResult(
            contract =
                ActivityResultContracts
                    .TakePicture()
        ) { success ->

            val photo =
                pendingPhoto

            if (
                success &&
                photo != null
            ) {
                onPhotoCaptured(
                    photo.filePath
                )
            } else {
                photo?.let {
                        cancelledPhoto ->

                    photoManager
                        .deletePhoto(
                            cancelledPhoto
                                .filePath
                        )
                }
            }

            pendingPhoto =
                null
        }

    fun hasLocationPermission():
            Boolean {

        val fineLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission
                    .ACCESS_FINE_LOCATION
            ) ==
                    PackageManager
                        .PERMISSION_GRANTED

        val coarseLocationGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission
                    .ACCESS_COARSE_LOCATION
            ) ==
                    PackageManager
                        .PERMISSION_GRANTED

        return fineLocationGranted ||
                coarseLocationGranted
    }

    fun requestCurrentLocation() {
        if (
            hasLocationPermission()
        ) {
            onCaptureLocation()
        } else {
            locationPermissionLauncher
                .launch(
                    arrayOf(
                        Manifest.permission
                            .ACCESS_FINE_LOCATION,

                        Manifest.permission
                            .ACCESS_COARSE_LOCATION
                    )
                )
        }
    }

    fun requestReminderChange(
        enabled: Boolean
    ) {
        if (!enabled) {
            onReminderEnabledChange(
                false
            )

            return
        }

        if (
            formState
                .parkingExpiryMillis ==
            null
        ) {
            return
        }

        if (
            Build.VERSION.SDK_INT <
            Build.VERSION_CODES.TIRAMISU
        ) {
            onReminderEnabledChange(
                true
            )

            return
        }

        val permissionGranted =
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission
                    .POST_NOTIFICATIONS
            ) ==
                    PackageManager
                        .PERMISSION_GRANTED

        if (
            permissionGranted
        ) {
            onReminderEnabledChange(
                true
            )
        } else {
            notificationPermissionLauncher
                .launch(
                    Manifest.permission
                        .POST_NOTIFICATIONS
                )
        }
    }

    fun takeParkingPhoto() {
        val photo =
            photoManager
                .createParkingPhoto()

        pendingPhoto =
            photo

        takePictureLauncher
            .launch(
                photo.uri
            )
    }

    Scaffold(
        modifier =
            modifier.fillMaxSize(),
        containerColor =
            WarmCream
    ) { innerPadding ->

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(
                        innerPadding
                    )
                    .statusBarsPadding()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 22.dp,
                        vertical = 14.dp
                    )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                TextButton(
                    onClick =
                        onBackClick
                ) {
                    Text(
                        text =
                            "← BACK",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            BonnetBlack
                    )
                }

                Spacer(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                )

                Text(
                    text =
                        "INCIDENT #001",
                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,
                    color =
                        AsphaltGrey
                )
            }

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            Text(
                text =
                    "WHERE DID YOU\nLEAVE IT?",
                style =
                    MaterialTheme
                        .typography
                        .displayLarge,
                color =
                    BonnetBlack,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier.fillMaxWidth()
            )

            Text(
                text =
                    "LET'S GET THE STORY STRAIGHT.",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    AsphaltGrey,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 8.dp
                        )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        22.dp
                    )
            )

            ParkingTicket(
                formState =
                    formState,

                onParkingLevelChange =
                    onParkingLevelChange,

                onSpotNumberChange =
                    onSpotNumberChange,

                onNoteChange =
                    onNoteChange,

                onParkingExpirySelected =
                    onParkingExpirySelected,

                onClearParkingExpiry =
                    onClearParkingExpiry,

                onReminderChange =
                    ::requestReminderChange,

                onCaptureLocation =
                    ::requestCurrentLocation,

                onTakeParkingPhoto =
                    ::takeParkingPhoto,

                onRemoveParkingPhoto =
                    onRemovePhoto
            )

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Button(
                onClick =
                    onSaveClick,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            62.dp
                        ),
                shape =
                    RoundedCornerShape(
                        18.dp
                    ),
                colors =
                    ButtonDefaults
                        .buttonColors(
                            containerColor =
                                MiniCitron,
                            contentColor =
                                BonnetBlack
                        )
            ) {
                Text(
                    text =
                        "SAVE THE MINI",
                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )
            }

            Text(
                text =
                    "Try not to lose the ticket as well.",
                style =
                    MaterialTheme
                        .typography
                        .bodyMedium,
                fontStyle =
                    FontStyle.Italic,
                color =
                    MutedGrey,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            top = 10.dp,
                            bottom = 28.dp
                        )
            )
        }
    }
}

@Composable
private fun ParkingTicket(
    formState: ParkingFormState,
    onParkingLevelChange: (String) -> Unit,
    onSpotNumberChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onParkingExpirySelected: (Int, Int) -> Unit,
    onClearParkingExpiry: () -> Unit,
    onReminderChange: (Boolean) -> Unit,
    onCaptureLocation: () -> Unit,
    onTakeParkingPhoto: () -> Unit,
    onRemoveParkingPhoto: () -> Unit
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width =
                        1.dp,
                    color =
                        BonnetBlack.copy(
                            alpha = 0.18f
                        ),
                    shape =
                        RoundedCornerShape(
                            22.dp
                        )
                ),
        shape =
            RoundedCornerShape(
                22.dp
            ),
        color =
            TicketPaper,
        shadowElevation =
            3.dp
    ) {

        Column(
            modifier =
                Modifier.padding(
                    20.dp
                )
        ) {

            Text(
                text =
                    "PARKING RECORD",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    AsphaltGrey
            )

            Spacer(
                modifier =
                    Modifier.height(
                        12.dp
                    )
            )

            TicketDivider()

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Text(
                text =
                    "CURRENT LOCATION",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    AsphaltGrey
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            LocationCard(
                location =
                    formState.location,
                isLocating =
                    formState.isLocating,
                locationError =
                    formState.locationError,
                onCaptureLocation =
                    onCaptureLocation
            )

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            ParkingTextField(
                label =
                    "FLOOR / LEVEL",
                value =
                    formState.parkingLevel,
                onValueChange =
                    onParkingLevelChange,
                placeholder =
                    "P3"
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            ParkingTextField(
                label =
                    "SPOT NUMBER",
                value =
                    formState.spotNumber,
                onValueChange =
                    onSpotNumberChange,
                placeholder =
                    "127"
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            ParkingTextField(
                label =
                    "NOTE",
                value =
                    formState.note,
                onValueChange =
                    onNoteChange,
                placeholder =
                    "Near the lift, beside the suspicious van"
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            ParkingExpirySelector(
                expiryText =
                    formState.parkingExpiry,
                expiryMillis =
                    formState.parkingExpiryMillis,
                onParkingExpirySelected =
                    onParkingExpirySelected,
                onClearParkingExpiry =
                    onClearParkingExpiry
            )

            Spacer(
                modifier =
                    Modifier.height(
                        16.dp
                    )
            )

            ParkingReminderCard(
                formState =
                    formState,
                onReminderChange =
                    onReminderChange
            )

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            PhotoEvidenceSection(
                photoPath =
                    formState.photoPath,
                onTakeParkingPhoto =
                    onTakeParkingPhoto,
                onRemoveParkingPhoto =
                    onRemoveParkingPhoto
            )

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .background(
                            color =
                                BritishRed.copy(
                                    alpha = 0.08f
                                ),
                            shape =
                                RoundedCornerShape(
                                    12.dp
                                )
                        )
                        .padding(
                            12.dp
                        )
            ) {
                Text(
                    text =
                        "IMPORTANT: Remembering where you parked is still encouraged.",
                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,
                    color =
                        BritishRed
                )
            }
        }
    }
}

@Composable
private fun PhotoEvidenceSection(
    photoPath: String?,
    onTakeParkingPhoto: () -> Unit,
    onRemoveParkingPhoto: () -> Unit
) {
    Text(
        text =
            "PHOTOGRAPHIC EVIDENCE",
        style =
            MaterialTheme
                .typography
                .labelMedium,
        color =
            AsphaltGrey
    )

    Spacer(
        modifier =
            Modifier.height(
                8.dp
            )
    )

    if (
        photoPath == null
    ) {
        OutlinedButton(
            onClick =
                onTakeParkingPhoto,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        54.dp
                    ),
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            colors =
                ButtonDefaults
                    .outlinedButtonColors(
                        contentColor =
                            BonnetBlack
                    )
        ) {
            Text(
                text =
                    "ADD PARKING PHOTO",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium
            )
        }

        Text(
            text =
                "No evidence yet. Suspicious.",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            fontStyle =
                FontStyle.Italic,
            color =
                MutedGrey,
            modifier =
                Modifier.padding(
                    top = 6.dp
                )
        )

        return
    }

    val photoBitmap =
        remember(
            photoPath
        ) {
            BitmapFactory
                .decodeFile(
                    photoPath
                )
                ?.asImageBitmap()
        }

    if (
        photoBitmap != null
    ) {
        Image(
            bitmap =
                photoBitmap,
            contentDescription =
                "Parking location evidence",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        190.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            14.dp
                        )
                    ),
            contentScale =
                ContentScale.Crop
        )
    } else {
        Surface(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        100.dp
                    ),
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            color =
                BritishRed.copy(
                    alpha = 0.08f
                )
        ) {
            Box(
                contentAlignment =
                    Alignment.Center
            ) {
                Text(
                    text =
                        "EVIDENCE COULD NOT BE LOADED",
                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,
                    color =
                        BritishRed
                )
            }
        }
    }

    Text(
        text =
            "● EVIDENCE SECURED",
        style =
            MaterialTheme
                .typography
                .labelMedium,
        color =
            MiniCitron,
        modifier =
            Modifier.padding(
                top = 10.dp
            )
    )

    Text(
        text =
            "An unusually thorough parking investigation.",
        style =
            MaterialTheme
                .typography
                .bodyMedium,
        fontStyle =
            FontStyle.Italic,
        color =
            MutedGrey,
        modifier =
            Modifier.padding(
                top = 3.dp
            )
    )

    Spacer(
        modifier =
            Modifier.height(
                10.dp
            )
    )

    Row(
        modifier =
            Modifier.fillMaxWidth()
    ) {

        OutlinedButton(
            onClick =
                onTakeParkingPhoto,
            modifier =
                Modifier.weight(
                    1f
                ),
            shape =
                RoundedCornerShape(
                    12.dp
                )
        ) {
            Text(
                text =
                    "RETAKE",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium
            )
        }

        Spacer(
            modifier =
                Modifier.weight(
                    0.08f
                )
        )

        TextButton(
            onClick =
                onRemoveParkingPhoto,
            modifier =
                Modifier.weight(
                    1f
                )
        ) {
            Text(
                text =
                    "REMOVE",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    BritishRed
            )
        }
    }
}

@Composable
private fun ParkingExpirySelector(
    expiryText: String,
    expiryMillis: Long?,
    onParkingExpirySelected: (Int, Int) -> Unit,
    onClearParkingExpiry: () -> Unit
) {
    val context =
        LocalContext.current

    fun showTimePicker() {
        val initialDateTime =
            expiryMillis?.let {
                    millis ->

                Instant
                    .ofEpochMilli(
                        millis
                    )
                    .atZone(
                        ZoneId
                            .systemDefault()
                    )

            } ?: ZonedDateTime
                .now()
                .plusMinutes(
                    30
                )

        TimePickerDialog(
            context,
            {
                    _,
                    hour,
                    minute ->

                onParkingExpirySelected(
                    hour,
                    minute
                )
            },
            initialDateTime.hour,
            initialDateTime.minute,
            false
        ).show()
    }

    Column {

        Text(
            text =
                "PARKING EXPIRES",
            style =
                MaterialTheme
                    .typography
                    .labelMedium,
            color =
                AsphaltGrey
        )

        Spacer(
            modifier =
                Modifier.height(
                    7.dp
                )
        )

        OutlinedButton(
            onClick =
                ::showTimePicker,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        56.dp
                    ),
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            colors =
                ButtonDefaults
                    .outlinedButtonColors(
                        contentColor =
                            BonnetBlack
                    )
        ) {
            Text(
                text =
                    if (
                        expiryText
                            .isBlank()
                    ) {
                        "SELECT EXPIRY TIME"
                    } else {
                        expiryText
                    },
                style =
                    MaterialTheme
                        .typography
                        .labelLarge
            )
        }

        if (
            expiryText.isNotBlank()
        ) {
            TextButton(
                onClick =
                    onClearParkingExpiry,
                modifier =
                    Modifier.align(
                        Alignment.End
                    )
            ) {
                Text(
                    text =
                        "REMOVE EXPIRY",
                    style =
                        MaterialTheme
                            .typography
                            .labelMedium,
                    color =
                        MutedGrey
                )
            }
        }
    }
}

@Composable
private fun ParkingReminderCard(
    formState: ParkingFormState,
    onReminderChange: (Boolean) -> Unit
) {
    val hasRealExpiry =
        formState
            .parkingExpiryMillis !=
                null

    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width =
                        1.dp,
                    color =
                        BonnetBlack.copy(
                            alpha = 0.14f
                        ),
                    shape =
                        RoundedCornerShape(
                            14.dp
                        )
                ),
        shape =
            RoundedCornerShape(
                14.dp
            ),
        color =
            WarmCream.copy(
                alpha = 0.45f
            )
    ) {

        Column(
            modifier =
                Modifier.padding(
                    14.dp
                )
        ) {

            Row(
                modifier =
                    Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Column(
                    modifier =
                        Modifier.weight(
                            1f
                        )
                ) {

                    Text(
                        text =
                            "PARKING REMINDER",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            BonnetBlack
                    )

                    Text(
                        text =
                            if (
                                hasRealExpiry
                            ) {
                                "Remind me around 15 minutes before."
                            } else {
                                "Choose an expiry time first."
                            },
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            MutedGrey,
                        modifier =
                            Modifier.padding(
                                top = 3.dp
                            )
                    )
                }

                Switch(
                    checked =
                        formState
                            .reminderEnabled,
                    onCheckedChange =
                        onReminderChange,
                    enabled =
                        hasRealExpiry
                )
            }

            if (
                formState
                    .reminderError !=
                null
            ) {
                Text(
                    text =
                        formState
                            .reminderError,
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    color =
                        BritishRed,
                    modifier =
                        Modifier.padding(
                            top = 8.dp
                        )
                )
            }

            if (
                formState
                    .parkingExpiry
                    .isNotBlank() &&
                !hasRealExpiry
            ) {
                Text(
                    text =
                        "This is an older saved expiry. Select the time again to enable reminders.",
                    style =
                        MaterialTheme
                            .typography
                            .bodyMedium,
                    fontStyle =
                        FontStyle.Italic,
                    color =
                        MutedGrey,
                    modifier =
                        Modifier.padding(
                            top = 8.dp
                        )
                )
            }
        }
    }
}

@Composable
private fun LocationCard(
    location: ParkingLocation?,
    isLocating: Boolean,
    locationError: String?,
    onCaptureLocation: () -> Unit
) {
    Surface(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                14.dp
            ),
        color =
            BonnetBlack
    ) {

        Column(
            modifier =
                Modifier.padding(
                    14.dp
                )
        ) {

            when {

                isLocating -> {

                    Text(
                        text =
                            "● LOOKING FOR THE MINI...",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            MiniCitron
                    )

                    Text(
                        text =
                            "Consulting the satellites. Very official.",
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            WarmCream.copy(
                                alpha = 0.78f
                            ),
                        modifier =
                            Modifier.padding(
                                top = 4.dp
                            )
                    )
                }

                location != null -> {

                    Text(
                        text =
                            "● LOCATION CAPTURED",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            MiniCitron
                    )

                    Text(
                        text =
                            formatCoordinates(
                                location
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            WarmCream,
                        modifier =
                            Modifier.padding(
                                top = 6.dp
                            )
                    )

                    Text(
                        text =
                            "Accuracy ±${location.accuracyMeters.roundToInt()} m",
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            WarmCream.copy(
                                alpha = 0.72f
                            ),
                        modifier =
                            Modifier.padding(
                                top = 2.dp
                            )
                    )

                    OutlinedButton(
                        onClick =
                            onCaptureLocation,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 12.dp
                                ),
                        colors =
                            ButtonDefaults
                                .outlinedButtonColors(
                                    contentColor =
                                        MiniCitron
                                )
                    ) {
                        Text(
                            text =
                                "CAPTURE AGAIN",
                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium
                        )
                    }
                }

                else -> {

                    Text(
                        text =
                            "● LOCATION NOT CAPTURED",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            MiniCitron
                    )

                    Text(
                        text =
                            locationError
                                ?: "We know it's somewhere. Let's narrow that down.",
                        style =
                            MaterialTheme
                                .typography
                                .bodyMedium,
                        color =
                            if (
                                locationError ==
                                null
                            ) {
                                WarmCream.copy(
                                    alpha = 0.78f
                                )
                            } else {
                                BritishRed
                            },
                        modifier =
                            Modifier.padding(
                                top = 4.dp
                            )
                    )

                    Button(
                        onClick =
                            onCaptureLocation,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    top = 12.dp
                                ),
                        colors =
                            ButtonDefaults
                                .buttonColors(
                                    containerColor =
                                        MiniCitron,
                                    contentColor =
                                        BonnetBlack
                                )
                    ) {
                        Text(
                            text =
                                "CAPTURE MY LOCATION",
                            style =
                                MaterialTheme
                                    .typography
                                    .labelMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ParkingTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column {

        Text(
            text =
                label,
            style =
                MaterialTheme
                    .typography
                    .labelMedium,
            color =
                AsphaltGrey
        )

        Spacer(
            modifier =
                Modifier.height(
                    7.dp
                )
        )

        OutlinedTextField(
            value =
                value,
            onValueChange =
                onValueChange,
            modifier =
                Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text =
                        placeholder,
                    color =
                        MutedGrey
                )
            },
            singleLine =
                label != "NOTE",
            minLines =
                if (
                    label ==
                    "NOTE"
                ) {
                    3
                } else {
                    1
                },
            shape =
                RoundedCornerShape(
                    14.dp
                ),
            colors =
                OutlinedTextFieldDefaults
                    .colors(
                        focusedBorderColor =
                            BonnetBlack,

                        unfocusedBorderColor =
                            BonnetBlack.copy(
                                alpha = 0.22f
                            ),

                        focusedContainerColor =
                            WarmCream.copy(
                                alpha = 0.45f
                            ),

                        unfocusedContainerColor =
                            WarmCream.copy(
                                alpha = 0.45f
                            ),

                        cursorColor =
                            BonnetBlack,

                        focusedTextColor =
                            BonnetBlack,

                        unfocusedTextColor =
                            BonnetBlack
                    )
        )
    }
}

@Composable
private fun TicketDivider() {
    Box(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(
                    1.dp
                )
                .background(
                    BonnetBlack.copy(
                        alpha = 0.13f
                    )
                )
    )
}

private fun formatCoordinates(
    location: ParkingLocation
): String {

    return String.format(
        Locale.US,
        "%.5f, %.5f",
        location.latitude,
        location.longitude
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun SaveParkingScreenPreview() {

    WheresTheMiniTheme {

        SaveParkingScreen(
            formState =
                ParkingFormState(),

            onParkingLevelChange = {},

            onSpotNumberChange = {},

            onNoteChange = {},

            onParkingExpirySelected = {
                    _, _ ->
            },

            onClearParkingExpiry = {},

            onReminderEnabledChange = {},

            onNotificationPermissionDenied = {},

            onPhotoCaptured = {},

            onRemovePhoto = {},

            onCaptureLocation = {},

            onLocationPermissionDenied = {},

            onBackClick = {},

            onSaveClick = {}
        )
    }
}