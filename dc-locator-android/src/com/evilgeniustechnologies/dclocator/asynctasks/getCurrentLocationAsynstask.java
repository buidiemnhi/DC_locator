package com.evilgeniustechnologies.dclocator.asynctasks;

import com.parse.LocationCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;

import com.evilgeniustechnologies.dclocator.commons.Config;

import android.location.Criteria;
import android.os.AsyncTask;

public class getCurrentLocationAsynstask extends AsyncTask<Void, Void, Void>{
	 Criteria criteria = new Criteria();
	
	public getCurrentLocationAsynstask() {
		criteria.setAccuracy(Criteria.ACCURACY_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Void doInBackground(Void... arg0) {
		ParseGeoPoint.getCurrentLocationInBackground(35000, criteria,
				new LocationCallback() {

					@Override
					public void done(ParseGeoPoint geopoint, ParseException e) {
						// TODO Auto-generated method stub
						if (e == null) {
							Config.lat = geopoint.getLatitude();
							Config.lng = geopoint.getLongitude();

						}
					}
				});
		return null;
		// TODO Auto-generated method stub
		
	
	}

}
