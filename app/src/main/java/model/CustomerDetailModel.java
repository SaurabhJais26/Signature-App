package model;

public class CustomerDetailModel {
    private String invoiceNum;
    private String customerName;

    public String getCustomerAcct() {
        return customerAcct;
    }

    public void setCustomerAcct(String customerAcct) {
        this.customerAcct = customerAcct;
    }

    private String customerAcct;

    public CustomerDetailModel(String invoiceNum, String customerName,String customerAcct) {
        this.invoiceNum = invoiceNum;
        this.customerName = customerName;
        this.customerAcct = customerAcct;
    }

    public String getInvoiceNum() {
        return invoiceNum;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
