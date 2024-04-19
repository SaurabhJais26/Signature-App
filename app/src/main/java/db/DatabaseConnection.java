package db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.sencosignapp.ui.HomeActivity;

import org.greenrobot.eventbus.EventBus;

import busevent.BusEventDefault;
import datamodel.CustomerInvoiceDataModel;
import datamodel.InvoiceDataModel;
import datamodel.SignatureDataModel;
import datamodel.SignatureDetailRequest;

public class DatabaseConnection extends SQLiteOpenHelper {

    SQLiteDatabase sq;

    private static final int DATABASE_VERSION = 1;
    private static DatabaseConnection mInstance = null;

    private static final String DATABASE_NAME = "SencoGold.db";


    private String TAG = "Database";


    public static DatabaseConnection getInstance(Context ctx) {
        if (mInstance == null) {
            mInstance = new DatabaseConnection(ctx);
        }
        return mInstance;
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        db.disableWriteAheadLogging();
    }


    private static final String CREATE_TABLE_USERDETAIL= " create table if not exists UserDetail(userid text,terminal text,active int,status text);";

    private static final String CREATE_TABLE_INVOICEDETAIL=" create table if not exists InvoiceDetail(userid text,terminal text, customerAcct text, transactionId text, customerName text, signatureNotRequired text);";

    private static final String CREATE_TABLE_CUSTOMERINVOICEDETAIL=" create table if not exists CustomerInvoiceDetail(terminal text, customerAccount text, transactionId text, customerName text, signature text, invoiceURL text);";

    private static final String CREATE_TABLE_SIGNATUREDETAIL=" create table if not exists SignatureDetail(invoiceID text,custAcct text, signature blob, id text, post text,status text);";



