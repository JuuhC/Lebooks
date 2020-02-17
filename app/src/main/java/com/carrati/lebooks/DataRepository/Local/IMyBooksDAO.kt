package com.carrati.lebooks.DataRepository.Local

import com.carrati.lebooks.Entities.Book

interface IMyBooksDAO {
    fun salvarLivroComprado(book: Book): Boolean
    fun procurarLivroComprado(book: Book): Boolean
    fun listarLivroComprado(): List<Book>
    fun salvarLivroFavorito(book: Book): Boolean
    fun procurarLivroFavorito(book: Book): Boolean
    fun deletarLivroFavorito(book: Book): Boolean
    fun cleanAllBooks()
}
