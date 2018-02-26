package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.LauncherActivity;
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

public class MainActivity extends AppCompatActivity {

    EditText nameInput;
    EditText idInput;
    Button addButton;
    ListView contestantList;
    static ArrayAdapter<String> entries;
    ArrayList<String> list;
    Button clearButton;



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
                entries = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
                entries.notifyDataSetChanged();

            }
        });

    }
}
