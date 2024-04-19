package busevent;

public class VersionBusEvent {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    private String versionName;

    public VersionBusEvent(String status,String versionName){
        this.status = status;
        this.versionName= versionName;
    }
}
