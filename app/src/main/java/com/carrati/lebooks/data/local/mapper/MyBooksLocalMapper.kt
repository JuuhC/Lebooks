package com.carrati.lebooks.data.local.mapper

import com.carrati.lebooks.data.local.model.MyBookLocal
import com.carrati.lebooks.domain.entities.MyBook

object MyBooksLocalMapper {
    //map from cache model to ui model
    fun mapFromDB(data: List<MyBookLocal>) = data.map { mapToDomain(it) }

    private fun mapToDomain(data: MyBookLocal) = MyBook(
            title = data.title,
            writer = data.writer,
            thumbURL = data.thumb_url

    )

    //map from ui to cache
    fun mapToDB(jobs: List<MyBook>) = jobs.map { mapFromDomain(it) }

    private fun mapFromDomain(data: MyBook) = MyBookLocal(
            title = data.title,
            writer = data.writer,
            thumb_url = data.thumbURL

    )

    fun mapFromDB(data: MyBookLocal) = mapToDomain(data)

    fun mapToDB(data: MyBook) = mapFromDomain(data)
}