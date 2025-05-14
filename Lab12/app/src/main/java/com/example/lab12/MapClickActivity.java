package com.example.lab12;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lab12.databinding.ActivityMapClickBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapClickActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final List<LatLng> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_click);

        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Anamaria - Amsterdam
        LatLng center = new LatLng(52.3676, 4.9041);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 12f));

        mMap.setOnMapClickListener(latLng -> {
            points.add(latLng);
            mMap.addMarker(new MarkerOptions().position(latLng));
            if (points.size() > 1) {
                mMap.addPolyline(new PolylineOptions()
                        .addAll(points)
                        .width(5f));
            }
        });
    }
}