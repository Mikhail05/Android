package com.magik;

import com.magik.R;

import android.app.Activity;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
 
public class SettingsActivity extends Activity {
	private Button clearHistory;
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private DataSource datasource;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme);
        setContentView(R.layout.settings_layout);
        
        Context context = getApplicationContext();
        dbHelper = new MySQLiteHelper(context);
        open();
        datasource = new DataSource(context);
        datasource.open();
        clearHistory = (Button) findViewById(R.id.clear);
        clearHistory.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
            	
            	Context context = getApplicationContext();
             	Toast.makeText(context, "History Cleared!", Toast.LENGTH_SHORT).show();
             	database.delete(MySQLiteHelper.TABLE_DOWNLOADS, null, null);
             	
             	HistoryDatabaseActivity.historyAdapter.clear();
 	            HistoryDatabaseActivity.historyAdapter.notifyDataSetChanged();
            }
        });
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
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
      }

    public void close() {
        dbHelper.close();
      }
}    

