package keac6.wishlist.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class WishListRepository {

    private JdbcTemplate jdbcTemplate;

    public WishListRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createWishlist(String wishlistName){
        String sql = "INSERT INTO wishlists (wishlist_name) VALUES (?);";
        jdbcTemplate.update(sql, wishlistName);
    }

}
