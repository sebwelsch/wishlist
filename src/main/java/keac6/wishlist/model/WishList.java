package keac6.wishlist.model;

public class WishList {
    private int wishListId;
    private int userId;
    private String wishListName;

    public WishList() {
    }

    public WishList(int userId, String wishListName) {
        this.userId = userId;
        this.wishListName = wishListName;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getWishListName() {
        return wishListName;
    }

    public void setWishListName(String wishListName) {
        this.wishListName = wishListName;
    }
}