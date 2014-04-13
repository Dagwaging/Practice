package com.hairforce.grouponalert;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hairforce.grouponalert.data.Deal;

public class DealAdapter extends BaseAdapter {
	private List<Deal> deals;
	private Activity activity;
	
	public DealAdapter(Activity activity) {
		this.activity = activity;
		
		this.deals = new ArrayList<Deal>();
	}
	
	@Override
	public int getCount() {
		return deals.size();
	}

	@Override
	public Deal getItem(int position) {
		return deals.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setData(List<Deal> deals) {
		this.deals = deals;
		
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null)
			convertView = activity.getLayoutInflater().inflate(R.layout.deal, null);
		
		TextView title = (TextView) convertView.findViewById(R.id.title);
		TextView value = (TextView) convertView.findViewById(R.id.value);
		TextView price = (TextView) convertView.findViewById(R.id.price);
		TextView address1 = (TextView) convertView.findViewById(R.id.address1);
		
		Deal deal = getItem(position);
		
		title.setText(deal.announcementTitle);
		value.setText(deal.options.get(0).value.formattedAmount);
		price.setText(deal.options.get(0).price.formattedAmount);
		address1.setText(deal.address + " (" + (int) deal.distance + "m)");
		
		value.setPaintFlags(value.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
		
		return convertView;
	}

}
