package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "SIGNATUREAPPTYPE"
})

public class VersionDetailRequest{

    @JsonProperty("SIGNATUREAPPTYPE")
    private String SIGNATUREAPPTYPE;

    @JsonProperty("SIGNATUREAPPTYPE")
    public void setType(String type) {
        this.SIGNATUREAPPTYPE = type;
    }

}