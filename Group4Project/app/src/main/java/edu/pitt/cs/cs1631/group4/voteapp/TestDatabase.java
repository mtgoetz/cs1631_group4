package edu.pitt.cs.cs1631.group4.voteapp;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {TestVote.class}, version = 1)
public abstract class TestDatabase extends RoomDatabase {
    public abstract TestingFragment.TestingDao testingDao();
}
