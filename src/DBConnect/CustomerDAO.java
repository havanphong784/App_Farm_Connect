package DBConnect;

import Model.Customer;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import static UI.LoginFrame.userid;

public class CustomerDAO {

    private static final String SQL_GET_ALL_CUSTOMERS = """
        SELECT CustomerId, CustomerName, CustomerSdt, CustomerEmail, CustomerAddress
        FROM Customer
        WHERE UserID = ?
        ORDER BY CustomerName
        """;

    private static final String SQL_GET_CUSTOMER_BY_ID = """
        SELECT CustomerId, CustomerName, CustomerSdt, CustomerEmail, CustomerAddress
        FROM Customer
        WHERE CustomerId = ?
        """;

    private static final String SQL_INSERT_CUSTOMER = """
        INSERT INTO Customer (CustomerName, CustomerSdt, CustomerEmail, CustomerAddress, UserID)
        VALUES (?, ?, ?, ?, ?)
        """;

    private static final String SQL_TOP_CUSTOMERS_BY_REVENUE = """
        SELECT TOP(?) c.CustomerName, SUM(oi.Quantity * oi.UnitPrice) AS TotalRevenue
        FROM Customer c
        JOIN [Order] o ON c.CustomerId = o.CustomerId
        JOIN OrderItem oi ON o.OrderId = oi.OrderId
        WHERE c.UserID = ?
        GROUP BY c.CustomerId, c.CustomerName
        ORDER BY TotalRevenue DESC
        """;

    public static List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_GET_ALL_CUSTOMERS)) {
            ps.setInt(1, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Customer customer = new Customer(
                    rs.getInt("CustomerId"),
                    rs.getString("CustomerName"),
                    rs.getString("CustomerSdt"),
                    rs.getString("CustomerEmail"),
                    rs.getString("CustomerAddress")
                );
                customers.add(customer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    public static Customer getCustomerById(int customerId) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_GET_CUSTOMER_BY_ID)) {
            ps.setInt(1, customerId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Customer(
                    rs.getInt("CustomerId"),
                    rs.getString("CustomerName"),
                    rs.getString("CustomerSdt"),
                    rs.getString("CustomerEmail"),
                    rs.getString("CustomerAddress")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int insertCustomer(String name, String sdt, String email, String address) {
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_INSERT_CUSTOMER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, sdt);
            ps.setString(3, email);
            ps.setString(4, address);
            ps.setInt(5, userid);
            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Map<String, BigDecimal> getTopCustomersByRevenue(int limit) {
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(SQL_TOP_CUSTOMERS_BY_REVENUE)) {
            ps.setInt(1, limit);
            ps.setInt(2, userid);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String name = rs.getString("CustomerName");
                BigDecimal revenue = rs.getBigDecimal("TotalRevenue");
                result.put(name, revenue != null ? revenue : BigDecimal.ZERO);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
