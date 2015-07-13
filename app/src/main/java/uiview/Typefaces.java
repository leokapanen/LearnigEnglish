package uiview;

import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by Leonid Kabanen on 12.07.15.
 */
public enum Typefaces {

    INSTANCE;

    private final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

    /**
     * @param ctx
     * @param path
     * @return typeface by font path
     */
    public Typeface get(Context ctx, String path) {
        synchronized (cache) {
            if (!cache.containsKey(path)) {
                Typeface t = Typeface.createFromAsset(ctx.getAssets(), path);
                cache.put(path, t);
            }

            return cache.get(path);
        }
    }

}
