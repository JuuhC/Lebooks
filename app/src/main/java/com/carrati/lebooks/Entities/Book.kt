package com.carrati.lebooks.Entities

class Book {

    var title: String? = null
    var writer: String? = null
    var thumbURL: String? = null
        private set
    var price: Int? = null
    var fav: Boolean = false

    fun setFav(){
        fav = true
    }

    fun unsetFav(){ fav = false }

    fun setThumb(thumb: String) {
        this.thumbURL = thumb
    }
}
