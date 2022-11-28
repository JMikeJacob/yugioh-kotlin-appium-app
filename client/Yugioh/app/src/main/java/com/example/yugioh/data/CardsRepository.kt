package com.example.yugioh.data

import com.example.yugioh.model.CardModel
import com.example.yugioh.model.PaginatedListModel
import com.example.yugioh.network.CardsService
import retrofit2.Call

interface CardsRepository {
    suspend fun getCards(page: Int, search: String?, itemsPerPage: Int?): PaginatedListModel<CardModel>
    suspend fun postCard(cardModel: CardModel): Call<CardModel>
}

class DefaultCardsRepository(
    private val cardsService: CardsService
) : CardsRepository {
    override suspend fun getCards(page: Int, search: String?, itemsPerPage: Int?): PaginatedListModel<CardModel> {
        return cardsService.getCards(page, search)
    }

    override suspend fun postCard(cardModel: CardModel): Call<CardModel> {
        return cardsService.postCard(cardModel)
    }

}