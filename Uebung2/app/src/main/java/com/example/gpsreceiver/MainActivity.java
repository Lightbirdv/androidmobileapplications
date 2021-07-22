package com.example.gpsreceiver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.*;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ScrollView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // declaration of global variables
    boolean gps_enabled = true;
    boolean network_enabled, passive_enabled = false;
    LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // check for permissions of fine location if not then request them
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


        // Acquire a reference to the system Location Manager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // create LocationListener object with on Location changed method
        LocationListener locationListenerGPS = new LocationListener() {
            public void onLocationChanged(Location location) {
                // output to textViews: Latitude Longitude Altitude and Speed from the location object
                TextView latLng = (TextView)findViewById(R.id.textLatLng);
                latLng.setText(location.getLatitude() + " " + location.getLongitude());
                TextView altitude = (TextView)findViewById(R.id.textAltitude);
                altitude.setText(Double.toString(location.getAltitude()));
                TextView speed = (TextView)findViewById(R.id.textSpeed);
                speed.setText(Double.toString(location.getSpeed()));
            }
        };
        // two more Location Listener to switch Provider
        LocationListener locationListenerNetwork = new LocationListener() {
            public void onLocationChanged(Location location) {
                TextView latLng = (TextView)findViewById(R.id.textLatLng);
                latLng.setText(location.getLatitude() + " " + location.getLongitude());
                TextView altitude = (TextView)findViewById(R.id.textAltitude);
                altitude.setText(Double.toString(location.getAltitude()));
                TextView speed = (TextView)findViewById(R.id.textSpeed);
                speed.setText(Double.toString(location.getSpeed()));
            }
        };

        LocationListener locationListenerPassive = new LocationListener() {
            public void onLocationChanged(Location location) {
                TextView latLng = (TextView)findViewById(R.id.textLatLng);
                latLng.setText(location.getLatitude() + " " + location.getLongitude());
                TextView altitude = (TextView)findViewById(R.id.textAltitude);
                altitude.setText(Double.toString(location.getAltitude()));
                TextView speed = (TextView)findViewById(R.id.textSpeed);
                speed.setText(Double.toString(location.getSpeed()));
            }
        };
        if(gps_enabled)
            // Request Location update from GPS_Provider after 2000 ms or 10 m distance
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListenerGPS);
        if(network_enabled)
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListenerNetwork);
        if(passive_enabled)
            lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2000, 10, locationListenerPassive);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu1) {
            if(gps_enabled)
                ;
            else {
                Toast.makeText(this, "switched Provider to GPS", Toast.LENGTH_SHORT).show();
                passive_enabled = false;
                network_enabled = false;
                gps_enabled = true;
            }
        }
        if (id == R.id.menu2) {
            if(network_enabled)
                ;
            else {
                Toast.makeText(this, "switched Provider to Network", Toast.LENGTH_SHORT).show();
                passive_enabled = false;
                gps_enabled = false;
                network_enabled = true;
            }
        }
        if (id == R.id.menu3) {
            if(passive_enabled)
                ;
            else {
                Toast.makeText(this, "switched Provider to Passive", Toast.LENGTH_SHORT).show();
                passive_enabled = true;
                gps_enabled = false;
                network_enabled = false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults){
        // check for permission result otherwise create Toast for Permission denied
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
}
