package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.io.FileOutputStream;

import java.io.FileOutputStream;

public class MakeTestFragment extends Fragment {

    //private OnFragmentInteractionListener mListener;

    Button addStart;
    Button addVote;
    Button addStop;
    ListView listView;
    Button saveButton;
    Button cancelButton;

    public MakeTestFragment() {
        // Required empty public constructor
    }

    public static MakeTestFragment newInstance() {
        MakeTestFragment fragment = new MakeTestFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_make_test, container, false);

        addStart = (Button) rootView.findViewById(R.id.add_start_button);
        addVote = (Button) rootView.findViewById(R.id.add_vote_button);
        addStop = (Button) rootView.findViewById(R.id.add_stop_button);
        listView = (ListView) rootView.findViewById(R.id.make_list);
        saveButton = (Button) rootView.findViewById(R.id.make_save_button);
        cancelButton = (Button) rootView.findViewById(R.id.make_cancel_button);

        addStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add start event to sequence and listview
            }
        });

        addVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add random vote to seq and listview
            }
        });

        addStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add stop event to seq and listview
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: save sequence to phone - will run at load

/*                String filename = "savedSequence";

                //TODO: this will be what is saved
                String fileContents = "Sample saved file";

                FileOutputStream outputStream;

                try {
                    outputStream = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                //this will do to save one sequence
                String fileContents = "Sample saved file";




                //IDK if this will work
/*                View item;

                String s = "";
                EditText e;
                for(int i = 0; i < listView.getCount(); i++) {
                    item = listView.getChildAt(i);
                    s += item.toString();
                    s += ";";
                }*/




                SharedPreferences sharedPref = getContext().getSharedPreferences("savedSequence", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("savedSequence", fileContents);
                editor.commit();

                getActivity().onBackPressed();
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