    public DatabaseConnection(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_TABLE_USERDETAIL);
        db.execSQL(CREATE_TABLE_INVOICEDETAIL);
        db.execSQL(CREATE_TABLE_CUSTOMERINVOICEDETAIL);
        db.execSQL(CREATE_TABLE_SIGNATUREDETAIL);
          }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists"+" "+CREATE_TABLE_USERDETAIL);
        db.execSQL("drop table if exists"+" "+CREATE_TABLE_INVOICEDETAIL);
        db.execSQL("drop table if exists"+" "+CREATE_TABLE_CUSTOMERINVOICEDETAIL);
        db.execSQL("drop table if exists"+" "+CREATE_TABLE_SIGNATUREDETAIL);


        onCreate(db);
    }

    public void insertUserDetails(String userid,String terminal, Integer active,String status ){
        sq = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put("userid",userid);
            contentValues.put("terminal",terminal);
            contentValues.put("active",active);
            contentValues.put("status",status);
            long i= sq.insert("UserDetail",null,contentValues);
            Log.d(TAG, "UserDetail: ex =============>" + i);
        }catch (Exception e){
            Log.d(TAG, "InsertUserDetail: ex =============>" + e.getMessage());
        }
    }

    public Cursor getUserDetails() {
        sq = this.getReadableDatabase();
        Cursor res = sq.rawQuery("select  userid from UserDetail", null);
        return res;
    }

    public void deleteTable(String tlbName) {
        sq = this.getWritableDatabase();
        sq.execSQL("DELETE from '" + tlbName + "'");
    }

    public void insertInvoiceDetails(InvoiceDataModel invoiceDataModel){
        try {
            sq = this.getWritableDatabase();
            if (getInvoiceDetails()!=null) {
                ContentValues contentValues = new ContentValues();
                for (int i=0; i<invoiceDataModel.getTerminalsList().size();i++) {
                    String userid =invoiceDataModel.getTerminalsList().get(i).getUserid();
                    String terminal =  invoiceDataModel.getTerminalsList().get(i).getTerminal();

                    for (int j = 0; j < invoiceDataModel.getTerminalsList().get(i).getOrderLine().size(); j++) {
                        contentValues.put("userid",userid);
                        contentValues.put("terminal",terminal);
                        contentValues.put("customerAcct", invoiceDataModel.getTerminalsList().get(i).getOrderLine().get(j).getCustaccount());
                        contentValues.put("transactionId", invoiceDataModel.getTerminalsList().get(i).getOrderLine().get(j).getTransactionid());
                        contentValues.put("customerName", invoiceDataModel.getTerminalsList().get(i).getOrderLine().get(j).getCustname());
                        contentValues.put("signatureNotRequired", invoiceDataModel.getTerminalsList().get(i).getOrderLine().get(j).getSignaturenotrequired());
                        long k = sq.insert("InvoiceDetail", null, contentValues);
                        Log.d(TAG, "insertInvoiceDetails: insert =============>" + k);
                    }
                }
                EventBus.getDefault().post(new BusEventDefault());
            }
        }catch (Exception e){
            Log.d(TAG, "InsertInvoiceDetail: ex =============>" + e.getMessage());
        }
    }

    public Cursor getInvoiceDetails(){
        sq = this.getReadableDatabase();
        Cursor res = sq.rawQuery("select  customerName,transactionId,customerAcct, signatureNotRequired from InvoiceDetail", null);
        return res;
    }

    public void insertCustomerInvoiceDetails(CustomerInvoiceDataModel customerInvoiceDataModel){
        sq = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put("terminal",customerInvoiceDataModel.getTerminal());
            contentValues.put("customerAccount",customerInvoiceDataModel.getCustaccount());
            contentValues.put("transactionId",customerInvoiceDataModel.getTransactionid());
            contentValues.put("customerName",customerInvoiceDataModel.getCustname());
            contentValues.put("signature",customerInvoiceDataModel.getSignature());
            contentValues.put("invoiceURL",customerInvoiceDataModel.getInvoiceURL());
            long i= sq.insert("CustomerInvoiceDetail",null,contentValues);
            Log.d(TAG, "value: ex =============>" + i);
        }catch (Exception e){
            Log.d(TAG, "InsertCustomerInvoiceDetail: ex =============>" + e.getMessage());
        }
    }
    public Cursor getCustomerInvoiceUrlDetails(){
        sq = this.getReadableDatabase();
        Cursor res = sq.rawQuery("select invoiceURL  from CustomerInvoiceDetail", null);
        return res;
    }
    public void insertSignatureDetailsGet(String invoiceID,String custAcct,String status, String id,String post,String signBaseStr){
        sq = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put("invoiceID",invoiceID);
            contentValues.put("custAcct",custAcct);
            contentValues.put("signature",signBaseStr);
            contentValues.put("id",id);
            contentValues.put("post",post);
            contentValues.put("status",status);
            long i = sq.insert("SignatureDetail",null,contentValues);
            Log.d(TAG, "value: ex =============>" + i);
        }catch (Exception e){
            Log.d(TAG,"InsertSignatureDetail: ex =============>"+e.getMessage());

        }
    }

    public void updateCustomerSign(byte[] byteArraySign,String invoiceID,String custAcct) {
        sq = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("signature", byteArraySign);
        contentValues.put("invoiceID",invoiceID);
        contentValues.put("custAcct",custAcct);
        contentValues.put("post","0");
        sq.update("SignatureDetail", contentValues, "id=?", new String[]{HomeActivity.SIGNATUREID});
    }
   public Cursor getSignatureDetails(){
        sq = this.getReadableDatabase();
        Cursor res = sq.rawQuery("select invoiceID from SignatureDetail",null);
        return res;
   }

    public Cursor getCustomerSignatureDetail() {
        sq = this.getReadableDatabase();
        Cursor res = sq.rawQuery(" select * from SignatureDetail where post=0", null);
        return res;

    }

    public void updatePost(String post) {
        sq = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("post", post);
        sq.update("SignatureDetail", contentValues, "id= ?", new String[]{HomeActivity.SIGNATUREID});
    }

    public void deleteBlankSign() {
        sq = this.getWritableDatabase();
        sq.execSQL("DELETE from SignatureDetail where  signature='' ");
    }
    public void deletePost(){
        sq = this.getWritableDatabase();
        sq.execSQL("DELETE from SignatureDetail where post='1'");
    }


}
