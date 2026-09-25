package com.danielguillaumont.wheresthemini.presentation.history

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielguillaumont.wheresthemini.domain.model.ParkingLocation
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import com.danielguillaumont.wheresthemini.presentation.components.MiniBottomNavigation
import com.danielguillaumont.wheresthemini.presentation.components.MiniTab
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
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.max

@Composable
fun HistoryScreen(
    parkingHistory: List<ParkingSession>,
    onMiniClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = WarmCream,
        bottomBar = {
            MiniBottomNavigation(
                selectedTab = MiniTab.HISTORY,
                onMiniClick = onMiniClick,
                onHistoryClick = {},
                onInfoClick = onInfoClick
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 22.dp,
                end = 22.dp,
                top = 28.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(
                16.dp
            )
        ) {

            item {
                HistoryHeader()
            }

            if (parkingHistory.isEmpty()) {
                item {
                    EmptyHistoryCard()
                }
            } else {
                items(
                    items = parkingHistory,
                    key = { parking ->
                        parking.id
                    }
                ) { parking ->

                    ParkingHistoryTicket(
                        parking = parking
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryHeader() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "PREVIOUS\nMISADVENTURES",
            style = MaterialTheme.typography.displayLarge,
            color = BonnetBlack,
            textAlign = TextAlign.Center
        )

        Text(
            text = "A CAREFULLY DOCUMENTED RECORD OF POOR MEMORY.",
            style = MaterialTheme.typography.labelMedium,
            color = AsphaltGrey,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = 8.dp
            )
        )

        Spacer(
            modifier = Modifier.height(
                6.dp
            )
        )
    }
}

@Composable
private fun EmptyHistoryCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BonnetBlack.copy(
                    alpha = 0.16f
                ),
                shape = RoundedCornerShape(
                    22.dp
                )
            ),
        shape = RoundedCornerShape(
            22.dp
        ),
        color = TicketPaper
    ) {

        Column(
            modifier = Modifier.padding(
                22.dp
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "NO PREVIOUS DISASTERS",
                style = MaterialTheme.typography.titleLarge,
                color = BonnetBlack,
                textAlign = TextAlign.Center
            )

            Text(
                text = "An impressive run. It won't last.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MutedGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = 7.dp
                )
            )
        }
    }
}

@Composable
private fun ParkingHistoryTicket(
    parking: ParkingSession
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BonnetBlack.copy(
                    alpha = 0.18f
                ),
                shape = RoundedCornerShape(
                    20.dp
                )
            ),
        shape = RoundedCornerShape(
            20.dp
        ),
        color = TicketPaper,
        shadowElevation = 2.dp
    ) {

        Column(
            modifier = Modifier.padding(
                18.dp
            )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(
                        1f
                    )
                ) {

                    Text(
                        text = formatDate(
                            parking.parkedAtMillis
                        ),
                        style = MaterialTheme.typography.labelMedium,
                        color = AsphaltGrey
                    )

                    Text(
                        text = buildLocationDescription(
                            parking
                        ),
                        style = MaterialTheme.typography.titleLarge,
                        color = BonnetBlack,
                        modifier = Modifier.padding(
                            top = 4.dp
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(
                        10.dp
                    ),
                    color = MiniCitron.copy(
                        alpha = 0.18f
                    )
                ) {

                    Text(
                        text = "RECOVERED ✓",
                        style = MaterialTheme.typography.labelMedium,
                        color = BonnetBlack,
                        modifier = Modifier.padding(
                            horizontal = 9.dp,
                            vertical = 6.dp
                        )
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(
                    14.dp
                )
            )

            TicketDivider()

            Spacer(
                modifier = Modifier.height(
                    13.dp
                )
            )

            Text(
                text = "Parked at ${
                    formatTime(
                        parking.parkedAtMillis
                    )
                }",
                style = MaterialTheme.typography.bodyMedium,
                color = AsphaltGrey
            )

            parking.recoveredAtMillis?.let {
                    recoveredAt ->

                Text(
                    text = "Recovered at ${
                        formatTime(
                            recoveredAt
                        )
                    }",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AsphaltGrey,
                    modifier = Modifier.padding(
                        top = 2.dp
                    )
                )

                Text(
                    text = formatDuration(
                        parking.parkedAtMillis,
                        recoveredAt
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = BonnetBlack,
                    modifier = Modifier.padding(
                        top = 10.dp
                    )
                )
            }

            if (
                parking.note.isNotBlank()
            ) {

                Text(
                    text = "“${parking.note}”",
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MutedGrey,
                    modifier = Modifier.padding(
                        top = 10.dp
                    )
                )
            }

            parking.location?.let {
                    location ->

                Text(
                    text = formatGps(
                        location
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = AsphaltGrey,
                    modifier = Modifier.padding(
                        top = 9.dp
                    )
                )
            }

            parking.photoPath
                ?.takeIf {
                        path ->
                    path.isNotBlank()
                }
                ?.let {
                        photoPath ->

                    Spacer(
                        modifier = Modifier.height(
                            15.dp
                        )
                    )

                    ParkingEvidencePhoto(
                        photoPath = photoPath
                    )
                }

            Spacer(
                modifier = Modifier.height(
                    14.dp
                )
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    10.dp
                ),
                color = WarmCream
            ) {

                Text(
                    text = incidentComment(
                        parking
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = BonnetBlack,
                    modifier = Modifier.padding(
                        horizontal = 12.dp,
                        vertical = 9.dp
                    )
                )
            }
        }
    }
}

@Composable
private fun ParkingEvidencePhoto(
    photoPath: String
) {
    val bitmap =
        remember(
            photoPath
        ) {
            BitmapFactory
                .decodeFile(
                    photoPath
                )
                ?.asImageBitmap()
        }

    Column {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "PHOTOGRAPHIC EVIDENCE",
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey,
                modifier = Modifier.weight(
                    1f
                )
            )

            Text(
                text = "EXHIBIT A",
                style = MaterialTheme.typography.labelMedium,
                color = MiniCitron
            )
        }

        Spacer(
            modifier = Modifier.height(
                8.dp
            )
        )

        if (
            bitmap != null
        ) {

            Image(
                bitmap = bitmap,
                contentDescription = "Parking evidence photo",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        150.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            12.dp
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Text(
                text = "● EVIDENCE FILED WITH INCIDENT",
                style = MaterialTheme.typography.labelMedium,
                color = MiniCitron,
                modifier = Modifier.padding(
                    top = 7.dp
                )
            )

        } else {

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        72.dp
                    ),
                shape = RoundedCornerShape(
                    12.dp
                ),
                color = BritishRed.copy(
                    alpha = 0.08f
                )
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "EVIDENCE FILE UNAVAILABLE",
                        style = MaterialTheme.typography.labelMedium,
                        color = BritishRed
                    )
                }
            }
        }
    }
}

