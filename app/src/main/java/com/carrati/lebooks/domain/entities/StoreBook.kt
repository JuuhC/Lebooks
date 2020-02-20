package com.carrati.lebooks.domain.entities

data class StoreBook (
    var title: String,
    var writer: String,
    var thumbURL: String,
    var price: Int,
    var favor: Boolean = false
)
