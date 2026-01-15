package Model;

import java.math.BigDecimal;

public class OrderItem {
    private int itemId;
    private int orderId;
    private int proId;
    private String proName;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem(int proId, int quantity, BigDecimal unitPrice) {
        this.proId = proId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public OrderItem(int itemId, int orderId, int proId, String proName, int quantity, BigDecimal unitPrice) {
        this.itemId = itemId;
        this.orderId = orderId;
        this.proId = proId;
        this.proName = proName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
