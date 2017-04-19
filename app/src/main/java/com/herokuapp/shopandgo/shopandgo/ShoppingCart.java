package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class ShoppingCart extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ProgressDialogBox.createBox(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("HELLO", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("HELLO", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Log.e("USER", user.getUid());
        }

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("username", user.getUid());
        client.get("http://shopandgo.herokuapp.com/android/getShoppingCart", params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                Log.e("Error", new String(bytes));
                try {
                    JSONArray array = new JSONArray(new String(bytes));
                    ShoppingCartItem[] arr = new ShoppingCartItem[array.length()];
                    for (int j = 0; j < array.length(); j++) {
                        JSONObject obj = array.getJSONObject(j);
                        if (obj.getInt("isCollected") == 1){
                            arr[j] = new ShoppingCartItem(obj.getString("ItemName"), obj.getDouble("Quantity"), obj.getString("Unit"), true);
                        } else {
                            arr[j] = new ShoppingCartItem(obj.getString("ItemName"), obj.getDouble("Quantity"), obj.getString("Unit"), false);
                        }

                    }
                    CustomAdapterShoppingList adapter = new CustomAdapterShoppingList(ShoppingCart.this, arr);
                    ListView view = (ListView) findViewById(R.id.shopping_list);
                    view.setAdapter(adapter);
                    ProgressDialogBox.hideBox(ShoppingCart.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                ProgressDialogBox.hideBox(ShoppingCart.this);

            }
        });

        Button addCart = (Button) findViewById(R.id.add_item_cart);
        addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(ShoppingCart.this);
                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();

                EditText itemName = (EditText) findViewById(R.id.item_name);
                EditText quant = (EditText) findViewById(R.id.quant);
                EditText unit = (EditText) findViewById(R.id.unit);
                params.put("itemName", itemName.getText().toString());
                params.put("Quantity", quant.getText().toString());
                params.put("Unit", unit.getText().toString());
                params.put("username", user.getUid());
                params.put("isCollected", 0);
                client.post("http://shopandgo.herokuapp.com/android/addToShoppingCart", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("FINISHED", new String(bytes));
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                    }
                });

            }
        });

        Button deleteItems = (Button) findViewById(R.id.delete_item);

        deleteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(ShoppingCart.this);
                ListView listView= (ListView) findViewById(R.id.shopping_list);
                CustomAdapterShoppingList adapter = (CustomAdapterShoppingList) listView.getAdapter();
                ShoppingCartItem[] arr = adapter.getObjects();
                Log.e("LENGTH", String.valueOf(arr.length));
                String[] itemsToDelete = new String[adapter.getNumberTicked()];
                int k = 0;
                for (int j = 0; j < arr.length; j++) {
                    if (arr[j].isChecked()) {
                        itemsToDelete[k] = arr[j].getItemName();
                        k++;
                    }
                }
                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();
                Gson gson = new Gson();
                params.put("item", gson.toJson(itemsToDelete));

                params.put("username", user.getUid());
                client.post("http://shopandgo.herokuapp.com/android/deleteItems", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("error", new String(bytes));
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                    }
                });

            }
        });

        Button checkItems = (Button) findViewById(R.id.check_item);
        checkItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(ShoppingCart.this);
                ListView listView= (ListView) findViewById(R.id.shopping_list);
                CustomAdapterShoppingList adapter = (CustomAdapterShoppingList) listView.getAdapter();
                ShoppingCartItem[] arr = adapter.getObjects();
                Log.e("LENGTH", String.valueOf(arr.length));
                String[] itemsToCheck = new String[adapter.getNumberTicked()];
                int k = 0;
                for (int j = 0; j < arr.length; j++) {
                    if (arr[j].isChecked()) {
                        itemsToCheck[k] = arr[j].getItemName();
                        k++;
                    }
                }
                AsyncHttpClient client = new AsyncHttpClient();

                RequestParams params = new RequestParams();
                Gson gson = new Gson();
                params.put("item", gson.toJson(itemsToCheck));

                params.put("username", user.getUid());
                client.post("http://shopandgo.herokuapp.com/android/checkItems", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {
                        Log.e("error", new String(bytes));
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                        finish();
                        startActivity(getIntent());
                    }

                    @Override
                    public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                        ProgressDialogBox.hideBox(ShoppingCart.this);
                    }
                });

            }
        });


    }
}
