package com.example.activitatedmc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;   // <-- Import pentru RatingBar
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class Lab4Activity2 extends AppCompatActivity {

    private EditText etNumeBanca, etFiliale;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private CheckBox cbCreditIpotecar, cbCreditAuto, cbCreditPersonal;
    private Button buttonSubmit;
    private RatingBar ratingBar;  // <-- Adăugat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab42); // Layout-ul cu RatingBar (ratingBar2)

        // Inițializare view-uri
        etNumeBanca = findViewById(R.id.etNumeBanca);
        etFiliale = findViewById(R.id.etFiliale);
        spinner = findViewById(R.id.spinner);
        radioGroup = findViewById(R.id.radioGroup);
        cbCreditIpotecar = findViewById(R.id.checkBox);
        cbCreditAuto = findViewById(R.id.checkBox2);
        cbCreditPersonal = findViewById(R.id.checkBox3);
        buttonSubmit = findViewById(R.id.button2);

        // Adăugăm referința la RatingBar
        ratingBar = findViewById(R.id.ratingBar2);

        // Configurare Spinner pentru Tip Bancă (folosind valorile din enum-ul TipBanca)
        ArrayAdapter<Banca.TipBanca> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Banca.TipBanca.values());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Eveniment pentru butonul de trimitere
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Se citesc valorile introduse
                String numeBanca = etNumeBanca.getText().toString().trim();
                int numarFiliale;
                try {
                    numarFiliale = Integer.parseInt(etFiliale.getText().toString().trim());
                } catch (NumberFormatException e) {
                    etFiliale.setError("Introduceți un număr valid");
                    return;
                }

                // Se obține tipul de bancă selectat din Spinner
                Banca.TipBanca tipBanca = (Banca.TipBanca) spinner.getSelectedItem();

                // Se colectează tipurile de credite selectate prin CheckBox-uri
                List<Banca.TipCredit> tipuriCredite = new ArrayList<>();
                if (cbCreditIpotecar.isChecked()) {
                    tipuriCredite.add(Banca.TipCredit.IPOTECAR);
                }
                if (cbCreditAuto.isChecked()) {
                    tipuriCredite.add(Banca.TipCredit.AUTO);
                }
                if (cbCreditPersonal.isChecked()) {
                    tipuriCredite.add(Banca.TipCredit.PERSONAL);
                }

                // Se determină opțiunea pentru Internet Banking din RadioGroup
                boolean internetBanking = false;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton rbSelected = findViewById(selectedId);
                    if (rbSelected.getText().toString().equalsIgnoreCase("Da")) {
                        internetBanking = true;
                    }
                }

                // Se obține rating-ul din RatingBar
                double rating = ratingBar.getRating();  // getRating() returnează un float, se convertește implicit la double

                // Creare obiect Banca cu rating-ul obținut
                Banca banca = new Banca(
                        numeBanca,
                        false,           // estePrivata - exemplu, rămâne fals dacă nu ai alt CheckBox dedicat
                        numarFiliale,
                        tipBanca,
                        rating,          // folosim rating-ul din RatingBar
                        tipuriCredite,
                        internetBanking
                );

                // Pregătim Intent-ul pentru a returna rezultatul
                Intent resultIntent = new Intent();
                resultIntent.putExtra("banca", banca);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}