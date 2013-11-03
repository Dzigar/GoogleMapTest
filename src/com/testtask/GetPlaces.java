package com.testtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GetPlaces extends AsyncTask<String, Void, String> {

	private GoogleMap map;

	public GetPlaces(GoogleMap map) {
		this.map = map;
	}

	public static String GET(String url) {
		InputStream inputStream = null;
		String result = "";
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));
			inputStream = httpResponse.getEntity().getContent();
			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";
		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

	private static String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

	public boolean isConnected() {
		ConnectivityManager connMgr = (ConnectivityManager) MainActivity
				.getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
			return true;
		else
			return false;
	}

	protected String getASCIIContentFromEntity(HttpEntity entity)
			throws IllegalStateException, IOException {
		InputStream in = entity.getContent();
		StringBuffer out = new StringBuffer();
		int n = 1;
		while (n > 0) {
			byte[] b = new byte[4096];
			n = in.read(b);
			if (n > 0)
				out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	@Override
	protected String doInBackground(String... url) {
		return GET(url[0]);
	}

	protected void onPostExecute(String jsonLine) {

		try {
			JSONObject json = new JSONObject(jsonLine);
			JSONArray venues = json.getJSONObject("response")
					.getJSONArray("groups").getJSONObject(0)
					.getJSONArray("items");

			for (int j = 0; j < venues.length(); j++) {
				JSONObject jsonObject = (JSONObject) venues.getJSONObject(j)
						.getJSONObject("venue");
				map.addMarker(new MarkerOptions()
						.position(
								new LatLng((Double) jsonObject.getJSONObject(
										"location").get("lat"),
										(Double) jsonObject.getJSONObject(
												"location").get("lng")))
						.title(jsonObject.get("name").toString())
						.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.ic_location_marker)));

			}
			
		} catch (Exception e) {
			e.getLocalizedMessage();
		}

	}
}
