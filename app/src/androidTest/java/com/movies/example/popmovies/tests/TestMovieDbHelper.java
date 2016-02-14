package com.movies.example.popmovies.tests;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.movies.example.popmovies.db.MovieContract.MovieTable;
import com.movies.example.popmovies.db.MovieDbHelper;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TestMovieDbHelper extends AndroidTestCase {

    public static final String LOG_TAG = TestMovieDbHelper.class.getSimpleName();

    private SQLiteDatabase db;
    private Cursor c;

    public void setUp() {
        db = new MovieDbHelper(mContext).getWritableDatabase();
    }

    public void testCreateDb() throws Throwable {

        assertEquals(true, db.isOpen());

        // have we created the tables we want?
        c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        Set<String> tables = new HashSet<>(Arrays.asList(
                MovieTable.DB_TABLE_NAME
        ));

        // cursor should contain 5 rows including
        // 3 our tables and 2 technical ones
        while (c.moveToNext()) {
            tables.remove(c.getString(0));
        }

        assertEquals(0, tables.size());
        c.close();
    }
    @Override
    protected void tearDown() throws Exception {
        db.close();
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }
}
