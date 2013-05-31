package com.gpxrecorder.helpers;

import android.location.Criteria;
import android.location.LocationManager;

public class LocationHelper {

	public static final String LOCATION_OBJECT = "Location";
	public static final String LOCATION_UPDATE = "LocationUpdate";
	
	
	public static Boolean hasProvider(LocationManager locationManager) {
		if (locationManager != null
				&& !locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			return false;
		}
		return true;
	}

	public static String getBestProvider(LocationManager locationManager) {
		if (locationManager != null
				&& !locationManager
						.isProviderEnabled(LocationManager.GPS_PROVIDER)
				&& !locationManager
						.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			//TODO: throw error
		}
		Criteria criteria = new Criteria();
		criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
		criteria.setAccuracy(Criteria.NO_REQUIREMENT);
		String bestProvider = locationManager.getBestProvider(criteria, true);
		return bestProvider;
	}
}
