package uk.ac.tees.t7099806.mediatracker2;

import android.content.Context;
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

        Picasso.get().load(movieInformation.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return movieInformationList.size();
    }

    public class MovieViewHolder extends  RecyclerView.ViewHolder {

        TextView id, name;
        ImageView image;



        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.bookTitle);
            image = itemView.findViewById(R.id.bookImage);
        }
    }
}
