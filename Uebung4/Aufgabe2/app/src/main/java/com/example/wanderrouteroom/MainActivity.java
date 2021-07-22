package com.example.wanderrouteroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements RouteItemClickListener{
    //MainActivity implements RouteItemClickListener interface to override the onclick method
    private RouteViewModel mRouteViewModel;
    public static final int NEW_ROUTE_ACTIVITY_REQUEST_CODE = 1;
    public static final int DETAILED_ROUTE_ACTIVITY_REQUEST_CODE = 2;
    private RouteListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new RouteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set Observer on ViewModel if routes change add new routes to Recyclerview
        mRouteViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(RouteViewModel.class);
        mRouteViewModel.getAllRoutes().observe(this, routes -> {
            adapter.setRoutes(routes);
        });
        // Clicking on fab send intent to new Route Activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewRouteActivity.class);
            startActivityForResult(intent, NEW_ROUTE_ACTIVITY_REQUEST_CODE);
        });
    }
    // if Request code is okay make Toast text with
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_ROUTE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //Route word = new Route(data.getStringExtra(NewRouteActivity.DESCRIPTION));
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    R.string.empty_not_saved,
                    Toast.LENGTH_LONG).show();
        }
    }

    //if item on Recyclerview is clicked send intend with Route details to DetailedRouteActivity
    @Override
    public void onRouteItemClick(Route route) {
        Toast.makeText(this, "Route Item has been clicked: " + route.getDescription(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,DetailedRouteActivity.class);
        intent.putExtra(DetailedRouteActivity.EXTRA_ROUTEID, route.getRouteId());
        intent.putExtra(DetailedRouteActivity.EXTRA_DESCRIPTION, route.getDescription());
        intent.putExtra(DetailedRouteActivity.EXTRA_BEGINNING, route.getBeginning());
        intent.putExtra(DetailedRouteActivity.EXTRA_END, route.getEnding());
        intent.putExtra(DetailedRouteActivity.EXTRA_DURATION, route.getDuration());
        intent.putExtra(DetailedRouteActivity.EXTRA_GPXDATA, route.getGpxdata());
        startActivityForResult(intent, DETAILED_ROUTE_ACTIVITY_REQUEST_CODE);
    }

}