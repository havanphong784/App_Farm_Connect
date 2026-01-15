package DBConnect;

import Model.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDao {

    public static final String SQL_INSERT_ITEM = 
        "INSERT INTO OrderItem (OrderId, ProId, Quantity, UnitPrice) VALUES (?, ?, ?, ?)";
    
    public static final String SQL_GET_ITEMS_BY_ORDER = """
        SELECT oi.ItemId, oi.OrderId, oi.ProId, p.ProName, oi.Quantity, oi.UnitPrice
        FROM OrderItem oi
        JOIN Product p ON oi.ProId = p.ProId
        WHERE oi.OrderId = ?
        ORDER BY oi.ItemId
        """;

    public static boolean insertOrderItem(int orderId, OrderItem item) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT_ITEM)) {
            ps.setInt(1, orderId);
            ps.setInt(2, item.getProId());
            ps.setInt(3, item.getQuantity());
            ps.setBigDecimal(4, item.getUnitPrice());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean insertOrderItems(int orderId, List<OrderItem> items) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT_ITEM)) {
            for (OrderItem item : items) {
                ps.setInt(1, orderId);
                ps.setInt(2, item.getProId());
                ps.setInt(3, item.getQuantity());
                ps.setBigDecimal(4, item.getUnitPrice());
                ps.addBatch();
            }
            int[] results = ps.executeBatch();
            return results.length == items.size();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_GET_ITEMS_BY_ORDER)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem(
                    rs.getInt("ItemId"),
                    rs.getInt("OrderId"),
                    rs.getInt("ProId"),
                    rs.getString("ProName"),
                    rs.getInt("Quantity"),
                    rs.getBigDecimal("UnitPrice")
                );
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }
}
