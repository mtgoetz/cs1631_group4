package edu.pitt.cs.cs1631.group4.voteapp;

import android.Manifest;
import android.content.Context;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class ResultsFragment extends Fragment {

    ListView resultsView;
    Button previewButton;
    Button stopButton;
    TextView top;
    //TallyTable table;
    ArrayAdapter<String> displayResults;
    ArrayList<String> list;
    VotingService service;
    VoteReceiver receiver;
    final int RESULT_SUCCESS = 0;
    final int RESULT_INVALID = 1;
    final int RESULT_DUPLICATE = 2;

    //private OnFragmentInteractionListener mListener;

    public ResultsFragment() {
        // Required empty public constructor
    }

    public static ResultsFragment newInstance() {
        ResultsFragment fragment = new ResultsFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //Double check permissions
        if(ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECEIVE_SMS) != 0)
        {
            int x = 1;
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.RECEIVE_SMS}, x);
        }

        if(ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECEIVE_SMS) != 0)
        {
            Toast.makeText(getContext(), "No permission", Toast.LENGTH_SHORT).show();

        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_results, container, false);

        resultsView = (ListView) rootView.findViewById(R.id.resultsList);
        previewButton = (Button) rootView.findViewById(R.id.previewButton);
        stopButton = (Button) rootView.findViewById(R.id.stopButton);
        top = (TextView)rootView.findViewById(R.id.textView5);
        list = new ArrayList<>();
        displayResults = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        resultsView.setAdapter(displayResults);
        service = new VotingService();
        receiver = new VoteReceiver(service);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        filter.addAction("android.provider.Telephony.RECEIVE_SMS");

        getActivity().registerReceiver(receiver, filter);

        service.toggleListening();

        //ArrayList<String> l = getActivity().getIntent().getStringArrayListExtra("theList");
        //Iterator<String> items = l.iterator();



        ///Temp to compile
        ArrayList<String> l = new ArrayList<>();
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

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack("newPoll", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }


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
        //mListener = null;
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
