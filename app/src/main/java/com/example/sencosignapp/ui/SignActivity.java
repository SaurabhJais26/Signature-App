package com.example.sencosignapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sencosignapp.R;
import com.williamww.silkysignature.views.SignaturePad;

import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import busevent.InvoiceBusEvent;
import busevent.InvoiceDetailBusEvent;
import busevent.SignatureBusEvent;
import datamodel.CustomerInvoiceDataModel;
import datamodel.SignatureDetailRequest;
import model.CustomerDetailModel;
import utils.PdfWebViewClient;
import utils.ProgressWheel;
import utils.Util;

public class SignActivity extends BaseActivity{

    private ArrayList<CustomerInvoiceDataModel> customerInvoiceDataModelArrayList;

    private SignaturePad signaturePad;
    private View progressLayout;
    private ProgressWheel progresswheel;

    Bitmap signatureBitmap;

    Boolean isSigned = false;



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.sign_landscape);
            initData();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.sign_portrait);
            initData();
        }
    }

    @Override
    protected void create(Bundle bundle) {
        setContentView(R.layout.sign_portrait);
        databaseConnection.insertSignatureDetailsGet("","","",HomeActivity.SIGNATUREID,"","");
        initData();
        executeSignData();

    }

    private void executeSignData(){
        if (Util.isNetworkAvailable(this)) {
            showProgress();
            executeInvoiceDetailApi(HomeActivity.INVOICENO,HomeActivity.CUSTOMERACCT);
        }
        else{
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void initData(){
        signaturePad = (SignaturePad)findViewById(R.id.signature_pad);
        TextView custName = (TextView)findViewById(R.id.custName);
        WebView webView = (WebView) findViewById(R.id.web_view);
        progressLayout = (View) findViewById(R.id.progressLayout);
        progresswheel = (ProgressWheel) findViewById(R.id.progresswheel);

        custName.setText(HomeActivity.CUSTOMERNAME);

        webView.getSettings().setJavaScriptEnabled(true);

        Cursor cursor = databaseConnection.getCustomerInvoiceUrlDetails();
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);

        if(cursor!=null){
            if (cursor.getCount()>0){
                cursor.moveToFirst();
                webView.loadDataWithBaseURL(null, cursor.getString(cursor.getColumnIndexOrThrow("invoiceURL")), "text/html", "UTF-8", null);

//                webView.loadData(cursor.getString(cursor.getColumnIndexOrThrow("invoiceURL")),"text/html","UTF-8");
//                webView.loadUrl(cursor.getString(cursor.getColumnIndexOrThrow("invoiceUrl")));
            }
        }
        Button submitBtn = (Button)findViewById(R.id.submit_button) ;

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (isSigned) {
                        if (Util.isNetworkAvailable(SignActivity.this)){
                            showProgress();
                            signatureBitmap = signaturePad.getTransparentSignatureBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            signatureBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byte[] byteArray = stream.toByteArray();
                            databaseConnection.updateCustomerSign(byteArray,HomeActivity.INVOICENO,HomeActivity.CUSTOMERACCT);
                            postSignatureDetailApi();

                        }else{
                            Toast.makeText(SignActivity.this,"Please Check Your Internet Connection",Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(SignActivity.this,"Please sign the pad first",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {

                }
            }
        });
        Button clearBtn = (Button)findViewById(R.id.clear_button);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signaturePad.clear();
            }
        });
        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initSignature();
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

    private void initSignature(){
        signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                isSigned = true;
            }

            @Override
            public void onClear() {
                isSigned = false;
            }
        });
    }

    @Subscribe
    public void onEvent(InvoiceDetailBusEvent invoiceDetailBusEvent){
        hideProgress();
        if (invoiceDetailBusEvent.getStatus()=="YES"){
            initData();
        }
        else{
            Toast.makeText(this,"Please try again",Toast.LENGTH_SHORT).show();
        }

    }
    @Subscribe
    public void onSignEvent(SignatureBusEvent signatureBusEvent){
        hideProgress();
        if (signatureBusEvent.getStatus()=="YES"){
            databaseConnection.updatePost("1");
            Util.pushNext(this,HomeActivity.class);
        }else{

        }
    }

}
