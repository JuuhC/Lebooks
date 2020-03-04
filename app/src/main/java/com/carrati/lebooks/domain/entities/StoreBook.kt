package com.carrati.lebooks.domain.entities

data class StoreBook (
    var id: Int = -1,
    var title: String,
    var writer: String,
    var thumbURL: String,
    var price: Int,
    var favor: Boolean = false
)
