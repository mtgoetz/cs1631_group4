package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class StartVoteFragment extends Fragment {


    EditText nameInput;
    EditText idInput;
    Button addButton;
    ListView contestantList;
    static ArrayAdapter<String> entries;
    ArrayList<String> list;
    Button clearButton;
    Button startButton;
    ArrayList<String> contestants;

    //private OnFragmentInteractionListener mListener;

    public interface VotingContestant {
        public void setContestantsList(ArrayList<String> contestantsList);
    }

    public StartVoteFragment() {
        // Required empty public constructor
    }

    public static StartVoteFragment newInstance() {
        StartVoteFragment fragment = new StartVoteFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_start_vote, container, false);


        nameInput = (EditText)rootView.findViewById(R.id.textName);
        idInput = (EditText)rootView.findViewById(R.id.enterID);
        addButton = (Button) rootView.findViewById(R.id.addButton);
        contestantList = (ListView)rootView.findViewById(R.id.listAdded);
        list = new ArrayList<String>();
        entries = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_list_item_1, list);
        contestantList.setAdapter(entries);
        clearButton = (Button) rootView.findViewById(R.id.clearButton);
        startButton = (Button) rootView.findViewById(R.id.startButton);
        contestants = new ArrayList<>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput.getText().toString().matches("")) return;
                if(idInput.getText().toString().matches("")) return;
                //else add to list and display
                String name = nameInput.getText().toString();
                String id = idInput.getText().toString();
                int idInt = Integer.parseInt(id);
                //add to map
                contestants.add(name);
                contestants.add(id);
                //add to display
                String toList = name + "\t\t:\t\t" + id;
                entries.add(toList);
                entries.notifyDataSetChanged();
                nameInput.setText("");
                idInput.setText("");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list = new ArrayList<String>();
                entries.clear();
                entries.notifyDataSetChanged();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //this starts the voting
                //Note: list is sent to mainactivity through interface and then to
                //resultsFragment through another interface.
                try {
                    VotingContestant main = (VotingContestant)getActivity();
                    main.setContestantsList(contestants);
                } catch (ClassCastException c) {
                    throw new ClassCastException(getActivity().toString()
                            + " must implement VotingContestant");
                }
                Fragment results = new ResultsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag_container, results)
                        .addToBackStack("newPoll")
                        .commit();
            }
        });

        //for back press
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Log.i(tag, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //Log.i(tag, "onKey Back listener is working!!!");
                    getFragmentManager().popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
