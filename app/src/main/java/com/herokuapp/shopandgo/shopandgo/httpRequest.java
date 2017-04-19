package com.herokuapp.shopandgo.shopandgo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ganhari123 on 4/8/2017.
 */

public class httpRequest {

    public static final String PREFERENCES = "PREFERENCES";

    public static AsyncHttpClient client = new AsyncHttpClient();

    public static void login(RequestParams params, final Context context, final String username) {

        client.get("http://shopandgo.herokuapp.com/android/login", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int i, Header[] headers, byte[] bytes) {
                String response = new String(bytes);
                Log.e("SD", response);
                if (response.equals("login successful")) {
                    Intent intent = new Intent(context, Dashboard.class);
                    SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.commit();
                    context.startActivity(intent);

                }
                ProgressDialogBox.hideBox(context);
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
}
