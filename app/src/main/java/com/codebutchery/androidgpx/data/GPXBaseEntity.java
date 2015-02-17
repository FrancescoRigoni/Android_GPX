package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class GPXBaseEntity {
	
	public static Date parseTimestampIntoDate(String timestampString) {
		
		/* 
		 * The timestamp string is encoded as xsd:dateTime datatype
		 * see: http://www.w3schools.com/schema/schema_dtypes_date.asp
		 * 
		 * 2012-10-13T08:47:27				No timezone
		 * 2012-10-13T08:47:27Z				UTC
		 */
		
		// For SimpleDateFormat patterns see
		// http://developer.android.com/reference/java/text/SimpleDateFormat.html
		String dateFormatNoTimezone = "yyyy-MM-dd'T'HH:mm:ss";
		String dateFormatTimezoneUTC = "yyyy-MM-dd'T'HH:mm:ss'Z'";
		
		Date convertedDate = null;

		try {
			
			// "yyyy-MM-dd'T'hh:mm:ss.S'Z'"
			
			SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatTimezoneUTC);
			dateFormat.setLenient(false);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			convertedDate = dateFormat.parse(timestampString); 

		} catch (ParseException e1) {

			try {
				
				// "yyyy-MM-dd'T'hh:mm:ss.S"
				
				SimpleDateFormat dateFormat = new SimpleDateFormat(dateFormatNoTimezone);
				dateFormat.setLenient(false);
				dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
				convertedDate = dateFormat.parse(timestampString);

			} catch (ParseException e2) {
				// Cannot parse this string with any known pattern, give up
			}	
		}
		
	    
		return convertedDate;   
		
	}
	
	/**
	 * Returns the xsd:dateTime compliant timestamp string for this GpxBasePoint
	 * */
	public static String getTimeStampAsString(Date date) {
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    
	    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    
	    return dateFormat.format(date);
		
	}

	protected void openXmlTag(String tag, PrintStream ps, ArrayList<String> attrsNames, ArrayList<String> attrsValues, boolean newLine, int tabs) {

        for(int i = 0; i < tabs; i++) ps.print("\t");

		ps.print("<" + tag + " ");
		
		if (attrsNames != null && attrsValues != null) {
			for (int i = 0; i < attrsNames.size(); i++) 
				ps.print(attrsNames.get(i) + "=\"" + attrsValues.get(i)+ "\" ");
		}
		
		ps.print(">");

        if (newLine) putNewLine(ps);
		
	}
	
	protected void openXmlTag(String tag, PrintStream ps, boolean newLine, int tabs) {
		
		openXmlTag(tag, ps, null, null, newLine, tabs);
		
	}
	
	protected void closeXmlTag(String tag, PrintStream ps, boolean newLine, int tabs) {

        for(int i = 0; i < tabs; i++) ps.print("\t");

		ps.print("</" + tag + ">");

        if (newLine) putNewLine(ps);
		
	}
	
	protected void putNewLine(PrintStream ps) {
		ps.print("\n");
	}
	
	protected void putFloatValueInXmlIfNotNull(String tag, Float value, PrintStream ps, int tabs) {
		
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");
		
		openXmlTag(tag, ps, false, 0);
		ps.print(Float.toString(value));
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
		
	}
	
	protected void putStringValueInXmlIfNotNull(String tag, String value, PrintStream ps, int tabs) {
		
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");
		
		openXmlTag(tag, ps, false, 0);
		ps.print("<![CDATA[" + value + "]]>");
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
		
	}
	
	protected void putDateTimeValueInXmlIfNotNull(String tag, Date value, PrintStream ps, int tabs) {
		
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");

		openXmlTag(tag, ps, false, 0);
		ps.print(getTimeStampAsString(value));
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
		
	}
	
}
