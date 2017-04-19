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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

/**
 * Created by bukbukbukh on 4/16/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<RecipeItem> {

    private final Context context;
    private final RecipeItem[] objects;

    public CustomArrayAdapter(@NonNull Context context, @NonNull RecipeItem[] objects) {
        super(context, -1,  objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.recipe_row_view, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.recipe_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        textView.setText(objects[position].getRecipe());
        new DownloadImageTask((ImageView) rowView.findViewById(R.id.image))
                .execute(objects[position].getImageURL());
        // change the icon for Windows and iPhone
        return rowView;
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
