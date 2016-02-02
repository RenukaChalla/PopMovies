package com.movies.example.popmovies;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.movies.example.popmovies.model.response.Movie;
import com.movies.example.popmovies.model.response.ReviewDetails;
import com.movies.example.popmovies.model.response.Reviews;
import com.movies.example.popmovies.model.response.TrailerDetails;
import com.movies.example.popmovies.model.response.Trailers;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by admin on 28/01/16.
 */

public class TrailerNReviewAdapter extends RecyclerView.Adapter<TrailerNReviewAdapter.ViewHolder> {

    private List<TrailerDetails> trailerDataset;
    private List<ReviewDetails> reviewsDataset;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case
        public TextView heading;
        public ImageView playbtn;
        private final String LOG_TAG = getClass().getName().toString();

        public ViewHolder(View detailsLL) {
            super(detailsLL);
            Log.v("Ina adapter", "viewholder");
            playbtn = (ImageView) detailsLL.findViewById(R.id.movie_details_trailer_imgbtn);
            heading = (TextView) detailsLL.findViewById(R.id.movie_details_trailer_textview);
            Log.i(LOG_TAG, "Adding Listener");
        }

    }
    public TrailerNReviewAdapter(List<TrailerDetails> movieDataset) {
        Log.v("Ina adapter", "constructor");
        if (movieDataset != null) {
            this.trailerDataset = movieDataset;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public TrailerNReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        // create a new view
        Log.v("Ina adapter", "oncreate");
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view, null, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.v("Ina adapter", "bind");
        if(trailerDataset.get(position) != null){
            holder.heading.setText(trailerDataset.get(position).name.toString());
        }
        Log.v("In adapter", trailerDataset.get(position).name.toString());
    }
    @Override
    public int getItemCount() {
        Log.v("Ina adapter", "getitemcount");
        Log.v("Ina adapter", "getitemcount"+ trailerDataset.size());
        return trailerDataset.size();
    }

    public void updateData(List<TrailerDetails> trailerDataset) {
        Log.v("Ina adapter", "updatedata");
        this.trailerDataset.clear();
        this.trailerDataset = trailerDataset;
        notifyDataSetChanged();
    }
}

