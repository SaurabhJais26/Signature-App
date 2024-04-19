package com.example.sencosignapp.ui;

import static utils.Constant.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sencosignapp.R;

import org.greenrobot.eventbus.Subscribe;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import busevent.VersionBusEvent;
import retrofit.SencoApi;
import retrofit.ServiceGenerator;
import retrofit2.Retrofit;
import utils.Constant;
import utils.SharedPref;
import utils.Util;

public class SplashActivity extends BaseActivity  implements View.OnClickListener {

    private EditText ip;
    private Button submit;
   private  String inputText;

   private String MAINURL = "";


   private View ipAddressMainView;
   private ImageView sencoIcon;



    @Override
    protected void create(Bundle bundle) {
        initData();
    }

    private void initData() {
        inflateView(R.layout.activity_splash);
        ip = (EditText) findViewById(R.id.editText);
        submit = (Button) findViewById(R.id.btn_submit);
        sencoIcon = (ImageView)findViewById(R.id.imageView);
        ipAddressMainView = (View)findViewById(R.id.ipAddressMainView);
        submit.setOnClickListener(this::onClick);
        checkBaseURL();

    }

    private boolean validate() {
        boolean flag = true;
        if (ip.getText().toString().isEmpty()) {
            flag = false;
            Util.showToast(this, "Please enter the Ip Address");
        }
        return flag;
    }

    public static boolean isValidIPAddress(String ip)
    {
        String IP_ADDRESS_PATTERN =
                "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                        "([01]?\\d\\d?|2[0-4]\\d|25[0-5]):" +
                        "(\\d{1,5})$";
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        // Compile the ReGex
        Pattern p = Pattern.compile(IP_ADDRESS_PATTERN);


        if (ip == null) {
            return false;
        }


        Matcher m = p.matcher(ip);

        return m.matches();
    }

    private void submitBtnClick() {
        Util.hideKeyBoard(SplashActivity.this);
        if (Util.isNetworkAvailable(SplashActivity.this)) {
            if (validate()) {
                inputText = ip.getText().toString();
                if (isValidIPAddress(inputText)){
                    MAINURL = "http://"+inputText+"/api/";
                    Constant.BASE_URL =MAINURL ;
                    sencoApi = ServiceGenerator.createService(SencoApi.class, "", "");
                    showProgress();
                        executeVersionDetailApi();
                }else {
                    ip.setText("");
                    Util.showOKAlert(SplashActivity.this, "Invalid IP address");
                }
            }
        } else {
            Util.showOKAlert(SplashActivity.this, "Please check your internet connection");
        }
    }

    public String getCurrentVersion (Context context){
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                String versionname = packageInfo.versionName;
                return versionname;

            } catch (PackageManager.NameNotFoundException e) {
                // should never happen
                throw new RuntimeException("Could not get package name: " + e);
            }
        }
        @Subscribe
        public void onEvent(VersionBusEvent versionBusEvent){
             hideProgress();
            if (versionBusEvent.getStatus().equalsIgnoreCase("YES")) {
                if (MAINURL.length()>0){
                    sharedPref.save("baseUrl", MAINURL);

                }
                if (getCurrentVersion(SplashActivity.this).equalsIgnoreCase(versionBusEvent.getVersionName())) {
                    Intent iNext = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(iNext);
                    finish();
                } else {
                    Util.showAlert(SplashActivity.this, "Please update to the latest version", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final String appPackageName = getPackageName();
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                    });
                }
            }else if(versionBusEvent.getStatus().equalsIgnoreCase("INVALID")) {
               Util.showOKAlert(SplashActivity.this,"INVALID IP ADDRESS");
            }else {
                Util.showOKAlert(SplashActivity.this,"Something went wrong Please try again later");
            }
        }


            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.btn_submit:{
                        submitBtnClick();
                    break;
                }
            }
        }
    public void checkBaseURL() {
        if(baseUrl.length()>0){
            Constant.BASE_URL = baseUrl;
            if (Util.isNetworkAvailable(SplashActivity.this)){
                showProgress();
                executeVersionDetailApi();
            }
        }else {
            ipAddressMainView.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        // Do nothing on back button navigation
    }
}
