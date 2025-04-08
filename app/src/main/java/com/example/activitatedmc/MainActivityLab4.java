package com.example.activitatedmc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class MainActivityLab4 extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;
    private static final String PREFS_NAME = "BankPrefs";
    private static final String BANKS_KEY = "banks";

    private ListView lvBanci;
    private List<Banca> banciList;
    private Button btnAddBanca, btnSettings;
    private BancaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab4);

        btnAddBanca = findViewById(R.id.btnAddBanca);
        btnSettings = findViewById(R.id.btnSettings);
        lvBanci = findViewById(R.id.lvBanci);

        // Încarcă lista de bănci din SharedPreferences sau creează una nouă
        banciList = loadBanksFromPreferences();
        if (banciList == null) {
            banciList = new ArrayList<>();
        }

        // Inițializează adapterul și îl setează în ListView
        adapter = new BancaAdapter(this, banciList);
        lvBanci.setAdapter(adapter);

        // Afișează în Log calea completă a directorului de fișiere
        Log.d("FILE_PATH", "Calea completă a directorului fișiere: " + getFilesDir().getAbsolutePath());
        // Exemplu: /data/data/com.example.activitatedmc/files/

        // Butonul de adăugare – deschide activitatea pentru adăugare
        btnAddBanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        // Butonul de setări – deschide activitatea SettingsActivity
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivityLab4.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        // Click scurt – deschide activitatea pentru editare (trimite banca și indexul)
        lvBanci.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Banca selectedBanca = banciList.get(position);
                Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
                intent.putExtra("banca", selectedBanca);
                intent.putExtra("index", position);
                startActivityForResult(intent, REQUEST_CODE_EDIT);
            }
        });

        // Click lung – adaugă banca selectată în fișierul de favorite, evitând duplicatele
        lvBanci.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Banca selectedBanca = banciList.get(position);
                if (isFavoriteDuplicate(selectedBanca)) {
                    Toast.makeText(MainActivityLab4.this, "Banca există deja în favorite!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                try {
                    FileOutputStream fos = openFileOutput("favoriteBanci.txt", MODE_APPEND);
                    OutputStreamWriter osw = new OutputStreamWriter(fos);
                    BufferedWriter bw = new BufferedWriter(osw);
                    bw.write(selectedBanca.toString());
                    bw.newLine();
                    bw.close();
                    Toast.makeText(MainActivityLab4.this, "Banca adăugată la favorite", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivityLab4.this, "Eroare la salvarea în favorite!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        // Aplică stilizarea pentru etichetele statice din layout (ex. TextView-uri care nu sunt butoane)
        applyTextSettingsToStaticLabels(findViewById(android.R.id.content));
    }

    // Metodă recursivă ce aplică setările de text (dimensiune și culoare) la toate TextView-urile, exceptând butoanele
    private void applyTextSettingsToStaticLabels(View view) {
        SharedPreferences preferences = getSharedPreferences("UserSettings", MODE_PRIVATE);
        int savedTextSize = preferences.getInt("textSize", 14);
        String savedTextColor = preferences.getString("textColor", "#000000");
        int textColor = Color.parseColor(savedTextColor);

        if (view instanceof TextView && !(view instanceof Button)) {
            ((TextView) view).setTextSize(savedTextSize);
            ((TextView) view).setTextColor(textColor);
        }
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i < vg.getChildCount(); i++) {
                applyTextSettingsToStaticLabels(vg.getChildAt(i));
            }
        }
    }

    // Preluarea rezultatului de la activitățile de adăugare sau editare
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Banca banca = data.getParcelableExtra("banca");
            if (banca != null) {
                if (requestCode == REQUEST_CODE_ADD) {
                    // Adaugă banca doar dacă nu există deja în listă
                    if (!bankExists(banca)) {
                        banciList.add(banca);
                    } else {
                        Toast.makeText(this, "Banca există deja!", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_CODE_EDIT) {
                    int index = data.getIntExtra("index", -1);
                    if (index != -1) {
                        banciList.set(index, banca);
                    }
                }
                adapter.notifyDataSetChanged();
                saveBanksToFile();
                saveBanksToPreferences();
            }
        }
    }

    // Metodă pentru verificarea duplicatelor în lista principală (după nume)
    private boolean bankExists(Banca newBank) {
        for (Banca b : banciList) {
            if (b.getNumeBanca().equalsIgnoreCase(newBank.getNumeBanca())) {
                return true;
            }
        }
        return false;
    }

    // Verificăm duplicate
    private boolean isFavoriteDuplicate(Banca banca) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(openFileInput("favoriteBanci.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("Nume Bancă:" + banca.getNumeBanca())) {
                    return true;
                }
            }
        } catch (IOException e) {
            // Dacă fișierul nu există încă, nu e duplicat.
        }
        return false;
    }

    // Salvează întreaga listă de bănci în fișierul "banci.txt" (fără duplicate) – folosește MODE_PRIVATE pentru a suprascrie fișierul
    private void saveBanksToFile() {
        try {
            FileOutputStream fos = openFileOutput("banci.txt", MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            BufferedWriter bw = new BufferedWriter(osw);
            for (Banca b : banciList) {
                bw.write(b.toString());
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Salvează lista de bănci în SharedPreferences sub formă de JSON, folosind clasele org.json (fără Gson)
    private void saveBanksToPreferences() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            JSONArray jsonArray = new JSONArray();
            for (Banca b : banciList) {
                JSONObject obj = new JSONObject();
                obj.put("numeBanca", b.getNumeBanca());
                obj.put("numarFiliale", b.getNumarFiliale());
                obj.put("tipBanca", b.getTipBanca().toString());
                obj.put("rating", b.getRating());
                obj.put("internetBanking", b.hasInternetBanking());
                obj.put("dataInfiintare", b.getDataInfiintare().getTime());
                JSONArray crediteArray = new JSONArray();
                if (b.getTipuriCredite() != null) {
                    for (Banca.TipCredit t : b.getTipuriCredite()) {
                        crediteArray.put(t.toString());
                    }
                }
                obj.put("tipuriCredite", crediteArray);
                jsonArray.put(obj);
            }
            editor.putString(BANKS_KEY, jsonArray.toString());
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Încarcă lista de bănci din SharedPreferences, parsând șirul JSON cu org.json
    private List<Banca> loadBanksFromPreferences() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String json = sp.getString(BANKS_KEY, null);
        List<Banca> list = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    String numeBanca = obj.getString("numeBanca");
                    int numarFiliale = obj.getInt("numarFiliale");
                    Banca.TipBanca tipBanca = Banca.TipBanca.valueOf(obj.getString("tipBanca"));
                    double rating = obj.getDouble("rating");
                    boolean internetBanking = obj.getBoolean("internetBanking");
                    long dateMillis = obj.getLong("dataInfiintare");
                    java.util.Date dataInfiintare = new java.util.Date(dateMillis);
                    JSONArray crediteArray = obj.getJSONArray("tipuriCredite");
                    List<Banca.TipCredit> tipuriCredite = new ArrayList<>();
                    for (int j = 0; j < crediteArray.length(); j++) {
                        Banca.TipCredit tip = Banca.TipCredit.valueOf(crediteArray.getString(j));
                        tipuriCredite.add(tip);
                    }
                    Banca banca = new Banca(numeBanca, numarFiliale, tipBanca, rating, tipuriCredite, internetBanking, dataInfiintare);
                    list.add(banca);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    // Adapterul personalizat pentru ListView – aplică setările de text (dimensiune și culoare) pe elementele fiecărei bănci
    private class BancaAdapter extends ArrayAdapter<Banca> {
        public BancaAdapter(Context context, List<Banca> banci) {
            super(context, 0, banci);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.banca_list_item, parent, false);
            }
            Banca banca = getItem(position);
            // Obține referințele la elementele din layout-ul fiecărui element de listă
            TextView tvNumeBanca = convertView.findViewById(R.id.tvNumeBanca);
            TextView tvFiliale = convertView.findViewById(R.id.tvFiliale);
            TextView tvTipBanca = convertView.findViewById(R.id.tvTipBanca);
            TextView tvData = convertView.findViewById(R.id.tvData);
            RadioButton rbHasInternet = convertView.findViewById(R.id.rbHasInternet);
            RadioButton rbNoInternet = convertView.findViewById(R.id.rbNoInternet);
            CheckBox cbCreditI = convertView.findViewById(R.id.cbCreditI);
            CheckBox cbCreditA = convertView.findViewById(R.id.cbCreditA);
            CheckBox cbCreditP = convertView.findViewById(R.id.cbCreditP);
            RatingBar rbRating = convertView.findViewById(R.id.rbRating);

            // Setează datele băncii
            tvNumeBanca.setText(banca.getNumeBanca());
            tvFiliale.setText(String.valueOf(banca.getNumarFiliale()));
            tvTipBanca.setText(banca.getTipBanca().toString());
            tvData.setText(banca.getDataInfiintare().toString());

            if (banca.hasInternetBanking()) {
                rbHasInternet.setChecked(true);
                rbNoInternet.setChecked(false);
            } else {
                rbHasInternet.setChecked(false);
                rbNoInternet.setChecked(true);
            }

            List<Banca.TipCredit> credite = banca.getTipuriCredite();
            if (credite != null) {
                cbCreditI.setChecked(credite.contains(Banca.TipCredit.IPOTECAR));
                cbCreditA.setChecked(credite.contains(Banca.TipCredit.AUTO));
                cbCreditP.setChecked(credite.contains(Banca.TipCredit.PERSONAL));
            } else {
                cbCreditI.setChecked(false);
                cbCreditA.setChecked(false);
                cbCreditP.setChecked(false);
            }

            rbRating.setRating((float) banca.getRating());

            // Aplică setările din SharedPreferences pentru text
            SharedPreferences preferences = getContext().getSharedPreferences("UserSettings", MODE_PRIVATE);
            int savedTextSize = preferences.getInt("textSize", 14);
            String savedTextColor = preferences.getString("textColor", "#000000");
            int textColor = Color.parseColor(savedTextColor);

            tvNumeBanca.setTextSize(savedTextSize);
            tvNumeBanca.setTextColor(textColor);
            tvFiliale.setTextSize(savedTextSize);
            tvFiliale.setTextColor(textColor);
            tvTipBanca.setTextSize(savedTextSize);
            tvTipBanca.setTextColor(textColor);
            tvData.setTextSize(savedTextSize);
            tvData.setTextColor(textColor);
            rbHasInternet.setTextSize(savedTextSize);
            rbHasInternet.setTextColor(textColor);
            rbNoInternet.setTextSize(savedTextSize);
            rbNoInternet.setTextColor(textColor);
            cbCreditI.setTextSize(savedTextSize);
            cbCreditI.setTextColor(textColor);
            cbCreditA.setTextSize(savedTextSize);
            cbCreditA.setTextColor(textColor);
            cbCreditP.setTextSize(savedTextSize);
            cbCreditP.setTextColor(textColor);

            return convertView;
        }
    }
}
