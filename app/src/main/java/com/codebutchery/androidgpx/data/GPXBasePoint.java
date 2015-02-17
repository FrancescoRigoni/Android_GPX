package com.codebutchery.androidgpx.data;

import java.util.Date;

/**
 * This is the base class for {@link GPXTrackPoint} and {@link com.codebutchery.androidgpx.data.GPXWayPoint}
 * Currently supported fields are:
 * 	- Latitude
 *  - Longitude
 *  - Horizontal precision
 *  - Vertical precision
 *  - Elevation
 *  - Timestamp
 *  - Name
 *  - Description
 *  - Type
 * */
public class GPXBasePoint extends GPXBaseEntity {
	
	public static class XML {

		public static final String ATTR_LAT = "lat";
		public static final String ATTR_LON = "lon";
		public static final String TAG_TIME = "time";
		public static final String TAG_NAME = "name";
		public static final String TAG_DESC = "desc";
		public static final String TAG_TYPE = "type";
		public static final String TAG_HDOP = "hdop";
		public static final String TAG_VDOP = "vdop";
		public static final String TAG_ELE = "ele";
		
	};
	
	@SuppressWarnings("unused")
	private static final String TAG = "GPXBasePoint";

	/**
	 *  Latitude in degrees. This value is in the range [-90, 90]
	 */
	private Float mLatitude = null;
	
	/**
	 *  Longitude in degrees. This value is in the range [-180, 180) 
	 */
	private Float mLongitude = null;
	
	/**
	 *  Horizontal dilution of precision
	 */
	private Float mHDop = null;
	
	/**
	 *  Vertical dilution of precision
	 */
	private Float mVDop = null;

	/**
	 * Elevation value in meters.
	 */
	private Float mElevation = null;
	
	/**
	 * Timestamp for this point
	 * */
	private Date mDate = null;
	
	/**
	 * Point Name
	 * */
	private String mName = null;

	/**
	 * Point Description
	 * */
	private String mDescription = null;
	
	/**
	 * Point Type as String
	 * */
	private String mType = null;
	
	/**
	 * Latitude and Longitude are mandatory values
	 * in order to construct this object
	 * 
	 * */
	protected GPXBasePoint(float lat, float lon) {
		mLatitude = lat;
		mLongitude = lon;
	}
	
	
	public Float getLatitude() {
		return mLatitude;
	}

	public void setLatitude(float mLatitude) {
		this.mLatitude = mLatitude;
	}

	public Float getLongitude() {
		return mLongitude;
	}

	public void setLongitude(float mLongitude) {
		this.mLongitude = mLongitude;
	}

	public Float getElevation() {
		return mElevation;
	}

	public void setElevation(float mElevation) {
		this.mElevation = mElevation;
	}

	public Date getTimeStamp() {
		return mDate;
	}

	public void setTimeStamp(Date date) {
		this.mDate = date;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(String mDescription) {
		this.mDescription = mDescription;
	}

	public String getType() {
		return mType;
	}

	public void setType(String mType) {
		this.mType = mType;
	}
	
	public Float getHDop() {
		return mHDop;
	}

	public void setHDop(Float mHDop) {
		this.mHDop = mHDop;
	}

	public Float getVDop() {
		return mVDop;
	}

	public void setVDop(Float mVDop) {
		this.mVDop = mVDop;
	}

}
