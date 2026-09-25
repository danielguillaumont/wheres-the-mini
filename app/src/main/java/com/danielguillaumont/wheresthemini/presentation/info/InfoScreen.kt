package com.danielguillaumont.wheresthemini.presentation.info

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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

@Composable
fun InfoScreen(
    onMiniClick: () -> Unit,
    onHistoryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = WarmCream,
        bottomBar = {
            MiniBottomNavigation(
                selectedTab = MiniTab.INFO,
                onMiniClick = onMiniClick,
                onHistoryClick = onHistoryClick,
                onInfoClick = {}
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 28.dp,
                bottom = 34.dp
            ),
            verticalArrangement = Arrangement.spacedBy(
                18.dp
            )
        ) {

            item {
                InfoHeader()
            }

            item {
                CaseStatusCard()
            }

            item {
                InvestigationKitCard()
            }

            item {
                PrivacyCard()
            }

            item {
                UnderTheBonnetCard()
            }

            item {
                FinalWarningCard()
            }

            item {
                VersionFooter()
            }
        }
    }
}

@Composable
private fun InfoHeader() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Surface(
            shape = RoundedCornerShape(
                50.dp
            ),
            color = BonnetBlack
        ) {
            Text(
                text = "CASE FILE #001",
                style = MaterialTheme.typography.labelMedium,
                color = MiniCitron,
                modifier = Modifier.padding(
                    horizontal = 14.dp,
                    vertical = 7.dp
                )
            )
        }

        Spacer(
            modifier = Modifier.height(
                18.dp
            )
        )

        Text(
            text = "WHERE'S THE\nMINI?",
            style = MaterialTheme.typography.displayLarge,
            color = BonnetBlack,
            textAlign = TextAlign.Center
        )

        Text(
            text = "SMALL CAR. LARGE INVESTIGATION.",
            style = MaterialTheme.typography.labelMedium,
            color = AsphaltGrey,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = 9.dp
            )
        )
    }
}

@Composable
private fun CaseStatusCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(
            18.dp
        ),
        color = BonnetBlack
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 18.dp
            )
        ) {

            Text(
                text = "CASE STATUS",
                style = MaterialTheme.typography.labelMedium,
                color = WarmCream.copy(
                    alpha = 0.65f
                )
            )

            Spacer(
                modifier = Modifier.height(
                    10.dp
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .width(
                            9.dp
                        )
                        .height(
                            9.dp
                        )
                        .background(
                            color = MiniCitron,
                            shape = CircleShape
                        )
                )

                Spacer(
                    modifier = Modifier.width(
                        9.dp
                    )
                )

                Text(
                    text = "OPERATIONAL",
                    style = MaterialTheme.typography.titleLarge,
                    color = MiniCitron
                )
            }

            Text(
                text = "The vehicle may still wander. The software appears prepared.",
                style = MaterialTheme.typography.bodyMedium,
                color = WarmCream.copy(
                    alpha = 0.78f
                ),
                modifier = Modifier.padding(
                    top = 9.dp,
                    end = 4.dp
                )
            )
        }
    }
}

@Composable
private fun InvestigationKitCard() {
    InfoCard(
        heading = "THE INVESTIGATION KIT"
    ) {

        FeatureItem(
            number = "01",
            title = "SAVE THE SCENE",
            description = "Record the parking level, space number, notes and GPS location."
        )

        FeatureDivider()

        FeatureItem(
            number = "02",
            title = "PHOTOGRAPHIC EVIDENCE",
            description = "Take a picture before walking away with suspicious confidence."
        )

        FeatureDivider()

        FeatureItem(
            number = "03",
            title = "THE CLOCK IS TICKING",
            description = "Set an expiry time and receive a reminder before parking runs out."
        )

        FeatureDivider()

        FeatureItem(
            number = "04",
            title = "FIND THE MINI",
            description = "Send the saved coordinates to your maps app when memory fails."
        )

        FeatureDivider()

        FeatureItem(
            number = "05",
            title = "PREVIOUS MISADVENTURES",
            description = "Keep a local record of recovered Minis and their associated evidence."
        )
    }
}

@Composable
private fun PrivacyCard() {
    InfoCard(
        heading = "CLASSIFIED INFORMATION"
    ) {

        Text(
            text = "YOUR PARKING RECORDS STAY LOCAL",
            style = MaterialTheme.typography.titleLarge,
            color = BonnetBlack
        )

        Text(
            text = "Parking details, history and photographic evidence are stored on this device. No account is required.",
            style = MaterialTheme.typography.bodyMedium,
            color = AsphaltGrey,
            modifier = Modifier.padding(
                top = 9.dp,
                end = 3.dp
            )
        )

        Text(
            text = "Location is captured only when you request it. Navigation is handed off to your maps app.",
            style = MaterialTheme.typography.bodyMedium,
            color = AsphaltGrey,
            modifier = Modifier.padding(
                top = 9.dp,
                end = 3.dp
            )
        )
    }
}

