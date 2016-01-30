package com.movies.example.popmovies;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.movies.example.popmovies.model.response.Movie;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by admin on 30/01/16.
 */
public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder> {
    List<Movie> movieDetailsDataset;
    int position;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImg;
        TextView title;
        TextView rating;
        TextView overview;
        TextView date;

        public ViewHolder(GridLayout moviesDetailsGrid) {
            super(moviesDetailsGrid);
            posterImg = (ImageView) moviesDetailsGrid.findViewById(R.id.movie_details_poster_ImageView);
            title = (TextView) itemView.findViewById(R.id.movie_details_title_textview);
            rating = (TextView) itemView.findViewById(R.id.movie_details_rating);
            overview = (TextView) itemView.findViewById(R.id.movie_details_overview);
            date = (TextView) itemView.findViewById(R.id.movie_details_release_date);

        }
    }

    public MovieDetailsAdapter(List<Movie> movieDetails, int position) {
        if (movieDetails != null) {
            movieDetailsDataset = movieDetails;
            this.position = position;
        }
    }
    @Override
    public MovieDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_details, null, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((GridLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MovieDetailsAdapter.ViewHolder holder, int position) {
        if(movieDetailsDataset.get(position) != null) {
            String posterURL = "http://image.tmdb.org/t/p/w185/"+movieDetailsDataset.get(position).poster_path;
            Picasso.with(holder.posterImg.getContext()).load(posterURL).into(holder.posterImg);
            holder.title.setText(movieDetailsDataset.get(position).title);
            holder.rating.setText(movieDetailsDataset.get(position).vote_average.toString() + "/10");
            holder.overview.setText(movieDetailsDataset.get(position).overview);
            holder.date.setText(movieDetailsDataset.get(position).release_date.toString());


        }
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieDetailsDataset.size();
    }
    public void updateData(List<Movie> movieDataset, int position){
        movieDetailsDataset.clear();
        movieDetailsDataset = movieDataset;
        this.position = position;
        notifyDataSetChanged();
    }
}
