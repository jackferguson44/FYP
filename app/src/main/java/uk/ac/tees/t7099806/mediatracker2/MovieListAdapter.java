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

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.MovieViewHolder> {

    private ArrayList<MovieInformation> movieInformationArrayList;
    private Context context;
    private String listType;


    public MovieListAdapter(ArrayList<MovieInformation> movieInformationArrayList, Context context, String listType)
    {
        this.movieInformationArrayList = movieInformationArrayList;
        this.context = context;
        this.listType = listType;
    }


    @NonNull
    @Override
    public MovieListAdapter.MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        System.out.println("adapter");
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieListAdapter.MovieViewHolder holder, int position) {

        //sets data to each show item
        final MovieInformation movieInformation = movieInformationArrayList.get(position);
        holder.name.setText(movieInformation.getName());
        holder.release.setText("Release Date: " + movieInformation.getReleaseDate());
        holder.language.setText("Language: " + movieInformation.getLanguage());
        holder.genre.setText(movieInformation.getGenre());


        //sets image
        Picasso.get().load("https://image.tmdb.org/t/p/w500/" +movieInformation.getImage()).into(holder.image);

        //when show item is clicked on passes details to show details page
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MovieDetailsActivity.class);
                i.putExtra("title", movieInformation.getName());
                i.putExtra("image", movieInformation.getImage());
                i.putExtra("language", movieInformation.getLanguage());
                i.putExtra("genre", movieInformation.getGenre());
                i.putExtra("release date", movieInformation.getReleaseDate());
                i.putExtra("overview", movieInformation.getOverview());
                i.putExtra("backdrop", movieInformation.getBackDropPath());

                context.startActivity(i);


            }
        });
    }






    @Override
    public int getItemCount() { return movieInformationArrayList.size(); }

    public class MovieViewHolder extends  RecyclerView.ViewHolder {

        TextView name, genre, release, language;
        ImageView image;




        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bookTitleList);
            image = itemView.findViewById(R.id.bookImageList);
            release = itemView.findViewById(R.id.pageCountList);
            language = itemView.findViewById(R.id.bookReleaseDateList);
            genre = itemView.findViewById(R.id.publisherList);


        }
    }

}
