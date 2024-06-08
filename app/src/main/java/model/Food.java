package model;

import java.io.Serializable;

public class Food implements Serializable {
    private String foodId;

    private String foodname;

    private float foodprice;

    private String description;

    private String image;

    public Food(String bookId, String foodname, float foodprice, String description, String image) {
        this.foodId = bookId;
        this.foodname = foodname;
        this.foodprice = foodprice;
        this.description = description;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public float getFoodprice() {
        return foodprice;
    }

    public String getFoodname() {
        return foodname;
    }


    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }


    public void setFoodprice(float foodprice) {
        this.foodprice = foodprice;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
