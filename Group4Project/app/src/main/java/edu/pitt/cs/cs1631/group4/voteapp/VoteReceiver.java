package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * Created by mthom on 2/23/2018.
 */

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

                    selection = Integer.parseInt(msg[i].getDisplayMessageBody().toString());



                    //ResultsActivity.vote(phoneNumber, selection);

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

