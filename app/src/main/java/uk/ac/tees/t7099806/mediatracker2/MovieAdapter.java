package uk.ac.tees.t7099806.mediatracker2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private List<MovieInformation> movieInformationList;

    private String genreString;
    public MovieAdapter(Context context, List<MovieInformation> movieInformationList)
    {
        this.context = context;
        this.movieInformationList = movieInformationList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_rv_item, parent, false);
        LayoutInflater inflater;

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        final MovieInformation movieInformation = movieInformationList.get(position);
        holder.name.setText(movieInformation.getName());
        holder.release.setText("Release Date: " + movieInformation.getReleaseDate());
        holder.language.setText("Language: " + movieInformation.getLanguage());


        int genre = Integer.parseInt(movieInformation.getGenre());
        genreString = "";
        getGenre(genre);
        holder.genre.setText(genreString);

//        if(movieInformation.getGenre() != "" || movieInformation.getGenre() == null)
//        {
//            System.out.println(movieInformation.getGenre());
//            int genre = Integer.parseInt(movieInformation.getGenre());
//            genreString = "";
//            getGenre(genre);
//            holder.genre.setText(genreString);
//        }
//        else
//        {
//            genreString = "null";
//            holder.genre.setText(genreString);
//        }


        //https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg


        Picasso.get().load("https://image.tmdb.org/t/p/w500/" +movieInformation.getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MovieDetailsActivity.class);
                i.putExtra("title", movieInformation.getName());
                i.putExtra("image", movieInformation.getImage());
                i.putExtra("language", movieInformation.getLanguage());
                i.putExtra("genre", genreString);
                i.putExtra("release date", movieInformation.getReleaseDate());
                i.putExtra("overview", movieInformation.getOverview());
                i.putExtra("backdrop", movieInformation.getBackDropPath());

                context.startActivity(i);


            }
        });

    }


    public void getGenre(int g)
    {
        switch (g) {
            case 28:
                genreString  = "Action";
                break;
            case 12:
                genreString = "Adventure";
                break;
            case 16:
                genreString = "Animation";
                break;
            case 35:
                genreString = "Comedy";
                break;
            case 80:
                genreString = "Crime";
                break;
            case 99:
                genreString = "Documentary";
                break;
            case 18:
                genreString = "Drama";
                break;
            case 10751:
                genreString = "Family";
                break;
            case 14:
                genreString = "Fantasy";
                break;
            case 36:
                genreString = "History";
                break;
            case 27:
                genreString = "Horror";
                break;
            case 10402:
                genreString = "Music";
                break;
            case 878:
                genreString = "Science Fiction";
                break;
            case 10770:
                genreString = "TV Movie";
                break;
            case 53:
                genreString = "Thriller";
                break;
            case 10752:
                genreString = "War";
                break;
            case 37:
                genreString = "Western";
            case 23:
                genreString = "null";



        }

    }

    @Override
    public int getItemCount() {
        return movieInformationList.size();
    }

    public class MovieViewHolder extends  RecyclerView.ViewHolder {

        TextView name, genre, release, language;
        ImageView image;
        



        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bookTitle);
            image = itemView.findViewById(R.id.bookImage);
            release = itemView.findViewById(R.id.pageCount);
            language = itemView.findViewById(R.id.bookReleaseDate);
            genre = itemView.findViewById(R.id.publisher);


        }
    }
}
