package com.example.yugioh.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardModel(
    val id: Int,
    val name: String,
    val type: String,
    var atk: Int? = null,
    var def: Int? = null,
    var level: Int? = null,
    var race: String? = null,
    var attribute: String? = null,
    val price: Double,
    var description: String,

    @SerialName(value = "image_uri")
    val imgUri: String,
    @SerialName(value = "image_small_uri")
    val imgSmallUri: String
)
