package DBConnect;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

// Xóa static import userid
// import static UI.LoginFrame.userid;

public class StatisticsDAO {

    private static final String SQL_REVENUE_BY_DATE = """
        SELECT CAST(o.OrderTime AS DATE) AS NgayBan,
               SUM(oi.Quantity * oi.UnitPrice) AS TongDoanhThu
        FROM [Order] o
        JOIN OrderItem oi ON o.OrderId = oi.OrderId
        WHERE o.UserID = ?
        AND o.OrderTime >= DATEADD(DAY, -7, GETDATE())
        GROUP BY CAST(o.OrderTime AS DATE)
        ORDER BY NgayBan ASC
    """;

    private static final String SQL_TOP_SELLING = """
        SELECT TOP(?) p.ProName, SUM(oi.Quantity) AS TongBan
        FROM OrderItem oi
        JOIN [Order] o ON oi.OrderId = o.OrderId
        JOIN Product p ON oi.ProId = p.ProId
        WHERE o.UserID = ?
        GROUP BY p.ProName
        ORDER BY TongBan DESC
    """;

    private static final String SQL_PRODUCT_BY_TYPE = """
        SELECT [Type], COUNT(*) AS SoLuong
        FROM Product
        WHERE UserID = ? AND [Type] IS NOT NULL
        GROUP BY [Type]
    """;

    private static final String SQL_LOW_STOCK = """
        SELECT TOP(?) ProName, Quantity
        FROM Product
        WHERE UserID = ? AND Quantity > 0
        ORDER BY Quantity ASC
    """;

    private static final String SQL_TOTAL_REVENUE = """
        SELECT COALESCE(SUM(oi.Quantity * oi.UnitPrice), 0) AS TongDoanhThu
        FROM [Order] o
        JOIN OrderItem oi ON o.OrderId = oi.OrderId
        WHERE o.UserID = ?
    """;

    private static final String SQL_TOTAL_ORDERS = """
        SELECT COUNT(*) AS TongDon
        FROM [Order]
        WHERE UserID = ?
    """;

    private static final String SQL_TOTAL_PRODUCTS = """
        SELECT COUNT(*) AS TongSP
        FROM Product
        WHERE UserID = ? AND Quantity > 0
    """;

    private static final String SQL_EXPIRED_PRODUCTS = """
        SELECT ProName, Quantity, Unit, ExpirationDate
        FROM Product
        WHERE UserID = ? AND ExpirationDate <= DATEADD(DAY, 3, GETDATE())
        ORDER BY ExpirationDate ASC
    """;

    public static Map<String, BigDecimal> getRevenueByDate(int userId) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_REVENUE_BY_DATE)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String date = rs.getString("NgayBan");
                BigDecimal revenue = rs.getBigDecimal("TongDoanhThu");
                result.put(date, revenue != null ? revenue : BigDecimal.ZERO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Integer> getTopSellingProducts(int limit, int userId) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_TOP_SELLING)) {
            ps.setInt(1, limit);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("ProName"), rs.getInt("TongBan"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Integer> getProductCountByType(int userId) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_PRODUCT_BY_TYPE)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String type = rs.getString("Type");
                if (type != null && !type.isEmpty()) {
                    result.put(type, rs.getInt("SoLuong"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Map<String, Integer> getLowStockProducts(int limit, int userId) {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_LOW_STOCK)) {
            ps.setInt(1, limit);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                result.put(rs.getString("ProName"), rs.getInt("Quantity"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static BigDecimal getTotalRevenue(int userId) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_REVENUE)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                BigDecimal total = rs.getBigDecimal("TongDoanhThu");
                return total != null ? total : BigDecimal.ZERO;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

    public static int getTotalOrders(int userId) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_ORDERS)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TongDon");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getTotalProducts(int userId) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_TOTAL_PRODUCTS)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("TongSP");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static Object[][] getExpiredProducts(int userId) {
        ArrayList<Object[]> list = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_EXPIRED_PRODUCTS)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            Timestamp now = new Timestamp(System.currentTimeMillis());
            while (rs.next()) {
                String proName = rs.getString("ProName");
                int quantity = rs.getInt("Quantity");
                String unit = rs.getString("Unit");
                Timestamp expDate = rs.getTimestamp("ExpirationDate");
                String formattedDate = expDate != null ?
                    new java.text.SimpleDateFormat("dd/MM/yyyy").format(expDate) : "N/A";
                String status = "expiring";
                if (expDate != null && expDate.before(now)) {
                    status = "expired";
                }
                list.add(new Object[]{proName, quantity, unit, formattedDate, status});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toArray(new Object[0][]);
    }
}
