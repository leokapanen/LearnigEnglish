package db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Leonid Kabanen on 13.07.15.
 * <p/>
 * SQLite database table
 */
public class DBTable {
    protected IDBColumn[] columns;
    protected String[] columnsNames;
    protected String tableName;


    public DBTable(IDBColumn[] columns, String tableName) {
        this.columns = columns;
        this.tableName = tableName;
        this.columnsNames = columns();
    }

    /**
     * Create table into db
     *
     * @param db
     */
    public void create(SQLiteDatabase db) {
        StringBuffer sb = new StringBuffer(200).append("CREATE TABLE ").append(tableName).append(" (");
        int counter = 0;
        for (IDBColumn col : columns) {
            sb.append(col.name()).append(" ").append(col.type());
            if (++counter < columns.length) {
                sb.append(", ");
            }
        }
        sb.append(")");
        db.execSQL(sb.toString());
    }


    /**
     * Filling columns columns names array
     *
     * @return
     */
    protected String[] columns() {
        String[] allCols = new String[columns.length];
        for (int i = 0; i < allCols.length; i++) {
            allCols[i] = columns[i].name();
        }
        return allCols;
    }

    /**
     * Remove table from db
     *
     * @param db
     */
    public void drop(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     * @return names of all columns into current table
     */
    public String[] getColumnsNames() {
        return columnsNames;
    }

    /**
     * @return current table name
     */
    public String getTableName() {
        return tableName;
    }

}

