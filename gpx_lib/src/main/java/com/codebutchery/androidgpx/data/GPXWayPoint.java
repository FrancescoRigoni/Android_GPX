package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


public class GPXWayPoint extends GPXBasePoint {
	public interface XML {
		String TAG_WPT = "wpt";
	}

	public GPXWayPoint(float lat, float lon) {
		super(lat, lon);
	}

	@Override
	public void toGPX(final PrintStream ps) {
		final List<String> attrsNames = new ArrayList<>();
		final List<String> attrsValues = new ArrayList<>();
		
		attrsNames.add(GPXBasePoint.XML.ATTR_LAT);
		attrsNames.add(GPXBasePoint.XML.ATTR_LON);
		attrsValues.add(Float.toString(getLatitude()));
		attrsValues.add(Float.toString(getLongitude()));
		
		openXmlTag(XML.TAG_WPT, ps, attrsNames, attrsValues, true, 1);
		
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_ELE, getElevation(), ps, 2);
		putDateTimeValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TIME, getTimeStamp(), ps, 2);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_NAME, getName(), ps, 2);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TYPE, getType(), ps, 2);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_DESC, getDescription(), ps, 2);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_HDOP, getHDop(), ps, 2);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_VDOP, getVDop(), ps, 2);
	
		closeXmlTag(XML.TAG_WPT, ps, true, 1);
	}
}
