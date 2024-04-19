package com.example.sencosignapp.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sencosignapp.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import busevent.LoginBusEvent;
import db.DatabaseConnection;
import utils.ProgressWheel;
import utils.SharedPref;
import utils.Util;

public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private View progressLayout;
    private ProgressWheel progresswheel;

    private EditText userid,password,store;
    private Button login;


    @Override
    protected void create(Bundle bundle) {
        databaseConnection = new DatabaseConnection(this);
        initData();

    }
    private void initData(){
        inflateView(R.layout.login_portrait);
        store = (EditText) findViewById(R.id.store);
        userid = (EditText) findViewById(R.id.userid);
        password = (EditText) findViewById(R.id.password);
        progressLayout = (View) findViewById(R.id.progressLayout);
        progresswheel = (ProgressWheel) findViewById(R.id.progresswheel);
        Button loginBtn = (Button) findViewById(R.id.btn_signIn);
        loginBtn.setOnClickListener(this::onClick);
//        store.setText("Store");
//        userid.setText("Sighpad1");
//        password.setText("123456");
    }

    private boolean validate() {
        boolean flag = true;
        if (store.getText().toString().isEmpty()){
            flag = false;
            Util.showToast(this, "Please enter the store");
        }
        else if(userid.getText().toString().isEmpty()){
            flag = false;
            Util.showToast(this, "Please enter the userid");
        } else if (password.getText().toString().isEmpty()) {
            flag = false;
            Util.showToast(this,"Please enter the password");
        }
        return flag;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_signIn:{
                loginBtnCLick();
            }
            break;
        }
    }
    private void loginBtnCLick(){
            Util.hideKeyBoard(LoginActivity.this);
            if (Util.isNetworkAvailable(this)){
                if (validate()){
                    showProgress();
                    executeLoginApi(userid.getText().toString(),password.getText().toString(),"");
                }
            }else {
                Util.showOKAlert(this,"Please check your internet connection and try again later");
            }

    }

    @Subscribe
    public void onEvent(LoginBusEvent loginBusEvent){
        hideProgress();

        if (loginBusEvent.getStrEvent() == "YES"){
            if(loginBusEvent.getActive()==1&&loginBusEvent.getStatus().equalsIgnoreCase("User Found")){
                Util.pushNext(this,HomeActivity.class);
            }else
            {
                Toast.makeText(this,loginBusEvent.getStatus(),Toast.LENGTH_SHORT).show();
            }
            }

        else {
            Toast.makeText(this,"Please try again later",Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        // Do nothing on  back button navigation
    }
}
