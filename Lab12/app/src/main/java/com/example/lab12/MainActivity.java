package com.example.lab12;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btnMapClick).setOnClickListener(v ->
                startActivity(new Intent(this, MapClickActivity.class))
        );

        findViewById(R.id.btnPolygon).setOnClickListener(v -> {
            Intent i = new Intent(this, MapShapeActivity.class);
            i.putExtra("shape", "polygon");
            startActivity(i);
        });

        findViewById(R.id.btnCircle).setOnClickListener(v -> {
            Intent i = new Intent(this, MapShapeActivity.class);
            i.putExtra("shape", "circle");
            startActivity(i);
        });
    }
}
