package com.carrati.lebooks.Config;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.carrati.lebooks.Model.Book;
import com.carrati.lebooks.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterMyBooks extends RecyclerView.Adapter<AdapterMyBooks.MyViewHolder> {

    private List<Book> books;

    public AdapterMyBooks(List<Book> books){ //um adapter comum
        this.books = books;
    }

    @Override @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.i("myBooksAdapter", "onCreate" );
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_books, parent, false);
        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = books.get(position);

        holder.titulo.setText(book.getTitle());
        holder.autor.setText(book.getWriter());
        //holder.thumb.setImageDrawable(book.getThumb(book.getThumbURL()));
        Picasso.get().load(book.getThumbURL()).into(holder.thumb);

        Log.i("myBooksAdapter", book.getTitle() );
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, autor;
        ImageView thumb;

        private MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.myBooksAdapterTitulo);
            autor = itemView.findViewById(R.id.myBookAdapterAutor);
            thumb = itemView.findViewById(R.id.myBooksAdapterThumb);

            Log.i("myBooksAdapter", "viewHolder" );
        }

    }
}
