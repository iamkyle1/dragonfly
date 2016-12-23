package com.android.performancetest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.os.SystemProperties;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

//import com.android.server.ConnectivityService;

public class DLspeedTestActivity extends Activity implements Observer {

	private EditText editURLInput = null;
	private TextView textResult = null;
	private Button BtnDownload = null;
	private Button cancelButton;
	private String URLString;
	private Download selectedDownload;
	private ProgressBar PrgBar;
	private TextView PCT;
	private Spinner SPNR;
	private String tempStr;
	private NWStatus nsts;
	private Button btnSpeedTest, btnResult, btnTool, btnAbout;
	private RelativeLayout RL;
	private LinearLayout LL, ATGuide, SPTM, RView, AbtView, TView,
			layoutOutsideTableAtTool;
	private TextView STGuide, SV, RTGuide;
	private ScrollView SCRView;
	private TableLayout TBHeader, tl, tableAtTool, resultTable;
	private ArrayList<Object> roa;
	private resultObject ro;
	private resultObject testro;

	public int status;
	public static final int DORMANT = 0;
	public static final int DNBTNPRESSED = 1;
	public static final int CANCELBTNPRESSED = 2;
	public int previousbutton = DORMANT;
	public Button BtneMail, BtnDelete;
	TextView ExternalIPAddressValue;
	int curYear, curMonth, curDay, curHour, curMinute, curNoon, curSecond;

	//Initial Screen
	private TextView b,URLText,DLSizeValue,DownloadedValue,kbpsValue,durationValue;
	

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// StrictMode.enableDefaults();
		setContentView(R.layout.main);

		// result box
		status = DORMANT;
		roa = new ArrayList();
		STGuide = (TextView) findViewById(R.id.TVGuide);
		editURLInput = (EditText) findViewById(R.id.EDTXTURL);
		SPNR = (Spinner) findViewById(R.id.spnURL);
		BtnDownload = (Button) findViewById(R.id.BtnDownload);
		cancelButton = (Button) findViewById(R.id.BtnCancel);
		PrgBar = (ProgressBar) findViewById(R.id.ProgressBar);
		PCT = (TextView) findViewById(R.id.Prtge);
		tl = (TableLayout) findViewById(R.id.header);
		tableAtTool = (TableLayout) findViewById(R.id.toolTable);
		layoutOutsideTableAtTool = (LinearLayout) findViewById(R.id.toolview);
		resultTable = (TableLayout) findViewById(R.id.resultTable);

		// Get all the menu button ID
		btnAbout = (Button) findViewById(R.id.About);
		btnSpeedTest = (Button) findViewById(R.id.SpeedTest);
		btnResult = (Button) findViewById(R.id.Result);
		btnTool = (Button) findViewById(R.id.Tool);
		btnAbout = (Button) findViewById(R.id.About);
		// speedtest View*

		SPTM = (LinearLayout) findViewById(R.id.speedtestmain);
		// result View
		RView = (LinearLayout) findViewById(R.id.resultview);

		// toolview
		TView = (LinearLayout) findViewById(R.id.toolview);

		// result View

		AbtView = (LinearLayout) findViewById(R.id.aboutview);
		// initialize the firstview
		SPTM.setVisibility(View.VISIBLE);
		// Initialize Menu button status
		BtnDownload.setEnabled(true);
		cancelButton.setEnabled(false);
		// Disable Result, About, Tools View
		RView.setVisibility(View.INVISIBLE);
		TView.setVisibility(View.INVISIBLE);
		AbtView.setVisibility(View.INVISIBLE);

		btnSpeedTest.setEnabled(true);
		btnResult.setEnabled(true);
		btnTool.setEnabled(true);
		btnAbout.setEnabled(true);

		Calendar cal;

		setInitialResultFromFile("mylog.log");
		// Log.e("oncreate", "on create");

		setInitialResultTableinTest();
		Context context = (Context) getApplicationContext();
		nsts = new NWStatus(context);

