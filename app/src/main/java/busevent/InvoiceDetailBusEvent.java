package busevent;

public class InvoiceDetailBusEvent {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    public InvoiceDetailBusEvent(String status){
        this.status=status;
    }

}
