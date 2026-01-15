package Model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Products {
    private int ProId,Quantity,UserId;
    private String ProName,Des,Unit;
    private BigDecimal price;
    private Timestamp expirationDate;
    private float pricePercent;
    private String type;

    public Products(int proId ,String proName, String des, int quantity, BigDecimal price,String unit ,int userId) {
        ProId = proId;
        Quantity = quantity;
        UserId = userId;
        ProName = proName;
        Des = des;
        Unit = unit;
        this.price = price;
    }

    public Products(String proName, String des, int quantity, BigDecimal price, String unit, Timestamp expirationDate , int userId) {
        Quantity = quantity;
        UserId = userId;
        ProName = proName;
        Des = des;
        Unit = unit;
        this.expirationDate = expirationDate;
        this.price = price;
    }

    public Products(int proId ,String proName, String des, int quantity, BigDecimal price,String unit ,float pricePercent,int userId) {
        ProId = proId;
        Quantity = quantity;
        UserId = userId;
        ProName = proName;
        Des = des;
        Unit = unit;
        this.price = price;
        this.pricePercent = pricePercent;
    }

    public Products(int quantity, int proId) {
        Quantity = quantity;
        ProId = proId;
    }

    public int getProId() {
        return ProId;
    }

    public float getPricePercent() {
        return pricePercent;
    }

    public void setPricePercent(int pricePercent) {
        this.pricePercent = pricePercent;
    }

    public void setProId(int proId) {
        ProId = proId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getProName() {
        return ProName;
    }

    public void setProName(String proName) {
        ProName = proName;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String des) {
        Des = des;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Timestamp getExpirationDate() {
        return expirationDate;
    }

    public void setPricePercent(float pricePercent) {
        this.pricePercent = pricePercent;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setExpirationDate(Timestamp expirationDate) {
        this.expirationDate = expirationDate;
    }
}
