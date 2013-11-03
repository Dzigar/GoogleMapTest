package com.testtask;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends Activity {

	public static final String CLIENT_ID = "CEL2F1ZQKCYH3VIJL0URV01AE0AUZ12YTK4PFBSSRIBLEBE5";
	public static final String CLIENT_SECRET = "KZ0H0FLVDVI1M0MBSJH0IBEO0X2AYBSVW455SQNVQMJ54ZB4";
	public static String CATEGORY = "arts";
	public static String URL = "https://api.foursquare.com/v2/venues/explore?client_id="
			+ CLIENT_ID
			+ "&client_secret="
			+ CLIENT_SECRET
			+ "&v=20130815&ll=49.13,28.28&query=" + CATEGORY;

	public LatLng MY_TOWN = new LatLng(49.23259183832939, 28.46832275390625);

	public static List<Place> places = new ArrayList<Place>();

	private static MainActivity activity;
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		activity = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		new GetPlaces(map).execute(URL);
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(MY_TOWN, 12));
		map.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static MainActivity getActivity() {
		return activity;
	}
}
