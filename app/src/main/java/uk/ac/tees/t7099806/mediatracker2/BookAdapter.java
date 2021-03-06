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


public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {


    private ArrayList<BookInformation> bookInfoArrayList;
    private Context mcontext;

    public BookAdapter(ArrayList<BookInformation> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        //Sets data to each book item
        final BookInformation bookInfo = bookInfoArrayList.get(position);
        holder.nameTV.setText(bookInfo.getTitle());
        holder.publisherTV.setText(bookInfo.getPublisher());
        holder.pageCountTV.setText("No of Pages : " + bookInfo.getPageCount());
        holder.dateTV.setText(bookInfo.getPublishedDate());


        //Set Image
        Picasso.get().load(bookInfo.getThumbnail()).into(holder.bookIV);

        //when book item is clicked on passes details to book details page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext, BookDetailsActivity.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubtitle());
                i.putExtra("authors", bookInfo.getAuthors());
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishedDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getPageCount());
                i.putExtra("thumbnail", bookInfo.getThumbnail());
                i.putExtra("previewLink", bookInfo.getPreviewLink());
                i.putExtra("subject", bookInfo.getCategory());

                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoArrayList.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder {
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
