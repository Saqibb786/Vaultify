package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Confirmationscreen extends AppCompatActivity {

    private TextView donePayment, sendingName, sendingAmount, timeTransaction;
    private Button backToHomeButton;
    private DatabaseReference transactionsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmationscreen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        donePayment = findViewById(R.id.donepayment);
        sendingName = findViewById(R.id.sendingname);
        sendingAmount = findViewById(R.id.sendingamount);
        timeTransaction = findViewById(R.id.timetransection);
        backToHomeButton = findViewById(R.id.nextmoney);


        Intent intent = getIntent();
        String holderName = intent.getStringExtra("holdername");
        long amount = intent.getLongExtra("Amount", 0);
        long transactionTime = intent.getLongExtra("Transectiontime", 0);


        if (holderName != null) {
            sendingName.setText("Holder Name: " + holderName);
        } else {
            sendingName.setText("Holder Name: Unknown");
        }

        sendingAmount.setText("Amount: " + amount + " RS");

        if (transactionTime > 0) {
            timeTransaction.setText("Transaction Time: " + getFormattedDate(transactionTime));
        } else {
            timeTransaction.setText("Transaction Time: Unknown");
        }

        backToHomeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(Confirmationscreen.this, home.class);
            startActivity(homeIntent);
            finish();
        });
    }

    private String getFormattedDate(long timestamp) {
        if (timestamp <= 0) return "Unknown Time";
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(timestamp);
    }
}