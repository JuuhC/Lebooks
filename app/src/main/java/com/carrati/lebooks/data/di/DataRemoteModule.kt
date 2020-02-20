package com.carrati.lebooks.data.di

import com.carrati.lebooks.data.old.api.ServerAPI
import com.carrati.lebooks.R
import com.carrati.lebooks.data.remote.source.BookstoreRemoteDataSourceImpl
import com.carrati.lebooks.data.remote.source.IBookstoreRemoteDataSource
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val remoteDataModule = module {
    factory { providesOkHttpClient() }
    single { createWebService<ServerAPI>(
            okHttpClient = get(),
            url =  androidContext().getString(R.string.base_url)
    ) }

    factory<IBookstoreRemoteDataSource> { BookstoreRemoteDataSourceImpl( serverAPI = get()) }
}

fun providesOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
}

inline fun <reified T> createWebService(okHttpClient: OkHttpClient , url: String): T {
    return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(url)
            .client(okHttpClient)
            .build()
            .create(T::class.java)
}