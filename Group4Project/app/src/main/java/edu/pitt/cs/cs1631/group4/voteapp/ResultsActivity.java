package edu.pitt.cs.cs1631.group4.voteapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

public class ResultsActivity extends AppCompatActivity {

    ListView resultsView;
    Button previewButton;
    Button stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsView = (ListView)findViewById(R.id.resultsList);
        previewButton = (Button)findViewById(R.id.previewButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        //iterate intent for entries into list
        //start broadcast receiver.
    }
}
