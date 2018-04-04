package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import edu.pitt.cs.cs1631.group4.voteapp.sisserver.ClientInfo;
import edu.pitt.cs.cs1631.group4.voteapp.sisserver.ServerService;

public class MainActivity extends AppCompatActivity {


    TextView statusText;
    TextView iptext;


    private ServerService serverService;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    //Constants from sisServer
    public static final String TAG = "VotingApp";
    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 2;
    public static final int MESSAGE_RECEIVED = 3;
    public static final int DISPLAY_INFO = 1;
    public static final int NEW_COMPONENT_REGISTERED = 2;
    public static final int NEW_COMPONENT_CONNECTED = 3;
    public static final int UPDATE_CLIENT_NAME = 4;
    public static final int CLIENT_DISCONNECTED = 5;
    public static final int UNREGISTERED_COMPONENT = 6;
    public static final String STOP_SERVER = "STOP SERVER";
    public static final String START_SERVER = "START SERVER";
    public static final String LISTENING_PORT = "8000";


    Handler callbacks = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str;
            String[] strs;
            //SimpleItemRecyclerViewAdapter adapter = null;
            ClientInfo.ClientItem item = null;
            switch (msg.what) {
                case DISPLAY_INFO:
                    str = (String) msg.obj;


                    //for display
                    //infoDisplay.append(str);



                    //button
                    //startServerButton.setText(STOP_SERVER);
                    break;
                case NEW_COMPONENT_REGISTERED:
                    str = (String) msg.obj;
                    Toast.makeText(MainActivity.this,"R:"+ str, Toast.LENGTH_SHORT).show();
                    //adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
                    strs = str.split(":");
                    item = new ClientInfo.ClientItem(strs[0],strs[1], str);
                    //adapter.notifyDataSetChanged();
                    break;
                case NEW_COMPONENT_CONNECTED:
                    str = (String) msg.obj;
                    Toast.makeText(MainActivity.this, "C:"+str, Toast.LENGTH_SHORT).show();
                    //adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
                    strs = str.split(":");
                    ClientInfo.updateItemStatus(strs[0],strs[1],2);
                    //item = new ClientInfo.ClientItem(strs[0],strs[1], str);
                    //adapter.notifyDataSetChanged();
                    break;
                case UPDATE_CLIENT_NAME:
                    //adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
                    str = (String) msg.obj;
                    int cid = msg.arg1;
                    ClientInfo.updateItem(cid + "", str);
                    //adapter.notifyDataSetChanged();
                    break;
                case CLIENT_DISCONNECTED:
                    str = (String) msg.obj;
                    strs = str.split(":");
                    ClientInfo.removeItem(strs[0]);
                    //adapter = (SimpleItemRecyclerViewAdapter) recyclerView.getAdapter();
                    //adapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;
                case UNREGISTERED_COMPONENT:
                    str = (String) msg.obj;
                    Toast.makeText(MainActivity.this, "Unregistered component->"+str, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private ServiceConnection mConnection = null;


/*    public interface Status {
        //1 = connected
        //2 = disconnected
        //3 = testing
        //4 = voting
        //5 = reporting
        //6 = error
        public void updateStatus(int status);
    }*/

    public void updateStatus(int status) {
        if(statusText == null) statusText = (TextView) findViewById(R.id.main_status_text);
        switch(status){
            case 1:
                statusText.setText("CONNECTED");
                break;
            case 2:
                statusText.setText("DISCONNECTED");
                break;
            case 3:
                statusText.setText("TESTING");
                break;
            case 4:
                statusText.setText("VOTING");
                break;
            case 5:
                statusText.setText("REPORTING");
                break;
            default:
                statusText.setText("ERROR");
                //error
        }
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instantiate server background service
        serverService = new ServerService();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        setContentView(R.layout.activity_main);

        //this starts the sisServer
        doBindService();

        iptext = (TextView)findViewById(R.id.main_port_text);
        statusText = (TextView) findViewById(R.id.main_status_text);

        Fragment home = new HomeFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frag_container, home)
                .commit();

    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
        //if(mConnection == null) doBindService();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }





    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SISClientList Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }



    void doBindService() {
        mConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                // This is called when the connection with the service has been
                // established, giving us the service object we can use to
                // interact with the service.  Because we have bound to a explicit
                // service that we know is running in our own process, we can
                // cast its IBinder to a concrete class and directly access it.
                serverService = ((ServerService.LocalBinder) service).getService();
                serverService.setActivityCallbacks(callbacks);

                //String port = portText.getText().toString();
/*                if(!port.equals(LISTENING_PORT)){
                    portText.setText(LISTENING_PORT);
                    port = LISTENING_PORT;
                }*/
                serverService.startServer("8000");
                //Display the listening IP and port
                String ip = NetTool.getIpAddress();
                String display = "IP: " + ip + " Port: 8000";
                iptext.setText(display);
                updateStatus(1);
                Toast.makeText(MainActivity.this, "SISServer connected.", Toast.LENGTH_SHORT).show();
            }
            //Callled when the server service is disconnected.
            public void onServiceDisconnected(ComponentName className) {
                serverService = null;
                Toast.makeText(MainActivity.this, "SISServer disconnected.", Toast.LENGTH_SHORT).show();
            }
        };
        bindService(new Intent(MainActivity.this,
                ServerService.class), mConnection, Context.BIND_AUTO_CREATE);
    }

    void stopService() {
        //stopService(new Intent(SISClientListActivity.this, ServerService.class));
        if(serverService!=null){
            Log.e(TAG, "Stopping server.");
            this.unbindService(mConnection);
            serverService.stopSelf();
        }
    }

    @Override
    protected void onDestroy() {
        stopService();
        super.onDestroy();
    }
}
