package com.codebutchery.androidgpx.data;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GPXTrack extends GPXBaseEntity {
	public interface XML {
		String TAG_TRK = "trk";

		String TAG_NAME = "name";
		String TAG_CMT = "cmt";
		String TAG_DESC = "desc";
		String TAG_TYPE = "type";
	}

	/**
	 * Name of the track
	 * */
	private String mName = null;
	
	/**
	 * GPS comment about this track
	 * */
	private String mGpsComment = null;
	
	/**
	 * User description about this track
	 * */
	private String mUserDescription = null;
	
	/**
	 * Track type
	 * */
	private String mType = null;
	
	/**
	 * Segments list
	 * */
	private final List<GPXSegment> mSegments = new ArrayList<GPXSegment>();

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

	public void setType(String mType) {
		this.mType = mType;
	}

	public List<GPXSegment> getSegments() {
		return Collections.unmodifiableList(mSegments);
	}

	public void addSegment(final GPXSegment seg) {
		this.mSegments.add(seg);
	}

	@Override
	public void toGPX(final PrintStream ps) {
		openXmlTag(XML.TAG_TRK, ps, true, 1);
		
		putStringValueInXmlIfNotNull(XML.TAG_NAME, getName(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_CMT, getGpsComment(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_DESC, getUserDescription(), ps, 2);
		putStringValueInXmlIfNotNull(XML.TAG_TYPE, getType(), ps, 2);

		for (GPXSegment s : mSegments) s.toGPX(ps);
	
		closeXmlTag(XML.TAG_TRK, ps, true, 1);
	}
}
