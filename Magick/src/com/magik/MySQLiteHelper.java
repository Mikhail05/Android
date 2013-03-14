package com.magik;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  public static final String TABLE_DOWNLOADS = "downloads";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_FILENAME = "filename";
  public static final String COLUMN_URL = "url";

  private static final String DATABASE_NAME = "download.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_DOWNLOADS + "(" + COLUMN_ID
      + " integer primary key autoincrement, "
      + COLUMN_FILENAME + " text not null,"
      + COLUMN_URL + " text not null);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOWNLOADS);
    onCreate(db);
  }

} 