package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class VotingService extends Application {
    private TallyTable ContestantTable;
    private HashSet<Long> voterSet;
    //private VoteReceiver listener;
    private boolean listening = false;


/*    public VotingService(){
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.RECEIVE_SMS");
        listener = new VoteReceiver();
        this.registerReceiver(listener, filter);
        toggleListening();
    }*/
    public VotingService()
    {
        voterSet = new HashSet<>();
        ContestantTable = new TallyTable();
    }

    public boolean addContestant(String name, int id) {
        if (ContestantTable == null) {
            ContestantTable = new TallyTable();
        }
        return ContestantTable.addContestant(name, id);
    }

    public Map<Contestant, Integer> getResults() {
        return this.ContestantTable.getResult(10);
    }

    public int castVote(Long userPhoneNum, int ContestantId) {
        if (!voterSet.contains(userPhoneNum)) {
            if (ContestantTable.castVote(ContestantId)) {
                return 0; //Vote Successful
            } else {
                return 1; //Vote Unsuccessful - Invalid ContestantId
            }
        }
        return 2; //Vote Unuccessful - Double Vote
    }

    public void toggleListening() {
        this.listening = !this.listening;
    }

/*    public void startPoll() {
        toggleListening();
        while (listening) {
            if (listener == null) {
                listener = new VoteReceiver();
            }
            //TODO: Get messages from listener, and call castVote()
        }
    }*/

    public Map<Contestant, Integer> endPoll() {
        toggleListening();
        return getResults();
    }

/*    public class VoteReceiver extends BroadcastReceiver {

        public VoteReceiver()
        {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            Toast.makeText(getApplicationContext(), "sms received", Toast.LENGTH_LONG).show();

            goAsync();
            Bundle bundle = intent.getExtras();
            SmsMessage[] msg;
            long phoneNumber;
            int selection;

            Log.d("sms received", "Intent recieved: " + intent.getAction());
            if(bundle != null){
                Object[] pdus = (Object[])bundle.get("pdus");
                msg = new SmsMessage[pdus.length];
                for(int i = 0; i < msg.length; i++){
                    try{
                        msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                        phoneNumber = Long.parseLong(msg[i].getOriginatingAddress());
                        String userphoneNumber = (msg[i].getOriginatingAddress());

                        selection = Integer.parseInt(msg[i].getDisplayMessageBody().toString());



                        int result = castVote(phoneNumber, selection);

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
*//*            PendingIntent pi = PendingIntent.getActivity(this, 0,
                    new Intent(this, VoteReceiver.class), 0);*//*
                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(userphoneNumber, null, message, null, null);


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
    }*/
}
