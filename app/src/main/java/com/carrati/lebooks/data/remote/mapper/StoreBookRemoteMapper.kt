package com.carrati.lebooks.data.remote.mapper

import com.carrati.lebooks.data.remote.model.StoreBookPayload
import com.carrati.lebooks.data.remote.model.BookstorePayload
import com.carrati.lebooks.domain.entities.StoreBook

object StoreBookRemoteMapper {

    fun mapFromAPI(payload: BookstorePayload) = payload.bookstorePayload.map { mapToDomain(it) }

    private fun mapToDomain(payload: StoreBookPayload) = StoreBook(
            title = payload.title,
            writer = payload.writer,
            price = payload.price,
            thumbURL = payload.thumb_url
    )


}