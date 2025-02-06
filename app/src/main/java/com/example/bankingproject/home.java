package com.example.bankingproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class home extends AppCompatActivity {

    private TextView wallet, lastActivity1, lastActivity3,lastActivity4,lastActivity2, username;
    private DatabaseReference dbRef;
    private String userId = "currentUserId";
    private ProgressBar loadingProgressBar;

    private static final String USERS_NODE = "users";
    private static final String CHILD_ACCOUNTS_NODE = "child_accounts";
    private static final String TRANSACTIONS_NODE = "transactions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        wallet = findViewById(R.id.wallet);
        lastActivity1 = findViewById(R.id.last_activity1);
        lastActivity2 = findViewById(R.id.last_activity2);
        loadingProgressBar = findViewById(R.id.loadingProgressBar);
        username = findViewById(R.id.user);
        lastActivity3 = findViewById(R.id.last_activity3);
        lastActivity4 = findViewById(R.id.last_activity4);

        Button homeButton = findViewById(R.id.bthome);
        Button cardButton = findViewById(R.id.btcard);
        Button helpButton = findViewById(R.id.bthelp);
        ImageButton sendButton = findViewById(R.id.send);
        ImageButton topUpButton = findViewById(R.id.topup);
        ImageButton billButton = findViewById(R.id.bill);
        ImageButton loanButton = findViewById(R.id.loan);
        ImageButton commerceButton = findViewById(R.id.commerce);
        AppCompatImageView userIcon = findViewById(R.id.userIcon);
        AppCompatImageView notificationIcon = findViewById(R.id.locationIcon);

        homeButton.setOnClickListener(v -> startActivity(new Intent(home.this, home.class)));
        cardButton.setOnClickListener(v -> startActivity(new Intent(home.this, card.class)));
        helpButton.setOnClickListener(v -> startActivity(new Intent(home.this, help.class)));
        sendButton.setOnClickListener(v -> {
            startActivity(new Intent(home.this, Sendmoney.class));
            fetchUserbalane();
        });

        topUpButton.setOnClickListener(v -> startActivity(new Intent(home.this, topup.class)));
        billButton.setOnClickListener(v -> startActivity(new Intent(home.this, bill.class)));
        loanButton.setOnClickListener(v -> startActivity(new Intent(home.this, loan.class)));
        commerceButton.setOnClickListener(v -> startActivity(new Intent(home.this, commerence.class)));
        notificationIcon.setOnClickListener(v -> startActivity(new Intent(home.this, location.class)));
        userIcon.setOnClickListener(v -> startActivity(new Intent(home.this, profile.class)));

        dbRef = FirebaseDatabase.getInstance().getReference(USERS_NODE).child(userId);

        fetchLastTransactions();
        fetchUserName();
        fetchUserbalane();
    }



    private void fetchLastTransactions() {
        loadingProgressBar.setVisibility(ProgressBar.VISIBLE);
        DatabaseReference transactionsRef = FirebaseDatabase.getInstance().getReference("transactions");

        transactionsRef.orderByKey().limitToLast(4)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        loadingProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        int count = 0;

                        for (DataSnapshot transactionSnapshot : snapshot.getChildren()) {
                            String transactionDetails = formatTransactionDetails(transactionSnapshot);
                            if (transactionDetails != null) {
                                if (count == 0) {
                                    lastActivity1.setText(transactionDetails);
                                } else if (count == 1) {
                                    lastActivity2.setText(transactionDetails);
                                } else if (count == 2) {
                                    lastActivity3.setText(transactionDetails);
                                } else if (count == 3) {
                                    lastActivity4.setText(transactionDetails);
                                }
                                count++;
                            }
                        }

                        if (count == 0) {
                            lastActivity1.setText("No transactions found.");
                            lastActivity2.setText("");
                            lastActivity3.setText("");
                            lastActivity4.setText("");
                        } else if (count == 1) {
                            lastActivity2.setText("");
                            lastActivity3.setText("");
                            lastActivity4.setText("");
                        } else if (count == 2) {
                            lastActivity3.setText("");
                            lastActivity4.setText("");
                        } else if (count == 3) {
                            lastActivity4.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        loadingProgressBar.setVisibility(ProgressBar.INVISIBLE);
                        Log.e("Firebase", "Failed to fetch transactions: " + error.getMessage());
                        Toast.makeText(home.this, "Failed to fetch transactions.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    @Nullable
    private String formatTransactionDetails(DataSnapshot transactionSnapshot) {
        Long amount = transactionSnapshot.child("amount").getValue(Long.class);
        String receiverAoucnt = transactionSnapshot.child("receiverAccount").getValue(String.class);
        String senderIdAcount = transactionSnapshot.child("senderAccount").getValue(String.class);
        Long timestamp = transactionSnapshot.child("timestamp").getValue(Long.class);

        if (amount != null && senderIdAcount != null && receiverAoucnt != null && timestamp != null) {
            String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                    .format(timestamp);

            return "Send Rs. " + amount + " \nTo Account Number:  " + receiverAoucnt +"\nTransection Id is: " +timestamp+"\nON DATE: " + formattedDate;
        } else {
            return null;
        }
    }


    private void fetchUserName() {
        Intent intent = getIntent();
        String receivedName = intent.getStringExtra("name");
        if (receivedName != null) {
            username.setText(receivedName);
        }
    }
    private void fetchUserbalane() {
        Intent intent = getIntent();
        String receivedbalance = intent.getStringExtra("balance");
        if (receivedbalance != null) {
            wallet.setText(receivedbalance);
        }
    }
}
