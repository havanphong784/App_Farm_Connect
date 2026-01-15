package DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    public static boolean checkLogin(String username, String password) {
        String sql = "SELECT * FROM [User] WHERE Email = ? AND [Password] = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());
            ps.setString(2, password.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkLogin2(String username) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";
        try (Connection con = DBConnect.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean chekRegistration(String username, String pass, String email) {
        String sql = "INSERT INTO dbo.[User] (Email, [Name], [Password]) VALUES (?, ?, ?)";
        if (checkLogin2(username)) return false;
        try (
                Connection con = DBConnect.getConnection();
                PreparedStatement ps = con.prepareStatement(sql);
        ) {
            ps.setString(1, email.trim());
            ps.setString(2, username);
            ps.setString(3, pass);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static int getUserIdByEmail(String email) {
        String sql = "SELECT ID FROM [User] WHERE Email = ?";
        try (
            Connection con = DBConnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ){
            ps.setString(1, email.trim());
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("ID");
            } else {
                return 0;
            }
        }catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
