package com.example.yugioh.ui.screens

import com.example.yugioh.model.CardModel
import com.example.yugioh.model.PaginatedListModel

sealed interface CardsUiState {
    data class Success(val cards: List<CardModel>): CardsUiState
    object Error: CardsUiState
    object Loading: CardsUiState
}