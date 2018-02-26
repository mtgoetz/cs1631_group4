package edu.pitt.cs.cs1631.group4.voteapp;

import android.app.Application;

import java.util.HashSet;
import java.util.Map;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class VotingService extends Application {
    private TallyTable ContestantTable;
    private HashSet<Long> voterSet;
    private VoteReceiver listener;
    private boolean listening = false;

    public boolean addContestant(String name, int id){
        if(ContestantTable == null){
            ContestantTable = new TallyTable();
        }
        return ContestantTable.addContestant(name, id);
    }

    public Map<Contestant, Integer> getResults(){
        return this.ContestantTable.getResult(10);
    }

    private int castVote(Long userPhoneNum, int ContestantId){
        if(!voterSet.contains(userPhoneNum)){
            if(ContestantTable.castVote(ContestantId)){
                return 0; //Vote Successful
            } else{
                return 1; //Vote Unsuccessful - Invalid ContestantId
            }
        }
        return 2; //Vote Unuccessful - Double Vote
    }

    public void toggleListening() {
        this.listening = !this.listening;
    }

    public void startPoll(){
        toggleListening();
        while(listening){
            if(listener == null){
                listener = new VoteReceiver();
            }
            //TODO: Get messages from listener, and call castVote()
        }
    }

    public Map<Contestant, Integer> endPoll(){
        toggleListening();
        return getResults();
    }
}
