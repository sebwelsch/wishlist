package keac6.wishlist.model;

public class Wish {
    private int id;
    private int wishListId;
    private String name;
    private String description;
    private int price;
    private String link;
    private boolean reserved;
    private int reservedByUserId;

    public Wish() {
    }

    public Wish(int id, int wishListId, String name, String description, int price, String link, boolean reserved, int reservedByUserId) {
        this.id = id;
        this.wishListId = wishListId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.link = link;
        this.reserved = reserved;
        this.reservedByUserId = reservedByUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getReservedByUserId() {
        return reservedByUserId;
    }

    public void setReservedByUserId(int reservedByUserId) {
        this.reservedByUserId = reservedByUserId;
    }
}