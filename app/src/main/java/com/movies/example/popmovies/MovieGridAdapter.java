package com.movies.example.popmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by admin on 28/01/16.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private String[] movieAdapterDataset;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView picassoimg;
        public ViewHolder(GridLayout moviesGrid) {
            super(moviesGrid);
            picassoimg = (ImageView) moviesGrid.findViewById(R.id.posterImgView);

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MovieGridAdapter(Context context, String[] movieDataset) {
        movieAdapterDataset = movieDataset;
        this.context = context;
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
        if(movieAdapterDataset != null) {
            Picasso.with(context).load(movieAdapterDataset[position]).into(holder.picassoimg);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return movieAdapterDataset.length;
    }

    public void updateData(String [] movieDataset){
        this.movieAdapterDataset = movieDataset;
        notifyDataSetChanged();

    }
}
