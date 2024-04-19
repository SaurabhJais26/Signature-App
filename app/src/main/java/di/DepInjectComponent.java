package di;

import com.example.sencosignapp.ui.BaseActivity;

import app.SencoApplication;


import javax.inject.Singleton;
import dagger.Component;
import retrofit.CategoryService;
import rx.RetryFunction;


@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface DepInjectComponent {
    void injects(RetryFunction retryFunction);
    void injects(CategoryService categoryService);
    void injects(SencoApplication application);
    void injects(BaseActivity baseActivity);
}
