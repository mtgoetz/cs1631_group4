package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        resultsView = (ListView)findViewById(R.id.resultsList);
        previewButton = (Button)findViewById(R.id.previewButton);
        stopButton = (Button) findViewById(R.id.stopButton);
        //table = new TallyTable();
        list = new ArrayList<>();
        displayResults = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        resultsView.setAdapter(displayResults);
        service = new VotingService();
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

    public void vote(Long userPhoneNum, int ContestantId){
        int result = service.castVote(userPhoneNum, ContestantId);



        //do something with the result.
        //0 = success
        //1 = invalid choice
        //2 = double vote.


    }

    public class VoteReceiver extends BroadcastReceiver {

        public VoteReceiver()
        {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            goAsync();
            Bundle bundle = intent.getExtras();
            SmsMessage[] msg;
            long phoneNumber;
            int selection;

            Log.d("sms received", "Intent recieved: " + intent.getAction());
            if(bundle != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")){
                Object[] pdus = (Object[])bundle.get("pdus");
                msg = new SmsMessage[pdus.length];
                for(int i = 0; i < msg.length; i++){
                    try{
                        msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        phoneNumber = Long.parseLong(msg[i].getOriginatingAddress());

                        selection = Integer.parseInt(msg[i].getMessageBody().toString());

                        vote(phoneNumber, selection);

                        //int status = service.castVote(phoneNumber, selection);
                        abortBroadcast();
                    } catch (Exception e) {

                        //add error code here
                        abortBroadcast();
                        return;
                    }
                }
            }

            //send these as a vote...



        }
    }
}
