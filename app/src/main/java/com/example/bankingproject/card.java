package com.example.bankingproject;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class card extends AppCompatActivity {

    private TextView cardNumberTextView;
    private TextView cardHolderNameTextView;
    private TextView expirationDateTextView;
    private Switch internationalTransactionsSwitch;
    private Switch onlineTransactionsSwitch;
    private ImageView copyIcon;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.card);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        cardNumberTextView = findViewById(R.id.card_number);
        cardHolderNameTextView = findViewById(R.id.cardholder_name);
        expirationDateTextView = findViewById(R.id.expiration_date);
        internationalTransactionsSwitch = findViewById(R.id.switch1);
        onlineTransactionsSwitch = findViewById(R.id.switch2);
        copyIcon = findViewById(R.id.copy);

        String cardNumber = generateCardNumber();
        String cardHolderName = "Ali Irfan";
        String expirationDate = generateExpirationDate();

        cardNumberTextView.setText(cardNumber);
        cardHolderNameTextView.setText(cardHolderName);
        expirationDateTextView.setText(expirationDate);

        internationalTransactionsSwitch.setChecked(true);
        onlineTransactionsSwitch.setChecked(false);

        internationalTransactionsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(card.this, "International Transactions Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(card.this, "International Transactions Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        onlineTransactionsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(card.this, "Online Transactions Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(card.this, "Online Transactions Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        copyIcon.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setText(cardNumber);
                Toast.makeText(card.this, "Card Number Copied", Toast.LENGTH_SHORT).show();
            }
        });

        saveCardDataToDatabase(cardNumber, cardHolderName, expirationDate);
    }

    private String generateCardNumber() {
        return "0000 " + (int)(Math.random() * 1000000000);
    }

    private String generateExpirationDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 2);
        Date expirationDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yy");
        return "VALID THRU: " + dateFormat.format(expirationDate);
    }

    private void saveCardDataToDatabase(String cardNumber, String cardHolderName, String expirationDate) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String userId = mAuth.getCurrentUser().getUid();

        CardData cardData = new CardData(cardNumber, cardHolderName, expirationDate);

        mDatabase.child("users").child(userId).child("card_info").setValue(cardData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(card.this, "Card data saved to database", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(card.this, "Failed to save card data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class CardData {
        public String cardNumber;
        public String cardHolderName;
        public String expirationDate;

        public CardData() {
        }

        public CardData(String cardNumber, String cardHolderName, String expirationDate) {
            this.cardNumber = cardNumber;
            this.cardHolderName = cardHolderName;
            this.expirationDate = expirationDate;
        }
    }
}
