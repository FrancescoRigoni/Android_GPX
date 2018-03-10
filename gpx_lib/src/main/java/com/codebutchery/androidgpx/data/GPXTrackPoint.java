package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class GPXTrackPoint extends GPXBasePoint {
	public interface XML {
		String TAG_TRKPT = "trkpt";
	}

	public GPXTrackPoint(float lat, float lon) {
		super(lat, lon);
	}

	@Override
	public void toGPX(final PrintStream ps) {
		final List<String> attrsNames = new ArrayList<String>();
		final List<String> attrsValues = new ArrayList<String>();
		
		attrsNames.add(GPXBasePoint.XML.ATTR_LAT);
		attrsNames.add(GPXBasePoint.XML.ATTR_LON);
		attrsValues.add(Float.toString(getLatitude()));
		attrsValues.add(Float.toString(getLongitude()));
		
		openXmlTag(XML.TAG_TRKPT, ps, attrsNames, attrsValues, true, 3);
		
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_ELE, getElevation(), ps, 4);
		putDateTimeValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TIME, getTimeStamp(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_NAME, getName(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TYPE, getType(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_DESC, getDescription(), ps, 4);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_HDOP, getHDop(), ps, 4);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_VDOP, getVDop(), ps, 4);
	
		closeXmlTag(XML.TAG_TRKPT, ps, true, 3);
	}
}
