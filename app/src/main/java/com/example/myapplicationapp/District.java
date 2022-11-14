package com.example.myapplicationapp;

public class District {
    private int sid;
    private String sname;
    private String dname;

    public District() {
    }

    public District(int sid, String dname) {
        this.sid = sid;
        this.sname = sname;
        this.dname = dname;
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

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }
}
