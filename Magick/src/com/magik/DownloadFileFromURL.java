package com.magik;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

class DownloadFileFromURL extends AsyncTask<String, String, String> {
	private URLConnection connection;
	private InputStream input;
	public static OutputStream output;

	public static String[] separated;
	private static int count = 0, oneMb = 1024*1024, numOfSegments= 10, position;
	public static float downloaded = 0; 
	private float lengthOfFile = 1;
	public static boolean complete = false;	
	
	/**
	 * Updating progress bar
	 * */
	protected void onProgressUpdate(String... progress) {
		
		// setting progress percentage	
		DownloadsActivity.mProgress.setProgress((int) ((DownloadFileFromURL.downloaded * 100)/Float.parseFloat(progress[0])));
		DownloadsActivity.mProgressStatus = (int)((DownloadFileFromURL.downloaded * 100)/Float.parseFloat(progress[0]));
		
		DownloadsActivity.percent.setText(DownloadsActivity.mProgressStatus + "% Complete");
		if(downloaded == lengthOfFile)
			DownloadsActivity.percent.setText("100% Complete");	
		
		String dlsize, totalSize;
		if(DownloadFileFromURL.downloaded/(1024) < 1000)
			dlsize = (int)(DownloadFileFromURL.downloaded/(1024)) + "kB";
		else dlsize = String.format("%.2f", DownloadFileFromURL.downloaded/(oneMb)) + "MB";
		if(lengthOfFile/(1024) < 1000)
			totalSize = (int)(lengthOfFile/(1024)) + "kB";
		else totalSize = String.format("%.2f",lengthOfFile/(oneMb))+"MB";
		DownloadsActivity.fsize.setText(dlsize + " of " + totalSize);
	}
	
		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {

			ExecutorService executor = Executors.newFixedThreadPool(numOfSegments);
			
			Runnable segment = new Runnable() { 
				
				public void run() {
							try {								
								URL url = new URL(DownloadsActivity.url);
				                connection = url.openConnection();
				                
				                File file = new File(DownloadsActivity.path);
				                file.delete();
				          
				                if(file.exists()){
				                	Log.d("I got here!", "YEAY!");
				                	connection.setRequestProperty("Range", "bytes=" + file.length() + "-"); 
				                	//downloaded = file.length();
				                }else {
				          
				                	connection.setRequestProperty("Range", "bytes=" + 0 + "-");	  
				                	lengthOfFile = connection.getContentLength();
				                	//downloaded = 0;
				                }
				                
				                connection.connect();
				                
				            	input = new BufferedInputStream(connection.getInputStream());

				                DownloadFileFromURL.output = new FileOutputStream(DownloadsActivity.path, true);
	
				                byte data[] = new byte[connection.getContentLength()];
				             
				                DownloadsActivity.isDownloading = true;
				                while ((count = input.read(data)) != -1 && downloaded < lengthOfFile && DownloadsActivity.isDownloading) {								
					                downloaded += count;

									// publishing the progress....
									// After this onProgressUpdate will be called
									publishProgress("" + lengthOfFile);
										
									// writing data to file
									DownloadFileFromURL.output.write(data, 0, count);									
								}		  

								// flushing output
				                DownloadFileFromURL.output.flush();
					
								// closing streams
				                DownloadFileFromURL.output.close();
								input.close();
															
							} catch (Exception e) {
								Log.e("Error: ", e.getMessage());
							}


				}
				
			};
			
			executor.execute(segment);
	        executor.shutdown();
	        while(downloaded != lengthOfFile && !executor.isTerminated()){
					
			}
			return null;
		}

		
		
		
		
		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {		
			if(downloaded == lengthOfFile) {
				//Context context = getApplicationContext();
				//Toast.makeText(context, "Download Complete!", Toast.LENGTH_SHORT).show();
				DownloadsActivity.downloadVisibility(false);
				downloaded = 0;
				DownloadsActivity.inputURL.setText("");
				
			}
			
			if(DownloadsActivity.links.size() == 0)
				DownloadsActivity.activeThread = false;

			if(!complete && (position < DownloadsActivity.links.size()))
			{
				complete = true;
				DownloadsActivity.url = DownloadsActivity.links.get(position);
				DownloadsActivity.links.remove(position);
				
				separated = DownloadsActivity.url.split("/");
				DownloadsActivity.fileName.setText(separated[separated.length-1]);
				DownloadsActivity.path = Environment.getExternalStorageDirectory()
						.toString() + "/" + separated[separated.length-1];		 
				  
				DownloadsActivity.downloadVisibility(true);
				new DownloadFileFromURL().execute(DownloadsActivity.url);
			}
		}

	}