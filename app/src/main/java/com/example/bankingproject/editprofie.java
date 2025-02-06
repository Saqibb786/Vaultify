package com.example.bankingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class editprofie extends AppCompatActivity {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editAccountNumber;
    private Button saveButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.editprofie);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        editUsername = findViewById(R.id.edit_username);
        editEmail = findViewById(R.id.edit_email);
        editAccountNumber = findViewById(R.id.edit_account_number);
        saveButton = findViewById(R.id.savebutton);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String username = extras.getString("holdername");
            String email = extras.getString("Email");
            String accountNumber = extras.getString("Account");

            editUsername.setText(username);
            editEmail.setText(email);
            editAccountNumber.setText(accountNumber);
        }

        saveButton.setOnClickListener(v -> {
            String updatedUsername = editUsername.getText().toString();
            String updatedEmail = editEmail.getText().toString();
            String updatedAccountNumber = editAccountNumber.getText().toString();

            if (updatedUsername.isEmpty() || updatedEmail.isEmpty() || updatedAccountNumber.isEmpty()) {
                Toast.makeText(editprofie.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            String userId = mAuth.getCurrentUser().getUid();
            User updatedUser = new User(updatedUsername, updatedEmail, updatedAccountNumber);

            mDatabase.child("users").child(userId).setValue(updatedUser)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(editprofie.this, "Changes Saved!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(editprofie.this, "Error saving changes", Toast.LENGTH_SHORT).show();
                        }
                    });
            finish();
        });
    }
}
