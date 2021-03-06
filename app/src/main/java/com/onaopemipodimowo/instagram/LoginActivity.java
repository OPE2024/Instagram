package com.onaopemipodimowo.instagram;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    public  static final String TAG = "loginActivity";
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstancesState){
        super.onCreate(savedInstancesState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null){
            goMainActivity();
        }

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick signUp button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                SignUpUser(username, password);

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick login button");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                loginUser(username, password);

            }
        });{
            }
        }
        private void SignUpUser(String username, String password){
            ParseUser.logOut();
            ParseUser user = new ParseUser();
            user.setPassword(password);
            user.setUsername(username);

            user.signUpInBackground(e -> {
                if (e == null){
                    Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    goMainActivity();
                }else {
                    Toast.makeText(LoginActivity.this, "Failed to make account", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void loginUser(String username, String password) {
            Log.i(TAG,"Attempting to login user" + username);
            ParseUser.logInInBackground(username, password, new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (e!= null){
                        Log.e(TAG,"Issue with login", e);
                        return;
                    }
                    // TODO: navigate to the main activity if the user has signed in properly
                    goMainActivity();
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
            });
        }
        private void goMainActivity() {
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
    }

    }

