package com.codebutchery.androidgpx.xml;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.codebutchery.androidgpx.data.GPXDocument;
import com.codebutchery.androidgpx.data.GPXRoute;
import com.codebutchery.androidgpx.data.GPXRoutePoint;
import com.codebutchery.androidgpx.data.GPXSegment;
import com.codebutchery.androidgpx.data.GPXTrack;
import com.codebutchery.androidgpx.data.GPXTrackPoint;
import com.codebutchery.androidgpx.data.GPXWayPoint;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * The parsing is done inside an AsyncTask, two listeners are available
 * for receiving parsing status
 * 
 * GpxParserListener to receive parsing started/completed/failed events, mandatory.<br>
 * GpxParserProgressListener to receive progress status on single tracks/segments/trackpoints/waypoints parsing, optional.<br>
 * 
 * To use this parser just create an instance and call the parse method, which will immediately return<p>
 * 
 * new GpxParser(inputStream, parserListener, parserProgressListener).parse();
 * </p>
 * The inputStream which is provided in the constructor will be closed by the parser once
 * the parsing completes, you do not have to deal with it
 * 
 * */
public class GpxParser implements GpxParserHandler.GpxParserProgressListener {
	
	private static final String TAG = "GpxParser";
	
	private InputStream mGpxIs = null;
	
	private ArrayList<GPXTrack> mTracks = new ArrayList<GPXTrack>();
    private ArrayList<GPXRoute> mRoutes = new ArrayList<GPXRoute>();
	private ArrayList<GPXWayPoint> mWayPoints = new ArrayList<GPXWayPoint>();
	
	public static interface GpxParserListener {
		
		public void onGpxParseStarted();
		public void onGpxParseCompleted(GPXDocument document);
		public void onGpxParseError(String type, String message, int lineNumber, int columnNumber);
		
	}
	
	private GpxParserListener mGpxParserListener = null;
	private GpxParserHandler.GpxParserProgressListener mGpxParserProgressListener = null;
	
	private Handler mMainHandler = null;
	
	private class ParserTask extends AsyncTask<Void, Void, Boolean> {
  
		@Override
		protected void onPreExecute() {
			 mGpxParserListener.onGpxParseStarted();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			
			mMainHandler = new Handler(Looper.getMainLooper());
			
			GpxParserHandler gpxHandler = null;
			
			try {   
				
			    /**
			    * Create a new instance of the SAX parser
			    **/
			    SAXParserFactory saxPF = SAXParserFactory.newInstance();
			    SAXParser saxP = saxPF.newSAXParser();
			    XMLReader xmlR = saxP.getXMLReader();

			    /** 
			    * Create the Handler to handle each of the XML tags. 
			    **/
			    gpxHandler = new GpxParserHandler(GpxParser.this);
			    xmlR.setContentHandler(gpxHandler);
			    
			    xmlR.parse(new InputSource(mGpxIs));
			    
			    mGpxIs.close();
			    
			    return true;
			         
			} catch (IOException e) {
				Log.e(TAG, "IOException " + e.getMessage());
				
				final String message = e.getMessage();
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						
						mGpxParserListener.onGpxParseError("IOException", message, -1, -1);
						
					}
				});
				
			} catch (SAXException e) {
				Log.e(TAG, "SAXException " + e.getMessage());
				
				final String message = e.getMessage();
				final int row = gpxHandler != null ? gpxHandler.getErrorLine() : -1;
				final int col = gpxHandler != null ? gpxHandler.getErrorColumn() : -1;
				
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						
				        mGpxParserListener.onGpxParseError("SAXException", message, row, col);
						
					}
				});
				
			} catch (ParserConfigurationException e) {
				Log.e(TAG, "ParserConfigurationException " + e.getMessage());
				final String message = e.getMessage();
				mMainHandler.post(new Runnable() {
					
					@Override
					public void run() {
						mGpxParserListener.onGpxParseError("ParserConfigurationException", message, -1, -1);
					}
				});
				
			}
			
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result == true) {
				GPXDocument document = new GPXDocument(mWayPoints, mTracks, mRoutes);
				mGpxParserListener.onGpxParseCompleted(document);
			}
		}
		
	}
	
	public GpxParser(InputStream gpxIs, GpxParserListener listener, GpxParserHandler.GpxParserProgressListener progressListener) {
		
		if (gpxIs == null) throw new IllegalArgumentException("gpxIs is null");
		if (listener == null) throw new IllegalArgumentException("listener is null");
		
		mGpxIs = gpxIs;
		mGpxParserListener = listener;
		mGpxParserProgressListener = progressListener;
		
	}
	
	public void parse() {
		new ParserTask().execute();
	}

	@Override
	public void onGpxNewTrackParsed(final int count, final GPXTrack track) {
		
		mTracks.add(track);
		
		// Forward event if listener is set
		if (mGpxParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mGpxParserProgressListener.onGpxNewTrackParsed(count, track);
					
				}
			});
		}
	}

    @Override
    public void onGpxNewRouteParsed(final int count, final GPXRoute route) {

        mRoutes.add(route);

        // Forward event if listener is set
        if (mGpxParserProgressListener != null) {
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mGpxParserProgressListener.onGpxNewRouteParsed(count, route);

                }
            });
        }

    }

    @Override
	public void onGpxNewSegmentParsed(final int count, final GPXSegment segment) {
		
		// Forward event if listener is set
		if (mGpxParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				
				@Override
				public void run() {mGpxParserProgressListener.onGpxNewSegmentParsed(count, segment);
				}
			});
		}
	}

	@Override
	public void onGpxNewTrackPointParsed(final int count, final GPXTrackPoint trackPoint) {
		
		// Forward event if listener is set
		if (mGpxParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				
				@Override
				public void run() { mGpxParserProgressListener.onGpxNewTrackPointParsed(count, trackPoint);
			
				}
			});
		}
	}

    @Override
    public void onGpxNewRoutePointParsed(final int count, final GPXRoutePoint routePoint) {

        // Forward event if listener is set
        if (mGpxParserProgressListener != null) {
            mMainHandler.post(new Runnable() {

                @Override
                public void run() {
                    mGpxParserProgressListener.onGpxNewRoutePointParsed(count, routePoint);

                }
            });
        }

    }

    @Override
	public void onGpxNewWayPointParsed(final int count, final GPXWayPoint wayPoint) {
		
		mWayPoints.add(wayPoint);
		
		// Forward event if listener is set
		if (mGpxParserProgressListener != null)  {
			mMainHandler.post(new Runnable() {
				
				@Override
				public void run() {
					mGpxParserProgressListener.onGpxNewWayPointParsed(count, wayPoint);
				}			
			});
		}
			
	}
	
}
