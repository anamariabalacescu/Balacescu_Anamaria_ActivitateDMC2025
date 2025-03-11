package com.example.activitatedmc;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_THIRD = 100;  // Codul pentru identificarea cererii

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // lansam ThirdActivity cu un Bundle de extras
        findViewById(R.id.buton_in_activitatea2).setOnClickListener(v -> {
            Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("message", "Salut din SecondActivity");
            bundle.putInt("valoare1", 5);
            bundle.putInt("valoare2", 7);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQUEST_CODE_THIRD);
        });
    }

    // preluare date de la ThirdActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_THIRD && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                String returnMessage = extras.getString("returnMessage");
                int sum = extras.getInt("sum", 0);
                Toast.makeText(this, "Mesaj: " + returnMessage + ", Suma: " + sum, Toast.LENGTH_LONG).show();
            }
        }
    }
}
