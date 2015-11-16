package com.zhy.Activity;



import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.dawnlightning.ucqa.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.Util.HttpConstants;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class PictureActivity extends Activity{
	private ImageView myView = null;
	private String path="";
	private List<String> allImageUrl;
	private ProgressDialog dialog;
	private int psit;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	  requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture);
      
        myView = (ImageView)findViewById(R.id.myView);
        Intent i=getIntent();
        allImageUrl=getIntent().getStringArrayListExtra("imageUrl");
        psit=getIntent().getExtras().getInt("position");
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20)).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		imageLoader.displayImage(HttpConstants.HTTP_HEAD+allImageUrl.get(psit), myView, options);
       
    }
   

   
}
