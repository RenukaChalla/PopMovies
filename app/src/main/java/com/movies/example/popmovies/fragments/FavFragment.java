package com.movies.example.popmovies.fragments;


import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.google.gson.Gson;
import com.movies.example.popmovies.R;
import com.movies.example.popmovies.adapters.FavouriteAdapter;
import com.movies.example.popmovies.db.MovieContract;
import com.movies.example.popmovies.models.Movie;
import com.movies.example.popmovies.utils.FavDbUtils;

import java.io.FileDescriptor;
import java.io.PrintWriter;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private GridView gridView;
    private final String LOG_TAG = getClass().getName();
    private FavouriteAdapter favouriteAdapter;

    public FavFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(getClass().getName(), "onCreateView");
        View rootview = inflater.inflate(R.layout.fragment_fav, container, false);
        gridView = (GridView) rootview.findViewById(R.id.fav_gridview);
        showFavourites();
        return rootview;
    }

    public void onResume() {
        super.onResume();
        showFavourites();
    }

    private void showFavourites() {
            Cursor cursor = FavDbUtils.getFavFromdb(getActivity());
            favouriteAdapter = new FavouriteAdapter(getActivity(), cursor, 0);
            gridView.setNumColumns(2);
            gridView.setAdapter(favouriteAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    gridView.setBackgroundColor(getResources().getColor(R.color.black));
                    view.setActivated(true);
                    Log.v(LOG_TAG, "Clicked on position : " + position);
                    Movie movie = Movie.buildDetailObject(
                            (Cursor) ((FavouriteAdapter) parent.getAdapter()).getItem(position));
                    ((Callback) getActivity()).onItemSelected(new Gson().toJson(movie));
                }
            });
    }
    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(),
                MovieContract.MovieTable.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        favouriteAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        favouriteAdapter.swapCursor(null);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(String movie);
    }
}
