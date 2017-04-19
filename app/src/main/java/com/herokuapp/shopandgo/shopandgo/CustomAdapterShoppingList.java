package com.herokuapp.shopandgo.shopandgo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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

import java.io.InputStream;

/**
 * Created by bukbukbukh on 4/16/17.
 */

public class CustomAdapterShoppingList extends ArrayAdapter<ShoppingCartItem> {

    private final Context context;
    private final ShoppingCartItem[] objects;
    private int numberTicked;

    public CustomAdapterShoppingList(@NonNull Context context, @NonNull ShoppingCartItem[] objects) {
        super(context, -1,  objects);
        this.context = context;
        numberTicked = 0;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.shopping_cart_row_item, parent, false);
        CheckBox box = (CheckBox) rowView.findViewById(R.id.item_name);
        box.setText(objects[position].getItemName());

        box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.e("POSITION", String.valueOf(position));
                objects[position].setChecked(b);
                if (b) {
                    numberTicked++;
                } else {
                    numberTicked--;
                }
            }
        });
        ImageView img = (ImageView) rowView.findViewById(R.id.checked);
        if (objects[position].isCollected()) {
            img.setVisibility(View.VISIBLE);
            img.setImageResource(R.mipmap.check_mark);
        } else {
            img.setVisibility(View.INVISIBLE);
        }

        return rowView;
    }

    public ShoppingCartItem[] getObjects() {
        return objects;
    }

    public int getNumberTicked() {
        return numberTicked;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
