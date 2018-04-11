package edu.pitt.cs.cs1631.group4.voteapp;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

//Interface for database operations
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

    @Query("delete from testvote where test_num=:i")
    void deleteSaved(int i);

    @Query("select * from testvote order by test_num desc limit 1")
    int getLastTestNum();
}
