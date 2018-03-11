package com.codebutchery.androidgpx.print;

import android.os.AsyncTask;
import android.util.Log;

import com.codebutchery.androidgpx.data.GPXDocument;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class GPXFilePrinter {
	public interface GPXFilePrinterListener {
		void onGPXPrintStarted();
		void onGPXPrintCompleted();
		void onGPXPrintError(String message);
	}

	private PrinterTask mTask;
	private final GPXFilePrinterListener mGPXFilePrinterListener;
	
	public GPXFilePrinter(final GPXFilePrinterListener listener) {
		if (listener == null) throw new IllegalArgumentException("listener must not be null");
		mGPXFilePrinterListener = listener;	
	}
	
	public void print(final GPXDocument doc, final String filePath) {
		if (filePath == null) throw new IllegalArgumentException("filePath must not be null");
		if (doc == null) throw new IllegalArgumentException("doc must not be null");
		if (mTask != null) throw new IllegalStateException("Please do not reuse instances of this class");

		mTask = new PrinterTask();
		mTask.mFilePath = filePath;
		mTask.mGPXDocument = new GPXDocument(doc.getWayPoints(), doc.getTracks(), doc.getRoutes());
		mTask.mGPXFilePrinterListener = mGPXFilePrinterListener;
		mTask.execute();
	}

	public void cancelPrint() {
		if (mTask != null && !mTask.isCancelled()) {
			mTask.cancel(false);
			mTask.mGPXFilePrinterListener = null;
		}
	}
	
	private static class PrinterTask extends AsyncTask<Void, Void, Boolean> {
		private static final String TAG = "PrinterTask";

		private GPXDocument mGPXDocument;
		private String mErrorMessage;
		private String mFilePath;
		private GPXFilePrinterListener mGPXFilePrinterListener;
		  
		@Override
		protected void onPreExecute() {
			if (mGPXFilePrinterListener != null) {
				mGPXFilePrinterListener.onGPXPrintStarted();
			}
		}
		
		@Override
		protected Boolean doInBackground(final Void... arg0) {
			try {
				final OutputStream os = new FileOutputStream(mFilePath);
				final PrintStream printStream = new PrintStream(os);
				mGPXDocument.toGPX(printStream);
				printStream.close();
				return true;
			} catch (final FileNotFoundException e) {
				mErrorMessage = "FileNotFoundException " + e.getMessage();
				Log.e(TAG, mErrorMessage);
			}
			return false;
		}

		@Override
		protected void onPostExecute(final Boolean result) {
			if (mGPXFilePrinterListener != null) {
				if (result) mGPXFilePrinterListener.onGPXPrintCompleted();
				else mGPXFilePrinterListener.onGPXPrintError(mErrorMessage);
			}
		}

		@Override
		protected void onCancelled() {
			Log.v(TAG, "Printer task was canceled");
		}
	}
}
