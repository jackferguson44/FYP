package uk.ac.tees.t7099806.mediatracker2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private ArrayList<BookInfoFirebase> bookInfoFirebaseArrayList;
    private Context context;

    public BookListAdapter(ArrayList<BookInfoFirebase> bookInfoFirebaseArrayList, Context context)
    {
        this.bookInfoFirebaseArrayList = bookInfoFirebaseArrayList;
        this.context = context;
    }


    @NonNull
    @Override
    public BookListAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookListAdapter.BookViewHolder holder, int position) {
        final BookInfoFirebase bookInfo = bookInfoFirebaseArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getNumberOfPages());
        holder.dateTV.setText(bookInfo.getPublishDate());

        Picasso.get().load(bookInfo.getBookImage()).into(holder.bookIV);

        //holder.itemView.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return bookInfoFirebaseArrayList.size();
    }


    public class BookViewHolder extends  RecyclerView.ViewHolder{
            TextView nameTV, publisherTV, pageCountTV, dateTV;
            ImageView bookIV;

            public BookViewHolder(View itemView) {
                super(itemView);
                nameTV = itemView.findViewById(R.id.bookTitle);
                publisherTV = itemView.findViewById(R.id.publisher);
                pageCountTV = itemView.findViewById(R.id.pageCount);
                dateTV = itemView.findViewById(R.id.bookReleaseDate);
                bookIV = itemView.findViewById(R.id.bookImage);
            }
        }

    }
