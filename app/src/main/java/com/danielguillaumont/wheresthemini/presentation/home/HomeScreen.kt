package com.danielguillaumont.wheresthemini.presentation.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielguillaumont.wheresthemini.domain.model.ParkingSession
import com.danielguillaumont.wheresthemini.presentation.components.MiniBottomNavigation
import com.danielguillaumont.wheresthemini.presentation.components.MiniTab
import com.danielguillaumont.wheresthemini.ui.theme.AsphaltGrey
import com.danielguillaumont.wheresthemini.ui.theme.BonnetBlack
import com.danielguillaumont.wheresthemini.ui.theme.ChromeGrey
import com.danielguillaumont.wheresthemini.ui.theme.MiniCitron
import com.danielguillaumont.wheresthemini.ui.theme.MutedGrey
import com.danielguillaumont.wheresthemini.ui.theme.TicketPaper
import com.danielguillaumont.wheresthemini.ui.theme.WarmCream
import com.danielguillaumont.wheresthemini.ui.theme.WindowBlue
import com.danielguillaumont.wheresthemini.ui.theme.WheresTheMiniTheme
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    currentParking: ParkingSession? = null,
    lastParking: ParkingSession? = null,
    onParkedHereClick: () -> Unit = {},
    onFoundItClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier =
            modifier.fillMaxSize(),
        containerColor =
            WarmCream,
        bottomBar = {
            MiniBottomNavigation(
                selectedTab =
                    MiniTab.MINI,

                onMiniClick = {},

                onHistoryClick =
                    onHistoryClick,

                onInfoClick =
                    onInfoClick
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    innerPadding
                )
                .padding(
                    horizontal = 24.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier =
                    Modifier.height(
                        26.dp
                    )
            )

            Text(
                text =
                    "WHERE'S THE MINI?",
                style =
                    MaterialTheme
                        .typography
                        .displayLarge,
                color =
                    BonnetBlack,
                textAlign =
                    TextAlign.Center
            )

            Text(
                text =
                    "PARKING SHOULDN'T REQUIRE DETECTIVE WORK.",
                style =
                    MaterialTheme
                        .typography
                        .labelMedium,
                color =
                    AsphaltGrey,
                textAlign =
                    TextAlign.Center,
                modifier =
                    Modifier.padding(
                        top = 8.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        18.dp
                    )
            )

            MiniHeroCard(
                currentParking =
                    currentParking
            )

            Spacer(
                modifier =
                    Modifier.height(
                        20.dp
                    )
            )

            Button(
                onClick =
                    onParkedHereClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(
                        64.dp
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
                        if (
                            currentParking ==
                            null
                        ) {
                            "I PARKED HERE"
                        } else {
                            "UPDATE PARKING"
                        },
                    style =
                        MaterialTheme
                            .typography
                            .labelLarge
                )
            }

            if (
                currentParking != null
            ) {
                Spacer(
                    modifier =
                        Modifier.height(
                            10.dp
                        )
                )

                OutlinedButton(
                    onClick =
                        onFoundItClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(
                            52.dp
                        ),
                    shape =
                        RoundedCornerShape(
                            16.dp
                        )
                ) {
                    Text(
                        text =
                            "I FOUND IT",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            BonnetBlack
                    )
                }
            }

            Text(
                text =
                    if (
                        currentParking ==
                        null
                    ) {
                        "Try to remember roughly where first."
                    } else {
                        "Remarkably, we have a record."
                    },
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
                    Modifier.padding(
                        top = 10.dp
                    )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        24.dp
                    )
            )

            ParkingRecordCard(
                currentParking =
                    currentParking,
                lastParking =
                    lastParking
            )
        }
    }
}

