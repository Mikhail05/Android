package com.magik;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class DownloadsActivity extends ListActivity {
	
	public static String url, path;
	
	public static String[] separated;
	private float storage[];
	private int position;
	public static int mProgressStatus = 0;
	public static boolean activeThread = false, isPaused = false;
	
	private Button btnStart;
	public static Button btnPause, btnCancel;
	public static ProgressBar mProgress;
	public static TextView percent, fsize, fileName, progress_listview;
	public static boolean isDownloading;
	
	private static DownloadsAdapter downloadsAdapter;
	
	public static List<String> links = new ArrayList<String>();
	
	private DataSource database;
	private StationAdapter mCursorAdapter;
	private Cursor mItemCursor;
	private MySQLiteHelper SQLite;
	public static AutoCompleteTextView inputURL;
	private Download download;
	
	@Override
	protected void onResume() {
		database.open();
		super.onResume();
	}
	
	@Override
	  protected void onPause() {
	    database.close();
	    super.onPause();
	}

	@SuppressWarnings("deprecation")
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		storage = new float[100];
		downloadsAdapter = new DownloadsAdapter(this, links);
		download  = new Download();	
		ListView lv = getListView();
        lv.addHeaderView(getLayoutInflater().inflate(R.layout.input_layout, null));
	    lv.setAdapter(downloadsAdapter);
	      
		btnPause = (Button) findViewById(R.id.btnPause);
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnStart = (Button) findViewById(R.id.btnStart);
		fileName = (TextView) findViewById(R.id.fileName);
		fsize = (TextView) findViewById(R.id.fileSize);
		percent = (TextView) findViewById(R.id.percent);
		mProgress = (ProgressBar) findViewById(R.id.progressBar1);
		
		position = 0;
		
		database = ((DatabaseData) getApplication()).getDataBaseData(); 
		database.open();
		
        mItemCursor = database.getStationCursor("");      
	    startManagingCursor(mItemCursor);	        
	    mCursorAdapter = new StationAdapter(getApplicationContext(), mItemCursor);
	        
	    DownloadsActivity.inputURL = (AutoCompleteTextView) findViewById(R.id.filter);
	    DownloadsActivity.inputURL.setAdapter(mCursorAdapter);
	    DownloadsActivity.inputURL.setThreshold(1);  
	    DownloadsActivity.inputURL.setDropDownBackgroundResource(R.drawable.background);    
	    DownloadsActivity.inputURL.setOnItemClickListener(new OnItemClickListener() {
        
       	 @SuppressWarnings("static-access")
		public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
       		 
       		Cursor cursor = (Cursor) adapter.getAdapter().getItem(position); 
       		DownloadsActivity.inputURL.setText(cursor.getString(cursor.getColumnIndex(SQLite.COLUMN_URL)));  		

            }
        });	
	    
	    
        
		btnCancel.setOnClickListener(new OnClickListener() {

	        public void onClick(View arg0) {
	        	Context context = getApplicationContext();
	         	Toast.makeText(context, "Download Cancelled!", Toast.LENGTH_SHORT).show();
	         	
	         	MySQLiteHelper sqlite = new MySQLiteHelper(DownloadsActivity.this);	
	         	SQLiteDatabase db;
	            db = sqlite.getWritableDatabase();				
	            db.delete(MySQLiteHelper.TABLE_DOWNLOADS, 
	            		MySQLiteHelper.COLUMN_ID + " = " + download.getId(), 
	            		null);
	            HistoryDatabaseActivity.historyDownloads.remove(HistoryDatabaseActivity.historyDownloads.size()-1);	
	            HistoryDatabaseActivity.historyAdapter.notifyDataSetChanged();
	            db.close();
	            
	    		activeThread = false;
	    		DownloadFileFromURL.downloaded = 0;
	    		File filePath = new File(DownloadsActivity.path);
	    		filePath.delete();
	    		
	    		if(isDownloading)
	    		{
	    			activeThread = true;
	    			isDownloading = false;    			
	    			try{
	    				DownloadFileFromURL.output.close();
	    			} catch(IOException e){
	    				Log.e("Error: ", e.getMessage());
	    			}	    			
	    		}
	    		
	    		downloadVisibility(false);
	    			
	    		if(links.size() != 0)
	    		{	    			
	    			DownloadsActivity.url = links.get(position);		
	    			links.remove(position);
	    			downloadsAdapter.notifyDataSetChanged();
	    		
	    			separated = DownloadsActivity.url.split("/");
				 
	    			fileName.setText(separated[separated.length-1]);
	    			DownloadsActivity.path = Environment.getExternalStorageDirectory()
						.toString() + "/Downloads/" + separated[separated.length-1];
	    			downloadVisibility(true); 
	    			new DownloadFileFromURL().execute(DownloadsActivity.inputURL.getText().toString());
	    		}
	    		
	    		
	        }
	    });
	
	btnPause.setOnClickListener(new OnClickListener() {
        public void onClick(View arg0) {
        	isPaused = true;
        	Context context = getApplicationContext();
         	Toast.makeText(context, "Download Paused!", Toast.LENGTH_SHORT).show();
         	DownloadsActivity.isDownloading = false;
         	
        	try{
        		 DownloadFileFromURL.output.close();
        	} catch(IOException e){
        		Log.e("Error: ", e.getMessage());
        	}
    
        	btnStart.setVisibility(View.VISIBLE);
     		btnPause.setVisibility(View.INVISIBLE);
        }
    });

	btnStart.setOnClickListener(new OnClickListener() {

        public void onClick(View arg0) {
        	isPaused = false;
        	Context context = getApplicationContext();
         	Toast.makeText(context, "Download Resumed!", Toast.LENGTH_SHORT).show();
        	new DownloadFileFromURL().execute(DownloadsActivity.inputURL.getText().toString());
        	          	       	
        	btnStart.setVisibility(View.INVISIBLE);
        	btnPause.setVisibility(View.VISIBLE);
        }
    });
}
	
	
	 @Override
	  protected void onListItemClick(ListView l, View v, final int position, long id) {
	  	super.onListItemClick(l, v, position, id);	  		  	
	  	TextView queueLabel=(TextView)v.findViewById(R.id.queueLabel);
	  	TextView queueName=(TextView)v.findViewById(R.id.queueName);
	  	float temp = DownloadFileFromURL.downloaded;
	  	DownloadFileFromURL.downloaded = storage[position-1];
	  	Log.d("Storage: " + position, storage[position-1] + "");
	  	storage[position-1] = temp;
	  	String []split = l.getItemAtPosition(position).toString().split("/");
		
		fileName.setText(split[split.length-1]);
		
		DownloadsActivity.path = Environment.getExternalStorageDirectory().toString() + "/Downloads/" +  separated[separated.length-1];	
	  	new DownloadFileFromURL().execute(l.getItemAtPosition(position).toString());
	  	queueLabel.setText(DownloadsActivity.mProgressStatus + "% Complete");
	  	
	  	queueName.setText(separated[separated.length-1]);
	  	Log.d("Position: ",  "" + position);
	 }
	 
	public void queueDownload(View v) {
		DownloadsActivity.url = DownloadsActivity.inputURL.getText().toString();
		 
	  Toast.makeText(getApplicationContext(), "content: "+ url , Toast.LENGTH_LONG).show();		

	  	if(activeThread && (links.size() >= 0) ) {
			links.add(DownloadsActivity.url);
			downloadsAdapter.notifyDataSetChanged();
		

			 download = database.createDownload(separated[separated.length-1], DownloadsActivity.url);		
			  HistoryDatabaseActivity.historyDownloads.add(download);	
			  //HistoryDatabaseActivity.historyAdapter.notifyDataSetChanged();
				

		}else if(links.size() == 0)
			DownloadsActivity.url = DownloadsActivity.inputURL.getText().toString();
		
		if(!activeThread) {		
			activeThread = true;
			if(links.size() > 0)
				DownloadsActivity.url = links.get(position);
			else
				DownloadsActivity.url = DownloadsActivity.inputURL.getText().toString();
			
			separated = DownloadsActivity.url.split("/");
			
			fileName.setText(separated[separated.length-1]);
			
			DownloadsActivity.path = Environment.getExternalStorageDirectory().toString() + "/Downloads" +  separated[separated.length-1];		
			
			downloadVisibility(true);
    			  	
			 download = database.createDownload(separated[separated.length-1], DownloadsActivity.url);			  
			 
			new DownloadFileFromURL().execute(DownloadsActivity.url);
		}
	}	
	public static void downloadVisibility(boolean makeVisible){
		DownloadsActivity.downloadsAdapter.notifyDataSetChanged();
		if(makeVisible){
			btnPause.setVisibility(View.VISIBLE);
			btnCancel.setVisibility(View.VISIBLE);
			fileName.setVisibility(View.VISIBLE); 
    		fsize.setVisibility(View.VISIBLE); 
    		percent.setVisibility(View.VISIBLE); 
    		mProgress.setVisibility(View.VISIBLE); 
		}else{
			btnPause.setVisibility(View.INVISIBLE);	
    		btnCancel.setVisibility(View.INVISIBLE); 
    		fileName.setVisibility(View.INVISIBLE); 
    		fsize.setVisibility(View.INVISIBLE); 
    		percent.setVisibility(View.INVISIBLE);
    		mProgress.setVisibility(View.INVISIBLE); 
		
		}
	}
}