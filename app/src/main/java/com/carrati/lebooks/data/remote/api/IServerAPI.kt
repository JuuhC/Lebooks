package com.carrati.lebooks.data.remote.api

import com.carrati.lebooks.data.remote.model.BookstorePayload
import io.reactivex.Single
import retrofit2.http.GET

interface IServerAPI {
    @GET("/store-books")
    fun fetchBooks(): Single<BookstorePayload>
}