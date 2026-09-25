package com.danielguillaumont.wheresthemini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.danielguillaumont.wheresthemini.data.notification.NotificationHelper
import com.danielguillaumont.wheresthemini.presentation.navigation.AppNavigation
import com.danielguillaumont.wheresthemini.ui.theme.WheresTheMiniTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(
            savedInstanceState
        )

        NotificationHelper
            .ensureNotificationChannel(
                applicationContext
            )

        enableEdgeToEdge()

        setContent {
            WheresTheMiniTheme {
                AppNavigation()
            }
        }
    }
}