package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class Profile extends AppCompatActivity {


    public static final String PREFS_NAME = "AOP_PREFS";

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_profile);
        mAuth = FirebaseAuth.getInstance();
        ProgressDialogBox.createBox(this);
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
            TextView welcomeText = (TextView) findViewById(R.id.welcome_text);
            welcomeText.setText("Welcome " + user.getUid());
        }

        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("username", user.getUid());
        client.get("http://shopandgo.herokuapp.com/android/profile", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.e("SD", response);
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject object = array.getJSONObject(0);
                    Log.e("ERROR", object.getString("dietrest"));
                    if (object.getString("dietrest").equals("null")) {
                        Log.e("NULL", "NULL");
                    } else {
                            JSONObject diet = new JSONObject(object.getString("dietrest"));
                            if (diet.has("diet")) {
                                String dietDetails = diet.getString("diet");
                                if (dietDetails.charAt(0) == '[') {
                                    Log.e("IN", "IN");
                                    array = diet.getJSONArray("diet");
                                    String[] dietResults = new String[array.length()];
                                    for (int j = 0; j < array.length(); j++) {
                                        dietResults[j] = array.getString(j);
                                        getCorrectCheckBox(array.getString(j));
                                    }
                                } else {
                                    String onlyDiet = diet.getString("diet");
                                    getCorrectCheckBox(onlyDiet);
                                }
                            }
                            String other = diet.getString("other");
                            EditText otherRes = (EditText) findViewById(R.id.other);
                            otherRes.setHint(other);
                    }
                    ProgressDialogBox.hideBox(Profile.this);
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


        Button logout = (Button) findViewById(R.id.log_out);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(Profile.this);
                SharedPreferences prefs = Profile.this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                String login_type = prefs.getString("login_type", "");
                if (login_type.equals("firebase")) {
                    mAuth.signOut();
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.commit();
                    ProgressDialogBox.hideBox(Profile.this);
                    Intent intent = new Intent(Profile.this, Login.class);
                    startActivity(intent);
                }
            }
        });

        Button update = (Button) findViewById(R.id.update_profile);
        update.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  ProgressDialogBox.createBox(Profile.this);
                  String dietBody = getAllCheckedBoxes();
                  AsyncHttpClient client = new AsyncHttpClient();

                  RequestParams params = new RequestParams();
                  params.put("username", user.getUid());
                  params.put("profile", dietBody);
                  client.post("http://shopandgo.herokuapp.com/android/updateProfile", params, new AsyncHttpResponseHandler() {
                      @Override
                      public void onStart() {
                          // called before request is started
                      }

                      @Override
                      public void onSuccess(int i, Header[] headers, byte[] bytes) {
                          Log.e("ERROR", new String(bytes));
                          ProgressDialogBox.hideBox(Profile.this);
                          finish();
                          startActivity(getIntent());
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
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.


    }

    private void getCorrectCheckBox(String diet) {
        if (diet.equals("veg")) {
            CheckBox box = (CheckBox) findViewById(R.id.veg);
            box.setChecked(true);
        } else if (diet.equals("vegan")) {
            CheckBox box = (CheckBox) findViewById(R.id.vegan);
            box.setChecked(true);
        } else if (diet.equals("celiac")) {
            CheckBox box = (CheckBox) findViewById(R.id.gluten_allergy);
            box.setChecked(true);
        } else if (diet.equals("nut")) {
            CheckBox box = (CheckBox) findViewById(R.id.nut_allergy);
            box.setChecked(true);
        } else if (diet.equals("lactose")) {
            CheckBox box = (CheckBox) findViewById(R.id.lactose);
            box.setChecked(true);
        }
    }

    private String getAllCheckedBoxes() {
        ArrayList<String> dietList = new ArrayList<>();
        CheckBox box = (CheckBox) findViewById(R.id.veg);
        if (box.isChecked()) {
            dietList.add("veg");
        }
        box = (CheckBox) findViewById(R.id.vegan);
        if (box.isChecked()) {
            dietList.add("vegan");
        }
        box = (CheckBox) findViewById(R.id.gluten_allergy);
        if (box.isChecked()) {
            dietList.add("celiac");
        }

        box = (CheckBox) findViewById(R.id.nut_allergy);

        if (box.isChecked()) {
            dietList.add("nut");
        }

        box = (CheckBox) findViewById(R.id.lactose);

        if (box.isChecked()) {
            dietList.add("lactose");
        }
        String[] dietSet = new String[dietList.size()];
        for (int i = 0; i < dietList.size(); i++) {
            dietSet[i] = dietList.get(i);
        }
        Diet diet = new Diet(dietSet, "");
        Gson gson = new Gson();
        String json = gson.toJson(diet);
        Log.e("ERROR", json);
        return json;

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.

    }


}
