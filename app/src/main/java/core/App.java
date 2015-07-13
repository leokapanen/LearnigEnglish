package core;

import android.app.Application;

import java.util.concurrent.Callable;

/**
 * Created by Leonid Kabanen on 13.07.15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Runtime.INSTANCE.init(getApplicationContext());
    }

}
