package com.movies.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.movies.example.popmovies.model.response.Movie;

import org.json.JSONException;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {
    private int position;
    String moviejson;
    private RecyclerView movieDetailsRecyclerView;
    private RecyclerView.Adapter movieDetailsAdapter;
    private RecyclerView.LayoutManager movieDetailsLayoutManager;
    List<Movie> movieDetailsDataset;
    public MovieDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootview = (ViewGroup) inflater.inflate(R.layout.fragment_movie_details, container, false);
        //ImageView movieImg = (ImageView) rootview.findViewById(R.id.posterImgView);
        movieDetailsRecyclerView = (RecyclerView) rootview.findViewById(R.id.movie_details_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        movieDetailsRecyclerView.setHasFixedSize(true);
        movieDetailsLayoutManager = new GridLayoutManager(getContext(), 2);
        movieDetailsRecyclerView.setLayoutManager(movieDetailsLayoutManager);
        movieDetailsDataset = new List<Movie>() {
            @Override
            public void add(int location, Movie object) {

            }

            @Override
            public boolean add(Movie object) {
                return false;
            }

            @Override
            public boolean addAll(int location, Collection<? extends Movie> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends Movie> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object object) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Movie get(int location) {
                return null;
            }

            @Override
            public int indexOf(Object object) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<Movie> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object object) {
                return 0;
            }

            @Override
            public ListIterator<Movie> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<Movie> listIterator(int location) {
                return null;
            }

            @Override
            public Movie remove(int location) {
                return null;
            }

            @Override
            public boolean remove(Object object) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public Movie set(int location, Movie object) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<Movie> subList(int start, int end) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] array) {
                return null;
            }
        };
        Intent intent = getActivity().getIntent();
        if(intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            position = intent.getIntExtra(Intent.EXTRA_TEXT, 0);
            moviejson = intent.getStringExtra(Intent.EXTRA_REFERRER);
        }
        try {
            movieDetailsDataset = MainActivityFragment.getMovieDataFromJSON(moviejson);
        } catch (JSONException e) {
            Log.e("getMovieDataFromJSON", moviejson);
        }
        movieDetailsAdapter = new MovieDetailsAdapter(movieDetailsDataset, position);
        movieDetailsRecyclerView.setAdapter(movieDetailsAdapter);
        ((MovieDetailsAdapter)movieDetailsAdapter).updateData(movieDetailsDataset, position);
        return rootview;
    }


}
