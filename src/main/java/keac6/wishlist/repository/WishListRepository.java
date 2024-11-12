package keac6.wishlist.repository;

import keac6.wishlist.model.User;
import keac6.wishlist.model.Wish;
import keac6.wishlist.model.WishList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;

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

    public void saveNewWish(Wish newWish) {
        String query = "INSERT INTO wishes (wishlist_id, wish_name, wish_price, wish_description, wish_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, 1);
            pstmt.setString(2, newWish.getName());
            pstmt.setInt(3, newWish.getPrice());
            pstmt.setString(4, newWish.getDescription());
            pstmt.setString(5, newWish.getLink());

            pstmt.executeUpdate();

        } catch (SQLException error) {
            throw new RuntimeException("Error saving new wish", error);
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

    public void createWishList(WishList newWishList) {
        String sql = "INSERT INTO wishlists (wishlist_name, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, newWishList.getWishListName(), newWishList.getUserId());
    }

    public ArrayList<WishList> getWishList(int userid) {
        ArrayList<WishList> list = new ArrayList<>();
        String query = "SELECT * FROM wishlists WHERE user_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WishList wishList = new WishList(
                        rs.getInt("wishlist_id"),
                        rs.getString("wishlist_name")
                );
                list.add(wishList);
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving user from database", error);
        }
        return list;
    }

    public WishList getWishListById(int wishListId) {
        String query = "SELECT * FROM wishlists WHERE wishlist_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, wishListId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new WishList(
                        rs.getInt("wishlist_id"),
                        rs.getString("wishlist_name")
                );
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving wish list from database", error);
        }
        return null;
    }

    public ArrayList<Wish> getWishesByWishListId(int wishListId) {
        ArrayList<Wish> wishes = new ArrayList<>();
        String query = "SELECT * FROM wishes WHERE wishlist_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, wishListId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                wishes.add(new Wish(
                        rs.getString("wish_name"),
                        rs.getString("wish_description"),
                        rs.getInt("wish_price"),
                        rs.getString("wish_url")
                ));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving wishes from database", error);
        }
        return wishes;
    }

    public Wish findWishByName(String name){
        String query = "SELECT * FROM wishes WHERE wish_name = ?";
        try(Connection connection = getDBConnection()){
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return new Wish(
                        rs.getString("wish_name"),
                        rs.getString("wish_description"),
                        rs.getInt("wish_price"),
                        rs.getString("wish_url")
                );
            }
        }catch (SQLException error) {
            throw new RuntimeException("Error retrieving user from database", error);
        }
        return null;
    }

    public void updateWish(Wish updatedWish, String oldWishName){
        String querey = "UPDATE wishlists SET wish_name = ?, wish_price = ?, wish_description = ?, wish_url = ? wish_price = ?";
        try(Connection connection = getDBConnection()){
            PreparedStatement pstmt =  connection.prepareStatement(querey);
            pstmt.setString(1, updatedWish.getName());
            pstmt.setInt(2, updatedWish.getPrice());
            pstmt.setString(3, updatedWish.getDescription());
            pstmt.setString(4, updatedWish.getLink());
            pstmt.setString(5, oldWishName);

            pstmt.executeUpdate();
        }catch (SQLException error) {
            throw new RuntimeException("Error updating wish", error);
        }
    }

}
