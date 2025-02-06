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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Sendmoney extends AppCompatActivity {

    private EditText accountNumberInput, amountInput;
    private TextView accountHolderName, sendingAmountView, transactionDate, branchname, sendingNumber, transactionHistory;
    private Button confirmButton, nextButton;

    private long sendingAmount, transactionDateMillis;
    private String receiverAccountNumber, senderAccountId, receiverAccountName, receiverAccountId;
    private long receiverBalance, senderBalance;

    private DatabaseReference dbRef;
    private String senderUserId,acountsend;
    private String receiverUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmoney);

        accountNumberInput = findViewById(R.id.accountnumber);
        amountInput = findViewById(R.id.sendmoney);
        accountHolderName = findViewById(R.id.sendingname);
        sendingAmountView = findViewById(R.id.sendingamount);
        transactionDate = findViewById(R.id.datetransection);
        sendingNumber = findViewById(R.id.sendingnumb);
        transactionHistory = findViewById(R.id.transection);
        confirmButton = findViewById(R.id.confirmmoney);
        nextButton = findViewById(R.id.next);
        branchname = findViewById(R.id.brachname);

        dbRef = FirebaseDatabase.getInstance().getReference();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        senderUserId = mAuth.getCurrentUser().getUid();

        setupMobileInput();

        nextButton.setOnClickListener(v -> {
            String enteredAccountNumber = accountNumberInput.getText().toString().trim();
            String amountString = amountInput.getText().toString().trim();

            if (TextUtils.isEmpty(enteredAccountNumber) || enteredAccountNumber.length() != 13 || !enteredAccountNumber.startsWith("+92")) {
                accountNumberInput.setError("Enter a valid 13-digit account number starting with +92!");
                return;
            }

            if (TextUtils.isEmpty(amountString)) {
                amountInput.setError("Enter an amount!");
                return;
            }

            try {
                sendingAmount = Long.parseLong(amountString);
            } catch (NumberFormatException e) {
                amountInput.setError("Invalid amount!");
                return;
            }

            fetchReceiverDetails(enteredAccountNumber, sendingAmount);
        });

        confirmButton.setOnClickListener(v -> {
            if (receiverAccountNumber == null || senderAccountId == null || receiverAccountId == null) {
                Toast.makeText(Sendmoney.this, "Transaction details missing!", Toast.LENGTH_SHORT).show();
                return;
            }

            performTransaction();
        });
    }

    private void fetchReceiverDetails(String enteredAccountNumber, long amount) {
        dbRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean accountFound = false;

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot childAccounts = userSnapshot.child("child_accounts");

                    for (DataSnapshot accountSnapshot : childAccounts.getChildren()) {
                        String dbAccountNumber = accountSnapshot.child("accountNumber").getValue(String.class);
                        String dbAccountholdername = accountSnapshot.child("name").getValue(String.class);

                        if (enteredAccountNumber.equals(dbAccountNumber)) {
                            receiverAccountNumber = dbAccountNumber;
                            receiverAccountName = dbAccountholdername;
                            receiverBalance = accountSnapshot.child("balance").getValue(Long.class);
                            receiverAccountId = accountSnapshot.getKey();
                            receiverUserId = userSnapshot.getKey();
                            accountFound = true;

                            senderAccountId = userSnapshot.getKey();

                            transactionHistory.setText("Transaction ID: " + System.currentTimeMillis());
                            accountHolderName.setText("Holder Name: " + receiverAccountName);
                            sendingNumber.setText("Account Number: " + receiverAccountNumber);
                            sendingAmountView.setText("Amount: " + amount + " RS");
                            branchname.setText("Branch Name: Vaultify");
                            transactionDateMillis = System.currentTimeMillis();
                            transactionDate.setText(getFormattedDate(transactionDateMillis));

                            validateSenderBalance(amount);

                            return;
                        }
                    }
                }

                if (!accountFound) {
                    Toast.makeText(Sendmoney.this, "Account not found! Please check the account number.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("Firebase", "Error fetching receiver details: " + error.getMessage());
            }
        });
    }

    private void validateSenderBalance(long amount) {
        if (senderUserId == null || senderUserId.isEmpty()) {
            Toast.makeText(Sendmoney.this, "User ID not found!", Toast.LENGTH_SHORT).show();
            return;
        }

        dbRef.child("users").child(senderUserId).child("child_accounts")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        boolean balanceValid = false;
                        senderBalance = 0L;
                        senderAccountId = null;

                        for (DataSnapshot accountSnapshot : snapshot.getChildren()) {
                            Long balance = accountSnapshot.child("balance").getValue(Long.class);

                            if (balance != null && balance >= amount) {
                                senderBalance = balance;
                                senderAccountId = accountSnapshot.getKey();
                                acountsend=accountSnapshot.child("accountNumber").getValue(String.class);
                                balanceValid = true;
                                break;
                            }
                        }


                        if (balanceValid) {
                                Toast.makeText(Sendmoney.this, "Balance Verified. Proceed to confirm.", Toast.LENGTH_SHORT).show();
                                confirmButton.setEnabled(true);
                            } else {
                                Toast.makeText(Sendmoney.this, "Insufficient balance!", Toast.LENGTH_SHORT).show();
                                confirmButton.setEnabled(false);
                            }
                        }



                    @Override
                    public void onCancelled(DatabaseError error) {
                        Log.e("Firebase", "Error fetching sender balance: " + error.getMessage());
                        Toast.makeText(Sendmoney.this, "Failed to fetch balance. Try again!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void performTransaction() {
        DatabaseReference transactionsRef = dbRef.child("transactions").push();
        String transactionId = transactionsRef.getKey();
        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("senderId", senderUserId);
        transactionData.put("senderAccount", acountsend);
        transactionData.put("receiverId", receiverUserId);
        transactionData.put("receiverAccount", receiverAccountNumber);
        transactionData.put("amount", sendingAmount);
        transactionData.put("timestamp", transactionDateMillis);

        Map<String, Object> updates = new HashMap<>();
        // Update sender's balance
        updates.put("users/" + senderUserId + "/child_accounts/" + senderAccountId + "/balance", senderBalance - sendingAmount);
        // Update receiver's balance
        updates.put("users/" + receiverUserId + "/child_accounts/" + receiverAccountId + "/balance", receiverBalance + sendingAmount);
        // Add transaction record
        updates.put("transactions/" + transactionId, transactionData);

        dbRef.updateChildren(updates).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d("Sendmoney", "Transaction successful! Navigating to Confirmationscreen.");
                Toast.makeText(Sendmoney.this, "Transaction successful!", Toast.LENGTH_SHORT).show();

                Intent snd = new Intent(Sendmoney.this, Confirmationscreen.class);
                snd.putExtra("holdername", accountHolderName.getText().toString());
                snd.putExtra("Transectiontime", transactionDateMillis);
                snd.putExtra("Amount", sendingAmount);
                startActivity(snd);
            } else {
                Log.e("Sendmoney", "Transaction failed: " + task.getException().getMessage());
                Toast.makeText(Sendmoney.this, "Transaction failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFormattedDate(long timestamp) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(timestamp);
    }

    private void setupMobileInput() {
        InputFilter[] filters = new InputFilter[]{new InputFilter.LengthFilter(13)};
        accountNumberInput.setFilters(filters);

        accountNumberInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = s.toString();
                if (!input.startsWith("+92")) {
                    accountNumberInput.setText("+92");
                    accountNumberInput.setSelection(accountNumberInput.getText().length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}