package com.example.activitatedmc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Lab4Activity2 extends AppCompatActivity {

    private EditText etNumeBanca, etFiliale;
    private Spinner spinner;
    private RadioGroup radioGroup;
    private CheckBox cbCreditIpotecar, cbCreditAuto, cbCreditPersonal;
    private Button buttonSubmit;
    private CalendarView datePickerCalendar;
    private Banca banca; // dacă ai definit această clasă într-un alt fișier
    private boolean isEditMode = false;
    private int editIndex = -1; // Indexul obiectului ce se modifică

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab42);

        // Inițializare view-uri
        etNumeBanca = findViewById(R.id.etNumeBanca);
        etFiliale = findViewById(R.id.etFiliale);
        spinner = findViewById(R.id.spinner);
        radioGroup = findViewById(R.id.radioGroup);
        cbCreditIpotecar = findViewById(R.id.checkBox);
        cbCreditAuto = findViewById(R.id.checkBox2);
        cbCreditPersonal = findViewById(R.id.checkBox3);
        buttonSubmit = findViewById(R.id.button2);
        datePickerCalendar = findViewById(R.id.calendarView);

        // Setare dată maximă pentru CalendarView
        Calendar cal = Calendar.getInstance();
        cal.set(2024, Calendar.DECEMBER, 10);
        datePickerCalendar.setMaxDate(cal.getTimeInMillis());

        // Configurare Spinner pentru Tip Bancă
        ArrayAdapter<Banca.TipBanca> adapterSpinner = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, Banca.TipBanca.values());
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);

        // Verificăm dacă activitatea a fost lansată pentru editare
        if (getIntent().hasExtra("banca")) {
            isEditMode = true;
            Banca banca = getIntent().getParcelableExtra("banca");
            editIndex = getIntent().getIntExtra("index", -1);
            if (banca != null) {
                etNumeBanca.setText(banca.getNumeBanca());
                etFiliale.setText(String.valueOf(banca.getNumarFiliale()));
                int spinnerPosition = adapterSpinner.getPosition(banca.getTipBanca());
                spinner.setSelection(spinnerPosition);
                // Setare rating
                // (Presupunând că ratingBar-ul este configurat în layoutul tău și că ai un getter corespunzător)
                // ratingBar.setRating((float) banca.getRating());
                // Setare CheckBox-uri pentru tipurile de credite
                List<Banca.TipCredit> tipuriCredite = banca.getTipuriCredite();
                if (tipuriCredite != null) {
                    cbCreditIpotecar.setChecked(tipuriCredite.contains(Banca.TipCredit.IPOTECAR));
                    cbCreditAuto.setChecked(tipuriCredite.contains(Banca.TipCredit.AUTO));
                    cbCreditPersonal.setChecked(tipuriCredite.contains(Banca.TipCredit.PERSONAL));
                }
                // Setare RadioGroup pentru Internet Banking
                if (banca.hasInternetBanking()) {
                    radioGroup.check(R.id.radioButton);
                } else {
                    radioGroup.check(R.id.radioButton2);
                }
                // Setare dată în CalendarView
                datePickerCalendar.setDate(banca.getDataInfiintare().getTime());
            }
        }

        // Eveniment pentru butonul de salvare
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numeBanca = etNumeBanca.getText().toString().trim();
                int numarFiliale;
                try {
                    numarFiliale = Integer.parseInt(etFiliale.getText().toString().trim());
                } catch (NumberFormatException e) {
                    etFiliale.setError("Introduceți un număr valid");
                    return;
                }

                Banca.TipBanca tipBanca = (Banca.TipBanca) spinner.getSelectedItem();

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

                boolean internetBanking = false;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    if (selectedId == R.id.radioButton) {
                        internetBanking = true;
                    } else if (selectedId == R.id.radioButton2) {
                        internetBanking = false;
                    }
                }

                // Presupunem că ai un RatingBar definit în layout dacă folosești rating (exemplu: ratingBar2)
                // double rating = ratingBar.getRating();
                double rating = 0; // dacă nu ai rating, poți seta o valoare implicită

                long selectedMillis = datePickerCalendar.getDate();
                Date selectedDate = new Date(selectedMillis);

                // Creăm obiectul Banca (actualizat sau nou)
                Banca updatedBanca = new Banca(
                        numeBanca,
                        numarFiliale,
                        tipBanca,
                        rating,
                        tipuriCredite,
                        internetBanking,
                        selectedDate
                );

                try {
                    FileOutputStream fos = openFileOutput("banci.txt", MODE_APPEND);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    BufferedWriter bw = new BufferedWriter(osw);

                    // Se salvează obiectul convertit la string (poate fi adaptat cu un format JSON sau altă metodă de serializare)
                    bw.write(updatedBanca.toString());
                    bw.newLine();
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(Lab4Activity2.this, "Eroare la salvarea obiectului!", Toast.LENGTH_SHORT).show();
                }

                Intent resultIntent = new Intent();
                resultIntent.putExtra("banca", updatedBanca);
                if (isEditMode) {
                    resultIntent.putExtra("index", editIndex);
                }
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}