package com.example.yugioh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yugioh.ui.CardsListApp
import com.example.yugioh.ui.screens.CardsViewModel
import com.example.yugioh.ui.theme.YugiohTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YugiohTheme {
                CardsListApp()
            }
        }
    }
}