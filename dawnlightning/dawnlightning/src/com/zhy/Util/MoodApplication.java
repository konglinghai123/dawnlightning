package com.zhy.Util;
import java.util.LinkedList;
import java.util.List;


import com.baidu.frontia.FrontiaApplication;
import com.baidu.mapapi.SDKInitializer;
import android.app.Activity;

public class MoodApplication extends FrontiaApplication {

	private List<Activity> allActivity=null;
	
	private static MoodApplication moodApplication=null;
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public MoodApplication() {
		// TODO Auto-generated constructor stub
		allActivity=new LinkedList();
	}
	
	public static MoodApplication getInstance(){
		if(moodApplication==null){
			moodApplication=new MoodApplication();
		}
		return moodApplication;
	}
	
	public void addActivity(Activity act){
		if(allActivity!=null && allActivity.size()>0){
			if(!allActivity.contains(act)){
				allActivity.add(act);
			}
		}else{
			allActivity.add(act);
		}
	}
	
	public void exit(){
		if(allActivity!=null && allActivity.size()>0){
			for(Activity act:allActivity){
				act.finish();
			}
			
		}
		System.exit(0);
	}
	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
	
		SDKInitializer.initialize(this);
		
		
		new App().init();
	}
	
	
}
