package retrofit;


import java.util.List;

import javax.inject.Inject;

import app.SencoApplication;

import datamodel.UserDetailRequest;
import io.reactivex.Observable;
import utils.SharedPref;

public class CategoryService {

    @Inject
    SharedPref sharedPref;

    private final SencoApi sencoApi;

    public CategoryService(SencoApi sencoApi) {
        this.sencoApi = sencoApi;
        SencoApplication.getComponent().injects(this);
    }

}
