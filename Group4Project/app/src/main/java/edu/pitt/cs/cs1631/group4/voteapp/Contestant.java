package edu.pitt.cs.cs1631.group4.voteapp;

import android.support.annotation.NonNull;

/**
 * Created by Daniel Rowe on 2/25/2018.
 */

public class Contestant{
    private String name;
    private int id;

    @Override
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof Contestant)) {
            return false;
        }

        Contestant other = (Contestant) o;
        return this.getId() == other.getId();
    }

    @Override
    public int hashCode() {
        return this.getId();
    }

    public Contestant(String name, int id){
        this.setName(name);
        this.setId(id);
    }

    public Contestant(int id){
        this("", id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
