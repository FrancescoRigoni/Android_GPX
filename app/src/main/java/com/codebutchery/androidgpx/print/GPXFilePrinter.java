package com.codebutchery.androidgpx.print;

import android.os.AsyncTask;
import android.util.Log;

import com.codebutchery.androidgpx.data.GPXDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class GPXFilePrinter {
	
	public static interface GPXFilePrinterListener {
		
		public void onGPXPrintStarted();
		public void onGPXPrintCompleted();
		public void onGPXPrintError(String message);
		
	}
	
	private GPXDocument mGPXDocument = null;
	private String mFilePath = null;
	private GPXFilePrinterListener mGPXFilePrinterListener = null;
	
	public GPXFilePrinter(GPXDocument doc, String filePath, GPXFilePrinterListener listener) {
		if (doc == null) throw new IllegalArgumentException("doc must not be null");
		if (filePath == null) throw new IllegalArgumentException("filePath must not be null");
		if (listener == null) throw new IllegalArgumentException("listener must not be null");
		
		mGPXDocument = doc;
		mFilePath = filePath;
		mGPXFilePrinterListener = listener;	
	}
	
	public void print() {
		new PrinterTask().execute();
	}
	
	private class PrinterTask extends AsyncTask<Void, Void, Boolean> {
		
		private static final String TAG = "PrinterTask";
		
		private String mErrorMessage = null;
		  
		@Override
		protected void onPreExecute() {
			mGPXFilePrinterListener.onGPXPrintStarted();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			try {
				
				OutputStream os = new FileOutputStream(mFilePath);
				final PrintStream printStream = new PrintStream(os);
				mGPXDocument.toGpx(printStream);
				printStream.close();
				
				return true;
				
			} catch (FileNotFoundException e) {
				mErrorMessage = "FileNotFoundException " + e.getMessage();
				Log.e(TAG, mErrorMessage);
			}

			return false;
			
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result == true) mGPXFilePrinterListener.onGPXPrintCompleted();
			else mGPXFilePrinterListener.onGPXPrintError(mErrorMessage);
		}
		
	}

}
