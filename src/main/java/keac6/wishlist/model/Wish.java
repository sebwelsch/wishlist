package keac6.wishlist.model;

public class Wish {
    private String name;
    private String description;
    private int price;
    private String link;

    public Wish(String name, String description, int price, String link) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.link = link;
    }

    public String getName(){

        return name;
    }

    public void setName(String name){

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