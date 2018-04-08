package edu.pitt.cs.cs1631.group4.voteapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TestVote {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "test_num")
    private int testNum;

    @ColumnInfo(name = "selection")
    private int selection;

    @ColumnInfo(name = "exp_result")
    private int expected;

    public int getSelection() {
        return selection;
    }

    public void setSelection(int selection) {
        this.selection = selection;
    }

    public int getExpected() {
        return expected;
    }

    public void setExpected(int expected) {
        this.expected = expected;
    }

    public void setTestNum(int num) {
        this.testNum = num;
    }
}
