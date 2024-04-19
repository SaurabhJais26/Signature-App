package datamodel;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "signatureappversion",
        "signatureapptype"
})

public class VersionDataModel{

    @JsonProperty("signatureappversion")
    private String signatureappversion;
    @JsonProperty("signatureapptype")
    private String signatureapptype;

    @JsonProperty("signatureappversion")
    public String getVersion() {
        return signatureappversion;
    }

    @JsonProperty("signatureappversion")
    public void setVersion(String version) {
        this.signatureappversion = version;
    }

    @JsonProperty("signatureapptype")
    public String getType() {
        return signatureapptype;
    }

    @JsonProperty("SIGNATUREAPPTYPE")
    public void setType(String type) {
        this.signatureapptype = type;
    }

}