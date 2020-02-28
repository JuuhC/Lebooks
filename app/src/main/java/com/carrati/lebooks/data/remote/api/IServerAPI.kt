package com.carrati.lebooks.data.remote.api

import com.carrati.lebooks.data.remote.model.StoreBookPayload
import io.reactivex.Single
import retrofit2.http.GET

interface IServerAPI {
    @GET("products.json")
    fun fetchBooks(): Single<List<StoreBookPayload>>
}