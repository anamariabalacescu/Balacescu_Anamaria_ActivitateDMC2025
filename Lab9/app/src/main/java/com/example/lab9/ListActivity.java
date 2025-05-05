package com.example.lab9;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        List<BankItem> data = new ArrayList<>();

        data.add(new BankItem(
                "https://www.bnro.ro/files/muzeu/2016/intro/plot1.jpg",
                "Prezentare Muzeu BNR",
                "https://www.bnro.ro/Muzeul-Bancii-Nationale-a-Romaniei-707.aspx"
        ));

        data.add(new BankItem(
                "https://www.bcr.ro/content/dam/ro/bcr/www_bcr_ro/george/george-id/Group-desk.png",
                "Internet Banking - cel mai mare ajutor",
                "https://www.bcr.ro/ro/persoane-fizice/digital-banking/george-id"
        ));

        data.add(new BankItem(
                "https://www.radiooltenia.ro/wp-content/uploads/2024/12/banca-centrala-europeana.jpg",
                "Clădirea BCE din Frankfurt",
                "https://www.ecb.europa.eu"
        ));

        data.add(new BankItem(
                "https://www.forbes.ro/wp-content/uploads/2024/02/Raiffeisen-Bank-Administratia-Centrala.jpg",
                "Administrația Centrală Raiffeisen Bank",
                "https://www.raiffeisen.ro"
        ));

        data.add(new BankItem(
                "https://img.yumpu.com/53690469/1/500x640/alphabank-romania-alpha-bank-romania.jpg",
                "Logo Alpha Bank România",
                "https://www.alphabank.ro"
        ));

        CustomAdapter adapter = new CustomAdapter(this, data);
        ListView lv = findViewById(R.id.listView);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                String link = data.get(pos).getLinkUrl();
                Intent i = new Intent(ListActivity.this, DetailActivity.class);
                i.putExtra("linkUrl", link);
                startActivity(i);
            }
        });
    }
}