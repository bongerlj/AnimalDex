package com.animaldex.animaldex;

import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileInputStream;
import java.util.List;

public class SightingHistoryMap extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<SightingHistoryElement> history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sighting_history_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        history = getIntent().getParcelableArrayListExtra("History");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(0, 0)));

        for(SightingHistoryElement i : history){
            //Prevents a marker from being placed at the 0,0 coordinate, the NULL location
            if(i.getCoordinate().latitude != 0 && i.getCoordinate().longitude != 0){
                mMap.addMarker(new MarkerOptions()
                        .position(i.getCoordinate())
                        .title(i.toString()));
            }
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(history.get(history.size() - 1).getCoordinate()));
    }
}
