package com.sauron.model;

/**
 * Created by Orangels on 2017/7/5.
 */
public class blacklistInfo {
    private String blackname;
    private String gender;
    private String idnum;
    private String blackdate;
    private int isblack;

    public String getBlackname() {
        return blackname;
    }

    public void setBlackname(String blackname) {
        this.blackname = blackname;
    }

    public String getBlackdate() {
        return blackdate;
    }

    public void setBlackdate(String blackdate) {
        this.blackdate = blackdate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public int getIsblack() {
        return isblack;
    }

    public void setIsblack(int isblack) {
        this.isblack = isblack;
    }
}
