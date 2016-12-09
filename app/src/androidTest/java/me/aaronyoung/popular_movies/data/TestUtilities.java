package me.aaronyoung.popular_movies.data;

import android.content.ContentValues;
import android.database.Cursor;

import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

/**
 * Created by shangweiyoung on 12/8/16.
 */

public class TestUtilities {
    public static ContentValues createMovieTestValues() {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, "1234");
        testValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "I love Ruben");
        testValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Ruben buys me dinner");
        testValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, "NOW");
        testValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, "1");
        testValues.put(MovieContract.MovieEntry.COLUMN_POPULAR, "1");
        testValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, "here/and/now");
        testValues.put(MovieContract.MovieEntry.COLUMN_TOP_RATED, "0");
        testValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVG, "99.9");

        return testValues;
    }

    public static void validateCurrentRecord(String error, Cursor cursor, ContentValues expectedValues) {
        Set<Map.Entry<String,Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = cursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not match the expected value '" +
                            expectedValue + "'. " + error, expectedValue, cursor.getString(idx));
        }
    }
}
