package com.example.yugioh

import android.app.Application
import com.example.yugioh.data.AppContainer
import com.example.yugioh.data.DefaultAppContainer

class CardsApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}