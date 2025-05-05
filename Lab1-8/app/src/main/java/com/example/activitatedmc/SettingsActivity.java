package com.example.activitatedmc;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private EditText etTextSize, etTextColor;
    private Button btnSaveSettings;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Obținerea referințelor la elementele de UI
        etTextSize = findViewById(R.id.etTextSize);
        etTextColor = findViewById(R.id.etTextColor);
        btnSaveSettings = findViewById(R.id.btnSaveSettings);

        // Accesăm setările salvate în SharedPreferences (dacă există)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int textSize = sharedPreferences.getInt("textSize", 14); // valoare implicită 14
        String textColor = sharedPreferences.getString("textColor", "#000000"); // valoare implicită negru

        // Pre-populăm câmpurile cu valorile existente
        etTextSize.setText(String.valueOf(textSize));
        etTextColor.setText(textColor);

        // Salvarea setărilor când se apasă butonul
        btnSaveSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    int newTextSize = Integer.parseInt(etTextSize.getText().toString());
                    String newTextColor = etTextColor.getText().toString();

                    // Validăm formatul codului de culoare (dacă nu este valid, parseColor va arunca o excepție)
                    Color.parseColor(newTextColor);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("textSize", newTextSize);
                    editor.putString("textColor", newTextColor);
                    editor.apply();

                    Toast.makeText(SettingsActivity.this, "Setări salvate!", Toast.LENGTH_SHORT).show();
                    finish();  // Închidem activitatea și revenim la cea anterioară
                } catch (NumberFormatException e) {
                    Toast.makeText(SettingsActivity.this, "Introduceți o dimensiune validă!", Toast.LENGTH_SHORT).show();
                } catch (IllegalArgumentException e) {
                    Toast.makeText(SettingsActivity.this, "Introduceți un cod de culoare valid (ex: #000000)!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
