package com.example.cs178_act_03_dingal_enriquez;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

public class MainActivity extends Activity {
	
	private String[] links = new String[]{
			"http://ww1.prweb.com/prfiles/2010/05/11/1751474/gI_TodoforiPadAppIcon512.png.jpg",
			"http://cdn4.iosnoops.com/wp-content/uploads/2011/08/Icon-Gmail_large-250x250.png",
			"http://kelpbeds.files.wordpress.com/2012/02/lens17430451_1294953222linkedin-icon.jpg?w=450",
			"http://snapknot.com/blog/wp-content/uploads/2010/03/facebook-icon-copy.jpg",
			"https://lh3.googleusercontent.com/-ycDGy_fZVZc/AAAAAAAAAAI/AAAAAAAAAAc/Q0MmjxPCOzk/s250-c-k/photo.jpg"
	};
	
	private int size = links.length;
	
	public ImageView[] images = new ImageView[size];
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        images[0]=(ImageView) findViewById(R.id.img1);
        images[1]=(ImageView) findViewById(R.id.img2);
        images[2]=(ImageView) findViewById(R.id.img3);
        images[3]=(ImageView) findViewById(R.id.img4);
        images[4]=(ImageView) findViewById(R.id.img5);
        for(int i = 0 ; i < size ; i++ ){
        	images[i].setTag(links[i]);
        	new GetImages().execute(images[i]);       	
        }       	 
}

    public Drawable storeImage(String url, String saveFilename) {

        try {
            InputStream imageBuffer = (InputStream) this.fetch(url);
            Drawable d = Drawable.createFromStream(imageBuffer, "src");
            return d;
        } catch(Exception e){
       	 	Log.e("Error: ", e.getMessage());
       	 	return null;	        	 
        }
    }
    
    public Object fetch(String address) throws MalformedURLException,IOException {
        URL url = new URL(address);
        Object content = url.getContent();
        return content;
    }	
}
