package utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.Primitives;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class HttpUtils {

    private static final Integer TIMEOUT = 30000;

    public synchronized static boolean getNetworkState(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Response getRequest(Context context, String requestURL) {
        if (!getNetworkState(context)) {
            return null;
        }

        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(TIMEOUT, TimeUnit.MILLISECONDS);
        client.setReadTimeout(TIMEOUT, TimeUnit.MILLISECONDS);

        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(requestURL);

        reqBuilder.get();

        Request request = reqBuilder.build();
        Response response = null;

        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            Log.e(HttpUtils.class.getSimpleName(), e.getMessage(), e);
            return null;
        }

        return response;
    }

    /**
     * Wrapper around GSON.
     * Returns ether parsed object or null if error happens
     *
     * @param json
     * @param classOfT
     * @return
     */
    public static <T> T parseJSON(String json, Class<T> classOfT) {
        Object result = null;
        try {
            Gson gson = new GsonBuilder().create();
            result = gson.fromJson(json, classOfT);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return null;
        } finally {
            return Primitives.wrap(classOfT).cast(result);
        }
    }
}
