package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "USERID"
})

public class InvoiceDetailRequest{

    @JsonProperty("USERID")
    private String userid;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    @JsonProperty("USERID")
    public String getUserid() {
        return userid;
    }

    @JsonProperty("USERID")
    public void setUserid(String userid) {
        this.userid = userid;
    }

}