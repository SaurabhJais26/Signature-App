package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "invoiceId",
        "custAccount",
        "status"
})
// "invoiceId": "DELHI-Delhi-4-1709269809436",
//         "custAccount": "INMF-000007",
//         "status": "Signature Updated Successfully"

public class SignatureDataModel {

    @JsonProperty("invoiceId")
    private String invoiceId;
    @JsonProperty("custAccount")
    private String custAccount;
    @JsonProperty("status")
    private String status;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("invoiceId")
    public String getInvoiceId() {
        return invoiceId;
    }

    @JsonProperty("invoiceId")
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    @JsonProperty("custAccount")
    public String getCustAccount() {
        return custAccount;
    }

    @JsonProperty("custAccount")
    public void setCustAccount(String custAccount) {
        this.custAccount = custAccount;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }


}
