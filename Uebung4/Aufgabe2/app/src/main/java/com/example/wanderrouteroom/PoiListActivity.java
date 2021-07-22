package com.example.wanderrouteroom;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PoiListActivity extends AppCompatActivity implements PoiItemClickListener {

    private PoiViewModel mPoiViewModel;
    private PoiListAdapter adapter;
    public static final String EXTRA_ROUTEPOIID = "com.example.wanderrouteroom.EXTRA_ROUTEPOIID";
    public static final int DETAILED_POI_ACTIVITY_REQUEST_CODE = 1;
    public static final int NEW_POI_ACTIVITY_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_poi);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new PoiListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Intent data = getIntent();
        int id = data.getIntExtra(PoiListActivity.EXTRA_ROUTEPOIID, -1);
        mPoiViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(PoiViewModel.class);
        List<Poi> pois = mPoiViewModel.getAll(id);
        adapter.setPois(pois);
        FloatingActionButton fab = findViewById(R.id.fabpoi);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(PoiListActivity.this, NewPoiActivity.class);
            intent.putExtra(NewPoiActivity.EXTRA_NEWROUTEPOIID, id);
            startActivityForResult(intent, NEW_POI_ACTIVITY_REQUEST_CODE);
        });
    }

    @Override
    public void onPoiItemClick(Poi poi) {
        Intent intent = new Intent(PoiListActivity.this,DetailedPoiActivity.class);
        intent.putExtra(DetailedPoiActivity.EXTRA_POIID, poi.getPoiId());
        intent.putExtra(DetailedPoiActivity.EXTRA_PLACE, poi.getPlace());
        intent.putExtra(DetailedPoiActivity.EXTRA_COORDINATES, poi.getCoordinates());
        intent.putExtra(DetailedPoiActivity.EXTRA_POIDESCRIPTION, poi.getDescription());
        intent.putExtra(DetailedPoiActivity.EXTRA_IMG, poi.getImg());
        startActivityForResult(intent, DETAILED_POI_ACTIVITY_REQUEST_CODE);
    }
}
