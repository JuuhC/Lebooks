package com.carrati.lebooks.data.old

import com.carrati.lebooks.domain.entities.Book
import com.carrati.lebooks.data.old.db.MyBooksDAO

class MyBooksRepository(private val myBooksDAO: MyBooksDAO) {

    fun getMyBooks(): MutableList<Book>{
        val repoBookList = myBooksDAO.listarLivroComprado()
        return repoBookList
    }

    fun getFavBooks(): MutableList<Book>{
        val repoBookList = mutableListOf<Book>()
        return repoBookList
    }
}