package com.magik;

public class Download {
	  private long id;
	  private String filename, url;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getFilename() {
	    return filename;
	  }

	  public void setFilename(String filename) {
	    this.filename = filename;
	  }
	  
	  public String getURL() {
		    return url;
	  }

	  public void setURL(String url) {
		    this.url = url;
	  }

	  // Will be used by the ArrayAdapter in the ListView
	  @Override
	  public String toString() {
	    return url;
	  }
	} 
