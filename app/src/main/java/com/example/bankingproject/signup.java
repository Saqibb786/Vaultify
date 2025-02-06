// filepath: /C:/Users/Saqib/OneDrive - Punjab Group of Colleges/UCP/Semester 5/Mobile Application Development (E14) - Ihtisham Ul Haq/Assesments/Project/BankingProject/app/src/main/java/com/example/bankingproject/signup.java
package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    private EditText pas, em, us;
    private Button signup, loginb;

    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        dbReference = FirebaseDatabase.getInstance().getReference("Users");

        pas = findViewById(R.id.password);
        em = findViewById(R.id.email);
        us = findViewById(R.id.username);
        signup = findViewById(R.id.signup);
        loginb = findViewById(R.id.login);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });

        loginb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(signup.this, login.class));
            }
        });
    }

    private void registerUser() {
        String email = em.getText().toString().trim();
        String password = pas.getText().toString().trim();
        String username = us.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            em.setError("Email is required");
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            em.setError("Please enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            pas.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            pas.setError("Password must be at least 6 characters");
            return;
        }

        if (TextUtils.isEmpty(username)) {
            us.setError("Username is required");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        User newUser = new User(username, email);
                        dbReference.child(user.getUid()).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(signup.this, "User registered successfully", Toast.LENGTH_SHORT)
                                                .show();
                                        startActivity(new Intent(signup.this, login.class));
                                    } else {
                                        Toast.makeText(signup.this, "Failed to register user", Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                });
                    } else {
                        Toast.makeText(signup.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class User {
        public String username;
        public String email;

        public User() {
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }
    }
}