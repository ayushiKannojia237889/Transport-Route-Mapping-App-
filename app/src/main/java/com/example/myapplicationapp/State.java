package com.example.myapplicationapp;

public class State {
    private int id;
    private int sid;
    private String sname;

    public State() {
    }

    public State(int sid, String sname) {
        this.sid = sid;
        this.sname = sname;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }
}