		btnSpeedTest.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				SPTM.setVisibility(View.VISIBLE);
				RView.setVisibility(View.INVISIBLE);
				TView.setVisibility(View.INVISIBLE);
				AbtView.setVisibility(View.INVISIBLE);

			}

		});

		btnResult.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				SPTM.setVisibility(View.INVISIBLE);
				RView.setVisibility(View.VISIBLE);
				TView.setVisibility(View.INVISIBLE);
				AbtView.setVisibility(View.INVISIBLE);

			}

		});

		btnTool.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				SPTM.setVisibility(View.INVISIBLE);
				RView.setVisibility(View.INVISIBLE);
				TView.setVisibility(View.VISIBLE);
				AbtView.setVisibility(View.INVISIBLE);
				displayResultInToolMenu();
				// tableAtTool
			}
		});

		btnAbout.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				SPTM.setVisibility(View.INVISIBLE);
				RView.setVisibility(View.INVISIBLE);
				TView.setVisibility(View.INVISIBLE);
				AbtView.setVisibility(View.VISIBLE);
			}

		});

		BtnDownload.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				final Calendar cal = Calendar.getInstance(Locale.KOREA);
				curYear = cal.get(Calendar.YEAR);
				curMonth = cal.get(Calendar.MONTH) + 1;
				curDay = cal.get(Calendar.DAY_OF_MONTH);
				curHour = cal.get(Calendar.HOUR_OF_DAY);
				curNoon = cal.get(Calendar.AM_PM);
				curMinute = cal.get(Calendar.MINUTE);
				curSecond = cal.get(Calendar.SECOND);

				status = DORMANT;
				BtnDownload.setEnabled(false);
				cancelButton.setEnabled(true);
				editURLInput.setEnabled(false);
				SPNR.setEnabled(false);
				resultTable.removeAllViews();
				setInitialResultTableinTest();
				actionExecute();

			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (status != CANCELBTNPRESSED) {
					status = CANCELBTNPRESSED;
					BtnDownload.setEnabled(true);
					cancelButton.setEnabled(false);
					editURLInput.setEnabled(true);
					SPNR.setEnabled(true);
					actionCancel();
				}
			}
		});

		BtneMail = (Button) findViewById(R.id.BtneMail);

		BtnDelete = (Button) findViewById(R.id.BtnDelete);

		BtneMail.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent sendIntent = new Intent(Intent.ACTION_SENDTO, Uri
						.parse("mailto:" + "kyle_lee@sk.com"));
				sendIntent.putExtra(Intent.EXTRA_SUBJECT,
						"Network Performance Test resut");
				sendIntent.setType("plain/text");
				sendIntent.putExtra(Intent.EXTRA_EMAIL, "kyle_lee@sk.com");
				sendIntent.setData(Uri.parse("mailto:kyle_lee@sk.com"));

				String mailText = getStringFromROA();
				//Log.e("String from ROA in the mail button", ""
				//		+ getStringFromROA() + "Mail Button");
				sendIntent.putExtra(Intent.EXTRA_TEXT, mailText);

				// sendIntent.putExtra(Intent.EXTRA_STREAM,
				// Uri.parse("file:///sdcard/file.ext"));
				// String filename =getFilesDir()+"mylog.log";
				// sendIntent.putExtra(Intent.EXTRA_STREAM,
				// Uri.parse(filename));

				startActivity(Intent.createChooser(sendIntent, "Send email"));
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
		});

		BtnDelete.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int count = tl.getChildCount();
				int i;
				if (count > 1) {
					tl.removeViewAt(count - 1);
					roa.remove(roa.size() - 1);
					try {
						removeNthLine(getFilesDir() + "/mylog.log", count - 2);
					} catch (IOException e) {
						Log.e("IOE", "" + e);
					}
				}
			}
		});

		SPNR.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(
						parent.getContext(),
						"Selected URL ==> "
								+ parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();
				tempStr = parent.getItemAtPosition(position).toString();
				if (tempStr.startsWith("[CDN]") || tempStr.startsWith("[NOR]")) {
					editURLInput.setText(parent.getItemAtPosition(position)
							.toString().substring(5));
				} else {
					editURLInput.setText(parent.getItemAtPosition(position)
							.toString());

				}
				;

			};

			public void onNothingSelected(AdapterView<?> parent) {

				// TODO Auto-generated method stub

			}

		});

	}// oncreate

	// "mylog.log"
	public static void removeNthLine(String f, int toRemove) throws IOException {

		RandomAccessFile raf = new RandomAccessFile(f, "rw");
		String str;
		// Leaf n first lines unchanged.
		for (int i = 0; i < toRemove; i++)
			str = raf.readLine();
		// Shift remaining lines upwards.
		long writePos = raf.getFilePointer();
		raf.readLine();
		long readPos = raf.getFilePointer();

		byte[] buf = new byte[1024];
		int n;
		while (-1 != (n = raf.read(buf))) {
			raf.seek(writePos);
			raf.write(buf, 0, n);
			readPos += n;
			writePos += n;
			raf.seek(readPos);
		}

		raf.setLength(writePos);
		raf.close();
	}

	private void actionExecute() {

		URLString = editURLInput.getText().toString();
		//URLString ="http://www.yahoo.co.kr";
		URL verifiedUrl = verifyUrl(URLString.trim());
		if (verifiedUrl != null) {
			try {
				selectedDownload = new Download(verifiedUrl);
			} catch (Exception e) {
				e.printStackTrace();
			}
			selectedDownload.addObserver(this);
			// Log.e("Download Status ", "" + selectedDownload.getStatus());
			actionResume();
		} else {
			status = DORMANT;
			BtnDownload.setEnabled(true);
			cancelButton.setEnabled(false);
			editURLInput.setEnabled(true);
			SPNR.setEnabled(true);
			diplayWrongURLMessage();
		}

	}// actionAdd

	// Verify download URL.
	
	public void diplayWrongURLMessage(){
		final int DYNAMIC_VIEW_ID = 0x8000;
		if (resultTable.getChildCount() > 0) {
			resultTable.removeAllViews();
		}
		TableRow tableRow0 = new TableRow(this);
		tableRow0.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, 20));

		TextView x = new TextView(this);
		x.setTextSize((float) 15);
		x.setText("It is not a file download URL \n"+
		"Try other URL");
		
		x.setGravity(Gravity.LEFT);
		tableRow0.addView(x);

		resultTable.addView(tableRow0, new TableLayout.LayoutParams(40,
				LayoutParams.WRAP_CONTENT));

		View dynamicView0 = new View(this);
		dynamicView0.setId(DYNAMIC_VIEW_ID);
		dynamicView0.setBackgroundColor(Color.WHITE);

		resultTable.addView(dynamicView0, new LayoutParams(
				LayoutParams.FILL_PARENT, 2));
		
	}

	private void actionResume() {

		try {
			selectedDownload.resume();
			// updateButtons();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actionCancel() {

		try {
			selectedDownload.cancel();
			// updateButtons();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Resume the selected download.

	public void update(Observable o, Object arg) {
		if (selectedDownload != null && selectedDownload.equals(o))
			updateButtons();
	}

	private void updateButtons() {
		if (selectedDownload != null) {
			int status = selectedDownload.getStatus();
			switch (status) {
			case Download.DOWNLOADING:
				// status = DORMANT;
				Message msg = Message.obtain(mHandler, 1);
				mHandler.sendMessage(msg);
				// Log.e("in the handler", "DownloadingO");
				// status = DNBTNPRESSED;
				break;
			case Download.PAUSED:
				// status = DORMANT;
				Message msg2 = Message.obtain(mHandler, 2);
				mHandler.sendMessage(msg2);
				// Log.e("in the handler", "PausedO");
				break;
			case Download.ERROR:
				// status = DORMANT;
				Message msg3 = Message.obtain(mHandler, 3);
				mHandler.sendMessage(msg3);
				// Log.e("in the handler", "ErrorO");
				break;
			case Download.COMPLETE: // COMPLETE
				// status = DORMANT;
				Message msg4 = Message.obtain(mHandler, 4);
				mHandler.sendMessage(msg4);
				Log.e("in the handler", "Complete");
				break;
			case Download.CANCELLED: // CANCELLED
				// status = DORMANT;
				Message msg5 = Message.obtain(mHandler, 5);
				mHandler.sendMessage(msg5);
				// Log.e("in the handler", "CancelledO");
				break;
			}
		} else {
			status = DORMANT;
			// No download is selected in table.
			// Message msg6= Message.obtain(mHandler, 6);
			// mHandler.sendMessage(msg6);
			// Log.e("in the handler", "No Download");
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// Log.e("Progress" , ""+selectedDownload.getProgress());
			PrgBar.setProgress(selectedDownload.getProgress());
			PCT.setText("" + selectedDownload.getProgress() + "%");

			// Log.e("Progress:   ", "" + selectedDownload.getProgress());
			switch (msg.what) {
			case 1:// DOWNLOADING
				/*
				 * BtnDownload.setEnabled(false); cancelButton.setEnabled(true);
				 * editURLInput.setEnabled(false); SPNR.setEnabled(false);
				 */
				// Log.e("in the handler", "Downloading");
				break;
			case 2:// PAUSED
				BtnDownload.setEnabled(true);
				cancelButton.setEnabled(true);
				// Log.e("in the handler", "Paused");
				break;
			case 3:// ERROR
					// textResult.setText("");
				BtnDownload.setEnabled(true);
				cancelButton.setEnabled(false);
				editURLInput.setEnabled(true);
				SPNR.setEnabled(true);
				setResultInTest("ERROR");
				// Log.e("in the handler", "Error");
				break;
			case 4:// COMPLETE
				BtnDownload.setEnabled(true);
				cancelButton.setEnabled(false);
				editURLInput.setEnabled(true);
				SPNR.setEnabled(true);
				setResultArray();
				setResultInTest("COMPLETE");
				insertResult();
				break;
			case 5:// CANCELLED
				BtnDownload.setEnabled(true);
				cancelButton.setEnabled(false);
				editURLInput.setEnabled(true);
				SPNR.setEnabled(true);
				setResultArray();
				setResultInTest("CANCEL");
				insertResult();
				break;
			}
			super.handleMessage(msg);
		}

	};

	private void setResultArray() {
		ro = new resultObject();

		ro.setNetworktype(nsts.getActiveNetwork().toUpperCase());
		// internal IP
		ro.setInternalIP(nsts.getMobileLocalIpAddress());
		// External IP
	
		if (nsts.getActiveNetwork().equalsIgnoreCase("WIFI")) {
			nsts.getWiFiInfo();
			ro.setInternalIP(nsts.getWiFiIPAddress());
			ro.setExternalIP("Unknown");
			String str = nsts.getWiFiDNS1();
			ro.setWiFiDNS1(str);
		//	ro.setWiFiDNS1(nsts.getWiFiDNS1());
			ro.setWiFiDNS2(nsts.getWiFiDNS2());
		}else{
			ro.setInternalIP(nsts.getMobileLocalIpAddress());
			ro.setExternalIP("Unknown");
			ro.setWiFiDNS1("Unknown");
			ro.setWiFiDNS2("Unknown");
		}
		ro.setDateString(curYear + "/" + curMonth + "/" + curDay + " "
				+ curHour + ":" + curMinute + ":" + curSecond);
		ro.setURL(selectedDownload.getUrl());
		ro.setTotalSize((float) selectedDownload.getSize());
		ro.setDLSize(selectedDownload.getDownloadedSize());
		ro.setkbps(selectedDownload.getKbps());
		ro.setduration(selectedDownload.getDuration());
		ro.setProgress(selectedDownload.getProgress());
		roa.add(ro);

		String logString = ro.getNetworktype() + "," + ro.getInternalIP() + ","
				+ ro.getExternalIP() + "," + ro.getWiFiDNS1() + ","
				+ ro.getWiFiDNS2() + "," + ro.getURL() + ","
				+ ro.getDateString() + "," + ro.getTotalSize() + ","
				+ ro.getDLSize() + "," + ro.getkbps() + "," + ro.getduration()
				+ "," + ro.getProgress() + "\n";
		writeLogFile(logString);

	}

	private void insertResult() {

		TableRow tr = new TableRow(this);
		tr.setLayoutParams(new LayoutParams(40, 2));
		TextView b = new TextView(this);
		b.setTextSize((float) 10);
		b.setText(nsts.getActiveNetwork().toUpperCase());
		b.setGravity(Gravity.RIGHT);
		tr.addView(b, new TableRow.LayoutParams(40, 25, 1));
		tr.setLayoutParams(new LayoutParams(40, 2));

		TextView b1 = new TextView(this);

		b1.setTextSize((float) 10);

		if (selectedDownload.getUrl().contains(
				"http://skplanet-qiip.clouddns.co.cc")) {
			b1.setText("CDN");

		} else if (selectedDownload.getUrl().contains(
				"http://m.qiip.jp/share/misc")) {
			b1.setText("NORMAL");
		}

		b1.setGravity(Gravity.RIGHT);
		tr.addView(b1, new TableRow.LayoutParams(40, 25, 1));
		TextView c = new TextView(this);
		c.setText(curMonth + "/" + curDay + " " + curHour + ":" + curMinute);
		c.setTextSize((float) 10);
		c.setGravity(Gravity.RIGHT);
		tr.addView(c, new TableRow.LayoutParams(40, 25, 1));

		TextView c1 = new TextView(this);
		c1.setGravity(Gravity.RIGHT);
		c1.setTextSize((float) 10);
		c1.setText(String.format("%.01f", selectedDownload.getKbps()));
		tr.addView(c1, new TableRow.LayoutParams(40, 25, 1));
		TextView d = new TextView(this);
		d.setGravity(Gravity.RIGHT);

		d.setTextSize((float) 10);

		d.setText(String.format("%.01f", selectedDownload.getDuration()));

		tr.addView(d, new TableRow.LayoutParams(40, 25, 1));

		// LayoutParams.FILL_PARENT
		TextView e = new TextView(this);
		e.setGravity(Gravity.RIGHT);
		e.setTextSize((float) 10);
		e.setText(String.format("%d", selectedDownload.getProgress()));
		tr.addView(e, new TableRow.LayoutParams(40, 25, 1));
		tl.addView(tr, new TableLayout.LayoutParams(40,
				LayoutParams.WRAP_CONTENT));
	}

	private void setInitialResultTableinTest(){
		String activeNetwork;
		final int DYNAMIC_VIEW_ID = 0x8000;
		
		if (resultTable.getChildCount() > 0) {
			resultTable.removeAllViews();
		}
		
		//activeNetwork = nsts.getActiveNetwork().toUpperCase();

		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				20));

		/*		ExternalIPAddressValue.setText(str);*/
		
		TextView a = new TextView(this);
		a.setTextSize((float) 15);
		a.setText("Active Network:");
		a.setGravity(Gravity.LEFT);
		tableRow.addView(a);

		b = new TextView(this);
		b.setTextSize((float) 15);
		b.setText("");
		b.setGravity(Gravity.RIGHT);
		tableRow.addView(b);

		resultTable.addView(tableRow, new TableLayout.LayoutParams(40,
				LayoutParams.WRAP_CONTENT));

		View dynamicView = new View(this);
		dynamicView.setId(DYNAMIC_VIEW_ID);
		dynamicView.setBackgroundColor(Color.WHITE);

		resultTable.addView(dynamicView, new LayoutParams(
				LayoutParams.FILL_PARENT, 2));

			TableRow tableRow2 = new TableRow(this);
			TextView URLT = new TextView(this);
			URLT.setTextSize((float) 15);
			URLT.setText("URL:");
			URLT.setGravity(Gravity.LEFT);
			tableRow2.addView(URLT);

			URLText = new TextView(this);
			URLText.setTextSize((float) 15);
			URLText.setText("");
			URLText.setGravity(Gravity.RIGHT);
			tableRow2.addView(URLText);

			resultTable.addView(tableRow2, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));
			
			View dynamicView2 = new View(this);
			dynamicView2.setId(DYNAMIC_VIEW_ID);
			dynamicView2.setBackgroundColor(Color.WHITE);
			resultTable.addView(dynamicView2, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

			TableRow tableRow3 = new TableRow(this);

			TextView DLSize = new TextView(this);
			DLSize.setTextSize((float) 15);
			DLSize.setText("Total Size:");
			DLSize.setGravity(Gravity.LEFT);
			tableRow3.addView(DLSize);

			//int size1 = selectedDownload.getSize();
			DLSizeValue = new TextView(this);
			DLSizeValue.setTextSize((float) 15);
			DLSizeValue.setText("");
			DLSizeValue.setGravity(Gravity.RIGHT);
			tableRow3.addView(DLSizeValue);

			resultTable.addView(tableRow3, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow4 = new TableRow(this);

			TextView Downloaded = new TextView(this);
			Downloaded.setTextSize((float) 15);
			Downloaded.setText("Downloaded:");
			Downloaded.setGravity(Gravity.LEFT);
			tableRow4.addView(Downloaded); // nsts.getMobileLocalIpAddress()

		DownloadedValue = new TextView(this);
			DownloadedValue.setTextSize((float) 15);
			DownloadedValue.setText("");
			DownloadedValue.setGravity(Gravity.RIGHT);
			tableRow4.addView(DownloadedValue);

			resultTable.addView(tableRow4, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow5 = new TableRow(this);

			TextView kbps = new TextView(this);
			kbps.setTextSize((float) 15);
			kbps.setText("kbps:");
			kbps.setGravity(Gravity.LEFT);
			tableRow5.addView(kbps); // nsts.getMobileLocalIpAddress()

			kbpsValue = new TextView(this);
			kbpsValue.setTextSize((float) 15);
			kbpsValue.setText("");
			kbpsValue.setGravity(Gravity.RIGHT);
			tableRow5.addView(kbpsValue);

			
			resultTable.addView(tableRow5, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow6 = new TableRow(this);
			TextView duration = new TextView(this);
			duration.setTextSize((float) 15);
			duration.setText("Duration:");
			duration.setGravity(Gravity.LEFT);
			tableRow6.addView(duration); // nsts.getMobileLocalIpAddress()

			durationValue = new TextView(this);
			durationValue.setTextSize((float) 15);
			durationValue.setText("");
			durationValue.setGravity(Gravity.RIGHT);
			tableRow6.addView(durationValue);
			

			resultTable.addView(tableRow6, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView3 = new View(this);
			dynamicView3.setId(DYNAMIC_VIEW_ID);
			dynamicView3.setBackgroundColor(Color.WHITE);
			resultTable.addView(dynamicView3, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));	
	}
	
	private void setResultInTest(String str) 
	{
	//	b,URLText,DLSizeValue,DownloadedValue,kbpsValue,durationValue;
		String activeNetwork;
		final int DYNAMIC_VIEW_ID = 0x8000;

		if (str.equals("ERROR")) {
			if (resultTable.getChildCount() > 0) {
				resultTable.removeAllViews();
			}
			TableRow tableRow0 = new TableRow(this);
			tableRow0.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, 20));

			TextView x = new TextView(this);
			x.setTextSize((float) 15);
			x.setText("Abnormally finished \n"
					+ "- Connection Error: Internet's unavailable\n"
					+ "- Connection type change during downloading");
			x.setGravity(Gravity.LEFT);
			tableRow0.addView(x);

			resultTable.addView(tableRow0, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView0 = new View(this);
			dynamicView0.setId(DYNAMIC_VIEW_ID);
			dynamicView0.setBackgroundColor(Color.WHITE);

			resultTable.addView(dynamicView0, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

		} else if (str.equals("COMPLETE") || str.equals("CANCEL")) {

			activeNetwork = nsts.getActiveNetwork().toUpperCase();

		
			b.setText(activeNetwork.toUpperCase());

			if (activeNetwork.equalsIgnoreCase("MOBILE")
					|| activeNetwork.equalsIgnoreCase("WIFI")) {
			
				URLText.setText(selectedDownload.getUrl().substring(0, 30));
				int size1 = selectedDownload.getSize();
				DLSizeValue.setText(String
						.format("%.01f", (float) size1 / 1024) + " K Bytes");
		
				DownloadedValue.setText(String.format("%.01f",
						(float) selectedDownload.getDownloadedSize() / 1024)
						+ " K Bytes");

				kbpsValue.setText(String.format("%.01f",
						(float) selectedDownload.getKbps()) + " kbps");
				durationValue.setText(String.format("%.01f",
						selectedDownload.getDuration())
						+ " sec");


			}
		}
		
	}
/*	private void setResultInTest(String str) {
		String activeNetwork;
		final int DYNAMIC_VIEW_ID = 0x8000;
		if (resultTable.getChildCount() > 0) {
			resultTable.removeAllViews();
		}

		if (str.equals("ERROR")) {
			TableRow tableRow0 = new TableRow(this);
			tableRow0.setLayoutParams(new LayoutParams(
					LayoutParams.FILL_PARENT, 20));

			TextView x = new TextView(this);
			x.setTextSize((float) 15);
			x.setText("Abnormally finished \n"
					+ "- Connection Error: Internet's unavailable\n"
					+ "- Connection type change during downloading");
			x.setGravity(Gravity.LEFT);
			tableRow0.addView(x);

			resultTable.addView(tableRow0, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView0 = new View(this);
			dynamicView0.setId(DYNAMIC_VIEW_ID);
			dynamicView0.setBackgroundColor(Color.WHITE);

			resultTable.addView(dynamicView0, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

		} else if (str.equals("COMPLETE") || str.equals("CANCEL")) {

			activeNetwork = nsts.getActiveNetwork().toUpperCase();

			TableRow tableRow = new TableRow(this);
			tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					20));

			TextView a = new TextView(this);
			a.setTextSize((float) 15);
			a.setText("Active Network:");
			a.setGravity(Gravity.LEFT);
			tableRow.addView(a);

			TextView b = new TextView(this);
			b.setTextSize((float) 15);
			b.setText(activeNetwork.toUpperCase());
			b.setGravity(Gravity.RIGHT);
			tableRow.addView(b);

			resultTable.addView(tableRow, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView = new View(this);
			dynamicView.setId(DYNAMIC_VIEW_ID);
			dynamicView.setBackgroundColor(Color.WHITE);

			resultTable.addView(dynamicView, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

			if (activeNetwork.equalsIgnoreCase("MOBILE")
					|| activeNetwork.equalsIgnoreCase("WIFI")) {
				TableRow tableRow2 = new TableRow(this);
				TextView URLT = new TextView(this);
				URLT.setTextSize((float) 15);
				URLT.setText("URL:");
				URLT.setGravity(Gravity.LEFT);
				tableRow2.addView(URLT);

				TextView URLText = new TextView(this);
				URLText.setTextSize((float) 15);
				URLText.setText(selectedDownload.getUrl().substring(0, 30));
				URLText.setGravity(Gravity.RIGHT);
				tableRow2.addView(URLText);

				resultTable.addView(tableRow2, new TableLayout.LayoutParams(40,
						LayoutParams.WRAP_CONTENT));
				View dynamicView2 = new View(this);
				dynamicView2.setId(DYNAMIC_VIEW_ID);
				dynamicView2.setBackgroundColor(Color.WHITE);
				resultTable.addView(dynamicView2, new LayoutParams(
						LayoutParams.FILL_PARENT, 2));

				TableRow tableRow3 = new TableRow(this);

				TextView DLSize = new TextView(this);
				DLSize.setTextSize((float) 15);
				DLSize.setText("Total Size:");
				DLSize.setGravity(Gravity.LEFT);
				tableRow3.addView(DLSize);

				int size1 = selectedDownload.getSize();
				TextView DLSizeValue = new TextView(this);
				DLSizeValue.setTextSize((float) 15);
				DLSizeValue.setText(String
						.format("%.01f", (float) size1 / 1024) + " K Bytes");
				DLSizeValue.setGravity(Gravity.RIGHT);
				tableRow3.addView(DLSizeValue);

				resultTable.addView(tableRow3, new TableLayout.LayoutParams(40,
						LayoutParams.WRAP_CONTENT));

				TableRow tableRow4 = new TableRow(this);

				TextView Downloaded = new TextView(this);
				Downloaded.setTextSize((float) 15);
				Downloaded.setText("Downloaded:");
				Downloaded.setGravity(Gravity.LEFT);
				tableRow4.addView(Downloaded); // nsts.getMobileLocalIpAddress()

				TextView DownloadedValue = new TextView(this);
				DownloadedValue.setTextSize((float) 15);
				DownloadedValue.setText(String.format("%.01f",
						(float) selectedDownload.getDownloadedSize() / 1024)
						+ " K Bytes");
				DownloadedValue.setGravity(Gravity.RIGHT);
				tableRow4.addView(DownloadedValue);

				resultTable.addView(tableRow4, new TableLayout.LayoutParams(40,
						LayoutParams.WRAP_CONTENT));

				TableRow tableRow5 = new TableRow(this);

				TextView kbps = new TextView(this);
				kbps.setTextSize((float) 15);
				kbps.setText("kbps:");
				kbps.setGravity(Gravity.LEFT);
				tableRow5.addView(kbps); // nsts.getMobileLocalIpAddress()

				TextView kbpsValue = new TextView(this);
				kbpsValue.setTextSize((float) 15);
				kbpsValue.setText(String.format("%.01f",
						(float) selectedDownload.getKbps()) + " kbps");
				kbpsValue.setGravity(Gravity.RIGHT);
				tableRow5.addView(kbpsValue);

				resultTable.addView(tableRow5, new TableLayout.LayoutParams(40,
						LayoutParams.WRAP_CONTENT));

				TableRow tableRow6 = new TableRow(this);
				TextView duration = new TextView(this);
				duration.setTextSize((float) 15);
				duration.setText("Duration:");
				duration.setGravity(Gravity.LEFT);
				tableRow6.addView(duration); // nsts.getMobileLocalIpAddress()

				TextView durationValue = new TextView(this);
				durationValue.setTextSize((float) 15);
				durationValue.setText(String.format("%.01f",
						selectedDownload.getDuration())
						+ " sec");
				durationValue.setGravity(Gravity.RIGHT);
				tableRow6.addView(durationValue);

				resultTable.addView(tableRow6, new TableLayout.LayoutParams(40,
						LayoutParams.WRAP_CONTENT));

				View dynamicView3 = new View(this);
				dynamicView3.setId(DYNAMIC_VIEW_ID);
				dynamicView3.setBackgroundColor(Color.WHITE);
				resultTable.addView(dynamicView3, new LayoutParams(
						LayoutParams.FILL_PARENT, 2));

			}
		}
	}
	
	*/

	private void displayResultInToolMenu() {
		String activeNetwork;
		final int DYNAMIC_VIEW_ID = 0x8000;

		if (tableAtTool.getChildCount() > 0) {
			tableAtTool.removeAllViews();
		}
		activeNetwork = nsts.getActiveNetwork().toUpperCase();

		TableRow tableRow = new TableRow(this);
		tableRow.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 20));

		TextView a = new TextView(this);
		a.setTextSize((float) 15);
		a.setText("Active Network...");
		a.setGravity(Gravity.LEFT);
		tableRow.addView(a);

		TextView b = new TextView(this);
		b.setTextSize((float) 15);
		b.setText(activeNetwork.toUpperCase());
		b.setGravity(Gravity.RIGHT);
		tableRow.addView(b);

		tableAtTool.addView(tableRow, new TableLayout.LayoutParams(40,
				LayoutParams.WRAP_CONTENT));

		View dynamicView = new View(this);
		dynamicView.setId(DYNAMIC_VIEW_ID);
		// dynamicView.setLayoutParams(new
		// LayoutParams(LayoutParams.FILL_PARENT,2));
		dynamicView.setBackgroundColor(Color.WHITE);
		// tableRow.addView(dynamicView);
		// layoutOutsideTableAtTool

		tableAtTool.addView(dynamicView, new LayoutParams(
				LayoutParams.FILL_PARENT, 2));
		tableAtTool.setColumnStretchable(3, true);

		/*
		 * layoutOutsideTableAtTool.addView(dynamicView, new LayoutParams(
		 * LayoutParams.FILL_PARENT, 2));
		 */
		// tableAtTool.addView(dynamicView, new TableLayout.LayoutParams(40,
		// LayoutParams.WRAP_CONTENT));

		if (activeNetwork.equalsIgnoreCase("MOBILE")) {
			TableRow tableRow2 = new TableRow(this);
			TextView LIA = new TextView(this);
			LIA.setTextSize((float) 15);
			LIA.setText("Local IP Address...");
			LIA.setGravity(Gravity.LEFT);
			tableRow2.addView(LIA); // nsts.getMobileLocalIpAddress()

			TextView LocalIP = new TextView(this);
			LocalIP.setTextSize((float) 15);
			LocalIP.setText(nsts.getMobileLocalIpAddress().toUpperCase());
			LocalIP.setGravity(Gravity.RIGHT);
			tableRow2.addView(LocalIP);

			tableAtTool.addView(tableRow2, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow3 = new TableRow(this);

			TextView ExternalIPAddressStr = new TextView(this);
			ExternalIPAddressStr.setTextSize((float) 15);
			ExternalIPAddressStr.setText("External IP Address...");
			ExternalIPAddressStr.setGravity(Gravity.LEFT);
			tableRow3.addView(ExternalIPAddressStr); // nsts.getMobileLocalIpAddress()

			nsts.getExternalIP(this);

			ExternalIPAddressValue = new TextView(this);
			ExternalIPAddressValue.setTextSize((float) 15);
			ExternalIPAddressValue.setText("trying");
			ExternalIPAddressValue.setGravity(Gravity.RIGHT);
			tableRow3.addView(ExternalIPAddressValue);

			tableAtTool.addView(tableRow3, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView2 = new View(this);
			dynamicView2.setId(DYNAMIC_VIEW_ID);
			// dynamicView.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,2));
			dynamicView2.setBackgroundColor(Color.WHITE);
			// tableRow.addView(dynamicView);
			// layoutOutsideTableAtTool
		
			tableAtTool.addView(dynamicView2, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

			nsts.getProperties();
			TableRow tableRow4 = new TableRow(this);
			TextView DNS1 = new TextView(this);
			DNS1.setTextSize((float) 15);
			DNS1.setText("net.dns1...");
			DNS1.setGravity(Gravity.LEFT);
			tableRow4.addView(DNS1); // nsts.getMobileLocalIpAddress()

			TextView DNS1Value = new TextView(this);
			DNS1Value.setTextSize((float) 15);
			DNS1Value.setText(nsts.getMobileDNS1());
			DNS1Value.setGravity(Gravity.RIGHT);
			tableRow4.addView(DNS1Value);


			tableAtTool.addView(tableRow4, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));
			TableRow tableRow5 = new TableRow(this);
			TextView DNS2 = new TextView(this);
			DNS2.setTextSize((float) 15);
			DNS2.setText("net.dns2...");
			DNS2.setGravity(Gravity.LEFT);
			tableRow5.addView(DNS2); // nsts.getMobileLocalIpAddress()

			TextView DNS2Value = new TextView(this);
			DNS2Value.setTextSize((float) 15);
			DNS2Value.setText(nsts.getMobileDNS2());
			DNS2Value.setGravity(Gravity.RIGHT);
			tableRow5.addView(DNS2Value);

			tableAtTool.addView(tableRow5, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));
			
			View dynamicView3 = new View(this);
			dynamicView3.setId(DYNAMIC_VIEW_ID);

			dynamicView3.setBackgroundColor(Color.WHITE);
			tableAtTool.addView(dynamicView3, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));
			
			nsts.getProperties();
			TableRow tableRow6 = new TableRow(this);
			TextView DNS3 = new TextView(this);
			DNS3.setTextSize((float) 15);
			DNS3.setText("net.rmnet0.dns1...");
			DNS3.setGravity(Gravity.LEFT);
			tableRow6.addView(DNS3); // nsts.getMobileLocalIpAddress()

			TextView DNS3Value = new TextView(this);
			DNS3Value.setTextSize((float) 15);
			DNS3Value.setText(nsts.getrmnetDNS1());
			DNS3Value.setGravity(Gravity.RIGHT);
			tableRow6.addView(DNS3Value);


			tableAtTool.addView(tableRow6, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));
			
			TableRow tableRow7 = new TableRow(this);
			TextView DNS4 = new TextView(this);
			DNS4.setTextSize((float) 15);
			DNS4.setText("net.rmnet0.dns2...");
			DNS4.setGravity(Gravity.LEFT);
			tableRow7.addView(DNS4); // nsts.getMobileLocalIpAddress()

			TextView DNS4Value = new TextView(this);
			DNS4Value.setTextSize((float) 15);
			DNS4Value.setText(nsts.getrmnetDNS2());
			DNS4Value.setGravity(Gravity.RIGHT);
			tableRow7.addView(DNS4Value);

			tableAtTool.addView(tableRow7, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));
			
			View dynamicView4 = new View(this);
			dynamicView4.setId(DYNAMIC_VIEW_ID);

			dynamicView4.setBackgroundColor(Color.WHITE);
			tableAtTool.addView(dynamicView4, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));


	//	String mDns1=nsts.getMobileDNS1();
	//	String mDns2=nsts.getMobileDNS2();
		
		} else if (activeNetwork.equalsIgnoreCase("WIFI")) {
			nsts.getWiFiInfo();

			TableRow tableRow2 = new TableRow(this);
			TextView LIA = new TextView(this);
			LIA.setTextSize((float) 15);
			LIA.setText("Local IP Address...");
			LIA.setGravity(Gravity.LEFT);
			tableRow2.addView(LIA); // nsts.getMobileLocalIpAddress()

			TextView LocalIP = new TextView(this);
			LocalIP.setTextSize((float) 15);
			LocalIP.setText(nsts.getWiFiIPAddress().toUpperCase());
			LocalIP.setGravity(Gravity.RIGHT);
			tableRow2.addView(LocalIP);

			tableAtTool.addView(tableRow2, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow3 = new TableRow(this);

			TextView ExternalIPAddressStr = new TextView(this);
			ExternalIPAddressStr.setTextSize((float) 15);
			ExternalIPAddressStr.setText("External IP Address...");
			ExternalIPAddressStr.setGravity(Gravity.LEFT);
			tableRow3.addView(ExternalIPAddressStr);

			nsts.getExternalIP(this);
			ExternalIPAddressValue = new TextView(this);
			ExternalIPAddressValue.setTextSize((float) 15);
			ExternalIPAddressValue.setText("trying");
			ExternalIPAddressValue.setGravity(Gravity.RIGHT);
			tableRow3.addView(ExternalIPAddressValue);

			tableAtTool.addView(tableRow3, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			// ExternalIPAddressValue.setText("DONE");

			View dynamicView2 = new View(this);
			dynamicView2.setId(DYNAMIC_VIEW_ID);
			// dynamicView.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,2));
			dynamicView2.setBackgroundColor(Color.WHITE);
			// tableRow.addView(dynamicView);
			// layoutOutsideTableAtTool
			tableAtTool.addView(dynamicView2, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));

			TableRow tableRow4 = new TableRow(this);

			TextView DNS1 = new TextView(this);
			DNS1.setTextSize((float) 15);
			DNS1.setText("DNS1...");
			DNS1.setGravity(Gravity.LEFT);
			tableRow4.addView(DNS1); // nsts.getMobileLocalIpAddress()

			TextView DNS1Value = new TextView(this);
			DNS1Value.setTextSize((float) 15);
			DNS1Value.setText(nsts.getWiFiDNS1());
			DNS1Value.setGravity(Gravity.RIGHT);
			tableRow4.addView(DNS1Value);

			tableAtTool.addView(tableRow4, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			TableRow tableRow5 = new TableRow(this);

			TextView DNS2 = new TextView(this);
			DNS2.setTextSize((float) 15);
			DNS2.setText("DNS2...");
			DNS2.setGravity(Gravity.LEFT);
			tableRow5.addView(DNS2); // nsts.getMobileLocalIpAddress()

			TextView DNS2Value = new TextView(this);
			DNS2Value.setTextSize((float) 15);
			DNS2Value.setText(nsts.getWiFiDNS2());
			DNS2Value.setGravity(Gravity.RIGHT);
			tableRow5.addView(DNS2Value);

			tableAtTool.addView(tableRow5, new TableLayout.LayoutParams(40,
					LayoutParams.WRAP_CONTENT));

			View dynamicView3 = new View(this);
			dynamicView3.setId(DYNAMIC_VIEW_ID);
			// dynamicView.setLayoutParams(new
			// LayoutParams(LayoutParams.FILL_PARENT,2));
			dynamicView3.setBackgroundColor(Color.WHITE);
			// tableRow.addView(dynamicView);
			// layoutOutsideTableAtTool
			tableAtTool.addView(dynamicView3, new LayoutParams(
					LayoutParams.FILL_PARENT, 2));
		}

	}

	public void setExternalIPvalue(String str) {
		ExternalIPAddressValue.setText(str);
	}

	private void writeLogFile(String strFileContents) {
		String localurl;
		FileOutputStream fos;

		try {
			// Log.e("the String in the try ", "" + strFileContents);
			fos = openFileOutput("mylog.log", MODE_APPEND);

			try {
				fos.write(strFileContents.getBytes());
				// Log.e("", "Secess in Writing");
			} catch (IOException e) {
				// Log.e("File write Error ", "");
			} finally {
				// Close file.
				// Log.e("in the finally", "");
				if (fos == null) {
					// Log.e("File has not bee Open", "");
				}
				if (fos != null) {
					// Log.e("", "File has  bee Open");
					try {
						// Log.e("", "File shall be closed Open");
						fos.flush();
						fos.close();
					} catch (Exception e) {
						// Log.e("File Close Error ", "");
					}
				}
			}
		} catch (FileNotFoundException e) {
			// Log.e("file Not found ", "");
		}
	}// writeLogFile

	private void setInitialResultFromFile(String filename) {
		try {
			// fis = openFileInput(filename);
			// Log.e("before file Reading", "before File Reading");
			InputStream in = openFileInput(filename);
			if (in != null) {
				InputStreamReader input = new InputStreamReader(in);
				BufferedReader reader = new BufferedReader(input);

				String strLine = null;

				while ((strLine = reader.readLine()) != null) {
					ro = new resultObject();
					// Log.e("Reading Line", "");
					String[] sa = strLine.split(",");
					// Log.e("in Readline", sa[0] + sa[1] + sa[2] + sa[3] +
					// sa[4]
					// + sa[5] + sa[6]);

					ro.setNetworktype(sa[0]);
					TableRow tr = new TableRow(this);
					tr.setLayoutParams(new LayoutParams(40, 2));
					// active Network
					TextView b = new TextView(this);
					b.setTextSize((float) 10);
					b.setText(sa[0].toUpperCase());
					b.setGravity(Gravity.RIGHT);
					tr.addView(b, new TableRow.LayoutParams(40, 25, 1));
					tr.setLayoutParams(new LayoutParams(40, 2));

					ro.setInternalIP(sa[1]);
					ro.setExternalIP(sa[2]);
					ro.setWiFiDNS1(sa[3]);
					ro.setWiFiDNS2(sa[4]);

					ro.setURL(sa[5]);
					TextView b1 = new TextView(this);
					b1.setTextSize((float) 10);

					if (sa[5].contains("http://skplanet-qiip.clouddns.co.cc")) {
						b1.setText("CDN");
					} else if (sa[5].contains("http://m.qiip.jp/share/misc")) {
						b1.setText("NORMAL");
					}
					b1.setGravity(Gravity.RIGHT);
					tr.addView(b1, new TableRow.LayoutParams(40, 25, 1));

					// Date&Time

					ro.setDateString(sa[6]);
					TextView c = new TextView(this);
					int end = sa[6].lastIndexOf(":");
					c.setText(sa[6].substring(5, end));

					c.setTextSize((float) 10);
					c.setGravity(Gravity.RIGHT);

					ro.setTotalSize(Float.parseFloat(sa[7]));
					ro.setDLSize(Float.parseFloat(sa[8]));

					ro.setkbps(Float.parseFloat(sa[9]));
					tr.addView(c, new TableRow.LayoutParams(40, 25, 1));
					TextView c1 = new TextView(this);
					c1.setGravity(Gravity.RIGHT);
					c1.setTextSize((float) 10);
					c1.setText(String.format("%.01f", Float.parseFloat(sa[9])));
					tr.addView(c1, new TableRow.LayoutParams(40, 25, 1));

					ro.setduration(Float.parseFloat(sa[10]));
					TextView d = new TextView(this);
					d.setGravity(Gravity.RIGHT);
					d.setTextSize((float) 10);
					d.setText(String.format("%.01f", Float.parseFloat(sa[10])));
					tr.addView(d, new TableRow.LayoutParams(40, 25, 1));

					ro.setProgress(Integer.parseInt(sa[11]));
					TextView e = new TextView(this);
					e.setGravity(Gravity.RIGHT);
					e.setTextSize((float) 10);
					e.setText(sa[11]);

					roa.add(ro);

					tr.addView(e, new TableRow.LayoutParams(40, 25, 1));
					tl.addView(tr, new TableLayout.LayoutParams(40,
							LayoutParams.WRAP_CONTENT));
				}
				in.close();
			}
		} catch (FileNotFoundException e) {
			Log.e("File Not found for reading", "");
		} catch (Throwable t) {
			Log.e("error reading", "error reading");
		}

	}

	private URL verifyUrl(String url) {

		// Only allow HTTP URLs.

		if (!url.toLowerCase().startsWith("http://"))
			return null;
		// Verify format of URL.
		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}

		// Make sure URL specifies a file.

		if (verifiedUrl.getFile().length() < 2)	{return null;}
		return verifiedUrl;
	}

	public String getStringFromROA() {

		String mailText = new String();
		// mailText = "mail";
		//Log.e("ROA Size in the Function", roa.size() + "'");
		mailText = mailText.concat("NetworkType" + ",");
		mailText = mailText.concat("Internal IP" + ",");
		mailText = mailText.concat("External IP" + ",");
		mailText = mailText.concat("DNS1" + ",");
		mailText = mailText.concat("DNS2" + ",");
		mailText = mailText.concat("URL" + ",");
		mailText = mailText.concat("Date" + ",");
		mailText = mailText.concat("TotalSize(K bytes)" + ",");
		mailText = mailText.concat("Downloaded Size(K bytes)" + ",");
		mailText = mailText.concat("kbps" + ",");
		mailText = mailText.concat("duration(second)");
		mailText = mailText.concat("Progress" + "\n");

		for (int i = 0; i < roa.size(); i++) {
			resultObject ro = (resultObject) roa.get(i);
			// Log.e("NetworkType get ROA", ro.getNetworktype() + ",");
			mailText = mailText.concat(ro.getNetworktype() + ",");
			mailText = mailText.concat(ro.getInternalIP() + ",");
			mailText = mailText.concat(ro.getExternalIP() + ",");
			mailText = mailText.concat(ro.getWiFiDNS1() + ",");
			mailText = mailText.concat(ro.getWiFiDNS2() + ",");
			mailText = mailText.concat(ro.getURL() + ",");
			mailText = mailText.concat(ro.getDateString() + ",");
			mailText = mailText.concat((float) ro.getTotalSize() / 1024 + ",");
			mailText = mailText.concat((float) ro.getDLSize() / 1024 + ",");
			mailText = mailText.concat(ro.getkbps() + ",");
			mailText = mailText.concat(ro.getduration() + ",");
			mailText = mailText.concat(ro.getProgress() + "\n");
		}
		return mailText;
	}
}// DLspeedTestActivity

