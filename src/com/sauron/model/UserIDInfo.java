package com.sauron.model;

public class UserIDInfo {
	
	private int id;
	private String idbaseinfo;
	private String idpicinfo;
	private double similar;
	private String visitdate; //yyyyMMdd hh:mm:ss
	private int blacklist;
	
	public String getIdbaseinfo() {
		return idbaseinfo;
	}
	public void setIdbaseinfo(String idbaseinfo) {
		this.idbaseinfo = idbaseinfo;
	}
	public String getIdpicinfo() {
		return idpicinfo;
	}
	public void setIdpicinfo(String idpicinfo) {
		this.idpicinfo = idpicinfo;
	}
	public double getSimilar() {
		return similar;
	}
	public void setSimilar(double similar) {
		this.similar = similar;
	}
	public String getVisitdate() {
		return visitdate;
	}
	public void setVisitdate(String visitdate) {
		this.visitdate = visitdate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}

	public int getBlacklist() {
		return blacklist;
	}

	public void setBlacklist(int blacklist) {
		this.blacklist = blacklist;
	}
}
