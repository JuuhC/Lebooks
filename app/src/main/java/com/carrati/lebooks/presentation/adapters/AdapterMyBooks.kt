package com.carrati.lebooks.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.carrati.lebooks.R
import com.carrati.lebooks.domain.entities.MyBook
import com.squareup.picasso.Picasso

class AdapterMyBooks(private val books: MutableList<MyBook>)//um adapter comum
    : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterMyBooks.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.i("myBooksAdapter", "onCreate")
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_my_books, parent, false)
        return MyViewHolder(itemLista)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = books[position]

        holder.titulo.text = book.title
        holder.autor.text = book.writer
        //holder.thumb.setImageDrawable(book.getThumb(book.getThumbURL()));
        Picasso.get().load(book.thumbURL).into(holder.thumb)

        //Log.i("myBooksAdapter", book.title)
    }


    override fun getItemCount(): Int {
        return books.size
    }

    //tirei o private do constructor mas não vai dar ruim?
    inner class MyViewHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        /*internal var titulo: TextView
        internal var autor: TextView
        internal var thumb: ImageView*/

        val titulo = itemView.findViewById<TextView>(R.id.myBooksAdapterTitulo)
        val autor = itemView.findViewById<TextView>(R.id.myBookAdapterAutor)
        val thumb = itemView.findViewById<ImageView>(R.id.myBooksAdapterThumb)

    }
}
