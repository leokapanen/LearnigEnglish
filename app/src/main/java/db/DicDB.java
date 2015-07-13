package db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import core.Runtime;
import datamodel.DicRecord;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * <p/>
 * Dictionary records DB
 */
public enum DicDB {

    INSTANCE;

    static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "dictionary.db";
    private List<DBTable> tables;

    private BaseDBOpener dbOpenHelper;

    DicDB() {
        tables = new ArrayList<>();
        tables.add(new DBTable(DicColumn.values(), "dictionary"));
        dbOpenHelper = new BaseDBOpener(
                Runtime.INSTANCE.getAppContext(),
                DATABASE_NAME,
                DATABASE_VERSION,
                tables
        );
    }

    // Table

    enum DicColumn implements IDBColumn {
        id("INTEGER"),
        sentence("TEXT"),
        translation("TEXT"),
        studied("INTEGER");

        private String type;

        DicColumn(String type) {
            this.type = type;
        }

        public String type() {
            return type;
        }

    }

    /**
     * Inserts records into DB
     *
     * @return
     */
    public void insert(List<DicRecord> bgsList) {
        if (bgsList == null) {
            return;
        }

        for (DicRecord record : bgsList) {
            insert(record);
        }
    }

    /**
     * Inserts record into DB
     *
     * @return
     */
    public synchronized boolean insert(DicRecord record) {
        String tableName = tables.get(0).getTableName();

        DBValues dbValues = new DBValues();
        dbValues.put(DicColumn.id.name(), record.getId());
        dbValues.put(DicColumn.sentence.name(), record.getSentence());
        dbValues.put(DicColumn.translation.name(), record.getTranslation());
        dbValues.put(DicColumn.studied.name(), (record.isStudied() == true) ? 1 : 0);

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long result = db.insert(tableName, null, dbValues.getContentValues());

        return true;//(result != 0) ? true : false;
    }

    /**
     * Updates record into DB
     *
     * @return
     */
    public boolean update(DicRecord record) {
        String tableName = tables.get(0).getTableName();

        DBValues dbValues = new DBValues();
        dbValues.put(DicColumn.id.name(), record.getId());
        dbValues.put(DicColumn.sentence.name(), record.getSentence());
        dbValues.put(DicColumn.translation.name(), record.getTranslation());
        dbValues.put(DicColumn.studied.name(), (record.isStudied() == true) ? 1 : 0);

        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        long result = db.update(
                tableName,
                dbValues.getContentValues(),
                DicColumn.id.name() + "=?",
                new String[]{Integer.toString(record.getId())}
        );

        return (result != 0) ? true : false;
    }

    /**
     * Gets all records from DB
     *
     * @return list of dictionary records
     */
    public List<DicRecord> getAll() {
        String tableName = tables.get(0).getTableName();
        Cursor cursor = dbOpenHelper.getReadableDatabase().query(
                tableName,
                tables.get(0).getColumnsNames(),
                null,
                null,
                null,
                null,
                null
        );

        return getDicRecords(cursor);
    }

    public int getStudiedCount() {
        Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery(
                "SELECT COUNT(*) FROM " + tables.get(0).getTableName() +
                        " WHERE " + DicColumn.studied + "=1",
                null
        );

        return rawQuery(
                "SELECT * FROM " + tables.get(0).getTableName() + " WHERE " + DicColumn.studied + "=1",
                null
        ).size();
    }

    public List<DicRecord> search(String searchString) {
        return rawQuery("SELECT * FROM " + tables.get(0).getTableName() +
                " WHERE " + DicColumn.sentence + " LIKE " + "'%" + searchString + "%'", null);
    }

    private List<DicRecord> rawQuery(String rawQuery, String[] selectionArgs) {
        Cursor cursor = dbOpenHelper.getReadableDatabase().rawQuery(rawQuery, selectionArgs);

        return getDicRecords(cursor);
    }

    private List<DicRecord> getDicRecords(Cursor cursor) {
        List<DicRecord> records = new ArrayList<>();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                DicRecord record = getRecordItemFromCursor(cursor);
                if (record != null) {
                    records.add(record);
                }
                cursor.moveToNext();
            }

        } finally {
            cursor.close();
        }
        return records;
    }

    private DicRecord getRecordItemFromCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }

        DicRecord record = new DicRecord();
        record.setId(DBUtils.getIntValue(cursor, DicColumn.id));
        record.setSentence(DBUtils.getStringValue(cursor, DicColumn.sentence));
        record.setTranslation(DBUtils.getStringValue(cursor, DicColumn.translation));
        record.setStudied(DBUtils.getBooleanValue(cursor, DicColumn.studied));

        return record;
    }

}
