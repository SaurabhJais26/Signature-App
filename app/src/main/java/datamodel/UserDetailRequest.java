package datamodel;
import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "USERID",
        "PASSWORD",
        "STORE"
})

public class UserDetailRequest{

    @JsonProperty("USERID")
    private String userid;
    @JsonProperty("PASSWORD")
    private String password;
    @JsonProperty("STORE")
    private String store;
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

    @JsonProperty("PASSWORD")
    public String getPassword() {
        return password;
    }

    @JsonProperty("PASSWORD")
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonProperty("STORE")
    public String getStore() {
        return store;
    }

    @JsonProperty("STORE")
    public void setStore(String store) {
        this.store = store;
    }

}