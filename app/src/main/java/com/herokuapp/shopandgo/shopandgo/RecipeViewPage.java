package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

import cz.msebera.android.httpclient.Header;

public class RecipeViewPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_view_page);
        ProgressDialogBox.createBox(this);
        Intent intent = getIntent();
        String item = intent.getStringExtra("ListItem");
        //Log.e("ERROR", item);
        Gson gson = new Gson();
        RecipeItem mainItem = gson.fromJson(item, RecipeItem.class);
        TextView title = (TextView) findViewById(R.id.recipe_title);
        title.setText(mainItem.getRecipe());
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://food2fork.com/api/get?key=125aec03ba4a0ffe5222a72a9783b3b6&rId=" + mainItem.getId(), new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("ERROR", new String(bytes));
                try {
                    JSONObject obj = new JSONObject(new String(bytes));
                    obj = obj.getJSONObject("recipe");
                    JSONArray ingrediants = obj.getJSONArray("ingredients");
                    String[] ing = new String[ingrediants.length()];
                    for (int j = 0; j < ingrediants.length(); j++) {
                        ing[j] = ingrediants.getString(j);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RecipeViewPage.this, android.R.layout.simple_list_item_1, ing);
                    ListView newListView = (ListView) findViewById(R.id.ing_list) ;
                    newListView.setAdapter(adapter);
                    new DownloadImageTask((ImageView) findViewById(R.id.recipe_pic))
                            .execute(obj.getString("image_url"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ProgressDialogBox.hideBox(RecipeViewPage.this);

            }
        });
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
            ProgressDialogBox.hideBox(RecipeViewPage.this);
        }
    }
}
