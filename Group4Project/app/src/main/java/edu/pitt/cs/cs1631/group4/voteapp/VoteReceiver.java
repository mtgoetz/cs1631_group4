package edu.pitt.cs.cs1631.group4.voteapp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import javax.xml.transform.Result;

public class VoteReceiver extends BroadcastReceiver {
    final int RESULT_SUCCESS = 0;
    final int RESULT_INVALID = 1;

    private VotingService service;

    public VoteReceiver(VotingService service)
    {
        super();
        this.service = service;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(getApplicationContext(), "sms received", Toast.LENGTH_LONG).show();
        if(!service.isListening()) return;
        //goAsync();
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

                    int result = service.castVote(phoneNumber, selection);

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
                    if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != 0)
                    {
                        int x = 1;
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, x);
                    }
                    SmsManager sms = SmsManager.getDefault();
                    sms.sendTextMessage(userphoneNumber, null, message, null, null);
                    Toast.makeText(context, "Sent to: " + phoneNumber + " : " + message, Toast.LENGTH_LONG).show();
                    //abortBroadcast();
                } catch (Exception e) {
                    Log.e("receiver", e.getMessage());
                    //abortBroadcast();
                    return;
                }
            }
        }
    }
}
