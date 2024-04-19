package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "INVOICEID",
        "CUSTACCOUNT",
        "Signature"
})

//{
//        "INVOICEID":"DELHI-Delhi-4-1709269809436",
//        "CUSTACCOUNT":"INMF-000007",
//        "Signature":"abcd"
//        }


public class SignatureDetailRequest  {

    @JsonProperty("INVOICEID")
    private String INVOICEID;
    @JsonProperty("CUSTACCOUNT")
    private String CUSTACCOUNT;
    @JsonProperty("Signature")
    private String signature;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("INVOICEID")
    public String getInvoiceId() {
        return INVOICEID;
    }

    @JsonProperty("INVOICEID")
    public void setInvoiceId(String invoiceId) {
        this.INVOICEID = invoiceId;
    }

    @JsonProperty("CUSTACCOUNT")
    public String getCustAccount() {
        return CUSTACCOUNT;
    }

    @JsonProperty("CUSTACCOUNT")
    public void setCustAccount(String custAccount) {
        this.CUSTACCOUNT = custAccount;
    }

    @JsonProperty("Signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("Signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
