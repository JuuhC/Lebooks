package com.carrati.lebooks.presentation.adapters

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView

import com.carrati.lebooks.R
import com.carrati.lebooks.domain.entities.StoreBook
import com.carrati.lebooks.presentation.viewmodels.ViewState
import com.squareup.picasso.Picasso

//um adapter com listener para button
class AdapterBookstore(private var books: List<StoreBook>,
                       private val onClickListenerRV: IRecyclerViewClickListener,
                       private val context: Context,
                       private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<AdapterBookstore.MyViewHolder>() {

    private val mutableBooks = mutableListOf<StoreBook>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_bookstore, parent, false)
        return MyViewHolder(itemLista)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val book = mutableBooks[position]

        holder.titulo.text = book.title
        holder.autor.text = book.writer
        Picasso.get().load(book.thumbURL).into(holder.thumb)
        holder.valor.text = "R$ " + book.price.toString()

        holder.comprar.setOnClickListener {
            val stateBuyBook = onClickListenerRV.onClickBuyBook(book)

            stateBuyBook.observe(lifecycleOwner, Observer { state ->
                when(state) {
                    is ViewState.Success -> {
                        if(state.data == true) {
                            mutableBooks.remove(book)
                            notifyDataSetChanged()
                            Toast.makeText(context, "Compra efetuada!", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Saldo insuficiente!", Toast.LENGTH_LONG).show()
                        }
                        stateBuyBook.removeObservers(lifecycleOwner)
                    }
                }
            })
        }

        holder.favorito.setOnClickListener {
            book.favor = if (book.favor) false else true  //cristo
            val stateFavBook = onClickListenerRV.onClickFavBook(book)

            stateFavBook.observe(lifecycleOwner, Observer { state ->
                when(state) {
                    is ViewState.Success -> {
                        mutableBooks.sortWith(compareByDescending<StoreBook>{ it.favor }.thenBy{ it.title })
                        notifyDataSetChanged()

                        if(book.favor)
                            holder.favorito.setImageDrawable(
                                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on))
                        else
                            holder.favorito.setImageDrawable(
                                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off))
                    }
                    is ViewState.Failed -> {
                        Toast.makeText(context,"Erro ao favoritar livro: " + book.title, Toast.LENGTH_SHORT).show()
                        Log.e("activityFavBook", state.throwable.toString())
                        stateFavBook.removeObservers(lifecycleOwner)
                    }
                }
            })
        }

        if(book.favor)
            holder.favorito.setImageDrawable(
                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_on))
        else
            holder.favorito.setImageDrawable(
                    ContextCompat.getDrawable(context, android.R.drawable.btn_star_big_off))

    }

    /////CARA$%#
    //é aqui que ele define qual lista ele vai usar no onBind
    override fun getItemCount(): Int {
        return mutableBooks.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val titulo = itemView.findViewById<TextView>(R.id.bookstoreAdapterTitulo)
        val autor = itemView.findViewById<TextView>(R.id.bookstoreAdapterAutor)
        val thumb = itemView.findViewById<ImageView>(R.id.bookstoreAdapterThumb)
        val valor = itemView.findViewById<TextView>(R.id.bookstoreAdapterValor)
        val comprar = itemView.findViewById<Button>(R.id.bookstoreAdapterComprar)
        val favorito = itemView.findViewById<ImageButton>(R.id.bookstoreAdapterFav)
    }

    fun updateAllItens(list: List<StoreBook>){
        mutableBooks.apply {
            clear()
            addAll(list)
            sortWith(compareByDescending<StoreBook>{ it.favor }.thenBy{ it.title })
        }
        this.books = list
        notifyDataSetChanged()
    }

    fun searchBook(text: String){
        mutableBooks.clear()

        for (book in books){
            if(text.isBlank()){
                mutableBooks.add(book)
            } else if(book.title.contains(text, ignoreCase = true) ||
                      book.writer.contains(text, ignoreCase = true)) {
                mutableBooks.add(book)
            }
        }

        mutableBooks.sortWith(compareByDescending<StoreBook>{ it.favor }.thenBy{ it.title })
        notifyDataSetChanged()
    }
}
