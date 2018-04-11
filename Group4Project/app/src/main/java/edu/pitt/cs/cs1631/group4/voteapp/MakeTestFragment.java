package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MakeTestFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    Button addValid;
    Button addDup;
    Button addInvalid;
    ListView listView;
    Button saveButton;
    Button cancelButton;
    ArrayList<TestVote> testVotes;
    private int nextTestNum = 1;
    private int phoneNum = 0;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;

    public interface MakeTestCom{
        int getNextTestCode();
        void saveSeq(List<TestVote> votes);
    }

    public MakeTestFragment() {
        // Required empty public constructor
    }

    public static MakeTestFragment newInstance() {
        MakeTestFragment fragment = new MakeTestFragment();
        Bundle args = new Bundle();
        return fragment;
    }

/*    public int rnd(){
        return Random.nextInt(4);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_make_test, container, false);

        addValid = (Button) rootView.findViewById(R.id.add_start_button);
        addDup = (Button) rootView.findViewById(R.id.add_vote_button);
        addInvalid = (Button) rootView.findViewById(R.id.add_stop_button);
        listView = (ListView) rootView.findViewById(R.id.make_list);
        saveButton = (Button) rootView.findViewById(R.id.make_save_button);
        cancelButton = (Button) rootView.findViewById(R.id.make_cancel_button);


        list = new ArrayList<>();
        adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        final MakeTestCom mtc = (MakeTestCom)getActivity();
        //nextTestNum = mtc.getNextTestCode();

        testVotes = new ArrayList<>();

        addValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add start event to sequence and listview
                String uniqueID = UUID.randomUUID().toString();

                Random rd = new Random();

                int selection = rd.nextInt(4);

                TestVote valid = new TestVote(uniqueID, nextTestNum, selection, 0, phoneNum++);
                testVotes.add(valid);
                //add to listview
                String disp = "Vote for " + selection;
                adapter.add(disp);
                adapter.notifyDataSetChanged();
            }
        });

        addDup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add random vote to seq and listview
                if(list.size() == 0) return;
                String dup = list.get(0);
                int selection = Integer.parseInt(dup.substring(9));
                String uniqueID = UUID.randomUUID().toString();
                TestVote duplicate = new TestVote(uniqueID, nextTestNum, selection, 2, --phoneNum);
                phoneNum++;
                testVotes.add(duplicate);

                String disp = "Duplicate Vote";
                adapter.add(disp);
                adapter.notifyDataSetChanged();
            }
        });

        addInvalid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add stop event to seq and listview
                String uniqueID = UUID.randomUUID().toString();
                TestVote invalid = new TestVote(uniqueID, nextTestNum, 5, 1, phoneNum++);
                testVotes.add(invalid);

                String disp = "Invalid Vote";
                adapter.add(disp);
                adapter.notifyDataSetChanged();

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mtc.saveSeq(testVotes);

                Fragment testing = new TestingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag_container, testing)
                        .commit();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment testing = new TestingFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_frag_container, testing)
                        .commit();
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
                    getFragmentManager().popBackStack("testing", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
