package com.carrati.lebooks.presentation.adapters

import android.content.Context
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import com.carrati.lebooks.R
import com.carrati.lebooks.databinding.AdapterBookstoreBinding
import com.carrati.lebooks.domain.entities.StoreBook
import com.squareup.picasso.Picasso

//um adapter com listener para button
class AdapterBookstore(private val books: MutableList<StoreBook>,
                       private val onClickListenerRV: IRecyclerViewClickListener,
                       private val context: Context
) : RecyclerView.Adapter<AdapterBookstore.MyViewHolder>() {

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista: AdapterBookstoreBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.adapter_bookstore, parent, false)
        return MyViewHolder(itemLista)
    }*/

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
            val bookPurchased: Boolean = onClickListenerRV.onClickBuyBook(book)
            if(bookPurchased) {
                books.remove(book)
                notifyDataSetChanged()
            }
        }

        holder.favorito.setOnClickListener {
            val bookFavorited: Boolean = onClickListenerRV.onClickFavBook(book)

            if(bookFavorited) {
                book.favor = if (book.favor) false else true  //cristo
                books.sortWith(compareBy({ it.favor }, { it.title }))
                notifyDataSetChanged()
            }
        }

        if(book.favor)
            holder.favorito.setImageDrawable(
                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on))
        else
            holder.favorito.setImageDrawable(
                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off))

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
