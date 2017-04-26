package com.herokuapp.shopandgo.shopandgo;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class NutritionItem {

    protected String itemID;
    protected String itemName;
    protected String brandName;
    protected double calories;
    protected double totalFat;
    protected int servingSize;

    public NutritionItem(String itemID, String itemName, String brandName, double calories, double totalFat, int servingSize) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.brandName = brandName;
        this.calories = calories;
        this.totalFat = totalFat;
        this.servingSize = servingSize;
    }

    public NutritionItem() {
        this.itemID = "";
        this.itemName = "";
        this.brandName = "";
        this.calories = 0.00;
        this.totalFat = 0.00;
        this.servingSize = 0;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public double getTotalFat() {
        return totalFat;
    }

    public void setTotalFat(double totalFat) {
        this.totalFat = totalFat;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }
}
