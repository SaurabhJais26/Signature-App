package com.example.sencosignapp.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sencosignapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Currency;
import java.util.Date;

import javax.inject.Inject;

import app.SencoApplication;
import busevent.BusEventDefault;
import busevent.InvoiceBusEvent;
import busevent.InvoiceDetailBusEvent;
import busevent.LoginBusEvent;
import busevent.SignatureBusEvent;
import busevent.VersionBusEvent;
import datamodel.CustomerInvoiceDataModel;
import datamodel.CustomerInvoiceRequest;
import datamodel.InvoiceDataModel;
import datamodel.InvoiceDetailRequest;
import datamodel.LoginDataModel;
import datamodel.SignatureDataModel;
import datamodel.SignatureDetailRequest;
import datamodel.UserDetailRequest;
import datamodel.VersionDataModel;
import datamodel.VersionDetailRequest;
import db.DatabaseConnection;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit.SencoApi;
import retrofit.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Constant;
import utils.ProgressWheel;
import utils.SharedPref;
import utils.Util;

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BASEACTIVITY";
    public DatabaseConnection databaseConnection;
    @Inject
    protected SharedPref sharedPref;

    public String baseUrl;

    private View progressLayout;
    private ProgressWheel progresswheel;
    protected LayoutInflater inflator = null;

    public SencoApi sencoApi;

    public LayoutInflater getInflator() {
        return inflator;
    }


    protected View inflateView(int layoutId) {
        ViewGroup v = (ViewGroup) findViewById(R.id.content);
        View view = getInflator().inflate(layoutId, v);
        return view;
    }

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SencoApplication.getComponent().injects(this);
        inflator = LayoutInflater.from(this);
        setContentView(R.layout.activity_main);
        databaseConnection = DatabaseConnection.getInstance(this);
        progressLayout = (View) findViewById(R.id.progressLayout);
        progresswheel = (ProgressWheel) findViewById(R.id.progresswheel);
        baseUrl = sharedPref.getString("baseUrl");
        if (baseUrl.length() > 0) {
            Constant.BASE_URL = baseUrl;
        }
        if (Constant.BASE_URL.length() > 0) {
            sencoApi = ServiceGenerator.createService(SencoApi.class, "", "");
        }
        create(savedInstanceState);
    }

    protected abstract void create(Bundle bundle);

    @Override
    public void onBackPressed() {
        if (BaseActivity.this instanceof HomeActivity) {
            Util.showAlert(this, "Are you sure want to exit the application", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == DialogInterface.BUTTON_POSITIVE){
                        finishAffinity();
                    }
                    else if (which == DialogInterface.BUTTON_NEGATIVE){
                        dialog.dismiss();
                    }
                }
            });

        } else {
            Util.pushNext(this, HomeActivity.class);
        }
    }

    public void apicall() {
    }

    public void executeLoginApi(String userID, String password, String storeID) {

        UserDetailRequest userDetailRequest = new UserDetailRequest();
        userDetailRequest.setUserid(userID);
        userDetailRequest.setPassword(password);
        userDetailRequest.setStore(storeID);

        Call<LoginDataModel> loginDataModelCall = sencoApi.userValidate(userDetailRequest);

        loginDataModelCall.enqueue(new Callback<LoginDataModel>() {
            boolean isSuccess;
            int active = 0;

            @Override
            public void onResponse(Call<LoginDataModel> call, Response<LoginDataModel> response) {
                if (response.code() == 200) {
                    Log.d("BASE-URL", "LoginApi: ==============>url - "
                            + call.request().url() + " ===============>" + bodyToString(call.request().body()));
                    if (response.body().getActive()) {
                        active = 1;
                    } else {
                        active = 0;
                    }
                    isSuccess = true;
                } else {
                    isSuccess = false;
                }

                if (isSuccess) {
                    databaseConnection.deleteTable("UserDetail");
                    sharedPref.save("userid", response.body().getUserid());
                    sharedPref.save("terminal", response.body().getTerminal());
                    databaseConnection.insertUserDetails(response.body().getUserid(), response.body().getTerminal(), active, response.body().getStatus());
                    EventBus.getDefault().post(new LoginBusEvent("YES", active, response.body().getStatus()));
                } else {
                    EventBus.getDefault().post(new LoginBusEvent("NO", active, response.body().getStatus()));
                }
            }

            @Override
            public void onFailure(Call<LoginDataModel> call, Throwable t) {
                isSuccess = false;
                EventBus.getDefault().post(new LoginBusEvent("NO", active, "User Not Found"));
            }

        });
    }

    public void executeInvoiceApi(String userId) {
        InvoiceDetailRequest invoiceDetailRequest = new InvoiceDetailRequest();
        invoiceDetailRequest.setUserid(userId);
        Call<InvoiceDataModel> invoiceDataModelCall = sencoApi.getInvoiceId(invoiceDetailRequest);


        invoiceDataModelCall.enqueue(new Callback<InvoiceDataModel>() {
            boolean isSuccess;

            @Override
            public void onResponse(Call<InvoiceDataModel> call, Response<InvoiceDataModel> response) {
                if (response.code() == 200) {
                    isSuccess = true;
                    Log.d("BASE-URL", "InvoiceApi: ==============>url - "
                            + call.request().url() + " ===============>" + bodyToString(call.request().body()));
                } else {
                    isSuccess = false;
                }
                if (isSuccess) {
                        databaseConnection.deleteTable("InvoiceDetail");
                        databaseConnection.insertInvoiceDetails(response.body());
                        EventBus.getDefault().post(new InvoiceBusEvent("YES"));
                        //busevent fire YES
                } else {
                    EventBus.getDefault().post(new InvoiceBusEvent("NO"));
                    //busevent fire NO

                }
            }

            @Override
            public void onFailure(Call<InvoiceDataModel> call, Throwable t) {
                isSuccess = false;
                EventBus.getDefault().post(new InvoiceBusEvent("NO"));
                //busevent fire NO

            }
        });

    }

    public void executeInvoiceDetailApi(String invoiceId, String custAcct) {
        CustomerInvoiceRequest customerInvoiceRequest = new CustomerInvoiceRequest();
        customerInvoiceRequest.setInvoiceid(invoiceId);
        customerInvoiceRequest.setCustaccount(custAcct);

        Call<CustomerInvoiceDataModel> customerInvoiceDataModelCall = sencoApi.getInvoiceDetails(customerInvoiceRequest);

        customerInvoiceDataModelCall.enqueue(new Callback<CustomerInvoiceDataModel>() {
            boolean isSuccess;

            @Override
            public void onResponse(Call<CustomerInvoiceDataModel> call, Response<CustomerInvoiceDataModel> response) {
                if (response.code() == 200) {
                    isSuccess = true;
                    Log.d("BASE-URL", "InvoiceDetailApi: ==============>url - "
                            + call.request().url() + " ===============>" + bodyToString(call.request().body()));
                } else {
                    isSuccess = false;
                }
                if (isSuccess) {
                    databaseConnection.deleteTable("CustomerInvoiceDetail");
                    databaseConnection.insertCustomerInvoiceDetails(response.body());
                    EventBus.getDefault().post(new InvoiceDetailBusEvent("YES"));
                    //busevent fire YES

                } else {
                    EventBus.getDefault().post(new InvoiceDetailBusEvent("NO"));
                    //busevent fire NO

                }
            }

            @Override
            public void onFailure(Call<CustomerInvoiceDataModel> call, Throwable t) {
                isSuccess = false;
                EventBus.getDefault().post(new InvoiceDetailBusEvent("NO"));
                //busevent fire NO

            }
        });

    }

    public void postSignatureDetailApi() {
        Cursor cursor = databaseConnection.getCustomerSignatureDetail();
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String id = cursor.getString(cursor.getColumnIndexOrThrow("id"));
                String invoiceId = cursor.getString(cursor.getColumnIndexOrThrow("invoiceID"));
                String custAcct = cursor.getString(cursor.getColumnIndexOrThrow("custAcct"));
                String signature = "";
                if (cursor.getBlob(2) != null) {
                    signature = Base64.encodeToString(cursor.getBlob(2), Base64.DEFAULT);
                }
                HomeActivity.SIGNATUREID = id;
                SignatureDetailRequest signatureDetailRequest = new SignatureDetailRequest();
                signatureDetailRequest.setInvoiceId(invoiceId);
                signatureDetailRequest.setCustAccount(custAcct);
                signatureDetailRequest.setSignature(signature);
                Call<SignatureDataModel> signatureDataModelCall = sencoApi.postSignatureDetails(signatureDetailRequest);

                signatureDataModelCall.enqueue(new Callback<SignatureDataModel>() {
                    boolean isSuccess;

                    @Override
                    public void onResponse(Call<SignatureDataModel> call, Response<SignatureDataModel> response) {
                        if (response.code() == 200) {
                            isSuccess = true;

                        } else {
                            isSuccess = false;
                        }
                        if (isSuccess) {
                            EventBus.getDefault().post(new SignatureBusEvent("YES"));
                        } else {
                            EventBus.getDefault().post(new SignatureBusEvent("NO"));
                        }
                    }

                    @Override
                    public void onFailure(Call<SignatureDataModel> call, Throwable t) {
                        EventBus.getDefault().post(new SignatureBusEvent("NO"));
                    }
                });
            }
        }


    }

    public void executeVersionDetailApi() {
        VersionDetailRequest versionDetailRequest = new VersionDetailRequest();
        versionDetailRequest.setType("Android");

        Call<VersionDataModel> versionDataModelCall = sencoApi.getVersionDetails(versionDetailRequest);

        versionDataModelCall.enqueue(new Callback<VersionDataModel>() {

            int isSuccess;

            @Override
            public void onResponse(Call<VersionDataModel> call, Response<VersionDataModel> response) {

                if (response.code() == 200) {
                    isSuccess = 1;
                    
                } else if (response.code() == 404) {
                    isSuccess = 2;
                } else {
                    isSuccess = 3;
                }
                if (isSuccess == 1) {
                    EventBus.getDefault().post(new VersionBusEvent("YES", response.body().getVersion()));
                } else if (isSuccess == 2) {
                    EventBus.getDefault().post(new VersionBusEvent("INVALID", ""));
                } else {
                    EventBus.getDefault().post(new VersionBusEvent("NO", ""));
                }
            }

            @Override
            public void onFailure(Call<VersionDataModel> call, Throwable t) {
                EventBus.getDefault().post(new VersionBusEvent("NO", ""));
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onEvent(BusEventDefault event) {

    }

    public void showProgress() {
        progressLayout.setVisibility(View.VISIBLE);
        progresswheel.spin();
        progressLayout.setEnabled(true);
        progressLayout.setClickable(true);
    }

    public void hideProgress() {
        progressLayout.setVisibility(View.GONE);
        progresswheel.stopSpinning();
        progressLayout.setEnabled(false);
        progressLayout.setClickable(false);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

}
