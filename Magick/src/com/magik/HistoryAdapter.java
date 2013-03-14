package com.magik;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HistoryAdapter extends ArrayAdapter<Download> {
	private Context con = null;
	private List<Download> downloads = null;
	
	public HistoryAdapter(Context context, List<Download> downloads) {
		super(context,R.layout.list_view, downloads);
		this.con = context;
		this.downloads = downloads;
	}
	
	@Override
	public View getView(int position, View v, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.list_view, parent, false);
		TextView filename = (TextView) view.findViewById(R.id.name);
		TextView url = (TextView) view.findViewById(R.id.link);
		
		filename.setText(downloads.get(position).getFilename());
		url.setText(downloads.get(position).getURL());
		return view;
	}

}