package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;

public class GPXSegment extends GPXBaseEntity {
	
	public static class XML {

		public static final String TAG_TRKSEG = "trkseg";
		
	};
	
	private ArrayList<GPXTrackPoint> mTrackPoints = new ArrayList<GPXTrackPoint>();
	
	public void addPoint(GPXTrackPoint point) {
		mTrackPoints.add(point);
	}
	
	/**
	 * @return a copy of the points ArrayList for this segment
	 * */
	public ArrayList<GPXTrackPoint> getTrackPoints() {
		
		// Return a copy of the points list so users won't be able to alter
		// our inner copy
		ArrayList<GPXTrackPoint> points = new ArrayList<GPXTrackPoint>();
		for (GPXTrackPoint p : mTrackPoints) points.add(p);
		
		return points;
		
	}
	
	public void toGPX(PrintStream ps) {

		openXmlTag(XML.TAG_TRKSEG, ps, true, 2);

		for (GPXTrackPoint p : mTrackPoints) p.toGPX(ps);
	
		closeXmlTag(XML.TAG_TRKSEG, ps, true, 2);
		
	}

}
