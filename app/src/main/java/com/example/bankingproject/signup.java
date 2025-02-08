package com.example.bankingproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup extends AppCompatActivity {

    private EditText pas, em, us;
    private Button signup,loginb;

    private DatabaseReference dbReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        pas = findViewById(R.id.password);
        em = findViewById(R.id.email);
        us = findViewById(R.id.username);
        signup = findViewById(R.id.btnSignUp);
        loginb=findViewById(R.id.allreadylogin);

        dbReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        signup.setOnClickListener(v -> {
            String username = us.getText().toString().trim();
            String email = em.getText().toString().trim();
            String password = pas.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                us.setError("Username is required!");
                us.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                em.setError("Enter a valid email!");
                em.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password) || password.length() < 6) {
                pas.setError("Password must be at least 6 characters long!");
                pas.requestFocus();
                return;
            }

            registerUser(email, password, username);
        });

        loginb.setOnClickListener(v -> {
            Intent intentt = new Intent(signup.this, login.class);
            startActivity(intentt);
        });
    }

    private void registerUser(String email, String password, String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        saveUserData(userId, username, email,password);
                    } else {
                       Toast.makeText(signup.this, "Email Allready Exist", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveUserData(String userId, String username, String email,String password) {
        struct userData = new struct(username, email, password);

        dbReference.child(userId).setValue(userData).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Signup", "User data saved successfully!");
                Toast.makeText(signup.this, "Sign-Up successful!", Toast.LENGTH_SHORT).show();


                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(signup.this, login.class);
                startActivity(intent);
                finish();
            } else {
                Log.e("Signup", "Failed to save user data: " + task.getException().getMessage());
                Toast.makeText(signup.this, "Failed to save user data. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void facebooklink(View view) {
        Intent a = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/login.php/"));
        startActivity(a);
        Toast.makeText(this, "Facebook page", Toast.LENGTH_SHORT).show();
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