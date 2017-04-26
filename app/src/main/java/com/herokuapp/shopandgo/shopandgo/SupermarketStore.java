package com.herokuapp.shopandgo.shopandgo;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class SupermarketStore {
    private String storeName;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String storeID;

    public SupermarketStore(String storeName, String address, String city, String state, String zipcode, String storeID) {
        this.storeName = storeName;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.storeID = storeID;
    }

    public SupermarketStore() {
        this.storeName = "";
        this.address = "";
        this.city = "";
        this.state = "";
        this.zipcode = "";
        this.storeID = "";
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }
}
