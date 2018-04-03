package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "VotingApp";
    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;

    EditText nameInput;
    EditText idInput;
    Button addButton;
    ListView contestantList;
    static ArrayAdapter<String> entries;
    ArrayList<String> list;
    Button clearButton;
    Button startButton;
    ArrayList<String> contestants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameInput = (EditText)findViewById(R.id.textName);
        idInput = (EditText)findViewById(R.id.enterID);
        addButton = (Button) findViewById(R.id.addButton);
        contestantList = (ListView)findViewById(R.id.listAdded);
        list = new ArrayList<String>();
        entries = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        contestantList.setAdapter(entries);
        clearButton = (Button) findViewById(R.id.clearButton);
        startButton = (Button) findViewById(R.id.startButton);
        contestants = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput.getText().toString().matches("")) return;
                if(idInput.getText().toString().matches("")) return;
                //else add to list and display
                String name = nameInput.getText().toString();
                String id = idInput.getText().toString();
                int idInt = Integer.parseInt(id);
                //add to map
                contestants.add(name);
                contestants.add(id.toString());;
                //add to display
                String toList = name + "\t\t:\t\t" + id;
                entries.add(toList);
                entries.notifyDataSetChanged();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<String>();
                entries.clear();
                entries.notifyDataSetChanged();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startVote = new Intent(MainActivity.this, ResultsActivity.class);
                startVote.putStringArrayListExtra("theList", contestants);
                startActivity(startVote);
            }
        });

    }
}
