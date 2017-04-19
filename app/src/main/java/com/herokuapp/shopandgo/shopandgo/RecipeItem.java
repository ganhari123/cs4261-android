package com.herokuapp.shopandgo.shopandgo;

import android.graphics.Bitmap;

/**
 * Created by bukbukbukh on 4/16/17.
 */

public class RecipeItem {

    protected String id;
    protected String recipe;
    protected String imageURL;
    protected Bitmap image;

    public RecipeItem(String id, String recipe, String imageURL) {
        this.id = id;
        this.recipe = recipe;
        this.imageURL = imageURL;
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getRecipe() {
        return recipe;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
