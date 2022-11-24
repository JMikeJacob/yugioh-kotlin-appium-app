package com.example.yugioh.data

import com.example.yugioh.network.CardsService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val cardsRepository: CardsRepository
}

class DefaultAppContainer: AppContainer {
    private val BASE_URL = "http://192.168.55.105:3000"
    private val retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(BASE_URL)
        .build()

    private val retrofitService: CardsService by lazy {
        retrofit.create(CardsService::class.java)
    }

    override val cardsRepository: CardsRepository by lazy {
        DefaultCardsRepository(retrofitService)
    }
}