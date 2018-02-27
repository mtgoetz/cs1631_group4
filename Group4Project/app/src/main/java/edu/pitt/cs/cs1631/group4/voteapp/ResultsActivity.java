package edu.pitt.cs.cs1631.group4.voteapp;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class ResultsActivity extends AppCompatActivity {

    ListView resultsView;
    Button previewButton;
    Button stopButton;
    //TallyTable table;
    ArrayAdapter<String> displayResults;
    ArrayList<String> list;
    VotingService service;
    VoteReceiver receiver;
    final int RESULT_SUCCESS = 0;
    final int RESULT_INVALID = 1;
    final int RESULT_DUPLICATE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != 0)
        {
            int x = 1;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, x);
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != 0)
        {
            Toast.makeText(this, "No permission", Toast.LENGTH_SHORT).show();

        }

        resultsView = (ListView)findViewById(R.id.resultsList);
        previewButton = (Button)findViewById(R.id.previewButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        //table = new TallyTable();
        list = new ArrayList<>();
        displayResults = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        resultsView.setAdapter(displayResults);
        service = new VotingService();
        receiver = new VoteReceiver(service);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.RECEIVE_SMS");
        //listener = new VoteReceiver();
        this.registerReceiver(receiver, filter);

        //register receiver and init with service as argument.
        //then fix service code to toggle listening etc.

        service.toggleListening();

        ArrayList<String> l = this.getIntent().getStringArrayListExtra("theList");
        Iterator<String> items = l.iterator();
        try {
            while (items.hasNext()) {
                String n = items.next();
                int i = Integer.parseInt(items.next());
                service.addContestant(n, i);
            }
        } catch (Exception e) {
            Log.e("error", "Error getting contestants");
        }

        previewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //displayResults.add("hello");

                //iterate and display
                displayResults.clear();

                Map<Contestant, Integer> res = service.getResults();
                for(Map.Entry<Contestant, Integer> entry : res.entrySet())
                {
                    String displ = entry.getKey().getName();
                    displ += "\t\t:\t\t" + entry.getValue();
                    displayResults.add(displ);

                }
                displayResults.notifyDataSetChanged();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                service.toggleListening();
                displayResults.clear();
                TextView top = (TextView)findViewById(R.id.textView5);
                top.setText("Final Results:");
                Map<Contestant, Integer> res = service.getResults();
                int i = 1;
                for(Map.Entry<Contestant, Integer> entry : res.entrySet())
                {
                    String displ = "" + i++ + ": " + entry.getKey().getName();
                    displ += "\t\t:\t\t" + entry.getValue();
                    displayResults.add(displ);

                }
                displayResults.notifyDataSetChanged();
            }
        });

        //iterate intent for entries into list
        //start broadcast receiver.
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    public void vote(Long userPhoneNum, int ContestantId){
        int result = service.castVote(userPhoneNum, ContestantId);

        String phoneNumber = userPhoneNum.toString();


        //do something with the result.
        //0 = success
        //1 = invalid choice
        //2 = double vote.
        String message;
        if(result == RESULT_SUCCESS)
        {
            message = "vote recorded";
        }
        else if(result == RESULT_INVALID)
        {
            message = "choice invalid, please try again.";
        }
        else
        {
            message = "only one vote allowed per user.";
        }
/*            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, VoteReceiver.class), 0);*/
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, null, null);
    }


}

