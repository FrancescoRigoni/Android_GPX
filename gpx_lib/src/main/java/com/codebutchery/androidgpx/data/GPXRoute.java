package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPXRoute extends GPXBaseEntity {
	public interface XML {
		String TAG_RTE = "rte";
		String TAG_NAME = "name";
		String TAG_CMT = "cmt";
		String TAG_DESC = "desc";
		String TAG_TYPE = "type";
	}

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
    private final List<GPXRoutePoint> mRoutePoints = new ArrayList<GPXRoutePoint>();
	
	/**
	 * Route type
	 * */
	private String mType = null;

	public String getName() {
		return mName;
	}

	public void setName(final String mName) {
		this.mName = mName;
	}

	public String getGpsComment() {
		return mGpsComment;
	}

	public void setGpsComment(final String mGpsComment) {
		this.mGpsComment = mGpsComment;
	}

	public String getUserDescription() {
		return mUserDescription;
	}

	public void setUserDescription(final String mUserDescription) {
		this.mUserDescription = mUserDescription;
	}

	public String getType() {
		return mType;
	}

	public void setType(final String mType) {
		this.mType = mType;
	}

    public void addPoint(final GPXRoutePoint point) {
        mRoutePoints.add(point);
    }

    /**
     * @return an immutable copy of the points ArrayList for this route
     * */
    public List<GPXRoutePoint> getRoutePoints() {
        return Collections.unmodifiableList(mRoutePoints);
    }

	@Override
	public void toGPX(final PrintStream ps) {
		openXmlTag(XML.TAG_RTE, ps, true, 1);
		
		putStringValueInXmlIfNotNull(XML.TAG_NAME, getName(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_CMT, getGpsComment(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_DESC, getUserDescription(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_TYPE, getType(), ps, 2);

        for (GPXRoutePoint rp : mRoutePoints) rp.toGPX(ps);

		closeXmlTag(XML.TAG_RTE, ps, true, 1);
	}
}
