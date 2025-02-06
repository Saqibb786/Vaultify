package com.example.bankingproject;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class commerence extends AppCompatActivity {

    private Switch internationalSwitch;
    private Switch onlineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_commerence);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        internationalSwitch = findViewById(R.id.switch1);
        onlineSwitch = findViewById(R.id.switch2);

        internationalSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(commerence.this, "International Transactions enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(commerence.this, "International Transactions disabled", Toast.LENGTH_SHORT).show();
            }
        });

        onlineSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(commerence.this, "Online Transactions enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(commerence.this, "Online Transactions disabled", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
