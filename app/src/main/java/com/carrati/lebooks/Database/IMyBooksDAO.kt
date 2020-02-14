package com.carrati.lebooks.Database

import com.carrati.lebooks.Model.Book

interface IMyBooksDAO {
    fun salvarLivroComprado(book: Book): Boolean
    fun procurarLivroComprado(book: Book): Boolean
    fun listarLivroComprado(): List<Book>
    fun salvarLivroFavorito(book: Book): Boolean
    fun procurarLivroFavorito(book: Book): Boolean
    fun deletarLivroFavorito(book: Book): Boolean
    fun cleanAllBooks()
}
