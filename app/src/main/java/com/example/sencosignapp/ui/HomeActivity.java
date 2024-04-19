package com.example.sencosignapp.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sencosignapp.R;

import org.greenrobot.eventbus.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import adapter.CustomerDetailAdapter;
import busevent.InvoiceBusEvent;
import datamodel.InvoiceDataModel;
import model.CustomerDetailModel;
import utils.Constant;
import utils.ProgressWheel;
import utils.SharedPref;
import utils.SpacesItemDecoration;
import utils.Util;

public class HomeActivity extends BaseActivity implements  CustomerDetailAdapter.ItemClickListener{

    private RecyclerView recyclerView;
    private CustomerDetailAdapter adapter;
    private ArrayList<CustomerDetailModel> customerDetailModelArrayList;
    private String userId,terminal;
    private View progressLayout;
    private ProgressWheel progresswheel;

    public static String INVOICENO = "",CUSTOMERACCT="",SIGNATUREID = "", CUSTOMERNAME="";
    public static String post;

    @Override
    protected void create(Bundle bundle) {
        inflateView(R.layout.home_portrait);
        Button btnGetInvoice = findViewById(R.id.btn_get_invoice);
        executeHomeData();
        btnGetInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(getIntent());
            }
        });
        databaseConnection.deleteBlankSign();
    }

    private void executeHomeData(){

        if (Util.isNetworkAvailable(this)) {
            showProgress();
            userId = sharedPref.getString("userid");
//            terminal = sharedPref.getString("terminal");
            executeInvoiceApi(userId);
        }
        else{
            Toast.makeText(this,"Check Your Internet Connection",Toast.LENGTH_SHORT).show();
        }

    }
    private void initRecycleView(){
        try {
            recyclerView = (RecyclerView) findViewById(R.id.homeListRv);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
            recyclerView.setLayoutManager(layoutManager);
            Cursor cursor = databaseConnection.getInvoiceDetails();
            customerDetailModelArrayList = new ArrayList<>();
            if (cursor != null){
                if (cursor.getCount()>0){
                    cursor.moveToFirst();
                    String snr = cursor.getString(cursor.getColumnIndexOrThrow("signatureNotRequired"));

                        for (int i=0; i<cursor.getCount(); i++) {
                            if (cursor.getString(cursor.getColumnIndexOrThrow("signatureNotRequired")).equalsIgnoreCase("false")){
                            customerDetailModelArrayList.add(new CustomerDetailModel(
                                    cursor.getString(cursor.getColumnIndexOrThrow("transactionId")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("customerName")),
                                    cursor.getString(cursor.getColumnIndexOrThrow("customerAcct"))
                            ));
                        }
                        cursor.moveToNext();
                    }
                    if (customerDetailModelArrayList != null){
                        adapter = new CustomerDetailAdapter(customerDetailModelArrayList);
                        recyclerView.setAdapter(adapter);
                        adapter.setClickListener(this);
                        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.recycler_view_item_width)));

                    }
                }

            }
        }catch (Exception e){
            e.getMessage();

        }

    }


    @Override
    public void onItemClick(View view, int position) {
        Log.d("HomeActivity" + "position:::", position + "");
        TextView custAcct = view.findViewById(R.id.custAcct);
        TextView invoice = view.findViewById(R.id.invoiceValue);
        TextView custName = view.findViewById(R.id.customerName);

        String custStr = custAcct.getText().toString();
        String invoiceVal = invoice.getText().toString();
        String customerNameSign = custName.getText().toString();

        INVOICENO = invoiceVal;
        CUSTOMERACCT = custStr;
        CUSTOMERNAME = customerNameSign;
        String strId = new SimpleDateFormat("ddmmyyyyhhmmss").format(new Date());
        SIGNATUREID = strId+custStr;

       Util.pushNext(HomeActivity.this,SignActivity.class);
    }


    @Subscribe
    public void onEvent(InvoiceBusEvent invoiceBusEvent){
        hideProgress();
        if (invoiceBusEvent.getStatus()=="YES"){
            initRecycleView();
        }
        else{
            Toast.makeText(this,"Please try again",Toast.LENGTH_SHORT).show();
        }
    }
}
