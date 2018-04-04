package edu.pitt.cs.cs1631.group4.voteapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

import static android.content.Context.MODE_PRIVATE;


public class TestingFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    Button defaultTestButton;
    Button loadSequenceButton;
    Button createSequencebutton;
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
