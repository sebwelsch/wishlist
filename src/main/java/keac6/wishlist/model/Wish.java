package keac6.wishlist.model;

public class Wish {
    private String name;
    private String description;
    private Double price;
    private String link;

    public String getName(){

        return name;
    }

    public void setName(String name){

        this.name = name;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {

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