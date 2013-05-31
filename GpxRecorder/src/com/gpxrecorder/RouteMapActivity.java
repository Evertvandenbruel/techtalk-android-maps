package com.gpxrecorder;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.gpxrecorder.database.DatabaseHelper;
import com.gpxrecorder.model.LocationStamp;
import com.gpxrecorder.model.Route;

public class RouteMapActivity extends Activity {

	private MapFragment mMap;
	private Route mRoute;

	static final LatLng LIMBURG = new LatLng(50.916667, 5.333333);
	static final LatLng LOUVAIN = new LatLng(50.883333, 4.7);
	static final LatLng GHENT = new LatLng(51.05, 3.733333);
	static final LatLng BRUGES = new LatLng(51.216667, 3.233333);
	
	private LatLng[] mLocations = {LIMBURG, LOUVAIN, GHENT, BRUGES};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_map);

		int routeId = getIntent().getExtras().getInt("id");
		DatabaseHelper databaseHelper = new DatabaseHelper(
				getApplicationContext());
		mRoute = databaseHelper.getRoute(routeId);

		mMap = (MapFragment) getFragmentManager().findFragmentById(R.id.map);

		LatLng first = null;
		LatLng second = null;
		
		for (LatLng loc : mLocations) {
			mMap.getMap().addMarker(new MarkerOptions()
	        .position(loc)
	        .icon(BitmapDescriptorFactory
	            .fromResource(R.drawable.parking)));
		}
		 
		ArrayList<LocationStamp> locations = databaseHelper
				.getLocationStamps(mRoute.getId());
		if (locations.size() > 1) {
			final LatLngBounds.Builder bld = new LatLngBounds.Builder();

			for (LocationStamp locationStamp : locations) {
				bld.include(new LatLng(locationStamp.getLatitude(),
						locationStamp.getLongitude()));
				if (first == null) {
					first = new LatLng(locationStamp.getLatitude(),
							locationStamp.getLongitude());
				} else {
					second = first;
					first = new LatLng(locationStamp.getLatitude(),
							locationStamp.getLongitude());
					mMap.getMap().addPolyline(
							new PolylineOptions().add(first, second).width(3)
									.color(Color.BLUE));
				}
			}

			do {
			} while (mMap.getMap() == null);

			mMap.getMap().setOnCameraChangeListener(
					new OnCameraChangeListener() {

						@Override
						public void onCameraChange(CameraPosition arg0) {
							mMap.getMap().moveCamera(
									CameraUpdateFactory.newLatLngBounds(
											bld.build(), 70));
							mMap.getMap().setOnCameraChangeListener(null);
						}
					});
		}
	}
}
