Android_GPX
===========

This is a small Android library that I created for a work project, it can be used to easily parse and create GPX files.

Have a look at the sample project using this library at: 

    https://github.com/FrancescoRigoni/Android_GPX_SampleProject

The library contains a SAX parser for the GPX format, as described in the GPX Schema documentation at http://www.topografix.com/gpx/1/1/ .

The Gpx parser is asynchronous, this means that an event listener interface must be provided when the parser is started. The parsed GPX document or any parsing error that may occur will be returned through the event listener interface.

Basic Usage
-----------

Include gradle dependency
```
dependencies {
    ...
    implementation 'com.codebutchery.android:gpx_lib:1.0.3'
}
```

This is the basic code required to parse a GPX file, assuming that we have a "file.gpx" in the assets folder:

```java
try {
    InputStream input = getAssets().open(fileName);
    // The GpxParser automatically closes the InputStream so we do not have to bother about it
    mParser = new GPXParser(this, this);
    mParser.parse(input);
} catch (IOException e) {
    Toast.makeText(this, "IOExeption opening file", Toast.LENGTH_SHORT).show();
}
```

The *GPXParser* constructor takes two parameters:
  - A *GPXParserListener* implementation (see below)
  - A *GPXParserProgressListener* implementation, can be null (see below)

Once the *GPXParser* has been constructed just call the *parse(inputStream)* method to start it.

The parser can be stopped with
```
mParser.cancelParse();
```

Parser Listeners
----------------

Two different event listeners can be provided to the GPXParser constructor:
```java
public GPXParser(GPXParserListener listener, 
                 GPXParserProgressListener progressListener)
```
The *GPXParserListener* is mandatory, an IllegalArgumentException will be thrown if this argument is null.
*GPXParserListener* provides basic feedback on the parser activity, the methods are pretty self explanatory:
```java
interface GPXParserListener {	
	void onGPXParseStarted();
	void onGPXParseCompleted(GPXDocument document);
	void onGPXParseError(String type, String message, int lineNumber, int columnNumber);
}
```
The *GPXParserProgressListener* on the other hand is only required if you need a fine degree of feedback on what the parser is currently doing in the background. This listener should be used only if necessary as it implies implementing a lot of methods.
```java
interface GPXParserProgressListener {
  void onGPXNewTrackParsed(int count, GPXTrack track);
  void onGPXNewRouteParsed(int count, GPXRoute track);
  void onGPXNewSegmentParsed(int count, GPXSegment segment);
  void onGPXNewTrackPointParsed(int count, GPXTrackPoint trackPoint);
  void onGPXNewRoutePointParsed(int count, GPXRoutePoint routePoint);
  void onGPXNewWayPointParsed(int count, GPXWayPoint wayPoint);
}
```
As you can see the *GPXParserProgressListener* can be used to get a callback on every GPX entity that is being parsed.

Print GPX
----------------
GPX document can be printed to file with
```
mPrinter = new GPXFilePrinter(mPrinterListener);
mPrinter.print(mDocument,
Environment.getExternalStorageDirectory() + "/output.gpx");
```
The print can be stopped with:
```
mPrinter.cancelPrint();
```
The print will complete but your listener will not be called again.
This will be improved.

Internal GPX representation
---------------------------

As you may have noticed this library provides a set of objects to represent different GPX entities

- *GPXDocument* Top level container, represents the GPX file
- *GPXTrack* GPX **trk** entity
- *GPXRoute* GPX **rte** entity
- *GPXSegment* GPX **trkseg** entity
- *GPXTrackPoint* GPX **trkpt** entity
- *GPXRoutePoint* GPX **rtept** entity
- *GPXWayPoint* GPX **wpt** entity

Please read the code for more details on how these classes work.



 
