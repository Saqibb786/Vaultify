package com.example.bankingproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {

    private EditText email, password;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.btnLogin);

        mAuth = FirebaseAuth.getInstance();

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Log.d("Login", "User is already logged in. Redirecting to home screen.");
            Intent homeIntent = new Intent(login.this, home.class);
            startActivity(homeIntent);
            finish();
        } else {
            Log.d("Login", "No user is logged in. Showing login screen.");
        }

        loginButton.setOnClickListener(v -> {
            String enteredEmail = email.getText().toString().trim();
            String enteredPassword = password.getText().toString().trim();

            if (TextUtils.isEmpty(enteredEmail)) {
                email.setError("Please enter your email!");
                email.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(enteredPassword)) {
                password.setError("Password is required!");
                password.requestFocus();
                return;
            }

            loginUser(enteredEmail, enteredPassword);
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Log.d("Login", "Login successful! User ID: " + user.getUid());
                            Toast.makeText(login.this, "Login successful!", Toast.LENGTH_SHORT).show();
                            Intent homeIntent = new Intent(login.this, account_create.class);
                            startActivity(homeIntent);
                            finish();
                        }
                    } else {
                        Log.e("Login", "Login failed: " + task.getException().getMessage());
                        Toast.makeText(login.this, "Invalid email or password!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void newaccountsignup(View view) {
        Intent i = new Intent(login.this, signup.class);
        startActivity(i);
    }

    public void facebooklink(View view) {
        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login.php/"));
        startActivity(a);
        Toast.makeText(this, "Facebook page", Toast.LENGTH_SHORT).show();
    }

    public void forgetpassword(View view) {
        Intent i = new Intent(login.this, passwordforgetpage.class);
        startActivity(i);
    }

    public void googlelink(View view) {
        Intent g = new Intent(Intent.ACTION_VIEW, Uri.parse("https://accounts.google.com/v3/signin/identifier?authuser=0&continue=https%3A%2F%2Fmyaccount.google.com%2F%3Fhl%3Den%26utm_source%3DOGB%26utm_medium%3Dact%26gar%3DWzJd&ec=GAlAwAE&hl=en&service=accountsettings&flowName=GlifWebSignIn&flowEntry=AddSession&dsh=S-837202237%3A1731258926523158&ddm=1"));
        startActivity(g);
        Toast.makeText(this, "Google page", Toast.LENGTH_SHORT).show();
    }

    public void xlink(View view) {
        Intent g = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=twitter+login+page+link&oq=twitter+login+page+link&gs_lcrp=EgZjaHJvbWUyBggAEEUYOTINCAEQABiGAxiABBiKBTIKCAIQABiABBiiBDIKCAMQABiABBiiBDIHCAQQABjvBTIHCAUQABjvBTIHCAYQABjvBdIBCDk0NjBqMGo3qAIAsAIA&sourceid=chrome&ie=UTF-8"));
        startActivity(g);
        Toast.makeText(this, "Twitter page", Toast.LENGTH_SHORT).show();
    }
}