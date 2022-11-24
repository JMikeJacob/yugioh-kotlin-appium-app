package com.example.yugioh.data

import com.example.yugioh.model.CardModel
import com.example.yugioh.model.PaginatedListModel
import com.example.yugioh.network.CardsService

interface CardsRepository {
    suspend fun getCards(page: Int, search: String?, itemsPerPage: Int?): PaginatedListModel<CardModel>
}

class DefaultCardsRepository(
    private val cardsService: CardsService
) : CardsRepository {
    override suspend fun getCards(page: Int, search: String?, itemsPerPage: Int?): PaginatedListModel<CardModel> {
        return cardsService.getCards(page, search)
    }
}