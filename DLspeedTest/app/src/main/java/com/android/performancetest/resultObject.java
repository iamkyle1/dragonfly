package com.android.performancetest;

import android.util.Log;
import com.android.performancetest.R;

public class resultObject {

	public String NetworkType = null;
	public String InternalIP = null;
	public String ExternalIP = null;
	public String WiFiDNS1;
	public String WiFiDNS2;
	public String Latitude = null;
	public String Longitude = null;
	public String URL = null;
	public String dateString = null;
	public float TotalSize = 0;
	public float DLSize = 0;
	public float kbps = 0;
	public float duration = 0;
	public int Progress;
	
	public String setWiFiIpAddr;


	public void setNetworktype(String str) {
		NetworkType = str;
		//Log.e("network type seetin in Ro",NetworkType);
	}

	public void setInternalIP(String str) {
		InternalIP = str;
	}

	public void setExternalIP(String str) {
		ExternalIP = str;
	}
	
/*	public void setWiFiIpAddr(String str) {

		setWiFiIpAddr = str;
	}*/
	
	public void setWiFiDNS1(String str) {
		WiFiDNS1 = str;
	}

	public void setWiFiDNS2(String str) {
		WiFiDNS2 = str;
	}

	public void setLatitude(String str) {
		Latitude = str;
	}
	
	public void setLongitude(String str) {
		Longitude = str;
	}


	public void setURL(String str) {
		URL= str;
	}


	public void setDateString(String str) {
		dateString = str;
	}
	
	public void setTotalSize(Float flt) {
		TotalSize = flt;
	}

	public void setDLSize(Float flt) {
		DLSize = flt;
	}

	public void setkbps(Float flt) {
		kbps = flt;
	}

	public void setduration(Float flt) {
		duration = flt;
	}

	public void setProgress(int progress){
		Progress = progress;
	}
	
	public String getNetworktype() {
		return NetworkType;
	}
	
	public String getInternalIP() {
		return InternalIP;
	}

/*	public String getWIFIIpAddr() {
		return setWiFiIpAddr;
	}*/
	
	public String getExternalIP() {
		return ExternalIP;
	}

	public String getWiFiDNS1() {
		return WiFiDNS1;
	}

	public String getWiFiDNS2() {
		return WiFiDNS2;
	}
	
	public String getLatitude() {
		return Latitude;
	}
	
	public String getLongitude(){
		return Longitude;
	}
	
	public String getURL() {
		return URL;
	}
	public String getDateString() {
		return dateString;
	}

	public float getTotalSize() {
		return TotalSize;
	}

	public float getDLSize() {
		return DLSize;
	}

	public float getkbps() {
		return kbps;
	}

	public float getduration() {

		return duration;

	}
	
	public int getProgress(){
		return Progress;
	}

}
