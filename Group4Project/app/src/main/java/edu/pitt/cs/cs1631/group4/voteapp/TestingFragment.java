package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import java.util.ArrayList;

public class TestingFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    Button defaultTestButton;
    Button loadSequenceButton;
    Button createSequencebutton;
    //private ArrayList<TestVote> testTable;

    public interface TestingCom {
        public void setTestTable(int i);
        public TestingDao getDB();
    }

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

        defaultTestButton = (Button) rootView.findViewById(R.id.default_test_button);
        loadSequenceButton = (Button) rootView.findViewById(R.id.load_sequence_button);
        createSequencebutton = (Button) rootView.findViewById(R.id.make_sequence_button);

        defaultTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTest(0);
            }
        });

        loadSequenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runTest(1);
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
                    getFragmentManager().popBackStack("home", FragmentManager.POP_BACK_STACK_INCLUSIVE);
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public void runTest(int i) {
        try{
        //set the testing table by test_num column in database.
        //0 is the default test, 1 is the user saved test.
        TestingCom tc = (TestingCom)getActivity();
        tc.setTestTable(i);

        //create 4 contestants for testing
        StartVoteFragment.VotingContestant vc = (StartVoteFragment.VotingContestant)getActivity();
        ArrayList<String> testContestants = new ArrayList<>();
        testContestants.add("C1");
        testContestants.add("0");
        testContestants.add("C2");
        testContestants.add("1");
        testContestants.add("C3");
        testContestants.add("2");
        testContestants.add("C4");
        testContestants.add("3");

        vc.setContestantsList(testContestants);
        vc.setTesting(true);

        } catch (ClassCastException c) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement VotingContestant");
        }
        Fragment results = new ResultsFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_frag_container, results)
                .addToBackStack("test")
                .commit();
    }
}





