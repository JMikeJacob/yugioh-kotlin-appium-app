package com.example.yugioh.network
import com.example.yugioh.model.CardModel
import com.example.yugioh.model.PaginatedListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface CardsService {
    @GET("cards")
    suspend fun getCards(
        @Query("page") page: Int,
        @Query("search") search: String?): PaginatedListModel<CardModel>
}