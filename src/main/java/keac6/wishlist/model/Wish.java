package keac6.wishlist.model;

public class Wish {
    private int id;
    private String name;
    private String description;
    private int price;
    private String link;
    private boolean reserved;


    public Wish(){}

    public Wish(int id, String name, String description, int price, String link, boolean reserved) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.link = link;
        this.reserved = reserved;
    }

    public boolean isReserved() {
        return reserved;
    }
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public int getPrice() {

        return price;
    }

    public void setPrice(int price) {

        this.price = price;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }

    public String getLink() {

        return link;
    }

    public void setLink(String link) {

        this.link = link;
    }


}