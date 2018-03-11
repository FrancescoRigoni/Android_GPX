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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * The parsing is done inside an AsyncTask, two listeners are available
 * for receiving parsing status
 * 
 * GPXParserListener to receive parsing started/completed/failed events, mandatory.<br>
 * GPXParserProgressListener to receive progress status on single tracks/segments/trackpoints/waypoints parsing, optional.<br>
 * 
 * To use this parser just create an instance and call the parse method, which will immediately return<p>
 * 
 * new GPXParser(parserListener, parserProgressListener).parse(inputStream);
 * </p>
 * The inputStream which is provided in the constructor will be closed by the parser once
 * the parsing completes, you do not have to deal with it.
 * 
 * */
public class GPXParser implements GPXListeners.GPXParserListener, GPXListeners.GPXParserProgressListener {
	private static final String TAG = "GPXParser";

	private final Handler mMainHandler = new Handler(Looper.getMainLooper());
	private ParserTask mTask;

	private GPXListeners.GPXParserListener mGPXParserListener;
	private GPXListeners.GPXParserProgressListener mGPXParserProgressListener;

    private final List<GPXTrack> mTracks = new ArrayList<>();
    private final List<GPXRoute> mRoutes = new ArrayList<>();
    private final List<GPXWayPoint> mWayPoints = new ArrayList<>();

	private static class ParserTask extends AsyncTask<Void, Void, Boolean> {
		private InputStream mGpxIs;
		private GPXListeners.GPXParserListener mGPXParserListener;
		private GPXListeners.GPXParserProgressListener mGPXParserProgressListener;
		private GPXParserHandler mParserHandler = new GPXParserHandler();
  
		@Override
		protected void onPreExecute() {
            mGPXParserListener.onGpxParseStarted();
		}
		
		@Override
		protected Boolean doInBackground(Void... arg0) {
			try {
			    final SAXParserFactory saxPF = SAXParserFactory.newInstance();
			    final SAXParser saxP = saxPF.newSAXParser();
			    final XMLReader xmlR = saxP.getXMLReader();

                mParserHandler.setListener(mGPXParserProgressListener);
			    xmlR.setContentHandler(mParserHandler);
			    xmlR.parse(new InputSource(mGpxIs));
			    return true;
			         
			} catch (final IOException e) {
				Log.e(TAG, "IOException " + e.getMessage());
				
				final String message = e.getMessage();
                if (mGPXParserListener != null)
                    mGPXParserListener.onGpxParseError("IOException", message, -1, -1);

			} catch (final SAXException e) {
				Log.e(TAG, "SAXException " + e.getMessage());
				
				final String message = e.getMessage();
				final int row = mParserHandler != null ? mParserHandler.getErrorLine() : -1;
				final int col = mParserHandler != null ? mParserHandler.getErrorColumn() : -1;
                if (mGPXParserListener != null)
				    mGPXParserListener.onGpxParseError("SAXException", message, row, col);
				
			} catch (final ParserConfigurationException e) {
				Log.e(TAG, "ParserConfigurationException " + e.getMessage());
				final String message = e.getMessage();
				if (mGPXParserListener != null)
				    mGPXParserListener.onGpxParseError("ParserConfigurationException", message, -1, -1);

			} finally {
			    if (mGpxIs != null) {
                    try { mGpxIs.close(); } catch (Exception e) {}
                }
            }
			return false;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result && mGPXParserListener != null) {
				// The GPXDocument will be build by the outer listener.
				mGPXParserListener.onGpxParseCompleted(null);
			}
		}

		@Override
		protected void onCancelled() {
			Log.v(TAG, "Parser task was canceled");
		}
	}
	
	public GPXParser(final GPXListeners.GPXParserListener listener,
                     final GPXListeners.GPXParserProgressListener progressListener) {
		if (listener == null) throw new IllegalArgumentException("listener is null");
		mGPXParserListener = listener;
		mGPXParserProgressListener = progressListener;
	}

	public void parse(final InputStream gpxIs) {
		if (gpxIs == null) throw new IllegalArgumentException("gpxIs must not be null");
		if (mTask != null) throw new IllegalStateException("Please do not reuse instances of this class");

		mTask = new ParserTask();
		mTask.mGpxIs = gpxIs;
		mTask.mGPXParserListener = this;
		mTask.mGPXParserProgressListener = this;
		mTask.mParserHandler.setListener(mGPXParserProgressListener);
		mTask.execute();
	}

	public void cancelParse() {
		if (mTask != null && !mTask.isCancelled()) {
			mTask.cancel(false);
			mTask.mGPXParserListener = null;
			mTask.mGPXParserProgressListener = null;
            mGPXParserListener = null;
            mGPXParserProgressListener = null;
		}
	}

    @Override
    public void onGpxParseStarted() {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mGPXParserListener.onGpxParseStarted();
            }
        });
    }

    @Override
    public void onGpxParseCompleted(final GPXDocument document) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mGPXParserListener.onGpxParseCompleted(new GPXDocument(mWayPoints, mTracks, mRoutes));
            }
        });
    }

    @Override
    public void onGpxParseError(final String type, final String message,
                                final int lineNumber, final int columnNumber) {
        mMainHandler.post(new Runnable() {
            @Override
            public void run() {
                mGPXParserListener.onGpxParseError(type, message, lineNumber, columnNumber);
            }
        });
    }

	@Override
	public void onGpxNewTrackParsed(final int count, final GPXTrack track) {
		mTracks.add(track);
		// Forward event if listener is set
		if (mGPXParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
                    mGPXParserProgressListener.onGpxNewTrackParsed(count, track);
				}
			});
		}
	}

    @Override
    public void onGpxNewRouteParsed(final int count, final GPXRoute route) {
        mRoutes.add(route);
        // Forward event if listener is set
        if (mGPXParserProgressListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mGPXParserProgressListener.onGpxNewRouteParsed(count, route);
                }
            });
        }
    }

    @Override
	public void onGpxNewSegmentParsed(final int count, final GPXSegment segment) {
		// Forward event if listener is set
		if (mGPXParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
                    mGPXParserProgressListener.onGpxNewSegmentParsed(count, segment);
				}
			});
		}
	}

	@Override
	public void onGpxNewTrackPointParsed(final int count, final GPXTrackPoint trackPoint) {
		// Forward event if listener is set
		if (mGPXParserProgressListener != null) {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
                    mGPXParserProgressListener.onGpxNewTrackPointParsed(count, trackPoint);
				}
			});
		}
	}

    @Override
    public void onGpxNewRoutePointParsed(final int count, final GPXRoutePoint routePoint) {
        // Forward event if listener is set
        if (mGPXParserProgressListener != null) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    mGPXParserProgressListener.onGpxNewRoutePointParsed(count, routePoint);
				}
            });
        }
    }

    @Override
	public void onGpxNewWayPointParsed(final int count, final GPXWayPoint wayPoint) {
		mWayPoints.add(wayPoint);
		// Forward event if listener is set
		if (mGPXParserProgressListener != null)  {
			mMainHandler.post(new Runnable() {
				@Override
				public void run() {
                    mGPXParserProgressListener.onGpxNewWayPointParsed(count, wayPoint);
				}			
			});
		}
	}
}
