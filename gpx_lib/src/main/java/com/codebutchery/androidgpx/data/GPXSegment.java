package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPXSegment extends GPXBaseEntity {
	interface XML {
		String TAG_TRKSEG = "trkseg";
	}
	
	private final List<GPXTrackPoint> mTrackPoints = new ArrayList<GPXTrackPoint>();
	
	public void addPoint(final GPXTrackPoint point) {
		mTrackPoints.add(point);
	}
	
	/**
	 * @return an immutable copy of the points ArrayList for this segment.
	 * */
	public List<GPXTrackPoint> getTrackPoints() {
		return Collections.unmodifiableList(mTrackPoints);
	}

	@Override
	public void toGPX(final PrintStream ps) {
		openXmlTag(XML.TAG_TRKSEG, ps, true, 2);

		for (GPXTrackPoint p : mTrackPoints) p.toGPX(ps);
	
		closeXmlTag(XML.TAG_TRKSEG, ps, true, 2);
	}
}
