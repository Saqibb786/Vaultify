package com.example.bankingproject;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class setting extends AppCompatActivity {

    private TextView settingsTitle;
    private TextView notificationLabel;
    private Switch switchNotifications;
    private TextView securityLabel;
    private Button changePasswordBtn;
    private TextView themeLabel;
    private Spinner themeSpinner;
    private Button saveSettingsBtn;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.setting); ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        settingsTitle = findViewById(R.id.settings_title);
        notificationLabel = findViewById(R.id.notification_label);
        switchNotifications = findViewById(R.id.switch_notifications);
        securityLabel = findViewById(R.id.security_label);
        changePasswordBtn = findViewById(R.id.change_password_btn);
        themeLabel = findViewById(R.id.theme_label);
        themeSpinner = findViewById(R.id.theme_spinner);
        saveSettingsBtn = findViewById(R.id.save_settings_btn);

       ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.theme_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);

        switchNotifications.setChecked(true); // Set initial state to ON
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
        });

        changePasswordBtn.setOnClickListener(v -> {
       });

        saveSettingsBtn.setOnClickListener(v -> {
            String selectedTheme = themeSpinner.getSelectedItem().toString();
            boolean notificationsEnabled = switchNotifications.isChecked();

            saveSettings(selectedTheme, notificationsEnabled);
        });
    }

    private void saveSettings(String selectedTheme, boolean notificationsEnabled) {
        String userId = mAuth.getCurrentUser().getUid();

       Settings userSettings = new Settings(selectedTheme, notificationsEnabled);

        mDatabase.child("users").child(userId).child("settings")
                .setValue(userSettings)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(setting.this, "Settings saved successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(setting.this, "Failed to save settings", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public static class Settings {
        public String theme;
        public boolean notificationsEnabled;

        public Settings() {
        }

        public Settings(String theme, boolean notificationsEnabled) {
            this.theme = theme;
            this.notificationsEnabled = notificationsEnabled;
        }
    }
}
