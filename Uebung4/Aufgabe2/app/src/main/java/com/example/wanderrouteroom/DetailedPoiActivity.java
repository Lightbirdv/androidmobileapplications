package com.example.wanderrouteroom;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;

public class DetailedPoiActivity extends AppCompatActivity {

    public static final String EXTRA_POIID = "com.example.wanderrouteroom.EXTRA_ROUTEID";
    public static final String EXTRA_PLACE = "com.example.wanderrouteroom.EXTRA_DESCRIPTION";
    public static final String EXTRA_COORDINATES = "com.example.wanderrouteroom.EXTRA_BEGINNING";
    public static final String EXTRA_POIDESCRIPTION = "com.example.wanderrouteroom.EXTRA_END";
    public static final String EXTRA_IMG = "com.example.wanderrouteroom.EXTRA_DURATION";
    private TextView t_poidescription, t_place, t_coordinates;
    private String poidescription,place, coordinates, img;
    private ImageView poiImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_poi);
        t_poidescription = findViewById(R.id.text_descriptionpoi);
        t_coordinates = findViewById(R.id.text_coordinates);
        t_place = findViewById(R.id.text_place);
        poiImage = findViewById(R.id.poiImage);
        setTextViewDetails();
    }

    public void setTextViewDetails(){
        Intent data = getIntent();
        poidescription = data.getStringExtra(DetailedPoiActivity.EXTRA_POIDESCRIPTION);
        place = data.getStringExtra(DetailedPoiActivity.EXTRA_PLACE);
        coordinates = data.getStringExtra(DetailedPoiActivity.EXTRA_COORDINATES);
        img = data.getStringExtra(DetailedPoiActivity.EXTRA_IMG);
        Log.d("DetailedPoiActivity", "setTextViewDetails: " + " poidescription: " + poidescription + " place: " + place + " coordinates: " + coordinates + " img: " + img);
        if(poidescription != null) {
            t_poidescription.setText(poidescription);
        }
        if(place != null) {
            t_place.setText(place);
        }
        if(coordinates != null) {
            t_coordinates.setText(coordinates);
        }
        if(img != null) {
            File imgFile = new File(img);
            if (imgFile.exists()) {
                poiImage.setImageURI(Uri.fromFile(imgFile));
            }
        }

    }

}
