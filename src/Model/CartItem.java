package Model;

import java.math.BigDecimal;

public class CartItem {
    private int proId;
    private String proName;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItem(int proId, String proName, BigDecimal unitPrice, int quantity) {
        this.proId = proId;
        this.proName = proName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
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

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
