package DBConnect;

import Model.Products;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static UI.LoginFrame.userid;
import static UI.LoginFrame.username;

public class ProductsDAO {

    private static final String sqlSelect = """
        SELECT
            p.ProId,
            p.ProName,
            p.[Des],
            p.Quantity,
            p.Price,
            p.Unit,
            p.PricePercent,
            p.[UserID]
        FROM dbo.Product AS p
        JOIN dbo.[User] AS u    
            ON p.UserID = u.ID
        WHERE u.Email = ?
        AND p.ExpirationDate > GETDATE()
        AND p.Quantity > 0
        """;

    private static final String sqlUpdate = """
        UPDATE dbo.Product
        SET [Des] = ?,
            [Quantity] = ?,
            [Price] = ?,
            [Unit] = ?,
            [PricePercent] = ?,
            [UserID] = ?
        WHERE [ProId] = ?
     """;

    private static final String sqlUpdateCell = """
        UPDATE dbo.Product
        SET [Quantity] = ?
        WHERE [ProId] = ?
     """;

    private static final String sqlSearch = """
        AND p.ProName LIKE ? 
        AND (? = N'Tất cả' OR p.[Type] = ?)
        """;



    private static ArrayList<Products> querySearchByName(String keyword, String type, String orderBy) {
        ArrayList<Products> list = new ArrayList<>();
        String sql = sqlSelect + "\n" + sqlSearch
                + (orderBy == null || orderBy.isBlank() ? "" : ("\n" + orderBy));

        try (Connection con = DBConnect.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, username);
            st.setString(2, "%" + (keyword == null ? "" : keyword.trim()) + "%");
            st.setString(3, type == null ? "Tất cả" : type);
            st.setString(4, type == null ? "" : type);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Products pro = new Products(
                            rs.getInt("ProId"),
                            rs.getString("ProName"),
                            rs.getString("Des"),
                            rs.getInt("Quantity"),
                            rs.getBigDecimal("Price"),
                            rs.getString("Unit"),
                            rs.getFloat("PricePercent"),
                            rs.getInt("UserID")
                    );
                    list.add(pro);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static ArrayList<Products> getAll() {
        return querySearchByName("", "Tất cả", "ORDER BY p.ProId, p.ProName ASC, p.Price ASC, p.Quantity ASC");
    }

    public static ArrayList<Products> searchOderNameASC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.ProName ASC, p.Price ASC, p.Quantity ASC");
    }

    public static ArrayList<Products> searchOderNameDESC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.ProName DESC, p.Price ASC, p.Quantity ASC");
    }

    public static ArrayList<Products> searchOderQuantityASC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.Quantity ASC, p.ProName ASC, p.Price ASC");
    }

    public static ArrayList<Products> searchOderQuantityDESC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.Quantity DESC, p.ProName ASC, p.Price ASC");
    }

    public static ArrayList<Products> searchOderPriceASC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.Price ASC, p.Quantity ASC, p.ProName ASC");
    }

    public static ArrayList<Products> searchOderPriceDESC(String keyword, String type) {
        return querySearchByName(keyword, type, "ORDER BY p.Price DESC, p.Quantity ASC, p.ProName ASC");
    }

    public static ArrayList<String> getTypes() {
        ArrayList<String> list = new ArrayList<>();
        String sql = "SELECT DISTINCT [Type] FROM Product WHERE [Type] IS NOT NULL";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static Boolean insertProduct(Products p) {
        String sql = "INSERT INTO dbo.Product (ProName, [Des], Quantity, Price, Unit,ExpirationDate, UserID) VALUES (?, ?, ?, ?, ?,?, ?)";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, p.getProName());
            st.setString(2, p.getDes());
            st.setInt(3, p.getQuantity());
            st.setBigDecimal(4, p.getPrice());
            st.setString(5, p.getUnit());
            st.setTimestamp(6,p.getExpirationDate());
            st.setInt(7, p.getUserId());

            st.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getProductIdByName(String productName) {
        String sql = "SELECT ProId FROM dbo.Product WHERE ProName = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, productName.trim());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ProId");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static int getProductQuantityById(int productId) {
        String sql = "SELECT Quantity FROM dbo.Product WHERE ProId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("Quantity");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static java.math.BigDecimal getProductPriceById(int productId) {
        String sql = "SELECT Price FROM dbo.Product WHERE ProId = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getBigDecimal("Price");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return java.math.BigDecimal.ZERO;
    }

    public static boolean update (Products p) {
        try (Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sqlUpdate))
        {
            ps.setString(1, p.getDes());
            ps.setInt(2, p.getQuantity());
            ps.setBigDecimal(3, p.getPrice());
            ps.setString(4, p.getUnit());
            ps.setFloat(5, p.getPricePercent());
            ps.setInt(6,userid);
            ps.setInt(7, p.getProId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateCell (Products products) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sqlUpdateCell))
        {
            ps.setInt(1, products.getQuantity());
            ps.setInt(2, products.getProId());
            int kt = ps.executeUpdate();
            return kt > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
