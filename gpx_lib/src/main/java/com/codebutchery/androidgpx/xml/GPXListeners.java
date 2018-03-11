package com.codebutchery.androidgpx.xml;

import com.codebutchery.androidgpx.data.GPXDocument;
import com.codebutchery.androidgpx.data.GPXRoute;
import com.codebutchery.androidgpx.data.GPXRoutePoint;
import com.codebutchery.androidgpx.data.GPXSegment;
import com.codebutchery.androidgpx.data.GPXTrack;
import com.codebutchery.androidgpx.data.GPXTrackPoint;
import com.codebutchery.androidgpx.data.GPXWayPoint;

public interface GPXListeners {
    interface GPXParserListener {
        void onGpxParseStarted();
        void onGpxParseCompleted(GPXDocument document);
        void onGpxParseError(String type, String message, int lineNumber, int columnNumber);
    }

    interface GPXParserProgressListener {
        void onGpxNewTrackParsed(int count, GPXTrack track);
        void onGpxNewRouteParsed(int count, GPXRoute track);
        void onGpxNewSegmentParsed(int count, GPXSegment segment);
        void onGpxNewTrackPointParsed(int count, GPXTrackPoint trackPoint);
        void onGpxNewRoutePointParsed(int count, GPXRoutePoint routePoint);
        void onGpxNewWayPointParsed(int count, GPXWayPoint wayPoint);
    }
}
