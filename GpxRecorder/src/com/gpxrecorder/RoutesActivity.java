package com.gpxrecorder;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.gpxrecorder.adapters.RouteAdapter;
import com.gpxrecorder.database.DatabaseHelper;
import com.gpxrecorder.model.Route;

public class RoutesActivity extends ListActivity {

	private ListView mRouteList;
	private ArrayList<Route> mRoutes;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_route_list);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		DatabaseHelper databaseHelper = new DatabaseHelper(
				getApplicationContext());
		mRoutes = databaseHelper.getAllRoutes();

		this.mRouteList = (ListView) findViewById(android.R.id.list);

		TextView emptyText = (TextView) findViewById(android.R.id.empty);
		emptyText.setText("leeg");

		final RouteAdapter adapter = new RouteAdapter(this, mRoutes);
		this.mRouteList.setAdapter(adapter);
		this.mRouteList.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent mapActivity = new Intent(RoutesActivity.this,
						RouteMapActivity.class);
				Route route = mRoutes.get(position);
				mapActivity.putExtra("id", route.getId());
				RoutesActivity.this.startActivity(mapActivity);
			}
		});
	}

}
