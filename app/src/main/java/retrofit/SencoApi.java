package retrofit;


import java.util.List;

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
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface SencoApi {

    @POST("GetData/LoginAPI")
    Call<LoginDataModel> userValidate(@Body UserDetailRequest jsonData);

    @POST("GetData/GetInvoiceId")
    Call<InvoiceDataModel> getInvoiceId(@Body InvoiceDetailRequest jsonData);

    @POST("GetData/GetInvoiceDetails")
    Call<CustomerInvoiceDataModel> getInvoiceDetails(@Body CustomerInvoiceRequest jsonData);

    @POST("Updatedata/UpdateSignatureInvoiceDetails")
    Call<SignatureDataModel> postSignatureDetails(@Body SignatureDetailRequest jsonData);

    @POST("GetData/GetVersion")
    Call<VersionDataModel> getVersionDetails(@Body VersionDetailRequest jsonData);

}