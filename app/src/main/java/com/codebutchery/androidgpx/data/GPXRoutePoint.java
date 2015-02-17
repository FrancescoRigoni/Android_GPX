package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;


public class GPXRoutePoint extends GPXBasePoint {

	public static class XML {

		public static final String TAG_RTEPT = "rtept";

	};

	public GPXRoutePoint(float lat, float lon) {
		super(lat, lon);
	}

	public void toGPX(PrintStream ps) {
		
		ArrayList<String> attrsNames = new ArrayList<String>();
		ArrayList<String> attrsValues = new ArrayList<String>();
		
		attrsNames.add(GPXBasePoint.XML.ATTR_LAT);
		attrsNames.add(GPXBasePoint.XML.ATTR_LON);
		
		attrsValues.add(Float.toString(getLatitude()));
		attrsValues.add(Float.toString(getLongitude()));
		
		openXmlTag(XML.TAG_RTEPT, ps, attrsNames, attrsValues, true, 3);
		
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_ELE, getElevation(), ps, 4);
		putDateTimeValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TIME, getTimeStamp(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_NAME, getName(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_TYPE, getType(), ps, 4);
		putStringValueInXmlIfNotNull(GPXBasePoint.XML.TAG_DESC, getDescription(), ps, 4);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_HDOP, getHDop(), ps, 4);
		putFloatValueInXmlIfNotNull(GPXBasePoint.XML.TAG_VDOP, getVDop(), ps, 4);
	
		closeXmlTag(XML.TAG_RTEPT, ps, true, 3);
		
	}
	
}