@Composable
private fun UnderTheBonnetCard() {
    InfoCard(
        heading = "UNDER THE BONNET"
    ) {

        Text(
            text = "BUILT WITH",
            style = MaterialTheme.typography.labelMedium,
            color = AsphaltGrey
        )

        Text(
            text = "Kotlin · Jetpack Compose",
            style = MaterialTheme.typography.titleLarge,
            color = BonnetBlack,
            modifier = Modifier.padding(
                top = 7.dp
            )
        )

        Text(
            text = "Room · WorkManager · Location Services · Android Camera · Navigation",
            style = MaterialTheme.typography.bodyMedium,
            color = AsphaltGrey,
            modifier = Modifier.padding(
                top = 7.dp,
                end = 3.dp
            )
        )

        Spacer(
            modifier = Modifier.height(
                18.dp
            )
        )

        CardDivider()

        Spacer(
            modifier = Modifier.height(
                16.dp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            TechnicalDetail(
                label = "PLATFORM",
                value = "ANDROID"
            )

            TechnicalDetail(
                label = "VERSION",
                value = "1.0"
            )

            TechnicalDetail(
                label = "STATUS",
                value = "STABLE"
            )
        }
    }
}

@Composable
private fun FinalWarningCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BritishRed.copy(
                    alpha = 0.18f
                ),
                shape = RoundedCornerShape(
                    16.dp
                )
            ),
        shape = RoundedCornerShape(
            16.dp
        ),
        color = BritishRed.copy(
            alpha = 0.07f
        )
    ) {

        Column(
            modifier = Modifier.padding(
                horizontal = 20.dp,
                vertical = 17.dp
            )
        ) {

            Text(
                text = "IMPORTANT",
                style = MaterialTheme.typography.labelMedium,
                color = BritishRed
            )

            Text(
                text = "Remembering where you parked is still strongly encouraged.",
                style = MaterialTheme.typography.bodyMedium,
                color = BritishRed,
                modifier = Modifier.padding(
                    top = 6.dp,
                    end = 4.dp
                )
            )
        }
    }
}

@Composable
private fun VersionFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 6.dp,
                bottom = 12.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "WHERE'S THE MINI? · CASE #001",
            style = MaterialTheme.typography.labelMedium,
            color = MutedGrey,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Parking shouldn't require detective work.",
            style = MaterialTheme.typography.bodyMedium,
            fontStyle = FontStyle.Italic,
            color = MutedGrey,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(
                top = 6.dp
            )
        )
    }
}

@Composable
private fun InfoCard(
    heading: String,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BonnetBlack.copy(
                    alpha = 0.16f
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
                horizontal = 20.dp,
                vertical = 19.dp
            )
        ) {

            Text(
                text = heading,
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey
            )

            Spacer(
                modifier = Modifier.height(
                    11.dp
                )
            )

            CardDivider()

            Spacer(
                modifier = Modifier.height(
                    16.dp
                )
            )

            content()
        }
    }
}

@Composable
private fun FeatureItem(
    number: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 2.dp
            ),
        verticalAlignment = Alignment.Top
    ) {

        Surface(
            shape = RoundedCornerShape(
                8.dp
            ),
            color = MiniCitron.copy(
                alpha = 0.20f
            )
        ) {

            Text(
                text = number,
                style = MaterialTheme.typography.labelMedium,
                color = BonnetBlack,
                modifier = Modifier.padding(
                    horizontal = 8.dp,
                    vertical = 5.dp
                )
            )
        }

        Spacer(
            modifier = Modifier.width(
                13.dp
            )
        )

        Column(
            modifier = Modifier
                .weight(
                    1f
                )
                .padding(
                    end = 3.dp
                )
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = BonnetBlack
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = AsphaltGrey,
                modifier = Modifier.padding(
                    top = 5.dp
                )
            )
        }
    }
}

@Composable
private fun FeatureDivider() {
    Spacer(
        modifier = Modifier.height(
            12.dp
        )
    )

    CardDivider()

    Spacer(
        modifier = Modifier.height(
            12.dp
        )
    )
}

@Composable
private fun TechnicalDetail(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MutedGrey
        )

        Text(
            text = value,
            style = MaterialTheme.typography.labelMedium,
            color = BonnetBlack,
            modifier = Modifier.padding(
                top = 4.dp
            )
        )
    }
}

@Composable
private fun CardDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                1.dp
            )
            .background(
                BonnetBlack.copy(
                    alpha = 0.12f
                )
            )
    )
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun InfoScreenPreview() {
    WheresTheMiniTheme {
        InfoScreen(
            onMiniClick = {},
            onHistoryClick = {}
        )
    }
}