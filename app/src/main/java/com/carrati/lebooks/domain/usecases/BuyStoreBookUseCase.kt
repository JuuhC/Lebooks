package com.carrati.lebooks.domain.usecases

import com.carrati.lebooks.data.UserPreferences
import com.carrati.lebooks.domain.entities.BookMapper
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.domain.repository.IBookstoreRepository
import com.carrati.lebooks.domain.repository.IMyBooksRepository
import io.reactivex.Scheduler
import io.reactivex.Single

class BuyStoreBookUseCase(
        private val bookstoreRepo: IBookstoreRepository,
        private val myBooksRepo: IMyBooksRepository,
        private val userPrefs: UserPreferences,
        private val mapper: BookMapper,
        private val scheduler: Scheduler
) {

    fun execute(book: StoreBook): Single<Boolean> {
        //2-adiciona no banco
        //3-subtrai do saldo
        val bookPrice: Int = book.price
        val result: Int = userPrefs.saldo - bookPrice

        if(result >= 0){
            //bookstoreRepo.buyBook(book)
            myBooksRepo.addPurchasedBook( mapper.storeToMyBook( book ))
            userPrefs.saldo = result
            return Single.just(true).subscribeOn(scheduler)
        } else {
            return Single.just(false).subscribeOn(scheduler)
        }
    }

}