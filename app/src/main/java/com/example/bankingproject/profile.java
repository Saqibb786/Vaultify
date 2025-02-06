package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import androidx.appcompat.app.AppCompatActivity;

public class profile extends AppCompatActivity {

    private TextView usernameTextView;
    private TextView emailTextView;
    private TextView accountNumberTextView;
    private Button editProfileButton;
    private Button settingsButton;
    private Button logoutButton;

    private DatabaseReference dbRef;
    private FirebaseUser currentUser;

    private static final String USERS_NODE = "users";
    private static final String ACCOUNTS_NODE = "child_accounts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        accountNumberTextView = findViewById(R.id.acountnumber);
        editProfileButton = findViewById(R.id.edit_profile);
        settingsButton = findViewById(R.id.settings);
        logoutButton = findViewById(R.id.logout);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            dbRef = FirebaseDatabase.getInstance().getReference(USERS_NODE).child(currentUser.getUid());
            fetchUserDetails();
        } else {
            Toast.makeText(this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        editProfileButton.setOnClickListener(v -> {
            Intent editProfileIntent = new Intent(profile.this, editprofie.class);
            startActivity(editProfileIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(profile.this, setting.class);
            startActivity(settingsIntent);
        });

        logoutButton.setOnClickListener(v -> {
            if (currentUser != null) {
                FirebaseAuth.getInstance().signOut();
                Intent loginIntent = new Intent(profile.this, login.class);
                startActivity(loginIntent);
                finish();
                Toast.makeText(profile.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(profile.this, "No user is logged in.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserDetails() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(profile.this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        dbRef.child("users").child(userId).child("child_accounts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            for (DataSnapshot accountSnapshot : snapshot.getChildren()) {
                                String accountNumber = accountSnapshot.child("accountNumber").getValue(String.class);
                                String username = accountSnapshot.child("name").getValue(String.class);
                                if (accountNumber != null && username != null) {
                                    new Handler(Looper.getMainLooper()).post(() -> {
                                        if (usernameTextView != null && accountNumberTextView != null && emailTextView != null) {
                                            usernameTextView.setText(username);
                                            accountNumberTextView.setText(accountNumber);
                                            emailTextView.setText(currentUser.getEmail());
                                        }
                                    });
                                } else {
                                    Toast.makeText(profile.this, "Account data is incomplete.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Log.e("Firebase", "No child accounts found for userId: " + userId);
                            Toast.makeText(profile.this, "No account data found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Error fetching data: " + error.getMessage());
                        Toast.makeText(profile.this, "Failed to load user details: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



}
