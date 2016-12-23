package com.android.performancetest;

/**

 *

 * @author dylee

 */

import java.io.File;

import java.io.FileOutputStream;

import java.io.InputStream;

import java.net.HttpURLConnection;

import java.net.URL;

import java.util.Observable;

import android.util.Log;
import com.android.performancetest.R;

// This class downloads a file from a URL.

class Download extends Observable implements Runnable { // Max size of download

	// buffer.

	private static final int MAX_BUFFER_SIZE = 1024; // These are the status

	// names.

	public static final String STATUSES[] = { "Downloading", "Not Started",

	"Complete", "Cancelled", "Error" }; // These are the status codes.

	public static final int DOWNLOADING = 0;

	public static final int PAUSED = 1;

	public static final int COMPLETE = 2;

	public static final int CANCELLED = 3;

	public static final int ERROR = 4;

	private URL url;

	// download URL

	private float size;

	// size of download in bytes

	private float downloaded;
	// number of bytes downloaded
	public int status;

	// current status of download

	long start = 0;

	long end = 0;

	long interimtime = 0; // Constructor for Download.

	public Download(URL url) {

		this.url = url;

		size = -1;

		downloaded = 0;

		// status = DOWNLOADING;

		status = PAUSED;

		// Begin the download.

		// download();

	}

	// Get this download's URL.

	public String getUrl() {

		return url.toString();

	}

	// Get this download's size.

	public int getSize() {

		return (int) size;

	}

	public float getDownloadedSize() {

		return (float) downloaded;

	}

	// Get this download's progress.

	public int getProgress() {

		// Log.e("size is ","" + (int) ((downloaded/size)*(float)100));

		return ((int) ((downloaded / size) * (float) 100));

	} // Get this download's status.

	public int getStatus() {

		return status;

	}

	// Get this download's speed

	public float getKbps() {
		if (status == DOWNLOADING) {
			// Log.e("downloaded:",""+ downloaded/(float) 1024);
			return (float) (downloaded / 1024 * 8)
					/ ((end - interimtime) / (float) 1000);
		} else if (status == COMPLETE || status == CANCELLED) {
			// Log.e("downloaded:",""+ downloaded);
			if(downloaded ==0 )return 0;
			return (float) (float) (downloaded / 1024 * 8)
			/ ((end - start) / (float) 1000);
		} else {
			return 0;
		}

		// if (status == COMPLETE) {

		// return (float) ( downloaded/( (float) ((end - start)/(float)1000 ));

		// return (float) ((((float) downloaded)/1024) / (float)( (end -

		// start)/(double)1000.0)) ;

		// } else

		// { return 0 ;}

	}

	// Get this download's duration

	public float getDuration() {
		if (end-start==0) return 0;
		return (float) ((end - start) / (float) 1000);
	} // Pause this download.

	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	// Resume this download.
	public void resume() {
		status = DOWNLOADING;
		downloaded = 0;
		end = (long) 0.0;

		start = (long) 0.0;

		// Log.e("", "in the resume Thread");

		download();

		stateChanged();

	}

	// Cancel this download.

	public void cancel() {

		status = CANCELLED;

	//	Log.e("in the download", "Cancelled1");

		// stateChanged();

	}

	// Mark this download as having an error.

	private void error() {
		status = ERROR;
		stateChanged();
	}

	// Start or resume downloading.

	private void download() {

		Thread thread = new Thread(this);

		// Log.e("", "in the downoad thread after new");

		thread.start();

	}

	// Get file name portion of URL.

	private String getFileName(URL url) {

		String fileName = url.getFile();

		return fileName.substring(fileName.lastIndexOf('/') + 1);

	} // Download file.

	public void run() {

		// RandomAccessFile file = null;

		String outputFileName = "DNL.apk";

		InputStream stream = null;

		FileOutputStream fos = null;

		// Log.e("", "in the start before URL Connection");

		try {

			// Open connection to URL.

			// Log.e("URL IS", ""+ getUrl());

			// url=new

			// URL("http://m.qiip.jp/share/misc/coreapp/G1_Client_v2.09_20120118.apk");

			HttpURLConnection connection = (HttpURLConnection) url
			.openConnection();
			// Log.e("", "Right After URLCOnnection");
			// Specify what portion of file to download.
			connection.setRequestProperty("Range", "bytes=" + downloaded + "-");
			// Connect to server.
			try {
				// Time Out : 20초
				connection.setConnectTimeout(20000);
				connection.connect();
			} catch (Exception e) {
				// Log.e("Connect error:", ""+ getUrl());
				error();
			}
			;
			// Log.e("", "before Output file Open");
			// fos = new FileOutputStream(new File(outputFileName));
			// fos = new FileOutputStream("DNL.apk");
			// Log.e("", "Output file Open");
			// Make sure response code is in the 200 range.
			if (connection.getResponseCode() / 100 != 2) {
				Log.e("", "Get response COde is not 2");
				error();
			}
			// Log.e("", "Right After Checking DLFILE");
			// Check for valid content length.
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				// Log.e("", "contents Length is smaller tha 1 in the Thread");
				error();
			}
			/*
			 * 
			 * Set the size for this download if it hasn't been already set.
			 */
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}

			// Log.e("My status is "+status, "Right Before File input");
			// Open file and seek to the end of it.
			// file = new RandomAccessFile(getFileName(url), "rw");
			// file.seek(downloaded);
			stream = connection.getInputStream();
			// Log.e(""+status, "Downloading continued");

			if (status == DOWNLOADING) {
				start = System.currentTimeMillis();
				// Log.e(""+status, "Downloading continued");
			}

			while (status == DOWNLOADING) {
				interimtime = System.currentTimeMillis();
				// Log.e("", "Start Downloading");
				/*
				 * 
				 * Size buffer according to how much of the file is left to
				 * 
				 * download.
				 */
				byte buffer[];
				/* Measure start time */
				if (size - downloaded > MAX_BUFFER_SIZE) {

					buffer = new byte[MAX_BUFFER_SIZE];

				} else {

					buffer = new byte[(int) size - (int) downloaded];

				}

				// Read from server into buffer.

				int read = stream.read(buffer);
				if (read == -1)
					break;

				// Write buffer to file.

				// fos.write(buffer, 0, read);

				downloaded += read;
				// Log.e("Downloaded:  "," "+downloaded);

				if (status != CANCELLED) {
					stateChanged();
				}

				end = System.currentTimeMillis();
			}

			/*
			 * 
			 * Change status to complete if this point was reached because
			 * 
			 * downloading has finished.
			 */

			if (status == DOWNLOADING) {
				status = COMPLETE;
				end = System.currentTimeMillis();
				// System.out.println("end 시간"+end/1000.0);
				stateChanged();
			} else if (status == CANCELLED) {
				stateChanged();
			}

		} catch (Exception e) {

			// Log.e("Downloaging loop", "" + getUrl());

			error();
		} finally {
			// Close file.
			if (fos != null) {
				try {
					fos.close();
				} catch (Exception e) {

				}

			}

			// Close connection to server.

			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
				}

				// end= System.currentTimeMillis();

				// System.out.println("end 시간"+end/1000.0);

			}

		}

	}

	// Notify observers that this download's status has changed.

	private void stateChanged() {
		setChanged();
		notifyObservers();
	}

}
