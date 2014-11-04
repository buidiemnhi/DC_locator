package com.evilgeniustechnologies.dclocator.asynctasks;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;


class MatchingNearByLocationTask extends
AsyncTask<Void, Void, Void> 
{
Activity activity;
ProgressDialog progressDialog;
public MatchingNearByLocationTask(Activity activity) {
		super();
		this.activity= activity;
		// TODO Auto-generated constructor stub
	}

@Override
protected void onPreExecute() {
super.onPreExecute();
// Showing progress dialog

progressDialog = new ProgressDialog(activity);
progressDialog.setMessage("Loading...");
progressDialog.setCancelable(true);
progressDialog.show();

}

@Override
protected Void doInBackground(Void... arg0) {

String jsonStr = getLocationInfo(com.evilgeniustechnologies.dclocator.commons.Config.lat, com.evilgeniustechnologies.dclocator.commons.Config.lng).toString();

if (jsonStr != null) {
    Log.i("location--??", jsonStr);

    JSONObject jsonObj;
    try {
        jsonObj = new JSONObject(jsonStr);

        String Status = jsonObj.getString("status");
        if (Status.equalsIgnoreCase("OK")) {
            JSONArray Results = jsonObj.getJSONArray("results");
            JSONObject zero = Results.getJSONObject(0);
            JSONArray address_components = zero
                    .getJSONArray("address_components");

            for (int i = 0; i < address_components.length(); i++) {
                JSONObject zero2 = address_components
                        .getJSONObject(i);
                String long_name = zero2.getString("long_name");
                JSONArray mtypes = zero2.getJSONArray("types");
                String Type = mtypes.getString(0);
                if (Type.equalsIgnoreCase("administrative_area_level_2")) {
                    // Address2 = Address2 + long_name + ", ";
                    String City = long_name;
                    Log.d(" CityName --->", City + "");
                }
            }
        }

    }

    catch (JSONException e) {

        e.printStackTrace();
    }

}

return null;
}

@Override
protected void onPostExecute(Void result) {
super.onPostExecute(result);
// Dismiss the progress dialog
if (progressDialog.isShowing()) {
    progressDialog.dismiss();
    
}

}

@Override
protected void onCancelled() {

super.onCancelled();
progressDialog.dismiss();

}






private JSONObject getLocationInfo(double lat, double lng) {

HttpGet httpGet = new HttpGet(
    "http://maps.googleapis.com/maps/api/geocode/json?latlng="
            + lat + "," + lng + "&sensor=false");
HttpClient client = new DefaultHttpClient();
HttpResponse response;
StringBuilder stringBuilder = new StringBuilder();

try {
response = client.execute(httpGet);
HttpEntity entity = response.getEntity();
InputStream stream = entity.getContent();
int b;
while ((b = stream.read()) != -1) {
    stringBuilder.append((char) b);
}
} catch (ClientProtocolException e) {
} catch (IOException e) {
}

JSONObject jsonObject = new JSONObject();
try {
jsonObject = new JSONObject(stringBuilder.toString());
} catch (JSONException e) {
e.printStackTrace();
}

return jsonObject;
}
}
