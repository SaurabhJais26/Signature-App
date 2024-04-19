package di;

import android.os.Build;
import android.text.TextUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.SencoApplication;

import java.util.concurrent.TimeUnit;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.CategoryService;
import retrofit.SencoApi;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import utils.Constant;
import utils.SharedPref;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    SencoApplication provideApp() {
        return SencoApplication.AppContext;
    }

    @Provides
    @Singleton
    Cache provideOkHttpCache(SencoApplication application) {
        int cacheSize = 100 * 1024 * 1024; // 100 MiB
        return new Cache(application.getCacheDir(), cacheSize);
    }

    @Provides
    @Singleton
    ObjectMapper provideObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Important to support empty object for ArrayList, as Jackson silenced the exception and hard to trace
        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return mapper;
    }


    @Provides
    Interceptor provideInterceptor(SencoApplication app, SharedPref sharedPref) {
        return chain -> {
            Request original = chain.request();

            Request.Builder requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("Accept-Language", "");

            String token = sharedPref.getString(Constant.PREF_AUTH_TOKEN);
            String authHeader = original.header("Auth");

            if (!TextUtils.isEmpty(token) &&
                    (TextUtils.isEmpty(authHeader) ||
                            !authHeader.equalsIgnoreCase("false"))) {
                requestBuilder.header("", token);
            }

            requestBuilder.header("x-security-header", "8550f3c5-c4da-4137-94e5-ff113fac145f");

            Request request = requestBuilder.build();
            return chain.proceed(request);
        };
    }

    @Provides
    OkHttpClient provideHttpClient(Cache cache, Interceptor interceptor) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .addInterceptor(logging)
                .connectTimeout(900, TimeUnit.SECONDS)
                .writeTimeout(900, TimeUnit.SECONDS)
                .readTimeout(900, TimeUnit.SECONDS);



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN && Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            //added this sleep for old android phones, without this sleep the OkHttpClient.Builder will ignore the connectTimeout
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return builder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, ObjectMapper jacksonMapper) {
        HttpUrl httpUrl = HttpUrl.parse(Constant.BASE_URL);
        return new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create(jacksonMapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(httpUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    CategoryService provideCategoryService(Retrofit retrofit) {
        return new CategoryService(retrofit.create(SencoApi.class));
    }

}
