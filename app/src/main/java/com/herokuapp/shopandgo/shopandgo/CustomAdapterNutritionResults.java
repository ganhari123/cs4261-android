package com.herokuapp.shopandgo.shopandgo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class CustomAdapterNutritionResults extends ArrayAdapter<NutritionItem> {
    private final Context context;
    private final NutritionItem[] objects;

    public CustomAdapterNutritionResults(@NonNull Context context, @NonNull NutritionItem[] objects) {
        super(context, -1, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.nutrition_row_view, parent, false);
        TextView itemName = (TextView) rowView.findViewById(R.id.nutrition_item_name);
        TextView itemBrand = (TextView) rowView.findViewById(R.id.nutrition_brand_name);
        TextView itemCalories = (TextView) rowView.findViewById(R.id.calories);
        itemName.setText(objects[position].getItemName());
        itemBrand.setText("Brand: " + objects[position].getBrandName());
        itemCalories.setText("Calories: " + String.valueOf(objects[position].getCalories()));
        return rowView;
    }

    public NutritionItem[] getObjects() {
        return objects;
    }

}
