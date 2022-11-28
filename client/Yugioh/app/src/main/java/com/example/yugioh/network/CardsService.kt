package com.example.yugioh.network
import com.example.yugioh.model.CardModel
import com.example.yugioh.model.PaginatedListModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CardsService {
    @GET("cards")
    suspend fun getCards(
        @Query("page") page: Int,
        @Query("search") search: String?): PaginatedListModel<CardModel>

    @POST("cards")
    suspend fun postCard(@Body cardModel: CardModel?): Call<CardModel>
}