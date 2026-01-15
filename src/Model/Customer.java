package Model;

public class Customer {
    private int customerId;
    private String customerName;
    private String customerSdt;
    private String customerEmail;
    private String customerAddress;

    public Customer(int customerId, String customerName, String customerSdt, String customerEmail, String customerAddress) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerSdt = customerSdt;
        this.customerEmail = customerEmail;
        this.customerAddress = customerAddress;
    }

    public Customer(int customerId, String customerName, String customerSdt) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerSdt = customerSdt;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerSdt() { return customerSdt; }
    public void setCustomerSdt(String customerSdt) { this.customerSdt = customerSdt; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public String getCustomerAddress() { return customerAddress; }
    public void setCustomerAddress(String customerAddress) { this.customerAddress = customerAddress; }

    @Override
    public String toString() {
        return customerName + (customerSdt != null ? " - " + customerSdt : "");
    }
}
