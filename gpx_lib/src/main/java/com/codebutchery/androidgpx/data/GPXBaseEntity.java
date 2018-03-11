package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public abstract class GPXBaseEntity {
	public static Date parseTimestampIntoDate(final String timestampString) {
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
	public static String getTimeStampAsString(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	    return dateFormat.format(date);
	}

	void openXmlTag(final String tag,
					final PrintStream ps,
					final List<String> attrsNames,
					final List<String> attrsValues,
					boolean newLine, int tabs) {
        for(int i = 0; i < tabs; i++) ps.print("\t");

		ps.print("<" + tag + " ");
		if (attrsNames != null && attrsValues != null) {
			for (int i = 0; i < attrsNames.size(); i++) 
				ps.print(attrsNames.get(i) + "=\"" + attrsValues.get(i)+ "\" ");
		}
		ps.print(">");

        if (newLine) putNewLine(ps);
	}
	
	void openXmlTag(final String tag, final PrintStream ps,
					boolean newLine, int tabs) {
		openXmlTag(tag, ps, null, null, newLine, tabs);
	}
	
	void closeXmlTag(final String tag, final PrintStream ps,
					 boolean newLine, int tabs) {
        for(int i = 0; i < tabs; i++) ps.print("\t");
		ps.print("</" + tag + ">");
        if (newLine) putNewLine(ps);
	}
	
	private void putNewLine(final PrintStream ps) {
		ps.print("\n");
	}
	
	void putFloatValueInXmlIfNotNull(final String tag, final Float value,
									 final PrintStream ps, int tabs) {
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");
		openXmlTag(tag, ps, false, 0);
		ps.print(Float.toString(value));
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
	}
	
	void putStringValueInXmlIfNotNull(final String tag, final String value,
									  final PrintStream ps, int tabs) {
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");
		openXmlTag(tag, ps, false, 0);
		ps.print("<![CDATA[" + value + "]]>");
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
		
	}
	
	void putDateTimeValueInXmlIfNotNull(final String tag, final Date value,
										final PrintStream ps, int tabs) {
		if (value == null) return;

        for(int i = 0; i < tabs; i++) ps.print("\t");
		openXmlTag(tag, ps, false, 0);
		ps.print(getTimeStampAsString(value));
		closeXmlTag(tag, ps, false, 0);
		putNewLine(ps);
	}

	public abstract void toGPX(PrintStream ps);
}
