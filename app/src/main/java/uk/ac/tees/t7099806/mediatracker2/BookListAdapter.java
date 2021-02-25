package uk.ac.tees.t7099806.mediatracker2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;




public class BookListAdapter extends RecyclerView.Adapter<BookListAdapter.BookViewHolder> {

    private ArrayList<BookInfoFirebase> bookInfoFirebaseArrayList;
    private Context context;
    private String listType;

    public BookListAdapter(ArrayList<BookInfoFirebase> bookInfoFirebaseArrayList, Context context, String listType)
    {
        this.bookInfoFirebaseArrayList = bookInfoFirebaseArrayList;
        this.context = context;
        this.listType = listType;
    }


    @NonNull
    @Override
    public BookListAdapter.BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, BookDetailsActivity.class);
                i.putExtra("title", bookInfo.getTitle());
                i.putExtra("subtitle", bookInfo.getSubTitle());
                i.putExtra("authors", "add");
                i.putExtra("publisher", bookInfo.getPublisher());
                i.putExtra("publishedDate", bookInfo.getPublishDate());
                i.putExtra("description", bookInfo.getDescription());
                i.putExtra("pageCount", bookInfo.getNumberOfPages());
                i.putExtra("thumbnail", bookInfo.getBookImage());
                i.putExtra("previewLink", "lol");
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookInfoFirebaseArrayList.size();
    }


    public class BookViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{
        TextView nameTV, publisherTV, pageCountTV, dateTV;
        ImageView bookIV;
        Button removeFromListTV;

        public BookViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.bookTitleList);
            publisherTV = itemView.findViewById(R.id.publisherList);
            pageCountTV = itemView.findViewById(R.id.pageCountList);
            dateTV = itemView.findViewById(R.id.bookReleaseDateList);
            bookIV = itemView.findViewById(R.id.bookImageList);
            removeFromListTV = itemView.findViewById(R.id.removeFromListButton);
            removeFromListTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == removeFromListTV)
            {
                removeFromList();
            }
        }

        private void removeFromList()
        {
            System.out.println(nameTV.toString());
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            final FirebaseAuth firebaseAuth  = FirebaseAuth.getInstance();;
            final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();;

            DatabaseReference checkRef = databaseReference.child("list").child(firebaseUser.getUid());
            Query query = checkRef.child("read list").orderByChild("bookImage").equalTo(bookIV.toString());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot dataSnapshot : snapshot.getChildren())
                    {
                        dataSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

//            checkRef.orderByChild("bookImage").equalTo(bookIV.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if(snapshot.exists())
//                    {
//                        //String a = databaseReference.child("lists").child(firebaseUser.getUid()).child("plantoreadlist").child("MUGYpHdo-i8Qshn4LdJ").get
//                        databaseReference.child("lists").child(firebaseUser.getUid()).child("plantoreadlist").child("MUGYpHdo-i8Qshn4LdJ").removeValue();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

        }

    }

}
