package com.example.temperature;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // declare variables to view constructs
        TextView textView1 = (TextView)findViewById(R.id.textView);
        Button bt1 = (Button)findViewById(R.id.button);
        Button bt2 = (Button)findViewById(R.id.button2);
        EditText et = (EditText)findViewById(R.id.editTextNumber);
        // initiate TextView with nothing to "make it disappear on startup
        textView1.setText("");
        // if button C -> F is clicked convert from celsius to Fahrenheit
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if editText is empty then set TextView to "Please enter a Value to be converted"
                if(et.getText().toString().isEmpty()) {
                    textView1.setText("Please enter a Value to be converted!");
                }
                else {
                    // getText of EditText as String then convert to Double and put that in variable a
                    double a=Double.parseDouble(String.valueOf(et.getText()));
                    // Convert Celsius to Fahrenheit
                    Double b=(a*9)/5+32;
                    // Cast to string to output it in textView
                    String r=String.valueOf(b);
                    textView1.setText("The converted temperature is: " + r + "°F" );
                }
            }
        });
        // if button F -> C is clicked convert from celsius to Fahrenheit
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if editText is empty then set TextView to "Please enter a Value to be converted"
                if(et.getText().toString().isEmpty()) {
                    textView1.setText("Please enter a Value to be converted!");
                }
                else {
                    // getText of EditText as String then convert to Double and put that in variable a
                    double a=Double.parseDouble(String.valueOf(et.getText()));
                    // Convert Fahrenheit to Celsius
                    Double b=(a-32)*5/9;
                    // Cast to string to output it in textView
                    String r=String.valueOf(b);
                    textView1.setText("The converted temperature is: " + r + "°C" );
                }
            }
        });
    }
}