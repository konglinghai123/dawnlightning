package com.zhy.Adapter;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.dawnlightning.ucqa.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.Util.HttpConstants;

import com.zhy.loadimage.AsynImageLoader;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;



public class MainGridViewAdapter extends BaseAdapter {

	private LayoutInflater layoutInflater;
	private List<String> allImageUrl;
	private Context ctx;
	private DisplayImageOptions options;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	//暂且用bitmap加载图片
	
	
	public MainGridViewAdapter(Context context,List<String> allImageUrl) {
		// TODO Auto-generated constructor stub
		this.ctx=context;
		this.allImageUrl=allImageUrl;
		layoutInflater = (LayoutInflater) LayoutInflater.from(context);
		imageLoader.init(ImageLoaderConfiguration.createDefault(context));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20)).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return allImageUrl.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return allImageUrl.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressWarnings({ "unused", "null" })
	@Override
	public View getView(int arg0, View contentView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		RelativeLayout view=null;
		ViewHolder viewHolder;
		if(view==null){
			view=(RelativeLayout) layoutInflater.inflate(R.layout.main_gridview_item, null);
			viewHolder=new ViewHolder();
			viewHolder.imageView=(ImageView)view.findViewById(R.id.main_gridview_imageView);
			view.setTag(viewHolder);
		}else{
			viewHolder=(ViewHolder)view.getTag();
		}
		//AsynImageLoader.showImageAsyn(viewHolder.imageView, HttpConstants.HTTP_REQUEST
				//+ allImageUrl.get(arg0), R.drawable.ic_launcher);
		imageLoader.displayImage(HttpConstants.HTTP_HEAD+allImageUrl.get(arg0), viewHolder.imageView, options);
		return view;
	}
	
	private static class ViewHolder{
		
		private ImageView imageView;
	}

}
