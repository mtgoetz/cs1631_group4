package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

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
        Bundle bundle = intent.getExtras();
        SmsMessage[] msg;
        String phoneNumber;
        int selection;

        if(bundle != null){
            Object[] pdus = (Object[])bundle.get("pdus");
            msg = new SmsMessage[pdus.length];
            for(int i = 0; i < msg.length; i++){
                try{
                msg[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                phoneNumber = msg[i].getOriginatingAddress();

                selection = Integer.parseInt(msg[i].getMessageBody().toString());


                } catch (Exception e) {

                    //add error code here
                    return;
                }
            }
        }

        //send these as a vote...



    }
}
