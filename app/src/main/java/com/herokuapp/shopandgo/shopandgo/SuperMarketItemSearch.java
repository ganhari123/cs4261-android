package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import cz.msebera.android.httpclient.Header;

public class SuperMarketItemSearch extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_market_item_search);

        Intent intent = getIntent();
        String json = intent.getStringExtra("ListItem");
        Gson gson = new Gson();
        final SupermarketStore supermarket = (SupermarketStore) gson.fromJson(json, SupermarketStore.class);

        Button button = (Button) findViewById(R.id.search_item_aisle);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText) findViewById(R.id.item_name_aisle_search);
                String query = text.getText().toString();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                String requestString = "http://www.SupermarketAPI.com/api.asmx/SearchForItem?APIKEY=9699d4f2d0&StoreId="
                                        + supermarket.getStoreID()
                                        + "&ItemName="
                                        + query;
                client.get(requestString, new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // called before request is started
                    }

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("RecipeResult", new String(bytes));

                        try {
                            JSONObject object = XML.toJSONObject(new String(bytes));
                            Log.e("Error", object.toString());
                            JSONArray array = object.getJSONObject("ArrayOfProduct").getJSONArray("Product");
                            ItemAisleObject[] mainArray = new ItemAisleObject[array.length()];
                            for (int j = 0; j < array.length(); j++) {
                                JSONObject obj = array.getJSONObject(j);
                                mainArray[j] = new ItemAisleObject();
                                if (!obj.getString("Itemname").equals("null")) {
                                    mainArray[j].setItemName(obj.getString("Itemname"));
                                }
//
                                if (!obj.getString("ItemDescription").equals("null")) {
                                    mainArray[j].setItemDescription(obj.getString("ItemDescription"));
                                }

                                if (!obj.getString("ItemImage").equals("null")) {
                                    mainArray[j].setItemImage(obj.getString("ItemImage"));
                                }

                                if (!obj.getString("ItemID").equals("null")) {
                                    mainArray[j].setItemID(obj.getString("ItemID"));
                                }

                                if (!obj.getString("AisleNumber").equals("null")) {
                                    mainArray[j].setAisleNumber(obj.getString("AisleNumber"));
                                }

                            }
                            CustomItemSearchAisleAdapter adapter = new CustomItemSearchAisleAdapter(SuperMarketItemSearch.this, mainArray);
                            ListView view = (ListView) findViewById(R.id.item_list);
                            view.setAdapter(adapter);
                            ProgressDialogBox.hideBox(SuperMarketItemSearch.this);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                    }

                    @Override
                    public void onRetry(int retryNo) {
                        // called when request is retried
                    }
                });
            }
        });
    }
}
