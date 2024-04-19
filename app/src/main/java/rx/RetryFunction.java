package rx;


import android.util.Log;
import android.util.Pair;


import com.fasterxml.jackson.databind.JsonMappingException;

import java.io.IOException;

import javax.inject.Inject;

import app.SencoApplication;
import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import retrofit.CategoryService;
import retrofit2.HttpException;
import utils.SharedPref;


public class RetryFunction implements Function<Observable<Throwable>, Observable<Long>> {

    private static final int MAX_RETRY = 1;
    private static final Long RETRY_STATUS_SKIP = -1L;
    private static final Long RETRY_STATUS_AUTH = -2L;

    @Inject
    CategoryService userService;

    @Inject
    SharedPref sharedPref;

    public RetryFunction() {
        SencoApplication.getComponent().injects(this);
    }

    @Override
    public Observable<Long> apply(Observable<Throwable> throwableObservable) throws Exception {
        return throwableObservable.zipWith(
                Observable.range(1, MAX_RETRY),
                new BiFunction<Throwable, Integer, Pair<Long, Throwable>>() {
                    @Override
                    public Pair<Long, Throwable> apply(Throwable throwable, Integer attempts) throws Exception {
                        Long backoffSec = Math.round(Math.pow(2, attempts) * 1);

                        if (throwable instanceof HttpException) {
                            // exception from server
                            HttpException httpException = (HttpException) throwable;
                            if (attempts >= MAX_RETRY || (
                                    httpException.code() != 401 && httpException.code() != -1099)) {
                                return new Pair<>(RETRY_STATUS_SKIP, throwable);
                            } else if (httpException.code() == 401) {
                                // bad token, refresh user
                                return new Pair<>(RETRY_STATUS_AUTH, throwable);
                            } else {
                                return new Pair<>(backoffSec, throwable);
                            }
                        } else if (throwable instanceof JsonMappingException) {
                            // json parsing error from Jackson
                            return new Pair<>(RETRY_STATUS_SKIP, throwable);
                        } else if (throwable instanceof IOException) {
                            // network timeout or no connection
                            Log.e("", "No Internet Connections");
                            return new Pair<>(backoffSec, throwable);
                        }

                        // everything else will be ignored
                        return new Pair<>(-1L, throwable);
                    }
                })
                .flatMap(secondsThrowablePair -> {
                    Long status = secondsThrowablePair.first;
                    final Throwable throwable = secondsThrowablePair.second;
                 /*   if (status > 0) {
                        return Observable.timer(status, TimeUnit.SECONDS).take(1);

                    } else if (status.equals(RETRY_STATUS_AUTH)) {
                        String handle = sharedPref.getString(PREF_EMAIL);
                        String pwd = sharedPref.getString(PREF_PASSWORD);

                        Observable<Login> tokenObservable = userService.reLogin(handle, pwd,"");

                        if (tokenObservable != null) {
                            return tokenObservable.flatMap(user -> {
                                return Observable.timer(1, TimeUnit.SECONDS).take(1);
                            });
                        }
                    }*/
                    return Observable.error(throwable);
                });

    }
}
