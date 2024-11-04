package keac6.wishlist.repository;

import keac6.wishlist.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class WishListRepository {
    @Value("${spring.datasource.url}")
    private String db_url;
    @Value("${spring.datasource.username}")
    private String db_username;
    @Value("${spring.datasource.password}")
    private String db_password;

    private Connection getDBConnection() throws SQLException {
        return DriverManager.getConnection(db_url, db_username, db_password);
    }

    public void saveNewUser(User newUser) {
        String query = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?)";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);

            pstmt.setString(1, newUser.getEmail());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFirstName());
            pstmt.setString(4, newUser.getLastName());

            pstmt.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Error saving new user to database", error);
        }
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = getDBConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setFirstName(rs.getString("first_name"));
                user.setLastName(rs.getString("last_name"));
                return user;
            } else {
                return null;
            }

        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving user from database", error);
        }
    }
}
