package com.example.yugioh.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CardModel(
    val id: String? = null,
    var name: String,
    var type: String,
    var atk: Int? = null,
    var def: Int? = null,
    var level: Int? = null,
    var race: String? = null,
    var attribute: String? = null,
    val price: Double? = null,
    var description: String,

    @SerialName(value = "image_uri")
    val imgUri: String? = null,
    @SerialName(value = "image_small_uri")
    val imgSmallUri: String? = null
)
