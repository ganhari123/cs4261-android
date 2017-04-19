package com.herokuapp.shopandgo.shopandgo;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.net.Uri;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.loopj.android.http.*;

import cz.msebera.android.httpclient.Header;

import static com.herokuapp.shopandgo.shopandgo.httpRequest.PREFERENCES;

public class Login extends AppCompatActivity {

    public static final String PREFS_NAME = "AOP_PREFS";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        Button login_button = (Button) findViewById(R.id.login);

// ...
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("HELLO", "onAuthStateChanged:signed_in:" + user.getUid());
                    SharedPreferences prefs = Login.this.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    Gson gson = new Gson();
                    String json = gson.toJson(user);
                    editor.putString("user", json);
                    editor.putString("login_type", "firebase");
                    editor.commit();
                    Intent intent = new Intent(Login.this, Dashboard.class);
                    Login.this.startActivity(intent);
                } else {
                    // User is signed out
                    Log.d("HELLO", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialogBox.createBox(Login.this);
                EditText username = (EditText) findViewById(R.id.user_name);
                EditText password = (EditText) findViewById(R.id.password);
                AsyncHttpClient client = new AsyncHttpClient();
                final String user = username.getText().toString();
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
                        if (!response.equals("either username or password is wrong")) {

                            mAuth.signInWithCustomToken(response).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            Log.d("Hello", "signInWithCustomToken:onComplete:" + task.isSuccessful());

                                            // If sign in fails, display a message to the user. If sign in succeeds
                                            // the auth state listener will be notified and logic to handle the
                                            // signed in user can be handled in the listener.
                                            if (!task.isSuccessful()) {
                                                Log.w("hello", "signInWithCustomToken", task.getException());
                                                Toast.makeText(Login.this, "Authentication failed.",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                            ProgressDialogBox.hideBox(Login.this);

                                        }
                                    });

                        }
                        ProgressDialogBox.hideBox(Login.this);

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

        TextView registerLink = (TextView) findViewById(R.id.register_link);
        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewIntent = new Intent("android.intent.action.VIEW",
                                Uri.parse("http://shopandgo.herokuapp.com/register"));
                startActivity(viewIntent);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        // ...
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        // ...
    }
}
