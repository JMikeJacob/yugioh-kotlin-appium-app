package com.example.yugioh.model

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedListModel<T> (
    var items: List<T> = listOf(),
    var totalItems: Int = 0,
    var page: Int = 1,
    val itemsPerPage: Int = 15
)