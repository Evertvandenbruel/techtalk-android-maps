package com.gpxrecorder;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;

import com.gpxrecorder.database.DatabaseHelper;
import com.gpxrecorder.helpers.DateHelper;
import com.gpxrecorder.helpers.Helper;
import com.gpxrecorder.helpers.LocationHelper;
import com.gpxrecorder.model.Route;
import com.gpxrecorder.services.LocationService;

public class RecordActivity extends Activity {

	private Button mStart;
	private Button mStop;
	private Route mRoute;
	private BroadcastReceiver mBroadcastReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		mStart = (Button) findViewById(R.id.button_start);
		mStop = (Button) findViewById(R.id.button_stop);
		mStop.setEnabled(false);
		mStart.setEnabled(false);
		mStop.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mStop.setEnabled(false);
				mStart.setEnabled(true);
				stopTracking();
				mRoute = null;
				return true;
			}
		});

		Button routes = (Button) findViewById(R.id.button_routes);
		routes.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Intent routesActivity = new Intent(RecordActivity.this,
						RoutesActivity.class);
				RecordActivity.this.startActivity(routesActivity);
				return true;
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateStartButton(Helper.isMyServiceRunning(getApplicationContext(),
				LocationService.class));

		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(LocationHelper.LOCATION_UPDATE);

		mBroadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				Location location = intent.getExtras().getParcelable(
						LocationHelper.LOCATION_OBJECT);
			}
		};

		this.registerReceiver(mBroadcastReceiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(mBroadcastReceiver);
	}

	private void updateStartButton(final Boolean running) {

		if (running) {
			mStop.setEnabled(true);
			mStart.setEnabled(false);
		} else {
			mStop.setEnabled(false);
			mStart.setEnabled(true);
		}
		setStartButtonEvent();
	}

	private void setStartButtonEvent() {
		mStart.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mStart.setEnabled(false);
					DatabaseHelper databaseHelper = new DatabaseHelper(
							getApplicationContext());
					mRoute = databaseHelper.createRoute(DateHelper
							.getFormatedStringFromMilliSeconds(
									System.currentTimeMillis(),
									"yyyy-MM-dd hh:mm:ss"));
					startTracking(mRoute.getId());
				
				return true;
			}
		});
	}

	private void startTracking(int routeId) {
		Intent startService = new Intent(RecordActivity.this,
				LocationService.class);
		startService.putExtra(Route.ROUTE_ID, routeId);
		RecordActivity.this.startService(startService);
		updateStartButton(true);
	}

	private void stopTracking() {
		Intent stopService = new Intent(RecordActivity.this,
				LocationService.class);
		RecordActivity.this.stopService(stopService);
		updateStartButton(false);
	}

}
