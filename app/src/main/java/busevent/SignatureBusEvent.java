package busevent;

public class SignatureBusEvent {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;
    public SignatureBusEvent(String status){
        this.status = status;
    }
}
