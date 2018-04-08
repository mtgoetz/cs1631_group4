package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.PendingIntent;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import java.io.File;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class TestingFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    Button defaultTestButton;
    Button loadSequenceButton;
    Button createSequencebutton;
    VoteReceiver receiver;
    VotingService service;

    private View rootView;

    public TestingFragment() {
        // Required empty public constructor
    }

    public static TestingFragment newInstance() {
        TestingFragment fragment = new TestingFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_testing, container, false);

        service = new VotingService();
        receiver = new VoteReceiver(service);
        receiver.toggleTesting();


        defaultTestButton = (Button) rootView.findViewById(R.id.default_test_button);
        loadSequenceButton = (Button) rootView.findViewById(R.id.load_sequence_button);
        createSequencebutton = (Button) rootView.findViewById(R.id.make_sequence_button);

        defaultTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: run default sequence
            }
        });

        loadSequenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: load a premade test sequence
/*                try {
                    File directory = getContext().getFilesDir();
                    File loadSeq = new File(directory, "savedSequence");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                SharedPreferences sharedPref = getContext().getSharedPreferences("savedSequence", MODE_PRIVATE);
                String savedSeq = sharedPref.getString("savedSequence", null);

                Toast.makeText(getContext(), savedSeq, Toast.LENGTH_LONG);


            }
        });

        createSequencebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment newTest = new MakeTestFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .addToBackStack("testing")
                        .replace(R.id.main_frag_container, newTest)
                        .commit();
            }
        });


        //for back pressed
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
/*    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
/*        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/

    }


    @Override
    public void onDetach() {
        super.onDetach();
        receiver.toggleTesting();
        //mListener = null;
    }


    public void runDefaultTest() {

        //receiver = new VoteReceiver(new VotingService());

        //code a sequence...how to start and stop????




    }

    public boolean sendTextMessage(Long phoneNumber, int selection, int expectedResultCode) {


        String message = Integer.toString(selection);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber.toString(), null, message, null, null);
        //TODO: !!!!!!!!!this must be insantiated at some point
        int result = receiver.getTestResult();

        if(result != expectedResultCode) return false;
        return true;

        //do something with the result.
        //0 = success
        //1 = invalid choice
        //2 = double vote.



/*        String message;
        if(result == 0)
        {
            message = "vote recorded";
        }
        else if(result == 1)
        {
            message = "choice invalid, please try again.";
        }
        else
        {
            message = "only one vote allowed per user.";
        }*/
/*        if(ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS) != 0)
        {
            int x = 1;
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.SEND_SMS}, x);
        }*/
        //SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(userphoneNumber, null, message, null, null);
        //Toast.makeText(getContext(), "Sent to: " + phoneNumber + " : " + message, Toast.LENGTH_LONG).show();

    }

    @Dao
    public interface TestingDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        public void insertTestVote(TestVote aVote);

        @Delete
        public void deleteTestVote(TestVote aVote);

        @Query("SELECT * FROM testvote")
        List<TestVote> getAll();

        @Query("SELECT * FROM testvote WHERE test_num = :testNum")
        List<TestVote> getSaved(int testNum);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}


//so to test - get info from db, cast vote as sms
///.....need to have a way to increment from phone numbers - split method using the boolean





