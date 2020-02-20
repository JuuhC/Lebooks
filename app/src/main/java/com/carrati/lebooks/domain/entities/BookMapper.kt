package com.carrati.lebooks.domain.entities

object BookMapper {

    fun storeToMyBook(storebook: StoreBook) = MyBook(
            title = storebook.title,
            writer = storebook.writer,
            thumbURL = storebook.thumbURL
    )
}