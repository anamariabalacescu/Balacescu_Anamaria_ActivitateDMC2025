// MainActivityLab4.java
package com.example.activitatedmc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivityLab4 extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    // private TextView tvBancaInfo;
    private ListView lvBanci;
    private List<Banca> banciList;
    private Button btnAddBanca;
    private ArrayAdapter<Banca> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab4); // asigură-te că layout-ul se numește corect

        btnAddBanca = findViewById(R.id.btnAddBanca);
        lvBanci = findViewById(R.id.lvBanci);

        // Inițializăm lista de bănci și adapterul
        banciList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, banciList);
        lvBanci.setAdapter(adapter);

        // Butonul de adăugare lansează Lab4Activity2
        btnAddBanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        // La click scurt pe un element din ListView, afișăm un Toast cu detaliile băncii
        lvBanci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Banca banca = banciList.get(position);
                Toast.makeText(MainActivityLab4.this, banca.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // La long click pe un element, acesta este șters din ListView și din lista de bănci
        lvBanci.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                banciList.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(MainActivityLab4.this, "Banca ștearsă", Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    // Preluăm rezultatul de la Lab4Activity2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Banca banca = data.getParcelableExtra("banca");
            if (banca != null) {
                banciList.add(banca);
                adapter.notifyDataSetChanged();
            }
        }
    }
}