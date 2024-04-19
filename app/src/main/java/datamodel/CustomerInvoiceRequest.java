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
        "CUSTACCOUNT"
})

public class CustomerInvoiceRequest {

    @JsonProperty("INVOICEID")
    private String invoiceid;
    @JsonProperty("CUSTACCOUNT")
    private String custaccount;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("INVOICEID")
    public String getInvoiceid() {
        return invoiceid;
    }

    @JsonProperty("INVOICEID")
    public void setInvoiceid(String invoiceid) {
        this.invoiceid = invoiceid;
    }

    @JsonProperty("CUSTACCOUNT")
    public String getCustaccount() {
        return custaccount;
    }

    @JsonProperty("CUSTACCOUNT")
    public void setCustaccount(String custaccount) {
        this.custaccount = custaccount;
    }

}