@Composable
private fun MiniHeroCard(
    currentParking:
    ParkingSession?
) {
    Surface(
        modifier =
            Modifier.fillMaxWidth(),
        shape =
            RoundedCornerShape(
                28.dp
            ),
        color =
            TicketPaper,
        shadowElevation =
            3.dp
    ) {
        Column(
            modifier =
                Modifier.padding(
                    horizontal =
                        20.dp,
                    vertical =
                        22.dp
                ),
            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {
            Box(
                modifier =
                    Modifier
                        .background(
                            color =
                                WarmCream,
                            shape =
                                RoundedCornerShape(
                                    22.dp
                                )
                        )
                        .padding(
                            horizontal =
                                8.dp,
                            vertical =
                                2.dp
                        )
            ) {
                MiniIllustration()
            }

            Spacer(
                modifier =
                    Modifier.height(
                        10.dp
                    )
            )

            Text(
                text =
                    if (
                        currentParking ==
                        null
                    ) {
                        "The Mini is currently\naccounted for."
                    } else {
                        "The Mini is parked.\nWe wrote it down."
                    },
                style =
                    MaterialTheme
                        .typography
                        .headlineMedium,
                color =
                    BonnetBlack,
                textAlign =
                    TextAlign.Center
            )

            Spacer(
                modifier =
                    Modifier.height(
                        8.dp
                    )
            )

            Text(
                text =
                    if (
                        currentParking ==
                        null
                    ) {
                        "A minor miracle."
                    } else {
                        "This feels unusually responsible."
                    },
                style =
                    MaterialTheme
                        .typography
                        .bodyLarge,
                fontStyle =
                    FontStyle.Italic,
                color =
                    AsphaltGrey,
                textAlign =
                    TextAlign.Center
            )
        }
    }
}

@Composable
private fun MiniIllustration() {
    Canvas(
        modifier =
            Modifier
                .fillMaxWidth()
                .height(
                    125.dp
                )
    ) {
        val canvasWidth =
            size.width

        val canvasHeight =
            size.height

        val carLeft =
            canvasWidth * 0.12f

        val carTop =
            canvasHeight * 0.38f

        val carWidth =
            canvasWidth * 0.76f

        val carHeight =
            canvasHeight * 0.32f

        drawOval(
            color =
                Color.Black.copy(
                    alpha = 0.12f
                ),
            topLeft =
                Offset(
                    canvasWidth *
                            0.13f,
                    canvasHeight *
                            0.70f
                ),
            size =
                Size(
                    canvasWidth *
                            0.74f,
                    canvasHeight *
                            0.16f
                )
        )

        drawRoundRect(
            color =
                MiniCitron,
            topLeft =
                Offset(
                    carLeft,
                    carTop
                ),
            size =
                Size(
                    carWidth,
                    carHeight
                )
        )

        drawRoundRect(
            color =
                MiniCitron,
            topLeft =
                Offset(
                    canvasWidth *
                            0.31f,
                    canvasHeight *
                            0.20f
                ),
            size =
                Size(
                    canvasWidth *
                            0.39f,
                    canvasHeight *
                            0.30f
                )
        )

        drawRoundRect(
            color =
                WindowBlue,
            topLeft =
                Offset(
                    canvasWidth *
                            0.51f,
                    canvasHeight *
                            0.24f
                ),
            size =
                Size(
                    canvasWidth *
                            0.15f,
                    canvasHeight *
                            0.20f
                )
        )

        drawRoundRect(
            color =
                WindowBlue,
            topLeft =
                Offset(
                    canvasWidth *
                            0.35f,
                    canvasHeight *
                            0.24f
                ),
            size =
                Size(
                    canvasWidth *
                            0.13f,
                    canvasHeight *
                            0.20f
                )
        )

        drawRoundRect(
            color =
                BonnetBlack,
            topLeft =
                Offset(
                    canvasWidth *
                            0.69f,
                    canvasHeight *
                            0.40f
                ),
            size =
                Size(
                    canvasWidth *
                            0.19f,
                    canvasHeight *
                            0.12f
                )
        )

        drawRoundRect(
            color =
                ChromeGrey,
            topLeft =
                Offset(
                    canvasWidth *
                            0.09f,
                    canvasHeight *
                            0.61f
                ),
            size =
                Size(
                    canvasWidth *
                            0.12f,
                    canvasHeight *
                            0.05f
                )
        )

        drawRoundRect(
            color =
                ChromeGrey,
            topLeft =
                Offset(
                    canvasWidth *
                            0.82f,
                    canvasHeight *
                            0.61f
                ),
            size =
                Size(
                    canvasWidth *
                            0.10f,
                    canvasHeight *
                            0.05f
                )
        )

        val wheelRadius =
            canvasHeight *
                    0.13f

        drawCircle(
            color =
                BonnetBlack,
            radius =
                wheelRadius,
            center =
                Offset(
                    canvasWidth *
                            0.30f,
                    canvasHeight *
                            0.68f
                )
        )

        drawCircle(
            color =
                ChromeGrey,
            radius =
                wheelRadius *
                        0.45f,
            center =
                Offset(
                    canvasWidth *
                            0.30f,
                    canvasHeight *
                            0.68f
                )
        )

        drawCircle(
            color =
                BonnetBlack,
            radius =
                wheelRadius,
            center =
                Offset(
                    canvasWidth *
                            0.72f,
                    canvasHeight *
                            0.68f
                )
        )

        drawCircle(
            color =
                ChromeGrey,
            radius =
                wheelRadius *
                        0.45f,
            center =
                Offset(
                    canvasWidth *
                            0.72f,
                    canvasHeight *
                            0.68f
                )
        )

        drawRoundRect(
            color =
                BonnetBlack.copy(
                    alpha = 0.45f
                ),
            topLeft =
                Offset(
                    canvasWidth *
                            0.47f,
                    canvasHeight *
                            0.46f
                ),
            size =
                Size(
                    canvasWidth *
                            0.18f,
                    canvasHeight *
                            0.18f
                ),
            style =
                Stroke(
                    width = 2f
                )
        )
    }
}

@Composable
private fun ParkingRecordCard(
    currentParking:
    ParkingSession?,
    lastParking:
    ParkingSession?
) {
    Surface(
        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    color =
                        BonnetBlack.copy(
                            alpha = 0.18f
                        ),
                    shape =
                        RoundedCornerShape(
                            18.dp
                        )
                ),
        shape =
            RoundedCornerShape(
                18.dp
            ),
        color =
            TicketPaper
    ) {
        Column(
            modifier =
                Modifier.padding(
                    18.dp
                )
        ) {
            Text(
                text =
                    if (
                        currentParking !=
                        null
                    ) {
                        "CURRENT PARKING"
                    } else {
                        "LAST INCIDENT"
                    },
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

            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(
                            1.dp
                        )
                        .background(
                            BonnetBlack
                                .copy(
                                    alpha =
                                        0.12f
                                )
                        )
            )

            Spacer(
                modifier =
                    Modifier.height(
                        14.dp
                    )
            )

            when {
                currentParking != null -> {
                    ParkingDetails(
                        parking =
                            currentParking,
                        recovered =
                            false
                    )
                }

                lastParking != null -> {
                    ParkingDetails(
                        parking =
                            lastParking,
                        recovered =
                            true
                    )
                }

                else -> {
                    Text(
                        text =
                            "NO PREVIOUS DISASTERS",
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        color =
                            BonnetBlack
                    )

                    Text(
                        text =
                            "Give it time.",
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
                                top = 4.dp
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun ParkingDetails(
    parking:
    ParkingSession,
    recovered:
    Boolean
) {
    Text(
        text =
            buildLocationDescription(
                parking
            ),
        style =
            MaterialTheme
                .typography
                .titleMedium,
        color =
            BonnetBlack
    )

    Text(
        text =
            "Parked at ${
                formatParkingTime(
                    parking.parkedAtMillis
                )
            }",
        style =
            MaterialTheme
                .typography
                .bodyMedium,
        color =
            AsphaltGrey,
        modifier =
            Modifier.padding(
                top = 6.dp
            )
    )

    if (
        recovered &&
        parking.recoveredAtMillis !=
        null
    ) {
        Text(
            text =
                "Recovered at ${
                    formatParkingTime(
                        parking
                            .recoveredAtMillis
                    )
                }",
            style =
                MaterialTheme
                    .typography
                    .bodyMedium,
            color =
                AsphaltGrey,
            modifier =
                Modifier.padding(
                    top = 2.dp
                )
        )
    }

    if (
        parking.note.isNotBlank()
    ) {
        Text(
            text =
                "“${parking.note}”",
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

    if (
        parking.parkingExpiry
            .isNotBlank()
    ) {
        Text(
            text =
                "EXPIRES: ${parking.parkingExpiry}",
            style =
                MaterialTheme
                    .typography
                    .labelMedium,
            color =
                AsphaltGrey,
            modifier =
                Modifier.padding(
                    top = 10.dp
                )
        )
    }

    if (
        recovered
    ) {
        Text(
            text =
                "MINI RECOVERED ✓",
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
    }
}

private fun buildLocationDescription(
    parking:
    ParkingSession
): String {
    val level =
        parking.parkingLevel
            .ifBlank {
                "UNKNOWN LEVEL"
            }

    val spot =
        parking.spotNumber
            .ifBlank {
                "UNKNOWN SPOT"
            }

    return "$level · SPOT $spot"
}

private fun formatParkingTime(
    parkedAtMillis:
    Long
): String {
    val formatter =
        DateTimeFormatter
            .ofPattern(
                "h:mm a",
                Locale.US
            )

    return Instant
        .ofEpochMilli(
            parkedAtMillis
        )
        .atZone(
            ZoneId.systemDefault()
        )
        .format(
            formatter
        )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HomeScreenEmptyPreview() {
    WheresTheMiniTheme {
        HomeScreen()
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun HomeScreenParkedPreview() {
    WheresTheMiniTheme {
        HomeScreen(
            currentParking =
                ParkingSession(
                    id = 1,
                    parkingLevel =
                        "P3",
                    spotNumber =
                        "127",
                    note =
                        "Beside the suspicious red car",
                    parkingExpiry =
                        "4:30 PM",
                    parkedAtMillis =
                        System.currentTimeMillis()
                )
        )
    }
}