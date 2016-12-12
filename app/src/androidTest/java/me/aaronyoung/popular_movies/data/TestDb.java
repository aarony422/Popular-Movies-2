package me.aaronyoung.popular_movies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by shangweiyoung on 12/8/16.
 */

@RunWith(AndroidJUnit4.class)
public class TestDb {
    public static final String LOG_TAG = TestDb.class.getSimpleName();
    private Context appContext = InstrumentationRegistry.getTargetContext();

    // deletes database
    private void deleteTheDatabase() {
        appContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    // Gets called before test is run
    public void setUp() {
        deleteTheDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable {
        // build a hashset of all table names we wish to test
        final HashSet<String> tableNameSet = new HashSet<String>();
        tableNameSet.add(MovieContract.MovieEntry.TABLE_NAME);
        tableNameSet.add(MovieContract.TrailerEntry.TABLE_NAME);
        tableNameSet.add(MovieContract.ReviewEntry.TABLE_NAME);

        appContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);

        SQLiteDatabase db = new MovieDbHelper(appContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // test whether tables were created correctly
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameSet.remove(c.getString(0));
        } while (c.moveToNext());

        // if this fails, it means that the database doesn't contain all databases
        assertTrue("Error: Your database was created without all 3 databases", tableNameSet.isEmpty());

        // do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieEntry.TABLE_NAME + ")", null);

        assertTrue("Error: This means that we were unable to query the database for table information", c.moveToFirst());

        // Build Hashset of all column names we want to look for
        final HashSet<String> movieColumnSet = new HashSet<>();
        movieColumnSet.add(MovieContract.MovieEntry._ID);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_ID);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_TITLE);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_VOTE_AVG);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_POPULAR);
        movieColumnSet.add(MovieContract.MovieEntry.COLUMN_TOP_RATED);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            movieColumnSet.remove(columnName);
        } while (c.moveToNext());

        // if this fails, it means that your database doesn't contain all required columns
        assertTrue("Error: The database doesn't contain all required movie entry columns", movieColumnSet.isEmpty());
        db.close();
    }

    @Test
    public void testMovieTable() {
        // get reference to writable database
        MovieDbHelper dbHelper = new MovieDbHelper(appContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create ContentValues you want to insert
        ContentValues testValues = TestUtilities.createMovieTestValues();

        long movieRowId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);

        // verify we got a row back
        assertTrue(movieRowId != -1);

        // test that the correct values were inserted
        Cursor cursor = db.query(
                MovieContract.MovieEntry.TABLE_NAME, // table name
                null, // all columns
                null, // columns for where clause
                null, // values for where clause
                null, // columns to group by
                null, // columns to filter by row group
                null // sort order
        );

        assertTrue("Error: No records returned from movie query!", cursor.moveToFirst());

        // check returned values are correct
        TestUtilities.validateCurrentRecord("Error: movie query validation failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in db
        assertFalse("Error: More than one record returned from movie query", cursor.moveToNext());

        // free resources
        cursor.close();
        db.close();
    }
}
