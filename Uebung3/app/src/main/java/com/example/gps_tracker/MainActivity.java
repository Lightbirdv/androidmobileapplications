package com.example.gps_tracker;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jcoord.LatLng;
import com.example.jcoord.UTMRef;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView pressure;
    TextView height;
    Button draw, recgpx, reccsv;
    Boolean recordingcsv = false;
    Boolean recordinggpx = false;
    double p,h;
    double geoBreite, geoLänge, geoSize, maxBreite, minBreite, maxLänge, minLänge, size, ytemp;
    List<UTMRef> utms = new ArrayList<>();
    String returnString;
    List<Point> points = new ArrayList<>();
    List<String> contents = new ArrayList<>();
    public static final int SizeWidth = 300;
    public static final int SizeHeight = 400;
    public static final int grid = 5;
    private static final String FILE_NAME_CSV = "coordinates.csv";
    private static final String FILE_NAME_GPX = "coordinates.gpx";
    // initiate sensor Listener and Manager
    LocationManager lm;
    SensorManager sensorManager;
    Sensor pressureSensor;
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // if sensor value changed get value 1 of array and put in pressure textView
            float[] values = event.values;
            p = values[0];
            // calculate height from international heightformula
            h= 44330 * (1 - Math.pow((values[0]/1013.25),(1/5.255)) );
            pressure.setText(String.format("%.3f mbar", p));
            height.setText(String.format("%.3f m", h));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sensorManager   = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor  = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        pressure = findViewById(R.id.pressureText);
        height   = findViewById(R.id.heightText);
        draw     = findViewById(R.id.drawButton);
        recgpx      = findViewById(R.id.recButtonGPX);
        reccsv      = findViewById(R.id.recButtonCSV);

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
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // output to textViews: Latitude Longitude Altitude and Speed from the location object
                TextView latLng = (TextView)findViewById(R.id.latlongitude);
                latLng.setText(location.getLatitude() + " " + location.getLongitude());
                if (recordingcsv == true || recordinggpx == true) {
                    String timeStamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new Date());
                    String heightString = height.getText().toString().substring(0, height.length() - 2);
                    String content = String.format("%s %f %f %s;", timeStamp, location.getLatitude(), location.getLongitude(), heightString);
                    Log.d("GPS-Tracker", content);
                    contents.add(content);
                }
            }
        };

        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.drawButton) {
                    draw();
                }
                if (v.getId() == R.id.recButtonCSV) {
                    if(recordingcsv == true) {
                        recordingcsv = false;
                        if(contents.isEmpty()) {
                            System.out.println("no content");
                        } else {
                            writeToFileCSV();
                        }
                        contents.clear();
                    }
                    if(recordingcsv == false) {
                        Log.d("GPS-Tracker","CSV recording...");
                        recordingcsv = true;
                    }
                }
                if (v.getId() == R.id.recButtonGPX) {
                    if(recordinggpx == true) {
                        recordinggpx = false;
                        if(contents.isEmpty()) {
                            System.out.println("no content");
                        } else {
                            writeToFileGPX();
                        }
                        contents.clear();
                    }
                    if(recordinggpx == false) {
                        Log.d("GPS-Tracker","GPX recording...");
                        recordinggpx = true;
                    }
                }
            }
        };
        draw.setOnClickListener(listener);
        reccsv.setOnClickListener(listener);
        recgpx.setOnClickListener(listener);
    }

    public void draw() {
        ImageView imageView=(ImageView) findViewById(R.id.imageView);
        Bitmap bitmap = Bitmap.createBitmap(100, 700, Bitmap.Config.ARGB_8888);
        // initialize canvas and paint
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        imageView.setImageBitmap(bitmap);
        // get coordinates and split at semicolon
        List<String> coordinates = Arrays.asList(readFromFile().split(";"));
        // check if utms list is empty if no clear
        if(utms != null) {
            utms.clear();
        }
        // convert coordinates to utm and add to utms list
        for (String coordinate : coordinates) {
            LatLng x = new LatLng(Double.parseDouble(coordinate.split(" ")[1]),Double.parseDouble(coordinate.split(" ")[2]));
            UTMRef utm = x.toUTMRef();
            utms.add(utm);
        }
        System.out.println(utms);
        // get max and min breite and max and min länge
        minBreite = 0;
        minLänge = 0;
        maxBreite = 0;
        minBreite = 0;
        // sorting for min max values
        for (UTMRef u : utms) {
            if (u.getEasting() > maxBreite) maxBreite = u.getEasting();
            if (u.getNorthing() > maxLänge) maxLänge = u.getNorthing();
            if (minLänge == 0) minLänge = u.getNorthing();
            if (minBreite == 0) minBreite = u.getEasting();
            if (u.getEasting() < minBreite) minBreite = u.getEasting();
            if (u.getNorthing() < minLänge) minLänge = u.getNorthing();
        }
        System.out.println("minBreite: " + minBreite + " maxBreite: " + maxBreite + " minLänge: " + minLänge + " maxLänge: " + maxLänge);
        // calculate geoBreite and geoLänge
        geoBreite = maxBreite - minBreite;
        geoLänge  = maxLänge  - minLänge;
        if (geoBreite > geoLänge) geoSize = geoBreite;
        else geoSize = geoLänge;
        System.out.println("geoBreite: " + geoBreite + " geoLänge: " + geoLänge + " geoSize: " + geoSize);
        paint.setStrokeWidth(6f);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        Path path = new Path();
        //tried to implement grid did not work
        float rowHt = (float)geoBreite / 5;
        float rowWid = (float)geoLänge / 5;
        for (int i = 0; i < 5; i++) {
            path.moveTo(0,i * rowHt);
            path.lineTo((float)geoLänge, i * rowHt);
            canvas.drawPath(path,paint);
        }
        for (int i = 0; i < 5; i++) {
            path.moveTo(i * rowWid, 0);
            path.lineTo(i * rowWid, (float) geoBreite);
            canvas.drawPath(path, paint);
        }
        // go through utms list and create Points that get added to Point list
        for (UTMRef u : utms) {
            double x = ((u.getNorthing() - minLänge)*SizeWidth)/geoSize;
            ytemp = ((u.getEasting() - minBreite)*SizeHeight)/geoSize;
            double y = geoBreite - ytemp;
            System.out.println("x: " + x + " y:" + y );
            points.add(new Point((int)x,(int)y));
        }
        // for point in Point list Move cursor to point and move to next point
        System.out.println(points);
        for (int i = 0; i < points.size() - 1; i++) {
            path.moveTo(points.get(i).x,points.get(i).y);
            path.lineTo(points.get(i+1).x,points.get(i+1).y);
            canvas.drawPath(path,paint);
        }
    }

    public void writeToFileCSV () {
        // reset fos to null for checks
        FileOutputStream fos = null;
        try {
            // Open file coordinates.csv with mode append to append new data
            fos = openFileOutput(FILE_NAME_CSV, MODE_APPEND);
            // write content string with coordinates as Bytes
            for (String content: contents) {
                fos.write(content.getBytes());
                // write new Line line seperator to seperate the coordinates
                fos.write(System.getProperty("line.separator").getBytes());
            }
            // show where the file is saved as a toast text
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME_CSV,
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

    public void writeToFileGPX () {
        // reset fos to null for checks
        FileOutputStream fos = null;
        try {
            // Open file coordinates.csv with mode append to append new data
            fos = openFileOutput(FILE_NAME_GPX, MODE_PRIVATE);
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
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME_GPX,
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


    public String readFromFile() {
        BufferedReader br = null;
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(getFilesDir() + "/" +FILE_NAME_CSV));
            while ((sCurrentLine = br.readLine()) != null) {
                returnString+=sCurrentLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return returnString;
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