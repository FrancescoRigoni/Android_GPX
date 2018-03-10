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
public abstract class GPXBasePoint extends GPXBaseEntity {
	public interface XML {
		String ATTR_LAT = "lat";
		String ATTR_LON = "lon";
		String TAG_TIME = "time";
		String TAG_NAME = "name";
		String TAG_DESC = "desc";
		String TAG_TYPE = "type";
		String TAG_HDOP = "hdop";
		String TAG_VDOP = "vdop";
		String TAG_ELE = "ele";
	}

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
	GPXBasePoint(float lat, float lon) {
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

	public void setTimeStamp(final Date date) {
		this.mDate = date;
	}

	public String getName() {
		return mName;
	}

	public void setName(final String mName) {
		this.mName = mName;
	}
	
	public String getDescription() {
		return mDescription;
	}

	public void setDescription(final String mDescription) {
		this.mDescription = mDescription;
	}

	public String getType() {
		return mType;
	}

	public void setType(final String mType) {
		this.mType = mType;
	}
	
	public Float getHDop() {
		return mHDop;
	}

	public void setHDop(final float mHDop) {
		this.mHDop = mHDop;
	}

	public Float getVDop() {
		return mVDop;
	}

	public void setVDop(final float mVDop) {
		this.mVDop = mVDop;
	}
}
