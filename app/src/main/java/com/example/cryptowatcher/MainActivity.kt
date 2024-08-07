package com.example.cryptowatcher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cryptowatcher.ui.composables.MyApp
import com.example.cryptowatcher.ui.theme.CryptoWatcherTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            CryptoWatcherTheme {
                MyApp()
            }
        }
    }
}
