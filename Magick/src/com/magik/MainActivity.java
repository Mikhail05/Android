package com.magik;

import com.magik.R;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;


@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        TabHost tabHost = getTabHost();
        
        
        TabSpec downloadSpec = tabHost.newTabSpec("Downloads");
        // setting Title and Icon for the Tab
        View downloadView = createTabView(tabHost.getContext(), "Downloads", R.drawable.download_icon);
        downloadSpec.setIndicator(downloadView);
        Intent downloadsIntent = new Intent(this, DownloadsActivity.class);
        downloadSpec.setContent(downloadsIntent);
        
        
        TabSpec historySpec = tabHost.newTabSpec("History");
        View historyView = createTabView(tabHost.getContext(), "History", R.drawable.history_icon);
        historySpec.setIndicator(historyView);
        Intent historyIntent = new Intent(this, HistoryDatabaseActivity.class);
        historySpec.setContent(historyIntent);
 
        
        TabSpec settingsSpec = tabHost.newTabSpec("Settings");
        View settingsView = createTabView(tabHost.getContext(), "Settings", R.drawable.settings_icon);
        settingsSpec.setIndicator(settingsView);
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        settingsSpec.setContent(settingsIntent);
 
 
        // Adding all TabSpec to TabHost
        tabHost.addTab(downloadSpec); // Adding photos tab
        tabHost.addTab(historySpec); // Adding songs tab
        tabHost.addTab(settingsSpec); // Adding videos tab
    }
    
    private static View createTabView(final Context context, final String text, final int iconId) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
		ImageView i = (ImageView) view.findViewById(R.id.tabsIcon);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		i.setBackgroundResource(iconId);
		tv.setText(text);
		return view;
	}
}
