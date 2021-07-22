package com.example.helloworld;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.view.View;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    // Overwriting the OnCreate method to add custom elements
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find View Element by id and add it to Textview Variable textView1
        TextView textView1 = (TextView)findViewById(R.id.textView);
        // if textView element is clicked Listener method will execute function onClick
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            // print message to Log.d
            public void onClick(View v) {
                Log.d("MainActivity", "Textfeld 1 wurde ausgewählt");
            }
        });
        TextView textView2 = (TextView)findViewById(R.id.textView2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Textfeld 2 wurde ausgewählt");
            }
        });
        TextView textView3 = (TextView)findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Textfeld 3 wurde ausgewählt");
            }
        });
        TextView textView4 = (TextView)findViewById(R.id.textView4);
        textView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "Textfeld 4 wurde ausgewählt");
            }
        });
    }
}