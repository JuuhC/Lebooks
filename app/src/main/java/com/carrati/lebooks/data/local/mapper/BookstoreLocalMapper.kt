package com.carrati.lebooks.data.local.mapper

import com.carrati.lebooks.data.local.model.StoreBookLocal
import com.carrati.lebooks.domain.entities.StoreBook


object BookstoreLocalMapper {

    //map list from cache model to ui model
    fun mapFromDB(data: List<StoreBookLocal>) = data.map { mapToDomain(it) }

    private fun mapToDomain(data: StoreBookLocal) = StoreBook(
            title = data.title,
            writer = data.writer,
            thumbURL = data.thumb_url,
            price = data.price,
            favor = data.favor
    )

    //map list from ui to cache
    fun mapToDB(jobs: List<StoreBook>) = jobs.map { mapFromDomain(it) }

    private fun mapFromDomain(data: StoreBook) = StoreBookLocal(
            title = data.title,
            writer = data.writer,
            thumb_url = data.thumbURL,
            price = data.price,
            favor = data.favor
    )

    fun mapFromDB(data: StoreBookLocal) = mapToDomain(data)

    fun mapToDB(data: StoreBook) = mapFromDomain(data)
}