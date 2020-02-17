package com.carrati.lebooks.DataRepository

import com.carrati.lebooks.Entities.Book
import com.carrati.lebooks.DataRepository.Local.MyBooksDAO

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