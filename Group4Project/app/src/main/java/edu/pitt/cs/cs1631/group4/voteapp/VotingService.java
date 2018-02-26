package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class VotingService extends Application {
    private TallyTable ContestantTable;
    private HashSet<Long> voterSet;
    private VoteReceiver listener;
    private boolean listening = false;

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

    public void startPoll() {
        toggleListening();
        while (listening) {
            if (listener == null) {
                listener = new VoteReceiver();
            }
            //TODO: Get messages from listener, and call castVote()
        }
    }

    public Map<Contestant, Integer> endPoll() {
        toggleListening();
        return getResults();
    }

/*    public class VoteReceiver extends BroadcastReceiver {

        public VoteReceiver() {
            super();
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            SmsMessage[] msg;
            long phoneNumber;
            int selection;

            Log.d("sms received", "Intent recieved: " + intent.getAction());
            if (bundle != null && intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                msg = new SmsMessage[pdus.length];
                for (int i = 0; i < msg.length; i++) {
                    try {
                        msg[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        phoneNumber = Long.parseLong(msg[i].getOriginatingAddress());

                        selection = Integer.parseInt(msg[i].getMessageBody().toString());

                        int status = castVote(phoneNumber, selection);

                    } catch (Exception e) {

                        //add error code here
                        return;
                    }
                }
            }

            //send these as a vote...


        }
    }*/
}
