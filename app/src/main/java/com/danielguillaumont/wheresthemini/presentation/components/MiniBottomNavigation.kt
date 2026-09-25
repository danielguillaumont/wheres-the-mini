package com.danielguillaumont.wheresthemini.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.danielguillaumont.wheresthemini.ui.theme.BonnetBlack
import com.danielguillaumont.wheresthemini.ui.theme.MiniCitron
import com.danielguillaumont.wheresthemini.ui.theme.WarmCream

enum class MiniTab {
    MINI,
    HISTORY,
    INFO
}

@Composable
fun MiniBottomNavigation(
    selectedTab: MiniTab,
    onMiniClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onInfoClick: () -> Unit
) {
    Surface(
        color = BonnetBlack,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(
                    horizontal = 20.dp,
                    vertical = 14.dp
                ),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                label = "MINI",
                selected = selectedTab == MiniTab.MINI,
                onClick = onMiniClick
            )

            BottomNavigationItem(
                label = "HISTORY",
                selected = selectedTab == MiniTab.HISTORY,
                onClick = onHistoryClick
            )

            BottomNavigationItem(
                label = "INFO",
                selected = selectedTab == MiniTab.INFO,
                onClick = onInfoClick
            )
        }
    }
}

@Composable
private fun BottomNavigationItem(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = 12.dp,
                vertical = 2.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (selected) {
                MiniCitron
            } else {
                WarmCream.copy(
                    alpha = 0.65f
                )
            }
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        androidx.compose.foundation.layout.Box(
            modifier = Modifier
                .width(
                    if (selected) {
                        30.dp
                    } else {
                        6.dp
                    }
                )
                .height(4.dp)
                .background(
                    color = if (selected) {
                        MiniCitron
                    } else {
                        Color.Transparent
                    },
                    shape = CircleShape
                )
        )
    }
}