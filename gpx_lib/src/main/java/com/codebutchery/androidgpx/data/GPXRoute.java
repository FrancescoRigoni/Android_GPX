package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;

public class GPXRoute extends GPXBaseEntity {
	
	public static class XML {
		
		public static final String TAG_RTE = "rte";

		public static final String TAG_NAME = "name";
		public static final String TAG_CMT = "cmt";
		public static final String TAG_DESC = "desc";
		public static final String TAG_TYPE = "type";
		
	};

	/**
	 * Name of the route
	 * */
	private String mName = null;
	
	/**
	 * GPS comment about this route
	 * */
	private String mGpsComment = null;
	
	/**
	 * User description about this route
	 * */
	private String mUserDescription = null;

    /**
     * Route points
     * */
    private ArrayList<GPXRoutePoint> mRoutePoints = new ArrayList<GPXRoutePoint>();
	
	/**
	 * Route type
	 * */
	private String mType = null;

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getGpsComment() {
		return mGpsComment;
	}

	public void setGpsComment(String mGpsComment) {
		this.mGpsComment = mGpsComment;
	}

	public String getUserDescription() {
		return mUserDescription;
	}

	public void setUserDescription(String mUserDescription) {
		this.mUserDescription = mUserDescription;
	}

	public String getType() {
		return mType;
	}

	public void setType(String mType) {
		this.mType = mType;
	}

    public void addPoint(GPXRoutePoint point) {
        mRoutePoints.add(point);
    }

    /**
     * @return a copy of the points ArrayList for this route
     * */
    public ArrayList<GPXRoutePoint> getRoutePoints() {

        // Return a copy of the points list so users won't be able to alter
        // our inner copy
        ArrayList<GPXRoutePoint> points = new ArrayList<GPXRoutePoint>();
        for (GPXRoutePoint p : mRoutePoints) points.add(p);

        return points;

    }

	public void toGPX(PrintStream ps) {
		
		openXmlTag(XML.TAG_RTE, ps, true, 1);
		
		putStringValueInXmlIfNotNull(XML.TAG_NAME, getName(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_CMT, getGpsComment(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_DESC, getUserDescription(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_TYPE, getType(), ps, 2);

        for (GPXRoutePoint rp : mRoutePoints) rp.toGPX(ps);

		closeXmlTag(XML.TAG_RTE, ps, true, 1);
		
	}
	
}
