package com.android.performancetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
//import android.os.SystemProperties;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;

public class NWStatus {

	Context context;

	private NetworkInfo ActivenetworkInfo, WIFI;
	ConnectivityManager conMan;

	private WifiManager wifiManager = null;

	private NetworkInfo wifi = null;
	private WifiInfo wifiInfo = null;
	private DhcpInfo dhcpInfo = null;
	private int STATIC_STATUES;
	private String[] wifiDetais = new String[8];
	private int ipAddress = -1;
	private String externalIP = null;
	private DLspeedTestActivity caller;
	private String dns1 = null, dns2 = null,dns3=null,dns4=null,dns5=null,dns6=null, rmnetdns1 = null,
			rmnetdns2 = null;
	private ArrayList<String> strDNS;

	public NWStatus(Context org) {
		context = org;
		conMan = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	/*
	 * @Override public void onReceive(Context context, Intent intent) { //
	 * String action = intent.getAction(); NetworkInfo activeNetInfo = //
	 * conMan.getActiveNetworkInfo(); Log.e("NW Status Chaged", "NETWQO"); }
	 */

	public String getActiveNetwork() {
		ActivenetworkInfo = conMan.getActiveNetworkInfo();
		// if(ActienetworInfo!)
		if (ActivenetworkInfo == null) {
			return "NONE";
		}
		return ActivenetworkInfo.getTypeName();
	}

	public void getWiFiInfo() {
		// return String=new String("Test","test");
		String connectedNW = ActivenetworkInfo.getTypeName();
		if (ActivenetworkInfo != null
				&& connectedNW.toUpperCase().equals("WIFI")) {

			wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			wifiInfo = wifiManager.getConnectionInfo();
			dhcpInfo = wifiManager.getDhcpInfo();
			// boolean temp = GlobalStorage.bDhcp;
			try {
				STATIC_STATUES = Settings.System.getInt(
						context.getContentResolver(),
						Settings.System.WIFI_USE_STATIC_IP);// static or dhcp
			} catch (SettingNotFoundException e) {

				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (STATIC_STATUES == 1) {// static일 경우의 wifi정보 가져오기
				wifiDetais[1] = "STATIC";

				wifiDetais[2] = Settings.System.getString(
						context.getContentResolver(),
						Settings.System.WIFI_STATIC_IP);

				wifiDetais[3] = Settings.System.getString(
						context.getContentResolver(),
						Settings.System.WIFI_STATIC_NETMASK);

				wifiDetais[4] = Settings.System.getString(
						context.getContentResolver(),
						Settings.System.WIFI_STATIC_GATEWAY);

				wifiDetais[5] = Settings.System.getString(
						context.getContentResolver(),
						Settings.System.WIFI_STATIC_DNS1);

				wifiDetais[6] = Settings.System.getString(
						context.getContentResolver(),
						Settings.System.WIFI_STATIC_DNS2);
			} else {
				dhcpInfo = wifiManager.getDhcpInfo(); // dhcp일 경우의 wifi정보 가져오기
				wifiDetais[1] = "DHCP";
				// IPaddr

				ipAddress = wifiInfo.getIpAddress();
				wifiDetais[2] = String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

				// default gateway
				ipAddress = dhcpInfo.gateway;
				wifiDetais[3] = String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

				// net mask
				ipAddress = dhcpInfo.netmask;
				wifiDetais[4] = String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff)); // pri
																				// DNS

				ipAddress = dhcpInfo.dns1;
				wifiDetais[5] = String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));

				// sec DNS
				ipAddress = dhcpInfo.dns2;
				wifiDetais[6] = String.format("%d.%d.%d.%d",
						(ipAddress & 0xff), (ipAddress >> 8 & 0xff),
						(ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
				// MAC addr
				wifiDetais[7] = wifiInfo.getMacAddress();
			}// else

		}
	}

	public String getWiFiIPAddress() {
		return wifiDetais[2];
	}

	public String getWiFiDNS1() {
		return wifiDetais[5];
	}

	public String getWiFiDNS2() {
		return wifiDetais[6];
	}

	public String getMobileLocalIpAddress() {
		ActivenetworkInfo = conMan.getActiveNetworkInfo();

		// if(ActienetworInfo!)
		if (ActivenetworkInfo == null) {
			return "NO Info";
		}

		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();

					if (!inetAddress.isLoopbackAddress()) {
						if (inetAddress instanceof Inet4Address) {
							String ipaddr = inetAddress.getHostAddress()
									.toString();
							// Log.e("inet Address", "IPV4");
							return ipaddr;
						} else if (inetAddress instanceof Inet6Address) {
							String ipaddr = inetAddress.getHostAddress()
									.toString();
							// Log.e("inet Address", "IPV6");
							return ipaddr;

						}

						// return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("TEst", ex.toString());
		}

		return null;

	}

