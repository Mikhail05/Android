package com.magik;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DownloadsAdapter extends ArrayAdapter<String> {
		private Context con = null;
		private List<String> links = null;
		
		public DownloadsAdapter(Context context, List<String> links) {
			super(context,R.layout.downloads_layout, links);
			this.con = context;
			this.links = links;
		}
		
		@Override
		public View getView(int position, View v, ViewGroup parent){
			LayoutInflater inflater = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = inflater.inflate(R.layout.downloads_layout, parent, false);
			TextView url = (TextView) view.findViewById(R.id.queueName);
			
			String[] separated = links.get(position).split("/");
			url.setText(separated[separated.length-1]);

			return view;
		}

}
