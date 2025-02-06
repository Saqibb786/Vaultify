package com.example.bankingproject;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class help extends AppCompatActivity {

    private RecyclerView faqRecyclerView;
    private FAQAdapter faqAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_help);

        faqRecyclerView = findViewById(R.id.faq_recycler_view);
        faqRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        List<FAQ> faqList = new ArrayList<>();
        faqList.add(new FAQ("How do I reset my password?", "Click on 'Forgot Password' on the login screen and follow the steps."));
        faqList.add(new FAQ("Is my data secure?", "Yes! We use industry-standard encryption to protect your data."));
        faqList.add(new FAQ("Can I send money internationally?", "Currently, transactions are limited to domestic transfers only."));
        faqList.add(new FAQ("How do I contact support?", "You can reach us via email or visit our official website for assistance."));
        faqList.add(new FAQ("How do I add a new card to my Vaultify account?",
                "Go to the 'Cards' section in the app, tap 'Add New Card' and enter the required details."));
        faqList.add(new FAQ("What should I do if I notice a suspicious transaction?",
                "Immediately report it via the 'Help' section in the app or contact customer support."));
        faqList.add(new FAQ("How do I change my email address on Vaultify?",
                "To change your email, go to 'Profile Settings' and update your email address. A confirmation will be sent to the new email."));


        faqAdapter = new FAQAdapter(faqList);
        faqRecyclerView.setAdapter(faqAdapter);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
