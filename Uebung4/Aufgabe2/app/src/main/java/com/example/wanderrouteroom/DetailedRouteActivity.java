
package com.example.wanderrouteroom;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class DetailedRouteActivity extends AppCompatActivity {

    public static final String EXTRA_ROUTEID = "com.example.wanderrouteroom.EXTRA_ROUTEID";
    public static final String EXTRA_DESCRIPTION = "com.example.wanderrouteroom.EXTRA_DESCRIPTION";
    public static final String EXTRA_BEGINNING = "com.example.wanderrouteroom.EXTRA_BEGINNING";
    public static final String EXTRA_END = "com.example.wanderrouteroom.EXTRA_END";
    public static final String EXTRA_DURATION = "com.example.wanderrouteroom.EXTRA_DURATION";
    public static final String EXTRA_GPXDATA = "com.example.wanderrouteroom.EXTRA_GPXDATA";
    public static final int POI_LIST_REQUEST_CODE = 1;
    private TextView t_routeId, t_description, t_beginning, t_end, t_duration, t_gpxdata;
    private Button b_showPois;
    private int id;
    private String description, beginning, end,duration, gpxdata;
    private PoiViewModel mPoiViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_route);
        t_routeId = findViewById(R.id.text_routeid);
        t_description = findViewById(R.id.text_description);
        t_beginning = findViewById(R.id.text_beginn);
        t_end = findViewById(R.id.text_end);
        t_duration = findViewById(R.id.text_duration);
        t_gpxdata = findViewById(R.id.text_gpxdata);
        b_showPois = findViewById(R.id.button_ShowPois);
        setTextViewDetails();

        mPoiViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoiViewModel.class);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.button_ShowPois) {
                    //List<Poi> pois = mPoiViewModel.getAll(id);
                    //Log.d("DetailedRouteActivity", "onClick: " + pois);
                    Intent intent = new Intent(DetailedRouteActivity.this, PoiListActivity.class);
                    intent.putExtra(PoiListActivity.EXTRA_ROUTEPOIID, id);
                    startActivityForResult(intent, POI_LIST_REQUEST_CODE);
                }
            }
        };
        b_showPois.setOnClickListener(listener);
    }

    public void setTextViewDetails(){
        Intent data = getIntent();
        id = data.getIntExtra(DetailedRouteActivity.EXTRA_ROUTEID, -1);
        description = data.getStringExtra(DetailedRouteActivity.EXTRA_DESCRIPTION);
        beginning = data.getStringExtra(DetailedRouteActivity.EXTRA_BEGINNING);
        end = data.getStringExtra(DetailedRouteActivity.EXTRA_END);
        duration = data.getStringExtra(DetailedRouteActivity.EXTRA_DURATION);
        gpxdata = data.getStringExtra(DetailedRouteActivity.EXTRA_GPXDATA);
        t_routeId.setText(id+"");
        t_description.setText(description);
        t_beginning.setText(beginning);
        t_end.setText(end);
        t_duration.setText(duration);
        t_gpxdata.setText(gpxdata);

    }

}
