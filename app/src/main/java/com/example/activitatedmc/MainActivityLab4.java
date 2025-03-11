// MainActivityLab4.java
package com.example.activitatedmc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivityLab4 extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private TextView tvBancaInfo;
    private Button btnAddBanca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab4); // Layout-ul principal

        tvBancaInfo = findViewById(R.id.tvBancaInfo);
        btnAddBanca = findViewById(R.id.btnAddBanca);

        // Deschidem activitatea de introducere date când se apasă butonul
        btnAddBanca.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    // Preluăm rezultatul din activitatea de introducere date
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Banca banca = data.getParcelableExtra("banca");
            if (banca != null) {
                tvBancaInfo.setText(banca.toString());
            }
        }
    }
}
