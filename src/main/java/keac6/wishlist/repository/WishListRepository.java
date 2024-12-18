package keac6.wishlist.repository;

import keac6.wishlist.model.User;
import keac6.wishlist.model.Wish;
import keac6.wishlist.model.WishList;
import org.springframework.beans.factory.annotation.Value;
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

    public User getUserByEmail(String email) {
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

    public User getUserById(int id) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, id);
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


    public void addWishList(WishList newWishList) {
        String query = "INSERT INTO wishlists (wishlist_name, user_id) VALUES (?, ?)";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, newWishList.getWishListName());
            pstmt.setInt(2, newWishList.getUserId());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newWishList.setWishListId(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error saving new wish", error);
        }
    }

    public void deleteWishList(int wishListId) {
        String query = "DELETE FROM wishlists WHERE wishlist_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, wishListId);

            pstmt.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Error deleting wishlist", error);
        }
    }

    public ArrayList<WishList> getAllWishLists(int userid) {
        ArrayList<WishList> list = new ArrayList<>();
        String query = "SELECT * FROM wishlists WHERE user_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, userid);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WishList wishList = new WishList(
                        rs.getInt("wishlist_id"),
                        rs.getInt("user_id"),
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
                        rs.getInt("user_id"),
                        rs.getString("wishlist_name")
                );
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving wish list from database", error);
        }
        return null;
    }

    public void addWish(Wish newWish) {
        String query = "INSERT INTO wishes (wishlist_id, wish_name, wish_price, wish_description, wish_url) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, newWish.getWishListId());
            pstmt.setString(2, newWish.getName());
            pstmt.setInt(3, newWish.getPrice());
            pstmt.setString(4, newWish.getDescription());
            pstmt.setString(5, newWish.getLink());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        newWish.setId(generatedKeys.getInt(1));
                    }
                }
            }

        } catch (SQLException error) {
            throw new RuntimeException("Error saving new wish", error);
        }
    }

    public void deleteWish(int wishId) {
        String query = "DELETE FROM wishes WHERE wish_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, wishId);

            pstmt.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Error deleting wish", error);
        }
    }

    public Wish getWishById(int wish_Id) {
        String query = "SELECT * FROM wishes WHERE wish_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, wish_Id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Wish(
                        rs.getInt("wish_id"),
                        rs.getInt("wishlist_id"),
                        rs.getString("wish_name"),
                        rs.getString("wish_description"),
                        rs.getInt("wish_price"),
                        rs.getString("wish_url"),
                        rs.getBoolean("reserved"),
                        rs.getInt("reserved_by_user_id")
                );
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving user from database", error);
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
                        rs.getInt("wish_id"),
                        rs.getInt("wishlist_id"),
                        rs.getString("wish_name"),
                        rs.getString("wish_description"),
                        rs.getInt("wish_price"),
                        rs.getString("wish_url"),
                        rs.getBoolean("reserved"),
                        rs.getInt("reserved_by_user_id")
                ));
            }
        } catch (SQLException error) {
            throw new RuntimeException("Error retrieving wishes from database", error);
        }
        return wishes;
    }

    public void reserveWish(boolean reserved, int reservedByUserId, int wishId) {
        String query = "UPDATE wishes SET reserved = ?, reserved_by_user_id = ? WHERE wish_id = ?";
        try (Connection connection = getDBConnection()) {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setBoolean(1, reserved);
            pstmt.setInt(2, reservedByUserId);
            pstmt.setInt(3, wishId);

            pstmt.executeUpdate();
        } catch (SQLException error) {
            throw new RuntimeException("Error updating wish", error);
        }
    }
}
