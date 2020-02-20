package com.carrati.lebooks.data.remote.model

import com.google.gson.annotations.SerializedName

class BookstorePayload (
        @SerializedName("books") val bookstorePayload: List<StoreBookPayload>
)

class StoreBookPayload (
    @SerializedName("title") val title: String,
    @SerializedName("writer") val writer: String,
    @SerializedName("price") val price: Int,
    @SerializedName("thumbnailHd") val thumb_url: String
)