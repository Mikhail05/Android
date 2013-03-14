package com.magik;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { 
		  MySQLiteHelper.COLUMN_ID,
		  MySQLiteHelper.COLUMN_FILENAME, 
		  MySQLiteHelper.COLUMN_URL };

  public DataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Download createDownload(String filename, String url) {
    ContentValues values = new ContentValues();
    values.put(MySQLiteHelper.COLUMN_FILENAME, filename);
    values.put(MySQLiteHelper.COLUMN_URL, url);
    
    long insertId = database.insert(MySQLiteHelper.TABLE_DOWNLOADS, null,
        values);
    
    Cursor cursor = database.query(MySQLiteHelper.TABLE_DOWNLOADS,
        allColumns, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
        null, null, null);
    cursor.moveToFirst();
    Download newDownload = cursorToDownload(cursor);
    cursor.close();
    return newDownload;
  }
  
  public Cursor getStationCursor(String url) {
	  Cursor result = null;
	
//	  if (database == null)
//	  {
//	   this.open();
//	   
//	  }
	  
	  if (database!=null)
	  { 
		 
	   result = database.query(MySQLiteHelper.TABLE_DOWNLOADS,
		        allColumns, MySQLiteHelper.COLUMN_URL + " LIKE '%" + url + "%' ", null,
		        null, null, null);
	   //this.close();
	  }
	  return result;
	  }

  public void deleteDownload(Download comment) {
    long id = comment.getId();
    System.out.println("Comment deleted with id: " + id);
    database.delete(MySQLiteHelper.TABLE_DOWNLOADS, MySQLiteHelper.COLUMN_ID
        + " = " + id, null);
  }

  public List<Download> getAllDownloads() {
    List<Download> downloads = new ArrayList<Download>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_DOWNLOADS,
        allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Download download = cursorToDownload(cursor);
      downloads.add(download);
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return downloads;
  }

  private Download cursorToDownload(Cursor cursor) {
	Download download = new Download();
	download.setId(cursor.getLong(0));
	download.setFilename(cursor.getString(1));
	download.setURL(cursor.getString(2));
    return download;
  }
} 
