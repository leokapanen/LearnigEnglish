package core;

import android.content.Context;

import db.DicDB;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public enum  Runtime {

    INSTANCE;

    private Context appContext = null;

    public void init(Context ctx) {
        appContext = ctx;
    }

    public Context getAppContext() {
        return appContext;
    }
}
