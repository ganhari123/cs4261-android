package com.herokuapp.shopandgo.shopandgo;

/**
 * Created by bukbukbukh on 4/18/17.
 */

public class Diet {

    private String[] diet;
    private String other;

    public Diet(String[] diet, String other) {
        this.diet = diet;
        this.other = other;
    }

    public String[] getDiet() {
        return diet;
    }

    public String getOther() {
        return other;
    }

    public void setDiet(String[] diet) {
        this.diet = diet;
    }

    public void setOther(String other) {
        this.other = other;
    }
}