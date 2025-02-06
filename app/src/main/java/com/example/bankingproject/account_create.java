package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class account_create extends AppCompatActivity {

    private EditText nameInput, mobileInput, passwordInput;
    private Button next;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private String firstRegisteredUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_create);

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        mAuth = FirebaseAuth.getInstance();

        nameInput = findViewById(R.id.name);
        mobileInput = findViewById(R.id.mobilenumb);
        passwordInput = findViewById(R.id.passaccount);
        next = findViewById(R.id.verifmobilebtn);

       getFirstRegisteredUserId();

        setupMobileInput();

      next.setOnClickListener(v -> createAccount());
    }

    private void getFirstRegisteredUserId() {
        databaseReference.orderByKey().limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        firstRegisteredUserId = child.getKey();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void setupMobileInput() {
        InputFilter[] filters = new InputFilter[] {
                new InputFilter.LengthFilter(13)
        };
        mobileInput.setFilters(filters);

       mobileInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
          }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int after) {
             String input = charSequence.toString();
                if (input.length() == 0) {
                    mobileInput.setText("+92");
                    mobileInput.setSelection(mobileInput.getText().length());
                } else if (input.length() > 0 && !input.startsWith("+92")) {
                    mobileInput.setText("+92");
                    mobileInput.setSelection(mobileInput.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    private void createAccount() {
        String name = nameInput.getText().toString().trim();
        String mobile = mobileInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mobile) || !mobile.startsWith("+92") || mobile.length() != 13) {
            Toast.makeText(this, "Enter a valid mobile number in +92 format", Toast.LENGTH_SHORT).show();
            Log.i("PhoneNumber", "Phone Number Sent: " + mobile);
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        databaseReference.orderByChild("mobile").equalTo(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
               Toast.makeText(account_create.this, "Mobile number already registered. Please log in.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(account_create.this, login.class);  // You should define your login activity
                    startActivity(intent);
                    finish();
                } else {


                    if (firstRegisteredUserId != null) {
                        DatabaseReference userRef = databaseReference.child(firstRegisteredUserId).child("child_accounts").push();
                        User user = new User(name, password, mobile);

                        userRef.setValue(user).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                Toast.makeText(account_create.this, "Account created successfully under first signup user", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(account_create.this, home.class);
                                intent.putExtra("name",name);
                                intent.putExtra("acnumb",mobile);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(account_create.this, "Account creation failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(account_create.this, "Error: No registered user found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("FirebaseError", "Error checking mobile number", databaseError.toException());
            }
        });
    }
}
