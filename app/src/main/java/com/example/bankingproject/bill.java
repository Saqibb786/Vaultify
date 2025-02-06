package com.example.bankingproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class bill extends AppCompatActivity {

    private EditText billAmountEditText, billDetailsEditText;
    private Button payBillButton;
    private TextView billStatusTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        billAmountEditText = findViewById(R.id.billAmountEditText);
        billDetailsEditText = findViewById(R.id.billDueDateEditText);
        payBillButton = findViewById(R.id.payBillButton);

        payBillButton.setOnClickListener(v -> {
            String billAmount = billAmountEditText.getText().toString().trim();
            String billDetails = billDetailsEditText.getText().toString().trim();


            if (billAmount.isEmpty() || billDetails.isEmpty()) {
                Toast.makeText(bill.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double amount = Double.parseDouble(billAmount);
                if (amount <= 0) {
                    Toast.makeText(bill.this, "Please enter a valid bill amount", Toast.LENGTH_SHORT).show();
                    return;
                }

                processBillPayment(amount, billDetails);
            } catch (NumberFormatException e) {
                Toast.makeText(bill.this, "Invalid amount entered", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processBillPayment(double amount, String billDetails) {

        billStatusTextView.setVisibility(View.VISIBLE);
        billStatusTextView.setText("Payment of Rs. " + amount + " for " + billDetails + " was successful!");

        billAmountEditText.setText("");
        billDetailsEditText.setText("");

        Toast.makeText(bill.this, "Bill paid successfully!", Toast.LENGTH_SHORT).show();
    }
}