	public void getExternalIP(DLspeedTestActivity cl) {
		ActivenetworkInfo = conMan.getActiveNetworkInfo();
		// if(ActienetworInfo!)
		if (ActivenetworkInfo != null) {
			new getExternalIPinBackgound()
					.execute("http://www.whatismyip.org/");
			caller = cl;
		}
		// new getExternalIPinBackgound().execute("http://www.whatismyip.org/");
		// caller = cl;
		// return externalIP;
	}

	public void setExternalIPinTool(String str) {
		caller.setExternalIPvalue(str);
	}

	private class getExternalIPinBackgound extends
			AsyncTask<String, Void, HttpResponse> {
		private String str;

		@Override
		protected HttpResponse doInBackground(String... urls) {
			HttpClient client = new DefaultHttpClient();
			HttpResponse response = null;
			HttpConnectionParams
					.setConnectionTimeout(client.getParams(), 10000);
			try {
				HttpGet request = new HttpGet(urls[0]);
				// request.setURI(new URI("http://www.whatismyip.org/"));
				response = client.execute(request);

			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("other Exception", "other" + e);
			}
			try {
				HttpEntity entity = response.getEntity();
				str = EntityUtils.toString(entity);
				// setExternalIPinTool(str);
				// String test=EntityUtils.toString(entity);
				// Log.e("eIP is ",externalIP);
				// response.getEntity().getContent();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				Log.e("other Exception", "other" + e);
				str = "Fail to get IP info";
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse response) {
			// super.onPostExecute(response);
			setExternalIPinTool(str);
		}
	}

	public void getProperties() {
		String output = null;
		String[] dnsStr={"dns"};
		String[] dns1Str = { "net.dns1" };
		String[] dns2Str = { "net.dns2" };
		String[] rmnetdns1Str = { "rmnet0.dns1" };
		String[] rmnetdns2Str = { "rmnet0.dns2" };
		// String dns1=null,dns2=null;
		try {

			/*
			 * ArrayList<String> processList = new ArrayList<String>();
			 */
			// java.lang.Process p = Runtime.getRuntime().exec("ps -e");

			String line;
			java.lang.Process p = Runtime.getRuntime().exec("getprop");

			BufferedReader input = new BufferedReader(new InputStreamReader(
					p.getInputStream()));
			
			while ((line = input.readLine()) != null) {
				
		/*		for (String ord : dnsStr){
					if (line.contains(ord)) {
						strDNS.add(object);
				  line.s
					}
				}*/
				
				for (String ord : dns1Str) {
					if (line.contains(ord)) {
						dns1 = line.substring(12).replace("[", "");
						dns1 = dns1.replace("]", "");
						Log.e("dns1  ", dns1);
					}
				}

				for (String ord : dns2Str) {
					if (line.contains(ord)) {

						dns2 = line.substring(12).replace("[", "");
						dns2 = dns2.replace("]", "");
						// dns2 = line.substring(12);
						Log.e("dns2 ", dns2);
					}
				}

				for (String ord : rmnetdns1Str) {
					if (line.contains(ord)) {
						rmnetdns1 = line.substring(19).replace("[", "");
						rmnetdns1 = rmnetdns1.replace("]", "");
						Log.e("rmnetdns1  ", rmnetdns1);
					}
				}
				for (String ord : rmnetdns2Str) {
					if (line.contains(ord)) {
						rmnetdns2 = line.substring(19).replace("[", "");
						rmnetdns2 = rmnetdns2.replace("]", "");
						Log.e("rmnetdns1  ", rmnetdns2);
					}
				}
				Log.e("line line ", line);
			}

			input.close();
		} catch (Exception err) {
			Log.e("line line error ", "");
			err.printStackTrace();
		}
		// return output;
	}

	/*
	 * // reassessPidDns(int myPid, boolean doBump); public void bumpDns() { /*
	 * Bump the property that tells the name resolver library to reread the DNS
	 * server list from the properties.
	 */
	/*
	 * String propVal = SystemProperties.get("net.dnschange"); int n = 0; if
	 * (propVal.length() != 0) { try { n = Integer.parseInt(propVal); } catch
	 * (NumberFormatException e) { } } SystemProperties.set("net.dnschange", ""
	 * + (n + 1)); }
	 * 
	 * public static String getDns1() { return
	 * SystemProperties.getProperty(SystemProperties.NET_DNS1); }
	 */

	public String getMobileDNS1() {
		return dns1;
	}

	public String getMobileDNS2() {
		return dns2;
	}

	public String getrmnetDNS1() {
		return rmnetdns1;
	}

	public String getrmnetDNS2() {
		return rmnetdns2;
	}
}// Class

