package com.movies.example.popmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.movies.example.popmovies.R;
import com.movies.example.popmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Renuka Challa on 28/01/16.
 */

public class MovieGridAdapter extends RecyclerView.Adapter<MovieGridAdapter.ViewHolder> {
    private List<Movie> movieAdapterDataset;

    private static ItemClickListener itemClickListener;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        public ImageView picassoimg;
        private final String LOG_TAG = getClass().getName().toString();

        public ViewHolder(LinearLayout moviesGrid) {
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

    public MovieGridAdapter(Context context, List<Movie> movieDataset) {
        this.mContext = context;
        if (movieDataset != null) {
            movieAdapterDataset = movieDataset;
        }
    }

    @Override
    public MovieGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grid_item_movies, null, false);
        ViewHolder vh = new ViewHolder((LinearLayout) v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (movieAdapterDataset.get(position) != null) {
            String posterURL = "http://image.tmdb.org/t/p/w185/" + movieAdapterDataset.get(position).poster_path;
            Log.v("In adapter", posterURL);
            Picasso.with(mContext).load(posterURL).into(holder.picassoimg);
        } else
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185//oXUWEc5i3wYyFnL1Ycu8ppxxPvs.jpg").into(holder.picassoimg);
    }

    @Override
    public int getItemCount() {
        return movieAdapterDataset.size();
    }

    public void updateData(List<Movie> movieDataset) {
        movieAdapterDataset.clear();
        movieAdapterDataset = movieDataset;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
    }
}

