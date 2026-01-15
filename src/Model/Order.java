package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private int orderId;
    private int userId;
    private Timestamp orderTime;
    private String customerName;
    private String customerSdt;
    private BigDecimal totalAmount;
    private List<OrderItem> items;

    public Order(int userId) {
        this.userId = userId;
        this.items = new ArrayList<>();
    }

    public Order(int orderId, int userId, Timestamp orderTime, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderTime = orderTime;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    public Order(int orderId, int userId, Timestamp orderTime, String customerName, String customerSdt, BigDecimal totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderTime = orderTime;
        this.customerName = customerName;
        this.customerSdt = customerSdt;
        this.totalAmount = totalAmount;
        this.items = new ArrayList<>();
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public BigDecimal calculateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItem item : items) {
            total = total.add(item.getSubtotal());
        }
        return total;
    }

    public int getItemCount() {
        return items.size();
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerSdt() {
        return customerSdt;
    }

    public void setCustomerSdt(String customerSdt) {
        this.customerSdt = customerSdt;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
}
