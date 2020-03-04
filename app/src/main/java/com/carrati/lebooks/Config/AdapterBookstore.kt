package com.carrati.lebooks.Config

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.carrati.lebooks.Activity.MainActivity

import com.carrati.lebooks.Model.Book
import com.carrati.lebooks.R
import com.squareup.picasso.Picasso

import java.lang.ref.WeakReference

//um adapter com listener para button
class AdapterBookstore(private val books: List<Book>,
                       private val onClickListenerBuyBook: IRecyclerViewBuyBookClickListener,
                       private val onClickListenerFavBook: IRecyclerViewFavClickListener,
                       private val context: Context) : RecyclerView.Adapter<AdapterBookstore.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_bookstore, parent, false)
        return MyViewHolder(itemLista)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = books[position]

        holder.titulo.text = book.title
        holder.autor.text = book.writer
        Picasso.get().load(book.thumbURL).into(holder.thumb)
        holder.valor.text = "R$ " + book.price.toString()

        holder.comprar.setOnClickListener {
            onClickListenerBuyBook.onClickBuyBook(position)
        }

        holder.favorito.setOnClickListener {
            onClickListenerFavBook.onClickFavBook(position)
            notifyDataSetChanged()
            /*if(book.fav)
                holder.favorito.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on))
            else
                holder.favorito.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off))*/
        }

        if(book.fav)
            holder.favorito.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on))
        else
            holder.favorito.setImageDrawable(ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off))

    }


    override fun getItemCount(): Int {
        return books.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        //tirei o private mas deveria coloca outra coisa?
        //private val listenerRefBuyBook: WeakReference<IRecyclerViewBuyBookClickListener> = WeakReference(onClickListenerBuyBook)

        //não precisava do init
        val titulo = itemView.findViewById<TextView>(R.id.bookstoreAdapterTitulo)
        val autor = itemView.findViewById<TextView>(R.id.bookstoreAdapterAutor)
        val thumb = itemView.findViewById<ImageView>(R.id.bookstoreAdapterThumb)
        val valor = itemView.findViewById<TextView>(R.id.bookstoreAdapterValor)
        val comprar = itemView.findViewById<Button>(R.id.bookstoreAdapterComprar)
        val favorito = itemView.findViewById<ImageButton>(R.id.bookstoreAdapterFav)
    }
}
