package app;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import di.AppModule;


import di.DaggerDepInjectComponent;
import di.DepInjectComponent;
import di.NetworkModule;

public class SencoApplication extends MultiDexApplication {

    public static SencoApplication AppContext;
    public static DepInjectComponent diComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext = (SencoApplication) getApplicationContext();
        init();
    }

    private void init() {
        diComponent = DaggerDepInjectComponent.builder()
                .appModule(new AppModule())
                .networkModule(new NetworkModule())
                .build();
        getComponent().injects(this);
    }


    public static DepInjectComponent getComponent() {
        return diComponent;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

}

