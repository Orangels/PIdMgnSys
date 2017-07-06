package com.sauron.service;

public class MyUtils {
	
	public static String bytesToHexString(byte[] src){
		StringBuilder sb = new StringBuilder();
		if(src == null || src.length <=0){
			return null;
		}
		for(int i=0;i<src.length;i++){
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if(hv.length()<2){
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();
	}

}
