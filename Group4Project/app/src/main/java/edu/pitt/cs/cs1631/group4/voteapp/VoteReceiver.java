package edu.pitt.cs.cs1631.group4.voteapp;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import javax.xml.transform.Result;

public class VoteReceiver extends BroadcastReceiver {
    final int RESULT_SUCCESS = 0;
    final int RESULT_INVALID = 1;
    private boolean test = false;
    private int testResult = 3;
    //final int TEST_SUCCESSFUL = 3;

    public static final String TAG = "Uploader";

    static ComponentSocket client;

    private static final String SENDER = "Uploader";
    private static final String REGISTERED = "Registered";
    private static final String DISCOONECTED =  "Disconnect";
    private static final String SCOPE = "SIS";

    private KeyValueList readingMessage;

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;


    //TODO: delete when done
    private VotingService service;

    static Handler callbacks = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str;
            String[] strs;
            switch (msg.what) {
                case CONNECTED:
                    //registerToServerButton.setText(REGISTERED);
                    Log.e(TAG, "===============================================================CONNECTED" );
                    break;
                case DISCONNECTED:
                    //connectToServerButton.setText("Connect");
                    Log.e(TAG, "===============================================================DISCONNECTED" );
                    break;
                case MESSAGE_RECEIVED:
                    str = (String)msg.obj;

                    //TODO:
                    //should listen for result codes and send text messages here.

/*                    messageReceivedListText.append(str+"********************\n");
                    final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
                    if (scrollAmount > 0)
                        messageReceivedListText.scrollTo(0, scrollAmount);
                    else
                        messageReceivedListText.scrollTo(0, 0);*/
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    public VoteReceiver(VotingService service)
    {
        super();
        //TODO: delete when done
        this.service = service;



        //register
        client = new ComponentSocket(NetTool.getIpAddress(), 8000,callbacks);
        client.start();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyValueList list = generateRegisterMessage();
                client.setMessage(list);
            }
        }, 500);

        //connect
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                KeyValueList list = generateConnectMessage();
                client.setMessage(list);
            }
        }, 100);



    }

    public void sendTestMessage() {

        KeyValueList testMessage = generateVoteMessage("555", "1");

        client.setMessage(testMessage);
    }

/*    public int simulateOnReceive(Long fromNumber, int selection)
    {
        //use for testing...
        //TODO
        return service.castVote(fromNumber, selection);

    }*/

    public int getTestResult() {
        return testResult;
    }

    public void toggleTesting() {
        test = !test;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(getApplicationContext(), "sms received", Toast.LENGTH_LONG).show();
        //TODO:
        if(!service.isListening()) return;
        //goAsync();
        Bundle bundle = intent.getExtras();
        SmsMessage[] msg;
        long phoneNumber;
        //int selection;
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

                    selection = Integer.parseInt(msg[i].getDisplayMessageBody());
                    //selection = msg[i].getDisplayMessageBody().toString();

                    //boolean testVar = false;
                    //TODO
                    //if(test) testVar = service.castTestVote(phoneNumber, selection, )
                    int result = service.castVote(phoneNumber, selection);
                    if(test) testResult = result;

/*                    KeyValueList vote = generateVoteMessage(userphoneNumber, selection);
                    client.setMessage(vote);*/

                    //do something with the result.
                    //0 = success
                    //1 = invalid choice
                    //2 = double vote.
                    //3 = successfull test
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

                } catch (Exception e) {
                    Log.e("receiver", e.getMessage());
                    //abortBroadcast();
                    return;
                }
            }
        }
    }

    KeyValueList generateRegisterMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Register");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        return list;
    }
    //Generate a test connect message, please replace something of attributes with your own.
    KeyValueList generateConnectMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Connect");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        //Set the name of the component
        list.putPair("Name",SENDER);
        return list;
    }

    //Generate a vote message.
    KeyValueList generateVoteMessage(String phoneNumber, String selection) {
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Vote");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");
        //Set the name of the component
        list.putPair("Name",SENDER);
        //put date in message
        list.putPair("From", phoneNumber);
        list.putPair("Vote", selection);
        return list;
    }
}
