package com.movies.example.popmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.movies.example.popmovies.model.response.Movie;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by admin on 28/01/16.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private List<Movie> movieAdapterDataset ;

    private static ItemClickListener itemClickListener;
    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        // each data item is just a string in this case
        public ImageView picassoimg;
        private final String LOG_TAG = getClass().getName().toString();
        public ViewHolder(GridLayout moviesGrid) {
            super(moviesGrid);
            picassoimg = (ImageView) moviesGrid.findViewById(R.id.posterImgView);
            Log.i(LOG_TAG, "Adding Listener");
            moviesGrid.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(getPosition(), v);
        }
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieGridAdapter(List<Movie> movieDataset) {
        if(movieDataset != null) {
            movieAdapterDataset = movieDataset;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MovieGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movies, null, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder((GridLayout) v);
        return vh;
    }
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        if(movieAdapterDataset.get(position) != null) {
            String posterURL = "http://image.tmdb.org/t/p/w185/"+movieAdapterDataset.get(position).poster_path;
            Log.v("In adapter", posterURL);
            Picasso.with(holder.picassoimg.getContext()).load(posterURL).into(holder.picassoimg);
        }
        else
            Picasso.with(holder.picassoimg.getContext()).load("http://image.tmdb.org/t/p/w185//oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg").into(holder.picassoimg);
    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieAdapterDataset.size();
    }
    public void updateData(List<Movie> movieDataset){
        movieAdapterDataset.clear();
        movieAdapterDataset = movieDataset;
        notifyDataSetChanged();
    }
    public interface ItemClickListener {
        public void onItemClick(int position, View v);
    }
}

