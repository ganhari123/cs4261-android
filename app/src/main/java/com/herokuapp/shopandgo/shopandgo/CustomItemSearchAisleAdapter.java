package com.herokuapp.shopandgo.shopandgo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
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
 * Created by bukbukbukh on 4/20/17.
 */

public class CustomItemSearchAisleAdapter extends ArrayAdapter<ItemAisleObject>{

    private final Context context;
    private final ItemAisleObject[] objects;

    public CustomItemSearchAisleAdapter(@NonNull Context context, @NonNull ItemAisleObject[] objects) {
        super(context, -1,  objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.item_list_aisle_row, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.item_title);
        TextView view = (TextView) rowView.findViewById(R.id.aisle_num);
        textView.setText(objects[position].getItemName());
        view.setText(objects[position].getAisleNumber());
        ImageView imageView = (ImageView) rowView.findViewById(R.id.item_pic);
        new CustomItemSearchAisleAdapter.DownloadImageTask((ImageView) rowView.findViewById(R.id.item_pic))
                .execute(objects[position].getItemImage());
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
