package com.example.yugioh.model

import kotlinx.serialization.Serializable

@Serializable
data class CardPhotoModel (
    val id: Int,
    val imgSrc: String
    )