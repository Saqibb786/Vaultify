package com.example.bankingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class passwordforgetpage extends AppCompatActivity {

    private EditText verifyEmail, submitVerify, restPassword1, restPassword2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.passwordforgetpage);

        verifyEmail = findViewById(R.id.verifyemail);
        submitVerify = findViewById(R.id.submitverify);
        restPassword1 = findViewById(R.id.restpassword1);
        restPassword2 = findViewById(R.id.restpassword2);

        mAuth = FirebaseAuth.getInstance();
    }

    public void submitEmail(View view) {
        String email = verifyEmail.getText().toString();

        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(passwordforgetpage.this, "Password reset link sent to your email!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(passwordforgetpage.this, "Error sending reset email", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void verifyCode(View view) {
        String codeEntered = submitVerify.getText().toString();

        if (codeEntered.isEmpty()) {
            Toast.makeText(this, "Please enter the verification code", Toast.LENGTH_SHORT).show();
            return;
        }

   }

    public void resetPassword(View view) {
        String newPassword = restPassword1.getText().toString();
        String confirmPassword = restPassword2.getText().toString();

        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please enter both new passwords", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(this, "Password reset successful", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Error resetting password", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
