package keac6.wishlist.repository;

import keac6.wishlist.model.User;
import keac6.wishlist.model.WishList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;

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

    private JdbcTemplate jdbcTemplate;

    public WishListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void saveNewUser(User newUser) {
        String query = "INSERT INTO users (email, password, first_name, last_name) VALUES (?, ?, ?, ?)";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newUser.getEmail());
            pstmt.setString(2, newUser.getPassword());
            pstmt.setString(3, newUser.getFirstName());
            pstmt.setString(4, newUser.getLastName());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newUser.setUserId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error saving new user to database", error);
        }
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
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

    public void createWishList(WishList newWishList){
        String sql = "INSERT INTO wishlists (wishlist_name, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, newWishList.getWishListName(), newWishList.getUserId());
    }

}
