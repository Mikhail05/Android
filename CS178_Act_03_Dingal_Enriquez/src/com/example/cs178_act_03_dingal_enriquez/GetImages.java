package com.example.cs178_act_03_dingal_enriquez;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

class GetImages extends AsyncTask<ImageView, Void, Drawable> {
	  	MainActivity main = new MainActivity();
	    Drawable drawable;
	    ImageView imageView;
	    String URL;
	   

	     protected Drawable doInBackground(ImageView... args) {

	    	 imageView=args[0];
	         URL = (String) args[0].getTag();    
	    	 drawable = main.storeImage(URL, "image.jpg");
	    	 
	         return drawable;
	     }

	     protected void onPostExecute(Drawable m_bitmap) {
	         if(m_bitmap != null)
	         {
	        	 imageView.setImageDrawable(m_bitmap);
	         } 
	     }

		
}