package di;

import android.content.Context;
import android.content.SharedPreferences;

import app.SencoApplication;


import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import utils.Constant;
import utils.SharedPref;

@Module
public class AppModule {
    @Provides
    @Singleton
    SharedPref providesSharedPref() {
        SharedPreferences sharedpref = SencoApplication.AppContext
                .getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new SharedPref(sharedpref);
    }
}
