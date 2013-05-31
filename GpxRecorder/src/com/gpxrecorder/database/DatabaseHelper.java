package com.gpxrecorder.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gpxrecorder.model.LocationStamp;
import com.gpxrecorder.model.Route;

public class DatabaseHelper extends SQLiteOpenHelper {

	// database name
	private static final String dbName = "LocationDatabase";

	// tables
	private static final String locationTable = "Location";
	private static final String routeTable = "route";

	// id column used for every table
	private static final String idCol = "id";

	// location table columns
	private static final String latitudeCol = "latitude";
	private static final String longitudeCol = "longitude";
	private static final String altitudeCol = "altitude";
	private static final String speedCol = "speed";
	private static final String timeCol = "time";
	private static final String routeIdCol = "routeId";

	// route table columns
	private static final String nameCol = "name";

	public DatabaseHelper(Context context) {
		super(context, dbName, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + locationTable + "(" + idCol
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + latitudeCol
				+ " DOUBLE, " + longitudeCol + " DOUBLE, " + altitudeCol
				+ " DOUBLE, " + speedCol + " FLOAT, " + timeCol + " LONG, "
				+ routeIdCol + " INTEGER)");
		db.execSQL("CREATE TABLE " + routeTable + "(" + idCol
				+ " INTEGER PRIMARY KEY AUTOINCREMENT," + nameCol + " STRING )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void deleteWorkflow(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(locationTable, idCol + "=" + id, null);
		db.close();
	}

	public int createLocation(double latitude, double longitude,
			double altitude, float speed, long time, int routeId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(latitudeCol, latitude);
		cv.put(longitudeCol, longitude);
		cv.put(altitudeCol, altitude);
		cv.put(speedCol, speed);
		cv.put(timeCol, time);
		cv.put(routeIdCol, routeId);
		long id = db.insert(locationTable, null, cv);

		db.close();
		return ((int) id);
	}

	public Route getRoute(int id) {
		Cursor cursor;
		SQLiteDatabase db = this.getWritableDatabase();
		cursor = db.query(routeTable, new String[] { idCol, nameCol }, idCol
				+ "='" + id + "'", null, null, null, null);
		Route route = null;
		if (cursor.moveToFirst()) {
			do {
				route = new Route(cursor.getString(1), cursor.getInt(0));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return route;
	}

	public ArrayList<Route> getAllRoutes() {
		ArrayList<Route> routes = new ArrayList<Route>();
		Cursor cursor;
		SQLiteDatabase db = this.getWritableDatabase();
		cursor = db.query(routeTable, new String[] { idCol, nameCol }, null,
				null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				routes.add(new Route(cursor.getString(1), cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		return routes;
	}

	public Route createRoute(String name) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put(nameCol, name);
		long id = db.insert(routeTable, null, cv);

		db.close();
		return this.getRoute((int) id);
	}

	public ArrayList<LocationStamp> getLocationStamps(int routeId) {
		ArrayList<LocationStamp> locations = new ArrayList<LocationStamp>();
		Cursor cursor;
		LocationStamp locationStamp;
		SQLiteDatabase db = this.getWritableDatabase();
		double latitude;
		double longitude;
		double altitude;
		float speed;
		long time;
		int id;

		cursor = db.query(locationTable,
				new String[] { longitudeCol, latitudeCol, altitudeCol,
						speedCol, timeCol, idCol, routeIdCol }, routeIdCol
						+ " = " + routeId, null, null, null, null);

		if (cursor.moveToFirst()) {
			do {
				longitude = cursor.getDouble(0);
				latitude = cursor.getDouble(1);
				altitude = cursor.getDouble(2);
				speed = cursor.getFloat(3);
				time = cursor.getLong(4);
				id = cursor.getInt(5);
				locationStamp = new LocationStamp(id, longitude, latitude,
						altitude, speed, time, routeId);
				locations.add(locationStamp);
			} while (cursor.moveToNext());
		}
		db.close();
		return locations;
	}

}