package com.example.localisation_mobile;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.localisation_mobile.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String showUrl = "http://10.30.80.185/localisation/showPositions.php";
    RequestQueue requestQueue;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(getApplicationContext());


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        setUpMap();
    }
    private void setUpMap() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                showUrl, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray positions = response.getJSONArray("positions");
                    Log.d("MapActivity", "Positions retrieved: " + positions.toString()); // Add this log

                    for (int i = 0; i < positions.length(); i++) {
                        JSONObject position = positions.getJSONObject(i);
                        double latitude = position.getDouble("latitude");
                        double longitude = position.getDouble("longitude");
                        Log.d("MapActivity", "Latitude: " + latitude + ", Longitude: " + longitude);
                        LatLng sydney = new LatLng(-122.08400000000002, -122.08400000000002);
                        mMap.addMarker(new MarkerOptions().position(sydney).title("hmida"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("MapActivity", "JSONException: " + e.getMessage()); // Add this log for JSON exception
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("MapActivity", "VolleyError: " + error.toString()); // Add this log for Volley error
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        setUpMap();

    }
}