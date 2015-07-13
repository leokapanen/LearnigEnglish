package db;

import android.content.ContentValues;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * <p/>
 * Wrapper for ContentValues
 */
public class DBValues {
    private ContentValues contentValues = new ContentValues();

    /**
     * Adds a value to the set.
     *
     * @param key
     * @param value
     * @return
     */
    public void put(String key, Boolean value) {
        if (value != null) {
            contentValues.put(key, value ? 1 : 0);
        } else {
            contentValues.put(key, value);
        }
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * @param value
     * @return
     */
    public void put(String key, Integer value) {
        contentValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * @param value
     * @return
     */
    public void put(String key, Long value) {
        contentValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * @param value
     * @return
     */
    public void put(String key, Double value) {
        contentValues.put(key, value);
    }

    /**
     * Adds a value to the set.
     *
     * @param key
     * @param value
     * @return
     */
    public void put(String key, String value) {
        contentValues.put(key, value);
    }

    /**
     * @return content values for DB
     */
    public ContentValues getContentValues() {
        return contentValues;
    }

}
