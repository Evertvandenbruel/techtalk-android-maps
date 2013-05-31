package com.gpxrecorder.helpers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class Helper {

	public static boolean isMyServiceRunning(Context context, Class<?> clz) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (clz.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}

}
