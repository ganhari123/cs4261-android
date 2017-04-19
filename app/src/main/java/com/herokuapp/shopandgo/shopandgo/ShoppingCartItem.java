package com.herokuapp.shopandgo.shopandgo;

/**
 * Created by bukbukbukh on 4/18/17.
 */

public class ShoppingCartItem {

    String itemName;
    double quantity;
    String unit;
    boolean isCollected;
    boolean isChecked;

    public ShoppingCartItem(String itemName, double quantity, String unit, boolean isCollected) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.unit = unit;
        this.isCollected = isCollected;
    }

    public String getItemName() {
        return itemName;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
