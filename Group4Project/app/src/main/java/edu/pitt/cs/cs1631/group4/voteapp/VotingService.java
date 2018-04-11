package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class VotingService extends Application {

    private static final String SENDER = "Results";
    private static final String REGISTERED = "Registered";
    private static final String DISCOONECTED =  "Disconnect";
    private static final String SCOPE = "SIS";

    private KeyValueList readingMessage;

    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;

    static ComponentSocket client;


    Handler callbacks = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String str;
            String[] strs;
            switch (msg.what) {
                case CONNECTED:
                    //registerToServerButton.setText(REGISTERED);
                    //Log.e(TAG, "===============================================================CONNECTED" );
                    break;
                case DISCONNECTED:
                    //connectToServerButton.setText("Connect");
                    //Log.e(TAG, "===============================================================DISCONNECTED" );
                    break;
                case MESSAGE_RECEIVED:
                    str = (String)msg.obj;
                    strs = str.split(" : ");
                    String phoneNumber;
                    String selection;
                    KeyValueList list = new KeyValueList();

                    for(int i = 0; i < strs.length; i+=2) {
                        list.putPair(strs[i], strs[i+1]);
                    }

                    try {
                        phoneNumber = list.getValue("From");
                        selection = list.getValue("Vote");
                        int result = castVote(Long.parseLong(phoneNumber), Integer.parseInt(selection));
                        sendResult(result);
                    } catch (Exception e) {
                        break;
                    }
                    //messageReceivedListText.append(str+"********************\n");
/*                    final int scrollAmount = messageReceivedListText.getLayout().getLineTop(messageReceivedListText.getLineCount()) - messageReceivedListText.getHeight();
                    if (scrollAmount > 0)
                        messageReceivedListText.scrollTo(0, scrollAmount);
                    else
                        messageReceivedListText.scrollTo(0, 0);*/


                    //do something with the message
                    Toast.makeText(getApplicationContext(), "Message received", Toast.LENGTH_LONG);

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private TallyTable ContestantTable;
    private HashSet<Long> voterSet;
    //private VoteReceiver listener;
    private boolean listening = false;
    static int count = 0;

    private KeyValueList messageInfo;

    //need to initialize
    //private ComponentSocket client;

    public VotingService()
    {
        voterSet = new HashSet<>();
        ContestantTable = new TallyTable();

        //get a connection to the server
        if(client!=null && client.isSocketAlive()){
            //Toast.makeText(MainActivity.this,"Already registered.",Toast.LENGTH_SHORT).show();
        }else {
            client = new ComponentSocket(NetTool.getIpAddress(), 8000, callbacks);
            client.start();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //register
                    KeyValueList list = generateRegisterMessage();
                    client.setMessage(list);
                }
            }, 500);

            Log.e(MainActivity.TAG, "Sending connectToServerButton.1");
            Log.e(MainActivity.TAG, "Sending connectToServerButton.2");
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //connect
                    KeyValueList list = generateConnectMessage();
                    client.setMessage(list);
                }
            }, 100);

            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //connect
                    KeyValueList list = generateReadingMessage();
                    client.setMessage(list);
                }
            }, 100);
        }
    }

    public boolean isListening(){
        return listening;
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

    /*
    * cast a test vote
    * @return true if the expected result is equal to the actual returned vale.
    * */
    public boolean castTestVote(Long userPhoneNum, int contestantID, int expectedResultCode) {
        int result = castVote(userPhoneNum, contestantID);
        if(result != expectedResultCode) return false;
        return true;
    }

    public int castVote(Long userPhoneNum, int ContestantId) {
        if (!voterSet.contains(userPhoneNum)) {
            if (ContestantTable.castVote(ContestantId)) {
                voterSet.add(userPhoneNum);
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
        //Set the name of the component
        list.putPair("Name",SENDER);
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

    KeyValueList generateReadingMessage(){
        KeyValueList list = new KeyValueList();
        //Set the scope of the message
        list.putPair("Scope",SCOPE);
        //Set the message type
        list.putPair("MessageType","Reading");
        //Set the sender or name of the message
        list.putPair("Sender",SENDER);
        //Set the role of the message
        list.putPair("Role","Basic");

        //the following three attributes are necessary for sending the message to Uploader through PC SIS server.
        list.putPair("Broadcast", "True");
        list.putPair("Direction", "Up");
        list.putPair("Receiver", "Uploader");

        return list;
    }

    public void sendResult(int result) {
        //TODO: send the result code and give it to the receiver.
    }

    //give this the current IP and port
/*    public void sendConnect(String IP){
        if(client==null){
            Toast.makeText(getApplicationContext(), "Please connect to the server first.",Toast.LENGTH_SHORT).show();
            return;
        }

        //String scope = messageScope.getText().toString();
        String scope = "SIS";

*//*        if(scope==null || scope.equals("")){
            Toast.makeText(PlaceholderFragment.this.getContext(), "Please enter a scope.",Toast.LENGTH_SHORT).show();
            return;
        }*//*

        messageInfo.putPair("Scope",scope);

        String type = messageType.getText().toString();
        if(type==null || type.equals("")){
            Toast.makeText(PlaceholderFragment.this.getContext(), "Please enter a message type.",Toast.LENGTH_SHORT).show();
            return;
        }
        messageInfo.putPair("MessageType", type);

        String name = messageName.getText().toString();
        if(name==null || name.equals("") || name.equals("Name")){
            Toast.makeText(PlaceholderFragment.this.getContext(), "Please enter a sender name.",Toast.LENGTH_SHORT).show();
            return;
        }
        messageInfo.putPair("Sender", name);

        String role = roleType.getText().toString();
        if(role!=null && !role.equals("") && !role.equals("Role")){
            messageInfo.putPair("Role",role );
        }

        String content = messageContent.getText().toString();
        if(content!=null && !content.equals("")  && !content.equals("Message")){
            messageInfo.putPair("Message", content);
        }


        String recStr = receiver.getText().toString();
        if(recStr!=null && !recStr.equals("") && !recStr.equals("Receiver")){
            messageInfo.putPair("Receiver", recStr);
        }
        KeyValueList tmp = messageInfo;
        sentMessage(tmp);
        messageInfo = new KeyValueList();
    }*/
}
