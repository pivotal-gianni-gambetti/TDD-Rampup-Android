package com.pivotallabs.yellowpages.adapters;

import com.pivotallabs.yellowpages.R;
import com.pivotallabs.yellowpages.api.YellowPagesApi;
import com.pivotallabs.yellowpages.api.YellowPagesApi.Listing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SearchResultAdapter extends ArrayAdapter<YellowPagesApi.Listing> {

	public SearchResultAdapter(Context context) {
		super(context, R.layout.activity_search_row, -1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder holder = null;
		
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.activity_search_row, null);
			
			holder = new ViewHolder();
			holder.businessName = (TextView) convertView.findViewById(R.id.business_title);
			
			convertView.setTag(holder);
			
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		Listing listing = getItem(position);
		
		holder.businessName.setText(listing.name);
		
		return convertView;
	}
	
	static class ViewHolder{
		TextView businessName;
	}
	
}
