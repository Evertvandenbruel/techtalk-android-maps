package com.gpxrecorder.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import com.gpxrecorder.R;
import com.gpxrecorder.model.Route;

public class RouteAdapter extends ArrayAdapter<Route> {

	public RouteAdapter(Activity context, ArrayList<Route> items) {
		super(context, R.layout.route_item, items);
	}

	static class ViewHolder {
		public CheckedTextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		View rowView = convertView;
		if (rowView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			rowView = inflater.inflate(R.layout.route_item, null, true);
			holder = new ViewHolder();
			holder.textView = (CheckedTextView) rowView
					.findViewById(R.id.checkedTextView);
			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}
		Route route = (Route) getItem(position);

		holder.textView.setText(route.getName());
		return rowView;
	}
}
