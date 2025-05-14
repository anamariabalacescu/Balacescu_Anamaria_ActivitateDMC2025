package com.example.lab12;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import java.util.Arrays;
import java.util.List;

public class MapShapeActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private String shapeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_shape);

        // Preiau tipul
        shapeType = getIntent().getStringExtra("shape");

        SupportMapFragment mapFrag = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapShape);
        mapFrag.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // centrul pe Berlin
        LatLng berlin = new LatLng(52.5200, 13.4050);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(berlin, 12f));

        drawShape(berlin);
    }

    private void drawShape(LatLng center) {
        if ("circle".equalsIgnoreCase(shapeType)) {
            // cerc cu raza de 5 km
            mMap.addCircle(new CircleOptions()
                    .center(center)
                    .radius(5000)    // metri
                    .strokeWidth(4f));
        } else {
            // poligon triunghiular
            double d = 0.02;
            List<LatLng> pts = Arrays.asList(
                    new LatLng(center.latitude + d, center.longitude),
                    new LatLng(center.latitude, center.longitude + d),
                    new LatLng(center.latitude - d, center.longitude - d)
            );
            mMap.addPolygon(new PolygonOptions()
                    .addAll(pts)
                    .strokeWidth(4f));
        }
    }
}
