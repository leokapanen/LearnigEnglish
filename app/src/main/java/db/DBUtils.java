package db;

import android.database.Cursor;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * <p/>
 * Database utilities
 */
public class DBUtils {

    /**
     * @param cursor
     * @param columnName
     * @return text value from DB cursor
     */
    public static String getStringValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (!cursor.isNull(columnIndex)) {
                return cursor.getString(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param column
     * @return text value from DB cursor
     */
    public static String getStringValue(Cursor cursor, IDBColumn column) {
        try {
            int columnIndex = cursor.getColumnIndex(column.name());
            if (!cursor.isNull(columnIndex)) {
                return cursor.getString(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param columnName
     * @return long value from DB cursor
     */
    public static Long getLongValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (!cursor.isNull(columnIndex)) {
                return cursor.getLong(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param column
     * @return long value from DB cursor
     */
    public static Long getLongValue(Cursor cursor, IDBColumn column) {
        try {
            int columnIndex = cursor.getColumnIndex(column.name());
            if (!cursor.isNull(columnIndex)) {
                return cursor.getLong(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param columnName
     * @return integer value from DB cursor
     */
    public static Integer getIntValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (!cursor.isNull(columnIndex)) {
                return cursor.getInt(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param column
     * @return integer value from DB cursor
     */
    public static Integer getIntValue(Cursor cursor, IDBColumn column) {
        try {
            int columnIndex = cursor.getColumnIndex(column.name());
            if (!cursor.isNull(columnIndex)) {
                return cursor.getInt(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param columnName
     * @return double value from DB cursor
     */
    public static Double getDoubleValue(Cursor cursor, String columnName) {
        try {
            int columnIndex = cursor.getColumnIndex(columnName);
            if (!cursor.isNull(columnIndex)) {
                return cursor.getDouble(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param column
     * @return double value from DB cursor
     */
    public static Double getDoubleValue(Cursor cursor, IDBColumn column) {
        try {
            int columnIndex = cursor.getColumnIndex(column.name());
            if (!cursor.isNull(columnIndex)) {
                return cursor.getDouble(columnIndex);
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param columnName
     * @return boolean value from DB cursor
     */
    public static Boolean getBooleanValue(Cursor cursor, String columnName) {
        try {
            Integer result = getIntValue(cursor, columnName);
            if (result != null) {
                return result != 0;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param cursor
     * @param column
     * @return boolean value from DB cursor
     */
    public static Boolean getBooleanValue(Cursor cursor, IDBColumn column) {
        try {
            Integer result = getIntValue(cursor, column);
            if (result != null) {
                return result != 0;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
