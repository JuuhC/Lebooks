package com.carrati.lebooks.Config;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.carrati.lebooks.Model.Book;
import com.carrati.lebooks.Activity.BookstoreActivity;
import com.carrati.lebooks.R;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.List;

//um adapter com listener para button
public class AdapterBookstore extends RecyclerView.Adapter<AdapterBookstore.MyViewHolder> {

    private final List<Book> books;
    private final IRecyclerViewClickListener listener;

    public AdapterBookstore(List<Book> books, IRecyclerViewClickListener listener){
        this.books = books;
        this.listener = listener;
    }

    @Override @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bookstore, parent, false);
        return new MyViewHolder(itemLista, listener);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Book book = books.get(position);

        holder.titulo.setText(book.getTitle());
        holder.autor.setText(book.getWriter());
        Picasso.get().load(book.getThumbURL()).into(holder.thumb);
        holder.valor.setText("R$ " + book.getPrice());
    }


    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView titulo, autor, valor;
        private ImageView thumb;
        private Button comprar;
        private WeakReference<IRecyclerViewClickListener> listenerRef;


        public MyViewHolder(View itemView, IRecyclerViewClickListener listener) {
            super(itemView);

            listenerRef = new WeakReference<>(listener);

            titulo = itemView.findViewById(R.id.bookstoreAdapterTitulo);
            autor = itemView.findViewById(R.id.bookstoreAdapterAutor);
            thumb = itemView.findViewById(R.id.bookstoreAdapterThumb);
            valor = itemView.findViewById(R.id.bookstoreAdapterValor);
            comprar = itemView.findViewById(R.id.bookstoreAdapterComprar);

            //itemView.setOnClickListener(this);
            comprar.setOnClickListener(this);
        }

        // onClick Listener for view
        @Override
        public void onClick(View v) {
            listenerRef.get().onClick(getAdapterPosition());
        }

    }
}
