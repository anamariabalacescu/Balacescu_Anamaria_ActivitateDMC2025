package com.example.activitatedmc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

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
    private Button btnInsert, btnSelectAll, btnSearchByName, btnSearchByFiliale, btnDeleteGreater, btnDeleteLesser, btnIncrement;
    private EditText etNumeBanca, etNumarFiliale, etSearchName, etMinFiliale, etMaxFiliale, etDeleteThreshold, etIncrementLetter;
    private BancaAdapter adapter;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab4);

        // Initialize Room database
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "banca_database")
                .build();

        // Initialize UI components
        btnAddBanca = findViewById(R.id.btnAddBanca);
        btnSettings = findViewById(R.id.btnSettings);
        lvBanci = findViewById(R.id.lvBanci);
        btnInsert = findViewById(R.id.btnInsert);
        btnSelectAll = findViewById(R.id.btnSelectAll);
        btnSearchByName = findViewById(R.id.btnSearchByName);
        btnSearchByFiliale = findViewById(R.id.btnSearchByFiliale);
        btnDeleteGreater = findViewById(R.id.btnDeleteGreater);
        btnDeleteLesser = findViewById(R.id.btnDeleteLesser);
        btnIncrement = findViewById(R.id.btnIncrement);
        etNumeBanca = findViewById(R.id.etNumeBanca);
        etNumarFiliale = findViewById(R.id.etNumarFiliale);
        etSearchName = findViewById(R.id.etSearchName);
        etMinFiliale = findViewById(R.id.etMinFiliale);
        etMaxFiliale = findViewById(R.id.etMaxFiliale);
        etDeleteThreshold = findViewById(R.id.etDeleteThreshold);
        etIncrementLetter = findViewById(R.id.etIncrementLetter);

        // Load original banciList
        banciList = loadBanksFromPreferences();
        if (banciList == null) {
            banciList = new ArrayList<>();
        }
        adapter = new BancaAdapter(this, banciList);
        lvBanci.setAdapter(adapter);

        // Original button listeners
        btnAddBanca.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
            startActivityForResult(intent, REQUEST_CODE_ADD);
        });

        btnSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivityLab4.this, SettingsActivity.class);
            startActivity(intent);
        });

        // Original click listeners
        lvBanci.setOnItemClickListener((parent, view, position, id) -> {
            Banca selectedBanca = banciList.get(position);
            Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
            intent.putExtra("banca", selectedBanca);
            intent.putExtra("index", position);
            startActivityForResult(intent, REQUEST_CODE_EDIT);
        });

        lvBanci.setOnItemLongClickListener((parent, view, position, id) -> {
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
            }
            return true;
        });

        // Room database operations
        btnInsert.setOnClickListener(v -> {
            String numeBanca = etNumeBanca.getText().toString().trim();
            String filialeStr = etNumarFiliale.getText().toString().trim();
            if (numeBanca.isEmpty() || filialeStr.isEmpty()) {
                Toast.makeText(this, "Completează toate câmpurile!", Toast.LENGTH_SHORT).show();
                return;
            }
            int numarFiliale;
            try {
                numarFiliale = Integer.parseInt(filialeStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Număr filiale invalid!", Toast.LENGTH_SHORT).show();
                return;
            }

            Banca banca = new Banca(
                    numeBanca,
                    numarFiliale,
                    Banca.TipBanca.COMERCIAL,
                    0.0,
                    new ArrayList<>(),
                    false,
                    new java.util.Date()
            );

            // Add to banciList and save
            if (!bankExists(banca)) {
                banciList.add(banca);
                adapter.notifyDataSetChanged();
                saveBanksToFile();
                saveBanksToPreferences();
            }

            // Insert into Room
            new InsertBancaTask().execute(banca);
        });

        btnSelectAll.setOnClickListener(v -> new GetAllBanciTask().execute());

        btnSearchByName.setOnClickListener(v -> {
            String name = etSearchName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(this, "Introdu un nume!", Toast.LENGTH_SHORT).show();
                return;
            }
            new GetBancaByNameTask().execute(name);
        });

        btnSearchByFiliale.setOnClickListener(v -> {
            String minStr = etMinFiliale.getText().toString().trim();
            String maxStr = etMaxFiliale.getText().toString().trim();
            if (minStr.isEmpty() || maxStr.isEmpty()) {
                Toast.makeText(this, "Introdu ambele valori!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int min = Integer.parseInt(minStr);
                int max = Integer.parseInt(maxStr);
                new GetBanciByFilialeRangeTask().execute(min, max);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valori numerice invalide!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteGreater.setOnClickListener(v -> {
            String thresholdStr = etDeleteThreshold.getText().toString().trim();
            if (thresholdStr.isEmpty()) {
                Toast.makeText(this, "Introdu o valoare!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int value = Integer.parseInt(thresholdStr);
                new DeleteBanciGreaterTask().execute(value);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valoare numerică invalidă!", Toast.LENGTH_SHORT).show();
            }
        });

        btnDeleteLesser.setOnClickListener(v -> {
            String thresholdStr = etDeleteThreshold.getText().toString().trim();
            if (thresholdStr.isEmpty()) {
                Toast.makeText(this, "Introdu o valoare!", Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                int value = Integer.parseInt(thresholdStr);
                new DeleteBanciLesserTask().execute(value);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valoare numerică invalidă!", Toast.LENGTH_SHORT).show();
            }
        });

        btnIncrement.setOnClickListener(v -> {
            String letter = etIncrementLetter.getText().toString().trim();
            if (letter.isEmpty() || letter.length() != 1) {
                Toast.makeText(this, "Introdu o singură literă!", Toast.LENGTH_SHORT).show();
                return;
            }
            new IncrementFilialeTask().execute(letter);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        applyTextSettingsToStaticLabels(findViewById(android.R.id.content));
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Banca banca = data.getParcelableExtra("banca");
            if (banca != null) {
                if (requestCode == REQUEST_CODE_ADD) {
                    if (!bankExists(banca)) {
                        banciList.add(banca);
                        // Also insert into Room
                        new InsertBancaTask().execute(banca);
                    } else {
                        Toast.makeText(this, "Banca există deja!", Toast.LENGTH_SHORT).show();
                    }
                } else if (requestCode == REQUEST_CODE_EDIT) {
                    int index = data.getIntExtra("index", -1);
                    if (index != -1) {
                        banciList.set(index, banca);
                        // Update in Room
                        new UpdateBancaTask().execute(banca);
                    }
                }
                adapter.notifyDataSetChanged();
                saveBanksToFile();
                saveBanksToPreferences();
            }
        }
    }

    private boolean bankExists(Banca newBank) {
        for (Banca b : banciList) {
            if (b.getNumeBanca().equalsIgnoreCase(newBank.getNumeBanca())) {
                return true;
            }
        }
        return false;
    }

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
            // File doesn't exist yet
        }
        return false;
    }

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

    // AsyncTasks for Room operations
    private class InsertBancaTask extends AsyncTask<Banca, Void, Long> {
        @Override
        protected Long doInBackground(Banca... bancas) {
            return db.bancaDao().insert(bancas[0]);
        }

        @Override
        protected void onPostExecute(Long result) {
            Toast.makeText(MainActivityLab4.this, "Banca inserată în DB cu ID: " + result, Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateBancaTask extends AsyncTask<Banca, Void, Integer> {
        @Override
        protected Integer doInBackground(Banca... bancas) {
            return db.bancaDao().update(bancas[0]);
        }

        @Override
        protected void onPostExecute(Integer result) {
            Toast.makeText(MainActivityLab4.this, "Actualizate " + result + " bănci în DB", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetAllBanciTask extends AsyncTask<Void, Void, List<Banca>> {
        @Override
        protected List<Banca> doInBackground(Void... voids) {
            return db.bancaDao().getAllBanci();
        }

        @Override
        protected void onPostExecute(List<Banca> bancas) {
            // Temporarily update ListView without modifying banciList
            List<Banca> tempList = new ArrayList<>(banciList);
            tempList.clear();
            tempList.addAll(bancas);
            adapter.clear();
            adapter.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    private class GetBancaByNameTask extends AsyncTask<String, Void, Banca> {
        @Override
        protected Banca doInBackground(String... names) {
            return db.bancaDao().getBancaByName(names[0]);
        }

        @Override
        protected void onPostExecute(Banca banca) {
            List<Banca> tempList = new ArrayList<>(banciList);
            tempList.clear();
            if (banca != null) {
                tempList.add(banca);
            } else {
                Toast.makeText(MainActivityLab4.this, "Nicio bancă găsită în DB!", Toast.LENGTH_SHORT).show();
            }
            adapter.clear();
            adapter.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    private class GetBanciByFilialeRangeTask extends AsyncTask<Integer, Void, List<Banca>> {
        @Override
        protected List<Banca> doInBackground(Integer... params) {
            return db.bancaDao().getBanciBetweenFiliale(params[0], params[1]);
        }

        @Override
        protected void onPostExecute(List<Banca> bancas) {
            List<Banca> tempList = new ArrayList<>(banciList);
            tempList.clear();
            tempList.addAll(bancas);
            adapter.clear();
            adapter.addAll(tempList);
            adapter.notifyDataSetChanged();
        }
    }

    private class DeleteBanciGreaterTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... values) {
            return db.bancaDao().deleteBanciWithFilialeGreaterThan(values[0]);
        }

        @Override
        protected void onPostExecute(Integer deletedRows) {
            Toast.makeText(MainActivityLab4.this, "Șterse " + deletedRows + " bănci din DB", Toast.LENGTH_SHORT).show();
            new GetAllBanciTask().execute();
        }
    }

    private class DeleteBanciLesserTask extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected Integer doInBackground(Integer... values) {
            return db.bancaDao().deleteBanciWithFilialeLessThan(values[0]);
        }

        @Override
        protected void onPostExecute(Integer deletedRows) {
            Toast.makeText(MainActivityLab4.this, "Șterse " + deletedRows + " bănci din DB", Toast.LENGTH_SHORT).show();
            new GetAllBanciTask().execute();
        }
    }

    private class IncrementFilialeTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... letters) {
            return db.bancaDao().incrementFilialeForBanciStartingWith(letters[0]);
        }

        @Override
        protected void onPostExecute(Integer updatedRows) {
            Toast.makeText(MainActivityLab4.this, "Actualizate " + updatedRows + " bănci în DB", Toast.LENGTH_SHORT).show();
            new GetAllBanciTask().execute();
        }
    }

    // Original BancaAdapter
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