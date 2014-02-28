package com.tddrampup.yellowpages.adapters;

import roboguice.inject.ContextSingleton;
import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.inject.Inject;
import com.tddrampup.R;
import com.tddrampup.yellowpages.api.YellowPagesApi;
import com.tddrampup.yellowpages.api.YellowPagesApi.Listing;

@ContextSingleton
public class SearchResultAdapter extends ArrayAdapter<YellowPagesApi.Listing> {

	Location currentBestLocation;

	@Inject
	public SearchResultAdapter(Context context) {
		super(context, R.layout.activity_search_row, -1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate(R.layout.activity_search_row, null);

			holder = new ViewHolder();
			holder.businessName = (TextView) convertView
					.findViewById(R.id.business_title);
			holder.distance = (TextView) convertView
					.findViewById(R.id.distance);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Listing listing = getItem(position);

		String distance = "(unknown)";

		if (currentBestLocation != null && listing.geoCode != null
				&& listing.geoCode.convertToLocation() != null) {

			distance = String.format("%.2f km", currentBestLocation
					.distanceTo(listing.geoCode.convertToLocation()) / 1000.0);

		}

		holder.businessName.setText(listing.name);
		holder.distance.setText(distance);

		return convertView;
	}

	public void setCurrentBestLocation(Location location) {
		currentBestLocation = location;
	}

	static class ViewHolder {
		TextView businessName;
		TextView distance;
	}
}
