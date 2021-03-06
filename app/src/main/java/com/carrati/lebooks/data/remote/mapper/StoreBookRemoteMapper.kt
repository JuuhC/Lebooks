package com.carrati.lebooks.data.remote.mapper

import com.carrati.lebooks.data.remote.model.StoreBookPayload
import com.carrati.lebooks.domain.entities.StoreBook

object StoreBookRemoteMapper {

    var id = 1

    fun mapFromAPI(payload: List<StoreBookPayload>) = payload.map { mapToDomain(it) }

    private fun mapToDomain(payload: StoreBookPayload) = StoreBook(
            id = id++,
            title = payload.title,
            writer = payload.writer,
            price = payload.price,
            thumbURL = payload.thumb_url
    )


}