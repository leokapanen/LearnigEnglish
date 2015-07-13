package leokapanen.learningenglish;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import datamodel.DicRecord;
import db.DicDB;
import utils.HttpUtils;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class DataLoader extends AsyncTaskLoader<List<DicRecord>> {

    public DataLoader(Context context) {
        super(context);
    }

    @Override
    public List<DicRecord> loadInBackground() {
        List<DicRecord> records = DicDB.INSTANCE.getAll();

        if (records.size() == 0) {
            try {
                Response response = HttpUtils.getRequest(getContext(), Conf.BASE_URL + Conf.GET_DICTIONARY);
                if (response != null) {
                    switch (response.code()) {
                        case 200:
                            String fullJSON = response.body().string();
                            DicRecord[] recordsArray = HttpUtils.parseJSON(
                                    (new JSONObject(fullJSON)).getJSONArray("dictionaryItems").toString(),
                                    DicRecord[].class
                            );
                            records.addAll(Arrays.asList(recordsArray));

                            DicDB.INSTANCE.insert(records);

                            break;
                    }
                }
            } catch (Exception e) {
                Log.e(DataLoader.class.getSimpleName(), e.getMessage(), e);
            }
        }

        return records;
    }
}
