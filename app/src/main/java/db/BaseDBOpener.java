package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class BaseDBOpener extends SQLiteOpenHelper {
    private static int dbVersion;
    private static String dbName;
    private List<DBTable> tables;

    public BaseDBOpener(Context context, String dbName, int dbVersion, List<DBTable> tables) {
        super(context, dbName, null, dbVersion);
        this.dbVersion = dbVersion;
        this.dbName = dbName;
        this.tables = tables;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(BaseDBOpener.class.getSimpleName(), "Upgrading from version " + oldVersion + " to " + newVersion);
        db.setVersion(newVersion);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.setVersion(dbVersion);
        for (DBTable table : tables) {
            table.drop(db);
            table.create(db);
        }
    }

}
