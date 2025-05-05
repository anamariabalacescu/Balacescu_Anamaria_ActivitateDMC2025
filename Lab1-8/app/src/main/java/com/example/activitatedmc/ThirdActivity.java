package com.example.activitatedmc;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;
import android.content.Intent;

public class ThirdActivity extends AppCompatActivity {

    private int valoare1;
    private int valoare2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        // Preluare extras din Intent
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String message = extras.getString("message");
            valoare1 = extras.getInt("valoare1", 0);
            valoare2 = extras.getInt("valoare2", 0);
            Toast.makeText(this, "Mesaj: " + message + ", Valoare1: " + valoare1 + ", Valoare2: " + valoare2, Toast.LENGTH_LONG).show();
        }

        // Buton pentru trimiterea datelor la SecondActivity
        findViewById(R.id.button_send_back).setOnClickListener(v -> {
            int sum = valoare1 + valoare2;
            Intent intent = new Intent();
            intent.putExtra("returnMessage", "Salut din ThirdActivity");
            intent.putExtra("sum", sum);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}