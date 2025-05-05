package com.example.lab10;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import org.json.*;

import java.io.*;
import java.net.*;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String API_KEY = "8GCUlG8DD8SSgxZlBSx6CUGRRvvaHEGV";
    private EditText etCity;
    private Spinner spinnerDays;
    private TextView tvResult;
    private Button btnSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etCity = findViewById(R.id.etCity);
        spinnerDays = findViewById(R.id.spinnerDays);
        tvResult = findViewById(R.id.tvResult);
        btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String city = etCity.getText().toString().trim();
                if (city.isEmpty()) {
                    etCity.setError("Trebuie un oras");
                    return;
                }
                // mai intai obținem Key-ul orasului
                new CitySearchTask().execute(city);
            }
        });
    }

    private class CitySearchTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            try {
                String urlStr = String.format(
                        Locale.US,
                        "https://dataservice.accuweather.com/locations/v1/cities/search?apikey=%s&q=%s",
                        API_KEY,
                        URLEncoder.encode(cityName, "UTF-8")
                );
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(conn.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();
                // raspunsul e un Array JSON; luam primul obiect
                JSONArray arr = new JSONArray(sb.toString());
                if (arr.length() > 0) {
                    JSONObject cityObj = arr.getJSONObject(0);
                    return cityObj.getString("Key");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String cityKey) {
            if (cityKey != null) {
                tvResult.setText("Cod oras: " + cityKey + "\n\n");
                // determinam cate zile din spinner
                String sel = spinnerDays.getSelectedItem().toString();
                String days = sel.contains("5") ? "5day" : sel.contains("10") ? "10day" : "1day";
                new ForecastTask().execute(cityKey, days);
            } else {
                tvResult.setText("Oras negasit!");
            }
        }
    }

    private class ForecastTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String cityKey  = params[0];
            String daysPath = params[1]; // "1day", "5day" sau "10day"
            try {
                String urlStr = String.format(
                        Locale.US,
                        "https://dataservice.accuweather.com/forecasts/v1/daily/%s/%s?apikey=%s",
                        daysPath, cityKey, API_KEY
                );
                Log.d("ForecastTask", "URL = " + urlStr);

                HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
                conn.setRequestMethod("GET");
                int code = conn.getResponseCode();
                Log.d("ForecastTask", "Response code = " + code);

                InputStream in = (code == HttpURLConnection.HTTP_OK)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) sb.append(line);
                reader.close();

                Log.d("ForecastTask", "Raw response = " + sb);
                // dacă nu e 200 OK, returnează răspunsul brut ca să-l vezi în UI
                if (code != HttpURLConnection.HTTP_OK) return sb.toString();

                return sb.toString();

            } catch (Exception e) {
                Log.e("ForecastTask", "Exception in ForecastTask", e);
                return e.getMessage();
            }
        }


        @Override
        protected void onPostExecute(String jsonStr) {
            if (jsonStr == null || !jsonStr.trim().startsWith("{")) {
                tvResult.setText("Eroare Forecast:\n" + jsonStr);
                return;
            }

            if (jsonStr == null) {
                tvResult.append("Eroare la obținerea prognozei.");
                return;
            }
            try {
                JSONObject root = new JSONObject(jsonStr);
                JSONArray daysArr = root.getJSONArray("DailyForecasts");
                StringBuilder result = new StringBuilder(tvResult.getText());
                for (int i = 0; i < daysArr.length(); i++) {
                    JSONObject day = daysArr.getJSONObject(i);
                    JSONObject temps = day.getJSONObject("Temperature");
                    JSONObject min = temps.getJSONObject("Minimum");
                    JSONObject max = temps.getJSONObject("Maximum");
                    result.append(String.format(
                            Locale.getDefault(),
                            "Ziua %d: Min = %.1f %s, Max = %.1f %s\n",
                            i+1,
                            min.getDouble("Value"), min.getString("Unit"),
                            max.getDouble("Value"), max.getString("Unit")
                    ));
                }
                tvResult.setText(result.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                tvResult.append("Eroare la parsarea JSON.");
            }
        }
    }
}