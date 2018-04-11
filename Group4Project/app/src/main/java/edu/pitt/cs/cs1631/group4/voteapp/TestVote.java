package edu.pitt.cs.cs1631.group4.voteapp;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class TestVote {

    public TestVote(String id, int testNum, int selection, int expected, int phoneNum) {
        this.id = id;
        this.testNum = testNum;
        this.selection = selection;
        this.expected = expected;
        this.phoneNum = phoneNum;
    }

    @PrimaryKey @NonNull
    private String id;

    @ColumnInfo(name = "test_num")
    private int testNum;

    @ColumnInfo(name = "selection")
    private int selection;

    @ColumnInfo(name = "exp_result")
    private int expected;

    @ColumnInfo(name = "phoneNum")
    private
    int phoneNum;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getTestNum() {
        return testNum;
    }

    public void setPhoneNum(int phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getPhoneNum() {
        return phoneNum;
    }
}
