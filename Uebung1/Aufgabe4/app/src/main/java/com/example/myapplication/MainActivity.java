package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.EditText;
import java.lang.String;

public class MainActivity extends AppCompatActivity {
    // creation of global variables
    float var1, var2;
    boolean plus,minus,div,mult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // creation of references to view elements
        EditText ed1 = (EditText) findViewById(R.id.editTextNumber);

        Button button0 = (Button) findViewById(R.id.button);
        Button button1 = (Button) findViewById(R.id.button2);
        Button button2 = (Button) findViewById(R.id.button3);
        Button button3 = (Button) findViewById(R.id.button4);
        Button button4 = (Button) findViewById(R.id.button5);
        Button button5 = (Button) findViewById(R.id.button6);
        Button button6 = (Button) findViewById(R.id.button7);
        Button button7 = (Button) findViewById(R.id.button8);
        Button button8 = (Button) findViewById(R.id.button9);
        Button button9 = (Button) findViewById(R.id.button10);

        Button decpntbutton = (Button) findViewById(R.id.button11);
        Button equalbutton = (Button) findViewById(R.id.button12);
        Button plusbutton = (Button) findViewById(R.id.button13);
        Button minusbutton = (Button) findViewById(R.id.button14);
        Button multbutton = (Button) findViewById(R.id.button15);
        Button divbutton = (Button) findViewById(R.id.button16);
        Button cnclbutton = (Button) findViewById(R.id.button17);
        Button cnclebutton = (Button) findViewById(R.id.button19);
        Button resetbutton = (Button) findViewById(R.id.button18);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v){
                // 0-9 Button
                if(v.getId()== R.id.button)
                    ed1.setText(ed1.getText()+"0");
                if(v.getId()== R.id.button2)
                    ed1.setText(ed1.getText()+"1");
                if(v.getId()== R.id.button3)
                    ed1.setText(ed1.getText()+"2");
                if(v.getId()== R.id.button4)
                    ed1.setText(ed1.getText()+"3");
                if(v.getId()== R.id.button5)
                    ed1.setText(ed1.getText()+"4");
                if(v.getId()== R.id.button6)
                    ed1.setText(ed1.getText()+"5");
                if(v.getId()== R.id.button7)
                    ed1.setText(ed1.getText()+"6");
                if(v.getId()== R.id.button8)
                    ed1.setText(ed1.getText()+"7");
                if(v.getId()== R.id.button9)
                    ed1.setText(ed1.getText()+"8");
                if(v.getId()== R.id.button10)
                    ed1.setText(ed1.getText()+"9");
                // Decimal Point Button
                if(v.getId()== R.id.button11) {
                    // if EditText already contains decimalpnt do nothing
                    if (String.valueOf(ed1.getText()).contains(".")) {
                        ;
                    // if EditText is empty add 0 and then decimalpnt
                    } else {
                        if (String.valueOf(ed1.getText()).equals("")) {
                            ed1.setText("0" + ".");
                        // if not empty get EditText and add decimalpoint
                        } else {
                            ed1.setText(ed1.getText() + ".");
                        }
                    }
                }
                // Plus Button
                if(v.getId()== R.id.button13) {
                    // if EditText is empty do nothing
                    if(String.valueOf(ed1.getText()).equals("")) {
                        ;
                    } else {
                        // get value 1 put it in global var 1 and parse from String to float then
                        // set boolean plus to true and reset EditText to empty string
                        // same for minus, div and mult
                        var1 = Float.parseFloat(String.valueOf(ed1.getText()));
                        plus = true;
                        ed1.setText("");
                    }
                }
                // Minus Button
                if(v.getId()== R.id.button14) {
                    // if EditText is empty do nothing
                    if(String.valueOf(ed1.getText()).equals("")) {
                        ;
                    } else {
                        var1 = Float.parseFloat(String.valueOf(ed1.getText()));
                        minus = true;
                        ed1.setText("");
                    }
                }
                // Division Button
                if(v.getId()== R.id.button16) {
                    // if EditText is empty do nothing
                    if(String.valueOf(ed1.getText()).equals("")) {
                        ;
                    } else {
                        var1 = Float.parseFloat(String.valueOf(ed1.getText()));
                        div = true;
                        ed1.setText("");
                    }
                }
                // Multiplication Button
                if(v.getId()== R.id.button15) {
                    // if EditText is empty do nothing
                    if(String.valueOf(ed1.getText()).equals("")) {
                        ;
                    } else {
                        var1 = Float.parseFloat(String.valueOf(ed1.getText()));
                        mult = true;
                        ed1.setText("");
                    }
                }
                // Cancel "c" Button
                if(v.getId()== R.id.button17)
                    // Delete Text in EditText
                    ed1.setText("");
                // Equals Button
                if(v.getId()== R.id.button12) {
                    // if EditText is empty do nothing
                    if(String.valueOf(ed1.getText()).equals("")) {
                        ;
                    } else {
                        // "save" String of EditText as Float to global variable var 2
                        var2 = Float.parseFloat(String.valueOf(ed1.getText()));
                        // if plus is true add var 1 to var 2 and reset plus boolean and set EditText to result
                        if (plus == true) {
                            ed1.setText(String.valueOf(var1 + var2));
                            plus = false;
                        } // if minus is true subtract var 2 from var 1 and reset minus boolean and set EditText to result
                        else if (minus == true) {
                            ed1.setText(String.valueOf(var1 - var2));
                            plus = false;
                        } // if div is true check if var2 is 0
                        else if (div == true) {
                            if (var2 == 0) {
                                ed1.setText("Error: can't divide with 0. Please hit Reset button");
                            } // if var 2 is not zero divide var 1 with var 2 and then set boolean div to false
                            else {
                                ed1.setText(String.valueOf(var1 / var2));
                                div = false;
                            }
                        } // if mult is true multiplicate var 1 to var 2 and reset mult boolean and set EditText to result
                        else if (mult == true) {
                            ed1.setText(String.valueOf(var1 * var2));
                            mult = false;
                        }
                    }
                }
                // Reset Button
                if(v.getId()== R.id.button18) {
                    // reset all variables to default settings
                    ed1.setText("");
                    var1 = 0;
                    var2 = 0;
                    plus = false;
                    minus = false;
                    div = false;
                    mult = false;
                }
                // CE Button which is basically Reset
                if(v.getId()== R.id.button19) {
                    // reset all variables to default settings
                    ed1.setText("");
                    var1 = 0;
                    var2 = 0;
                    plus = false;
                    minus = false;
                    div = false;
                    mult = false;
                }
            }
        };

        // Set onclick Listener for buttons to func listener
        button0.setOnClickListener(listener);
        button1.setOnClickListener(listener);
        button2.setOnClickListener(listener);
        button3.setOnClickListener(listener);
        button4.setOnClickListener(listener);
        button5.setOnClickListener(listener);
        button6.setOnClickListener(listener);
        button7.setOnClickListener(listener);
        button8.setOnClickListener(listener);
        button9.setOnClickListener(listener);
        decpntbutton.setOnClickListener(listener);
        plusbutton.setOnClickListener(listener);
        minusbutton.setOnClickListener(listener);
        divbutton.setOnClickListener(listener);
        multbutton.setOnClickListener(listener);
        cnclbutton.setOnClickListener(listener);
        equalbutton.setOnClickListener(listener);
        cnclebutton.setOnClickListener(listener);
        resetbutton.setOnClickListener(listener);
    }
}