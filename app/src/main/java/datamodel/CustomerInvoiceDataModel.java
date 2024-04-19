package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "terminal",
        "custaccount",
        "custname",
        "transactionid",
        "signature",
        "invoiceURL"
})

public class CustomerInvoiceDataModel {

    @JsonProperty("terminal")
    private String terminal;
    @JsonProperty("custaccount")
    private String custaccount;
    @JsonProperty("custname")
    private String custname;
    @JsonProperty("transactionid")
    private String transactionid;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("invoiceURL")
    private String invoiceURL;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("terminal")
    public String getTerminal() {
        return terminal;
    }

    @JsonProperty("terminal")
    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }

    @JsonProperty("custaccount")
    public String getCustaccount() {
        return custaccount;
    }

    @JsonProperty("custaccount")
    public void setCustaccount(String custaccount) {
        this.custaccount = custaccount;
    }

    @JsonProperty("custname")
    public String getCustname() {
        return custname;
    }

    @JsonProperty("custname")
    public void setCustname(String custname) {
        this.custname = custname;
    }

    @JsonProperty("transactionid")
    public String getTransactionid() {
        return transactionid;
    }

    @JsonProperty("transactionid")
    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @JsonProperty("invoiceURL")
    public String getInvoiceURL() {
        return invoiceURL;
    }

    @JsonProperty("invoiceURL")
    public void setInvoiceURL(String invoiceURL) {
        this.invoiceURL = invoiceURL;
    }

}
