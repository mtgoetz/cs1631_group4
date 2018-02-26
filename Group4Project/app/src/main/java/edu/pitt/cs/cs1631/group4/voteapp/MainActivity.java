package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    EditText nameInput;
    EditText idInput;
    Button addButton;
    ListView contestantList;
    static ArrayAdapter<String> entries;
    ArrayList<String> list;
    Button clearButton;
    //private static Map<String, Integer> buildList;
    Button startButton;
    //Context context;
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
                //buildList.put(name, Integer.valueOf(id));
                //Contestant newContestant = new Contestant(name, Integer.parseInt(id));
                //contestants.add(newContestant);
                contestants.add(name);
                contestants.add(id.toString());
                //list.add(name);
                //list.add(id.toString());
                //add to display
                String toList = name + "\t\t:\t\t" + id;
                entries.add(toList);
                entries.notifyDataSetChanged();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //list = new list
                list = new ArrayList<String>();
                //buildList.clear();
                //entries = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                entries.clear();
                entries.notifyDataSetChanged();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startVote = new Intent(MainActivity.this, ResultsActivity.class);
                //startVote.putParcelableArrayListExtra((ArrayList<Parcelable>)contestants);
                startVote.putStringArrayListExtra("theList", contestants);
                startActivity(startVote);
            }
        });

    }
}
