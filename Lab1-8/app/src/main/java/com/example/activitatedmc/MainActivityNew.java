package com.example.activitatedmc;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivityNew extends AppCompatActivity {
    private static final String TAG = "MainActivityLifecycle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.e(TAG, "onCreate() error log");
        Log.w(TAG, "onCreate() warning log");
        Log.d(TAG, "onCreate() debug log");
        Log.i(TAG, "onCreate() info log");
        Log.v(TAG, "onCreate() verbose log");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG, "onStart() error log");
        Log.w(TAG, "onStart() warning log");
        Log.d(TAG, "onStart() debug log");
        Log.i(TAG, "onStart() info log");
        Log.v(TAG, "onStart() verbose log");
        Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume() error log");
        Log.w(TAG, "onResume() warning log");
        Log.d(TAG, "onResume() debug log");
        Log.i(TAG, "onResume() info log");
        Log.v(TAG, "onResume() verbose log");
        Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause() error log");
        Log.w(TAG, "onPause() warning log");
        Log.d(TAG, "onPause() debug log");
        Log.i(TAG, "onPause() info log");
        Log.v(TAG, "onPause() verbose log");
        Toast.makeText(this, "onPause()", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
        Log.e(TAG, "onStop() error log");
        Log.w(TAG, "onStop() warning log");
        Log.d(TAG, "onStop() debug log");
        Log.i(TAG, "onStop() info log");
        Log.v(TAG, "onStop() verbose log");
        Toast.makeText(this, "onStop()", Toast.LENGTH_SHORT).show();
    }

}