package com.example.hoehenmeter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.content.SharedPreferences;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MyActivity";
    // create global variables
    double interaction;
    TextView pressure;
    TextView height;
    TextView interact;
    Button plus;
    Button minus;
    Button save;
    double p,h;
    public static final String SHARED_PREFS = "sharedPrefs";

    // initiate sensor Listener and Manager
    SensorManager sensorManager;
    Sensor pressureSensor;
    SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
                // if sensor value changed get value 1 of array and put in pressure textView
                float[] values = event.values;
                p = values[0];
                pressure.setText(String.format("%.3f mbar", p));
                // calculate height from international heightformula
                h= 44330 * (1 - Math.pow((values[0]/1013.25),(1/5.255)) );
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
        // find Views and declare to variables
        pressure = findViewById(R.id.textPressure);
        height   = findViewById(R.id.textHeight);
        interact = findViewById(R.id.textInteract);
        plus     = findViewById(R.id.plusButton);
        minus    = findViewById(R.id.minusButton);
        save     = findViewById(R.id.saveValue);
        interact.setText(String.valueOf(interaction = 0));
        sensorManager   = (SensorManager) getSystemService(SENSOR_SERVICE);
        pressureSensor  = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        // check if interactionKey exists and load
        if (sharedPreferences.contains("interactionKey")) {
            interact.setText(sharedPreferences.getString("interactionKey", ""));
            Log.d(TAG,sharedPreferences.getString("interactionKey", ""));
        }
        //overwritten on click method for buttons
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.getId() == R.id.plusButton) {
                    // if + button is hit unregister the sensor listener to influence data
                    sensorManager.unregisterListener(sensorEventListener);
                    // increase interaction variable
                    interaction = interaction + 1;
                    interact.setText(String.valueOf(interaction));
                    // get height from textView
                    String str = String.valueOf(height.getText());
                    double h = Double.parseDouble(str.substring(0, str.length() - 2)) + 1;
                    height.setText(String.format("%.3f m", h));
                    // calculate pressure from height
                    double p = 1013.25 * Math.pow((1-(0.0065*h)/288.15),5.255);
                    pressure.setText((String.format("%.3f mbar",p)));
                }
                if(v.getId() == R.id.minusButton) {
                    sensorManager.unregisterListener(sensorEventListener);
                    // decrease interaction rest stays the same
                    interaction = interaction - 1;
                    interact.setText(String.valueOf(interaction));
                    String str = String.valueOf(height.getText());
                    double h = Double.parseDouble(str.substring(0, str.length() - 2)) - 1;
                    height.setText(String.format("%.3f m", h));
                    double p = 1013.25 * Math.pow((1-(0.0065*h)/288.15),5.255);
                    pressure.setText((String.format("%.3f mbar",p)));
                }
                if(v.getId() == R.id.saveValue) {
                    String i = String.valueOf(interact.getText());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    System.out.println("hallo");
                    Log.i(TAG,"hallo");
                    editor.putString("interactionKey", i);
                    editor.commit();
                }
            }
        };
        // set buttons on Listener listener
        plus.setOnClickListener(listener);
        minus.setOnClickListener(listener);
        save.setOnClickListener(listener);
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
}