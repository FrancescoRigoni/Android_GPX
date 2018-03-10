package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPXDocument extends GPXBaseEntity {
	private final static String XMLNS = "http://www.topografix.com/GPX/1/1";
	private final static String CREATOR = "AndroidGPX ( http://codebutchery.wordpress.com )";
	private final static String VERSION = "1.1";
	private final static String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	private final static String XSI_SCHEMALOCATION = "http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd";

	public interface XML {
		String ATTR_XMLNS = "xmlns";
		String ATTR_CREATOR = "creator";
		String ATTR_VERSION = "version";
		String ATTR_XMLNS_XSI = "xmlns:xsi";
		String ATTR_XSI_SCHEMALOCATION = "xsi:schemaLocation";
		
		String TAG_GPX = "gpx";
	}
	
	private final List<GPXWayPoint> mWayPoints;
	private final List<GPXTrack> mTracks;
    private final List<GPXRoute> mRoutes;

	public GPXDocument(final List<GPXWayPoint> wayPoints,
					   final List<GPXTrack> tracks,
					   final List<GPXRoute> routes) {
		mWayPoints = wayPoints;
		mTracks = tracks;
        mRoutes = routes;
	}

	public List<GPXTrack> getTracks() {
		return Collections.unmodifiableList(mTracks);
	}

	public List<GPXWayPoint> getWayPoints() {
		return Collections.unmodifiableList(mWayPoints);
	}

	public List<GPXRoute> getRoutes() {
		return Collections.unmodifiableList(mRoutes);
	}

	@Override
	public void toGPX(final PrintStream ps) {
		final List<String> attrsNames = new ArrayList<String>();
		final List<String> attrsValues = new ArrayList<String>();
		
		attrsNames.add(XML.ATTR_XMLNS);
		attrsNames.add(XML.ATTR_CREATOR);
		attrsNames.add(XML.ATTR_VERSION);
		attrsNames.add(XML.ATTR_XMLNS_XSI);
		attrsNames.add(XML.ATTR_XSI_SCHEMALOCATION);
		
		attrsValues.add(XMLNS);
		attrsValues.add(CREATOR);
		attrsValues.add(VERSION);
		attrsValues.add(XMLNS_XSI);
		attrsValues.add(XSI_SCHEMALOCATION);
		
		openXmlTag(XML.TAG_GPX, ps, attrsNames, attrsValues, true, 0);
		
		if (mWayPoints != null) for (GPXWayPoint wp : mWayPoints) wp.toGPX(ps);
		if (mTracks != null) for (GPXTrack t : mTracks) t.toGPX(ps);
        if (mRoutes != null) for (GPXRoute r : mRoutes) r.toGPX(ps);
		
		closeXmlTag(XML.TAG_GPX, ps, true, 0);
	}
}
