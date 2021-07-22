package com.example.wanderrouteroom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class NewRouteActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.routelistsql.REPLY";
    public String FILE_NAME;
    private EditText mEditRouteView;
    private TextView mRecordingMsg;

    private Button recgpx;
    private Float p;
    private Double h;
    private Boolean recordinggpx = false;
    private List<String> contents = new ArrayList<>();
    private LocationManager lm;
    private SensorManager sensorManager;
    private Sensor pressureSensor;
    private String b,e,duration;

    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // if sensor value changed get value 1 of array and put in pressure textView
            float[] values = event.values;
            p = values[0];
            // calculate height from international heightformula
            h= 44330 * (1 - Math.pow((values[0]/1013.25),(1/5.255)) );
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route);
        sensorManager   = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor  = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        mEditRouteView = findViewById(R.id.edit_description);
        mRecordingMsg = findViewById(R.id.recordingmsg);
        recgpx = findViewById(R.id.recButtonGPX);
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent(NewRouteActivity.this, MainActivity.class);
            if (TextUtils.isEmpty(mEditRouteView.getText()) || contents.isEmpty()) {
                Log.d("CONTENTCHECK", contents.get(0));
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String description = mEditRouteView.getText().toString();
                String beginn = contents.get(0);
                String ende = contents.get(contents.size() - 1);
                SimpleDateFormat sdf
                        = new SimpleDateFormat(
                        "yyyy-MM-dd'T'HH:mm:ssZ");
                try {
                    Date bd = sdf.parse(b);
                    Date ed = sdf.parse(e);
                    long d = Math.abs(bd.getTime() - ed.getTime());
                    duration = Long.toString(d);
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                }
                Route resR = new Route(description,beginn,ende, FILE_NAME, duration);
                RouteViewModel mRouteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteViewModel.class);
                mRouteViewModel.insert(resR);
                contents.clear();
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });

        if (ContextCompat.checkSelfPermission(NewRouteActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(NewRouteActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(NewRouteActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(NewRouteActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        // Acquire a reference to the system Location Manager
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // create LocationListener object with on Location changed method
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // output to textViews: Latitude Longitude Altitude and Speed from the location object
                if (recordinggpx == true) {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date());
                    String heightString = h.toString();
                    String content = String.format("%s %f %f %s;", timeStamp, location.getLatitude(), location.getLongitude(), heightString);
                    Log.d("GPS-Tracker", content);
                    contents.add(content);
                    b = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date());;
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.recButtonGPX) {
                    if(recordinggpx == true) {
                        recordinggpx = false;
                        mRecordingMsg.setText("Stopped Recording...");
                        if(contents.isEmpty()) {
                            System.out.println("no content");
                        } else {
                            writeToFileGPX();
                            e = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date());;
                        }
                    }
                    if(recordinggpx == false) {
                        Log.d("GPS-Tracker","GPX recording...");
                        mRecordingMsg.setText("Recording Route...");
                        recordinggpx = true;
                    }
                }
            }
        };
        recgpx.setOnClickListener(listener);
    }

    public void writeToFileGPX () {
        // reset fos to null for checks
        FileOutputStream fos = null;
        try {
            // Open file coordinates.csv with mode append to append new data
            FILE_NAME = UUID.randomUUID().toString() + ".gpx";
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            String beg ="<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n"
                    + "<gpx version=\"1.1\" creator=\"noone\">\n"
                    + "<name>Trackname1</name>\n"
                    + "    <desc>Trackbeschreibung</desc> \n"
                    + "    <trkseg> \n";
            String end = "    </trkseg>\n"
                    + "  </trk>\n"
                    + "</gpx>";
            fos.write(beg.getBytes());
            // write content string with coordinates as Bytes
            for (String content: contents) {
                String middle = "      <trkpt lat=\"" + content.split(" ")[1] + "\" lon=\"" + content.split(" ")[2] + "\">\n"
                        +       "        <ele>" + content.split(" ")[3] + "</ele>\n"
                        +       "        <time>" + content.split(" ")[0] + "</time>\n "
                        +       "      </trkpt>\n";
                fos.write(middle.getBytes());
            }
            fos.write(end.getBytes());
            // show where the file is saved as a toast text
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // if the app gets stopped for some reason f.e call resume by registering the Listener to the Sensor
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_UI);
    }
    // if the app gets paused unregister the Listener to stop the processing
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventListener);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        // check for permission result otherwise create Toast for Permission denied
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(NewRouteActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}