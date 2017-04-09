package com.herokuapp.shopandgo.shopandgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login_button = (Button) findViewById(R.id.login);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText username = (EditText) findViewById(R.id.user_name);
                EditText password = (EditText) findViewById(R.id.password);
                AsyncHttpClient client = new AsyncHttpClient();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                RequestParams params = new RequestParams();
                params.put("username", user);
                params.put("password", pass);
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
                            Intent intent = new Intent(Login.this, Dashboard.class);
                            startActivity(intent);
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