@Composable
private fun TicketDivider() {
    Box(
        modifier = Modifier
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

private fun buildLocationDescription(
    parking: ParkingSession
): String {

    val level =
        parking.parkingLevel
            .ifBlank {
                "LEVEL ?"
            }

    val spot =
        parking.spotNumber
            .ifBlank {
                "?"
            }

    return "$level · SPOT $spot"
}

private fun formatDate(
    millis: Long
): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "MMM dd, yyyy",
            Locale.US
        )

    return Instant
        .ofEpochMilli(
            millis
        )
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
        .uppercase(
            Locale.US
        )
}

private fun formatTime(
    millis: Long
): String {

    val formatter =
        DateTimeFormatter.ofPattern(
            "h:mm a",
            Locale.US
        )

    return Instant
        .ofEpochMilli(
            millis
        )
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}

private fun formatDuration(
    parkedAtMillis: Long,
    recoveredAtMillis: Long
): String {

    val durationMillis =
        max(
            0L,
            recoveredAtMillis -
                    parkedAtMillis
        )

    val minutes =
        durationMillis /
                60_000L

    return "${minutes}m parked"
}

private fun formatGps(
    location: ParkingLocation
): String {

    return String.format(
        Locale.US,
        "GPS %.5f, %.5f",
        location.latitude,
        location.longitude
    )
}

private fun incidentComment(
    parking: ParkingSession
): String {

    val recoveredAt =
        parking.recoveredAtMillis
            ?: return "Case remains suspicious."

    val durationMinutes =
        max(
            0L,
            recoveredAt -
                    parking.parkedAtMillis
        ) / 60_000L

    return when {

        durationMinutes <= 2 ->
            "Suspiciously efficient."

        durationMinutes <= 30 ->
            "A surprisingly competent recovery."

        durationMinutes <= 120 ->
            "Eventually, the Mini was located."

        else ->
            "An investigation of unnecessary length."
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HistoryScreenPreview() {

    WheresTheMiniTheme {

        HistoryScreen(
            parkingHistory = listOf(
                ParkingSession(
                    id = 1,
                    parkingLevel = "P4",
                    spotNumber = "301",
                    note = "Near the elevator",
                    parkingExpiry = "5:00 PM",
                    parkedAtMillis =
                        System.currentTimeMillis() -
                                120_000L,
                    location = ParkingLocation(
                        latitude = 37.42200,
                        longitude = -122.08400,
                        accuracyMeters = 5f
                    ),
                    recoveredAtMillis =
                        System.currentTimeMillis(),
                    reminderEnabled = true,
                    photoPath = null
                )
            ),
            onMiniClick = {},
            onInfoClick = {}
        )
    }
}