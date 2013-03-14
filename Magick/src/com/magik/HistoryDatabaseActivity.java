package com.magik;


import java.io.File;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HistoryDatabaseActivity extends ListActivity {
  private DataSource datasource;
  public static HistoryAdapter historyAdapter;
  public static List<Download> historyDownloads;
  private String[] fileName;
  
  @Override
  public void onCreate(Bundle savedInstanceState) {
	  
    super.onCreate(savedInstanceState);
    setContentView(R.layout.history_layout);
    
    datasource = ((DatabaseData) getApplication()).getDataBaseData();
    datasource.open();

    HistoryDatabaseActivity.historyDownloads = datasource.getAllDownloads();

    HistoryDatabaseActivity.historyAdapter = new HistoryAdapter(this, HistoryDatabaseActivity.historyDownloads);
	
	ListView lv = getListView();
    lv.addHeaderView(getLayoutInflater().inflate(R.layout.history_layout, null));
    lv.setAdapter(historyAdapter);
    
    setListAdapter(historyAdapter);
    
  } 
 
  @Override
  protected void onListItemClick(ListView l, View v, final int position, long id) {
  	super.onListItemClick(l, v, position, id);
  
  	fileName = l.getItemAtPosition(position).toString().split("/");
  	File history = new File(Environment.getExternalStorageDirectory() + "/Downloads/" + fileName[fileName.length-1]);
  	if(history.exists())
  	{
  		Intent i = new Intent();
  		File videoFile2Play2 = new File(Environment.getExternalStorageDirectory() + "/Downloads/test.mp4");
  		i.setDataAndType(Uri.fromFile(videoFile2Play2), "video/*");
  		startActivity(i);
 
  		Context context = getApplicationContext();
  		Toast.makeText(context,Environment.getExternalStorageDirectory() + "/Downloads/test.mp4", Toast.LENGTH_SHORT).show();
  	}
  	else{
  		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
  		alert.setTitle("Warning! The file no longer exists.");
  		alert.setMessage("Do you want to delete this data?");
  		alert.setIcon(android.R.drawable.ic_dialog_alert);
  		alert.setNegativeButton("Cancel",null);
  		alert.setPositiveButton("OK", new OnClickListener() {

  			public void onClick(DialogInterface dialog, int which) {
  				
  				@SuppressWarnings("unchecked")
 				 ArrayAdapter<Download> adapter = (ArrayAdapter<Download>) getListAdapter();
  				 historyDownloads.remove(position-1);		 
  				 adapter.notifyDataSetChanged();
  				 
  				if(getListAdapter().getCount() >0)
  				{
  				Download download = null;
  				
  					download = (Download) getListAdapter().getItem(position-1);
  				
  		        datasource.deleteDownload(download);
  		        datasource.close();
  				}
  			}
  		});
  		alert.show();
  		
  	}
  }

  @Override
  protected void onResume() {
    datasource.open();
    super.onResume();
  }

  @Override
  protected void onPause() {
    datasource.close();
    super.onPause();
  }

} 