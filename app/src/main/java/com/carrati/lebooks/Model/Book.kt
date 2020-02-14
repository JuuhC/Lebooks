package com.carrati.lebooks.Model

import android.graphics.drawable.Drawable
import android.util.Log

import java.io.InputStream
import java.net.URL

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
