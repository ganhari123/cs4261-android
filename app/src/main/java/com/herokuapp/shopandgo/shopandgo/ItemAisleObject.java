package com.herokuapp.shopandgo.shopandgo;

/**
 * Created by bukbukbukh on 4/20/17.
 */

public class ItemAisleObject {
    String itemName;
    String itemID;
    String itemDescription;
    String itemImage;
    String AisleNumber;

    public ItemAisleObject() {
        this.itemName = "";
        this.itemID = "";
        this.itemDescription = "";
        this.itemImage = "";
        AisleNumber = "";
    }

    public ItemAisleObject(String itemName, String itemID, String itemDescription, String itemImage, String aisleNumber) {
        this.itemName = itemName;
        this.itemID = itemID;
        this.itemDescription = itemDescription;
        this.itemImage = itemImage;
        AisleNumber = aisleNumber;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public String getAisleNumber() {
        return AisleNumber;
    }

    public void setAisleNumber(String aisleNumber) {
        AisleNumber = aisleNumber;
    }
}
