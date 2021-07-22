package com.example.learningcardroomapp;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements LearningCardItemClickListener {

    private LearningCardViewModel mLearningCardViewModel;
    public static final int NEW_LEARNING_CARD_ACTIVITY_REQUEST_CODE = 1;
    public static final int DETAILED_LEARNING_CARD_ACTIVITY_REQUEST_CODE = 2;
    public static final int PICKFILE_RESULT_CODE = 3;
    private  final int REQUEST_EXTERNAL_STORAGE = 4;
    private LearningCardListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        adapter = new LearningCardListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set Observer on ViewModel if learningCards change add new learningCards to Recyclerview
        mLearningCardViewModel = new ViewModelProvider(this, new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(LearningCardViewModel.class);
        mLearningCardViewModel.getAll().observe(this, learningCards -> {
            adapter.setLearningCards(learningCards);
        });
        //ItemTouch helper for the recyclerview to enable swiping to the left to delete the learningCard on position
        //First parameter 0 for not onMove Directions added
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mLearningCardViewModel.delete(adapter.getLearningCardAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "LearningCard deleted", Toast.LENGTH_SHORT);
            }
        }).attachToRecyclerView(recyclerView);
        // Clicking on fab send intent to new LearningCard Activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewLearningCardActivity.class);
            startActivityForResult(intent, NEW_LEARNING_CARD_ACTIVITY_REQUEST_CODE);
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_LEARNING_CARD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            //Route word = new Route(data.getStringExtra(NewRouteActivity.DESCRIPTION));
            Toast.makeText(
                    getApplicationContext(),
                    "A new Learningcard has been created",
                    Toast.LENGTH_LONG).show();
        }
        //if pickfile intent was successful do the following code
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            JSONArray alc = null;
            JSONObject object = null;
            //get src from file of pickfile intent
            Uri uri= data.getData();
            String src = uri.getPath();
            //split src from /raw: that is added for some reason
            String jsondata = readFromFile(src.split("/raw:")[1]);
            Toast.makeText(
                    getApplicationContext(),
                    src,
                    Toast.LENGTH_LONG).show();
            try {
                // create json object from String
                alc = new JSONArray(jsondata);
                Log.d("MainActivity", "onActivityResult: " + alc.toString());
                // create object from JSONArray at index and insert it
                for(int i = 0; i < alc.length(); i++) {
                    object = alc.getJSONObject(i);
                    mLearningCardViewModel.insert(new LearningCard(object.get("question").toString(),object.get("answer").toString(),object.get("subject").toString()));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "The saving of the Learningcard was not successful",
                    Toast.LENGTH_LONG).show();
        }
    }
    // get the inflate menu and enable searchView
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.learningcard_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        // after searchView has been clicked set Listener on text change in box
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            // send new Text on textchange to adapter
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            // if item1 of inflate menu is pressed send pick file intent and import cards
            case R.id.item1:
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
                return true;
            // if item2 of inflate menu is pressed export all cards as json via writeToFile function
            case R.id.item2:
                List<LearningCard> learningCards = mLearningCardViewModel.getAllLC();
                Gson gson = new Gson();
                String json = gson.toJson(learningCards);
                Log.d("MainActivity", "onOptionsItemSelected: " + json);
                writeToFile(json);
                return true;
            // if item3 of inflate menu is pressed delete all learningCards
            case R.id.item3:
                mLearningCardViewModel.deleteAll();
                Toast.makeText(
                        MainActivity.this,
                        "All Learning Cards have been deleted",
                        Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    // When LearningCard item on RecyclerView is clicked open DetailedLearningCardActivity and send data with intent
    @Override
    public void onLearningCardItemClick(LearningCard learningCard) {
        Toast.makeText(this, "Route Item has been clicked: " + learningCard.getQuestion(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MainActivity.this,DetailedLearningCardActivity.class);
        intent.putExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_ID, learningCard.getLearningCardId());
        intent.putExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_QUESTION, learningCard.getQuestion());
        intent.putExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_ANSWER, learningCard.getAnswer());
        intent.putExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_SUBJECT, learningCard.getSubject());
        intent.putExtra(DetailedLearningCardActivity.EXTRA_LEARNING_CARD_LEARNED, learningCard.getLearned());
        startActivityForResult(intent, DETAILED_LEARNING_CARD_ACTIVITY_REQUEST_CODE);
    }

    // Store permission in String Array and then request them in onCreate
    private  String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    // Create BufferedReader and read line after line and return string
    public String readFromFile(String path) {
        BufferedReader br = null;
        String returnString ="";
        try {
            String sCurrentLine;
            br = new BufferedReader(new FileReader(path));
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

    // Write json to File with FileOutputStream
    public void writeToFile (String json) {
        // reset fos to null for checks
        FileOutputStream fos = null;
        try {
            String FILE_NAME = "ExportedLearningCards.json";
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(json.getBytes());
            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME,
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

}