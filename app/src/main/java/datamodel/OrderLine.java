package datamodel;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "transactionid",
        "custaccount",
        "custname",
        "signature",
        "signaturenotrequired"
})
public class OrderLine{

    @JsonProperty("transactionid")
    private String transactionid;
    @JsonProperty("custaccount")
    private String custaccount;
    @JsonProperty("custname")
    private String custname;
    @JsonProperty("signature")
    private String signature;
    @JsonProperty("signaturenotrequired")
    private String signaturenotrequired;



    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("transactionid")
    public String getTransactionid() {
        return transactionid;
    }

    @JsonProperty("transactionid")
    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
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

    @JsonProperty("signature")
    public String getSignature() {
        return signature;
    }

    @JsonProperty("signature")
    public void setSignature(String signature) {
        this.signature = signature;
    }
    public String getSignaturenotrequired() {
        return signaturenotrequired;
    }

    public void setSignaturenotrequired(String signaturenotrequired) {
        this.signaturenotrequired = signaturenotrequired;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}