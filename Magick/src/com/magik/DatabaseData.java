package com.magik;

import android.app.Application;

public class DatabaseData extends Application{
	private DataSource dataSource = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		setDataBaseData(new DataSource(this));
	}
	
	public DataSource getDataBaseData() {
	    return dataSource;
	}

	public void setDataBaseData(DataSource dataSource) {
	    this.dataSource = dataSource;
	}
}
