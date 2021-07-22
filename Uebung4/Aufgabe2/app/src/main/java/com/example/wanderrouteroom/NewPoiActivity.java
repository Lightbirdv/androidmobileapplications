package com.example.wanderrouteroom;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewPoiActivity extends AppCompatActivity {

    public static final String EXTRA_NEWROUTEPOIID = "com.example.wanderrouteroom.EXTRA_NEWROUTEPOIID";
    private EditText mEditPoiDescription, mEditPoiPlace, mEditPoiCoordinates;
    private String description, place, coordinates, ImgUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi);
        mEditPoiDescription = findViewById(R.id.edit_description);
        mEditPoiPlace = findViewById(R.id.edit_place);
        mEditPoiCoordinates = findViewById(R.id.edit_coordinates);
        Button picButton = findViewById(R.id.poiPictureIntent);
        Intent data = getIntent();
        int id = data.getIntExtra(NewPoiActivity.EXTRA_NEWROUTEPOIID, -1);
        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
            }
        });
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(view -> {
            Intent replyIntent = new Intent(NewPoiActivity.this, MainActivity.class);
            if (TextUtils.isEmpty(mEditPoiPlace.getText()) || TextUtils.isEmpty(mEditPoiDescription.getText()) || TextUtils.isEmpty(mEditPoiCoordinates.getText()) || ImgUrl == null){
                setResult(RESULT_CANCELED, replyIntent);
            } else {
                String description = mEditPoiDescription.getText().toString();
                String place = mEditPoiPlace.getText().toString();
                String coordinates = mEditPoiCoordinates.getText().toString();

                Poi poi = new Poi(place,(long) id,coordinates, description, ImgUrl);
                PoiViewModel mPoiViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoiViewModel.class);
                mPoiViewModel.insert(poi);
                setResult(RESULT_OK, replyIntent);
            }
            finish();
        });
    }

    private void openCamera()
    {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File f = null;
        try
        {
            f = File.createTempFile("temppic",".jpg",getApplicationContext().getCacheDir());
            if (takePictureIntent.resolveActivity(getPackageManager()) != null)
            {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        f));
                ImgUrl = Uri.fromFile(f)+"";
                Log.d("texts", "openCamera: " + ImgUrl);
                startActivityForResult(takePictureIntent, 3);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 3 && resultCode == RESULT_OK) {
            Log.d("texts", "onActivityResult: " + ImgUrl);
        }
    }
}
