package com.movies.example.popmovies;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;

import com.movies.example.popmovies.db.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 11/02/16.
 */
public class FavouriteAdapter extends CursorAdapter {

    private Context mContext;

    public FavouriteAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_item_movies, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView posterImg = (ImageView) view.findViewById(R.id.posterImgView);
        String posterPath = cursor.getString(cursor.getColumnIndex(
                MovieContract.MovieTable.DB_POSTER_PATH));
        Picasso.with(mContext).load(posterPath).into(posterImg);

    }
}
