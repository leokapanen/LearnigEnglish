package leokapanen.learningenglish;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;

import java.util.List;

import datamodel.DicRecord;
import db.DicDB;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class SearchLoader extends AsyncTaskLoader<List<DicRecord>> {

    String searchString;

    public SearchLoader(Context context, Bundle bundle) {
        super(context);
        searchString = bundle.getString(Conf.KEY_SEARCH_STRING);
    }

    @Override
    public List<DicRecord> loadInBackground() {
        List<DicRecord> records = DicDB.INSTANCE.search(searchString);
        return records;
    }
}
