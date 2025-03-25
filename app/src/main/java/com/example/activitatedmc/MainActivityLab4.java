package com.example.activitatedmc;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioButton;
import android.widget.CheckBox;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivityLab4 extends AppCompatActivity {
    private static final int REQUEST_CODE_ADD = 1;
    private static final int REQUEST_CODE_EDIT = 2;

    private ListView lvBanci;
    private List<Banca> banciList;
    private Button btnAddBanca;
    private BancaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_lab4);

        btnAddBanca = findViewById(R.id.btnAddBanca);
        lvBanci = findViewById(R.id.lvBanci);

        // Inițializăm lista și adapterul personalizat
        banciList = new ArrayList<>();
        adapter = new BancaAdapter(this, banciList);
        lvBanci.setAdapter(adapter);

        // Butonul de adăugare – deschide activitatea pentru adăugare
        btnAddBanca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivityLab4.this, Lab4Activity2.class);
                startActivityForResult(intent, REQUEST_CODE_ADD);
            }
        });

        // Click scurt: se deschide activitatea pentru modificarea obiectului selectat
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

        // Click lung: șterge elementul selectat
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

    // Preluare rezultat de la activitate
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            Banca banca = data.getParcelableExtra("banca");
            if (banca != null) {
                if (requestCode == REQUEST_CODE_ADD) {
                    banciList.add(banca);
                } else if (requestCode == REQUEST_CODE_EDIT) {
                    int index = data.getIntExtra("index", -1);
                    if (index != -1) {
                        banciList.set(index, banca);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        }
    }

    // Adapter personalizat pentru afișarea obiectelor Banca
    private class BancaAdapter extends ArrayAdapter<Banca> {
        public BancaAdapter(Context context, List<Banca> banci) {
            super(context, 0, banci);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext())
                        .inflate(R.layout.banca_list_item, parent, false);
            }
            Banca banca = getItem(position);

            // Obținerea referințelor la elementele din layout-ul bank_list_item.xml
            TextView tvNumeBanca = convertView.findViewById(R.id.tvNumeBanca);
            TextView tvFiliale = convertView.findViewById(R.id.tvFiliale);
            TextView tvTipBanca = convertView.findViewById(R.id.tvTipBanca);
            // Pentru Internet Banking, avem două RadioButton-uri din cadrul RadioGroup:
            RadioButton rbHasInternet = convertView.findViewById(R.id.rbHasInternet);
            RadioButton rbNoInternet = convertView.findViewById(R.id.rbNoInternet);
            // Pentru credite, avem CheckBox-uri:
            CheckBox cbCreditI = convertView.findViewById(R.id.cbCreditI);
            CheckBox cbCreditA = convertView.findViewById(R.id.cbCreditA);
            CheckBox cbCreditP = convertView.findViewById(R.id.cbCreditP);
            // Pentru rating, avem un RatingBar:
            RatingBar rbRating = convertView.findViewById(R.id.rbRating);
            // Pentru data înființării:
            TextView tvData = convertView.findViewById(R.id.tvData);

            // Setarea datelor din obiectul Banca
            tvNumeBanca.setText(banca.getNumeBanca());
            tvFiliale.setText(String.valueOf(banca.getNumarFiliale()));
            tvTipBanca.setText(banca.getTipBanca().toString());

            // Pentru Internet Banking, selectăm radio button-ul corespunzător:
            if (banca.hasInternetBanking()) {
                rbHasInternet.setChecked(true);
                rbNoInternet.setChecked(false);
            } else {
                rbHasInternet.setChecked(false);
                rbNoInternet.setChecked(true);
            }

            // Pentru credite, bifăm CheckBox-urile în funcție de tipurile de credite:
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

            // Setăm rating-ul în RatingBar (ca indicator doar):
            rbRating.setRating((float) banca.getRating());

            // Setăm data înființării:
            tvData.setText(banca.getDataInfiintare().toString());

            return convertView;
        }
    }

}
