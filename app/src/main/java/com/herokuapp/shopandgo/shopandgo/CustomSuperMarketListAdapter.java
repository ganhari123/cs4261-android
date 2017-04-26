package com.herokuapp.shopandgo.shopandgo;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by bukbukbukh on 4/19/17.
 */

public class CustomSuperMarketListAdapter extends ArrayAdapter<SupermarketStore> {
    private final Context context;
    private final SupermarketStore[] objects;

    public CustomSuperMarketListAdapter(@NonNull Context context, @NonNull SupermarketStore[] objects) {
        super(context, -1, objects);
        this.context = context;
        this.objects = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.super_market_row, parent, false);
        ImageView google = (ImageView) rowView.findViewById(R.id.google_map_link);
        google.setImageResource(R.mipmap.google_maps);
        TextView supermarkettitle = (TextView) rowView.findViewById(R.id.super_market_name);
        supermarkettitle.setText(objects[position].getStoreName());
        TextView supermarketAddress = (TextView) rowView.findViewById(R.id.super_market_address);
        supermarketAddress.setText(objects[position].getAddress());
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + objects[position].getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                context.startActivity(mapIntent);
            }
        });
        return rowView;
    }

    public SupermarketStore[] getObjects() {
        return objects;
    }

}

