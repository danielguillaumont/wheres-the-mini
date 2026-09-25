package com.danielguillaumont.wheresthemini.presentation.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import com.danielguillaumont.wheresthemini.presentation.components.MiniBottomNavigation
import com.danielguillaumont.wheresthemini.presentation.components.MiniTab
import com.danielguillaumont.wheresthemini.ui.theme.AsphaltGrey
import com.danielguillaumont.wheresthemini.ui.theme.BonnetBlack
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
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 22.dp,
                end = 22.dp,
                top = 28.dp,
                bottom = 30.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
            modifier = Modifier.height(6.dp)
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

            if (
                parking.location != null
            ) {
                Text(
                    text = String.format(
                        Locale.US,
                        "GPS %.5f, %.5f",
                        parking.location.latitude,
                        parking.location.longitude
                    ),
                    style = MaterialTheme.typography.labelMedium,
                    color = AsphaltGrey,
                    modifier = Modifier.padding(
                        top = 11.dp
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(
                    14.dp
                )
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = WarmCream,
                        shape = RoundedCornerShape(
                            10.dp
                        )
                    )
                    .padding(
                        11.dp
                    )
            ) {
                Text(
                    text = parkingComment(
                        parking
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = BonnetBlack
                )
            }
        }
    }
}

@Composable
private fun TicketDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                BonnetBlack.copy(
                    alpha = 0.12f
                )
            )
    )
}

private fun buildLocationDescription(
    parking: ParkingSession
): String {
    val level =
        parking.parkingLevel.ifBlank {
            "UNKNOWN LEVEL"
        }

    val spot =
        parking.spotNumber.ifBlank {
            "UNKNOWN SPOT"
        }

    return "$level · SPOT $spot"
}

private fun formatDate(
    millis: Long
): String {
    val formatter =
        DateTimeFormatter.ofPattern(
            "MMM d, yyyy",
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
    val totalMinutes =
        max(
            0L,
            (
                    recoveredAtMillis -
                            parkedAtMillis
                    ) / 60_000L
        )

    val hours =
        totalMinutes / 60L

    val minutes =
        totalMinutes % 60L

    return if (
        hours > 0
    ) {
        "${hours}h ${minutes}m parked"
    } else {
        "${minutes}m parked"
    }
}

private fun parkingComment(
    parking: ParkingSession
): String {
    val recoveredAt =
        parking.recoveredAtMillis
            ?: return "The Mini was eventually accounted for."

    val totalMinutes =
        max(
            0L,
            (
                    recoveredAt -
                            parking.parkedAtMillis
                    ) / 60_000L
        )

    return when {
        totalMinutes < 20 ->
            "Suspiciously efficient."

        totalMinutes < 120 ->
            "Almost like you knew what you were doing."

        totalMinutes < 360 ->
            "The Mini had time to think about what happened."

        else ->
            "You did remember eventually."
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
                                5_400_000L,
                    recoveredAtMillis =
                        System.currentTimeMillis()
                )
            ),
            onMiniClick = {},
            onInfoClick = {}
        )
    }
}