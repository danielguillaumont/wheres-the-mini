package com.danielguillaumont.wheresthemini.presentation.parking

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.danielguillaumont.wheresthemini.ui.theme.AsphaltGrey
import com.danielguillaumont.wheresthemini.ui.theme.BonnetBlack
import com.danielguillaumont.wheresthemini.ui.theme.BritishRed
import com.danielguillaumont.wheresthemini.ui.theme.MiniCitron
import com.danielguillaumont.wheresthemini.ui.theme.MutedGrey
import com.danielguillaumont.wheresthemini.ui.theme.TicketPaper
import com.danielguillaumont.wheresthemini.ui.theme.WarmCream
import com.danielguillaumont.wheresthemini.ui.theme.WheresTheMiniTheme

@Composable
fun SaveParkingScreen(
    formState: ParkingFormState,
    onParkingLevelChange: (String) -> Unit,
    onSpotNumberChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onParkingExpiryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = WarmCream
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(
                    horizontal = 22.dp,
                    vertical = 14.dp
                )
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                TextButton(
                    onClick = onBackClick
                ) {
                    Text(
                        text = "← BACK",
                        style = MaterialTheme.typography.labelMedium,
                        color = BonnetBlack
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "INCIDENT #001",
                    style = MaterialTheme.typography.labelMedium,
                    color = AsphaltGrey
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "WHERE DID YOU\nLEAVE IT?",
                style = MaterialTheme.typography.displayLarge,
                color = BonnetBlack,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "LET'S GET THE STORY STRAIGHT.",
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Spacer(
                modifier = Modifier.height(22.dp)
            )

            ParkingTicket(
                formState = formState,
                onParkingLevelChange = onParkingLevelChange,
                onSpotNumberChange = onSpotNumberChange,
                onNoteChange = onNoteChange,
                onParkingExpiryChange = onParkingExpiryChange
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(62.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MiniCitron,
                    contentColor = BonnetBlack
                )
            ) {
                Text(
                    text = "SAVE THE MINI",
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Text(
                text = "Try not to lose the ticket as well.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MutedGrey,
                textAlign = TextAlign.Center,
                modifier = Modifier
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
    onParkingExpiryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = BonnetBlack.copy(alpha = 0.18f),
                shape = RoundedCornerShape(22.dp)
            ),
        shape = RoundedCornerShape(22.dp),
        color = TicketPaper,
        shadowElevation = 3.dp
    ) {

        Column(
            modifier = Modifier.padding(20.dp)
        ) {

            Text(
                text = "PARKING RECORD",
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            TicketDivider()

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Text(
                text = "CURRENT LOCATION",
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = BonnetBlack
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {

                    Text(
                        text = "● LOCATION NOT CAPTURED YET",
                        style = MaterialTheme.typography.labelMedium,
                        color = MiniCitron
                    )

                    Text(
                        text = "The GPS investigation comes next.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = WarmCream.copy(alpha = 0.78f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            ParkingTextField(
                label = "FLOOR / LEVEL",
                value = formState.parkingLevel,
                onValueChange = onParkingLevelChange,
                placeholder = "P3"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ParkingTextField(
                label = "SPOT NUMBER",
                value = formState.spotNumber,
                onValueChange = onSpotNumberChange,
                placeholder = "127"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ParkingTextField(
                label = "NOTE",
                value = formState.note,
                onValueChange = onNoteChange,
                placeholder = "Near the lift, beside the suspicious van"
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            ParkingTextField(
                label = "PARKING EXPIRES",
                value = formState.parkingExpiry,
                onValueChange = onParkingExpiryChange,
                placeholder = "No expiry"
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Text(
                text = "PHOTOGRAPHIC EVIDENCE",
                style = MaterialTheme.typography.labelMedium,
                color = AsphaltGrey
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = BonnetBlack
                )
            ) {
                Text(
                    text = "ADD PARKING PHOTO",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Text(
                text = "Camera support is coming shortly.",
                style = MaterialTheme.typography.bodyMedium,
                fontStyle = FontStyle.Italic,
                color = MutedGrey,
                modifier = Modifier.padding(top = 6.dp)
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = BritishRed.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = "IMPORTANT: Remembering where you parked is still encouraged.",
                    style = MaterialTheme.typography.labelMedium,
                    color = BritishRed
                )
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
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = AsphaltGrey
        )

        Spacer(
            modifier = Modifier.height(7.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    color = MutedGrey
                )
            },
            singleLine = label != "NOTE",
            minLines = if (label == "NOTE") 3 else 1,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BonnetBlack,
                unfocusedBorderColor = BonnetBlack.copy(alpha = 0.22f),
                focusedContainerColor = WarmCream.copy(alpha = 0.45f),
                unfocusedContainerColor = WarmCream.copy(alpha = 0.45f),
                cursorColor = BonnetBlack,
                focusedTextColor = BonnetBlack,
                unfocusedTextColor = BonnetBlack
            )
        )
    }
}

@Composable
private fun TicketDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(
                BonnetBlack.copy(alpha = 0.13f)
            )
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
            formState = ParkingFormState(),
            onParkingLevelChange = {},
            onSpotNumberChange = {},
            onNoteChange = {},
            onParkingExpiryChange = {},
            onBackClick = {},
            onSaveClick = {}
        )
    }
}