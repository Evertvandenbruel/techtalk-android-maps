package com.gpxrecorder.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.gpxrecorder.database.DatabaseHelper;
import com.gpxrecorder.helpers.LocationHelper;
import com.gpxrecorder.model.Route;

public class LocationService extends Service {
	private LocationManager mLocationManager = null;
	private LocationListener mLocationListener;
	private DatabaseHelper mDatabaseHelper;
	private int mRouteId;

	private class LocationListener implements android.location.LocationListener {
		Location mLastLocation;

		public LocationListener(String provider) {
			mLastLocation = new Location(provider);
		}

		@Override
		public void onLocationChanged(Location location) {
			mLastLocation.set(location);
			Intent broadcastIntent = new Intent();
			broadcastIntent.putExtra(LocationHelper.LOCATION_OBJECT, location);
			broadcastIntent.setAction(LocationHelper.LOCATION_UPDATE);
			sendBroadcast(broadcastIntent);
			mDatabaseHelper.createLocation(location.getLatitude(),
					location.getLongitude(), location.getAltitude(),
					location.getSpeed(), location.getTime(), mRouteId);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		mRouteId = intent.getExtras().getInt(Route.ROUTE_ID);
		return START_STICKY;
	}

	@Override
	public void onCreate() {
		initializeLocationManager();

		mDatabaseHelper = new DatabaseHelper(getApplicationContext());

		mLocationListener = new LocationListener(LocationManager.GPS_PROVIDER);
		mLocationManager.requestLocationUpdates(
				LocationHelper.getBestProvider(mLocationManager), 5000, 0,
				mLocationListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mLocationManager != null) {
			mLocationManager.removeUpdates(mLocationListener);
		}
	}

	private void initializeLocationManager() {
		if (mLocationManager == null) {
			mLocationManager = (LocationManager) getApplicationContext()
					.getSystemService(Context.LOCATION_SERVICE);
			Log.d("test", mLocationManager.toString());
		}
	}
}