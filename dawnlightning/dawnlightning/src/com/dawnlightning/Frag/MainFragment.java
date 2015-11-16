package com.dawnlightning.Frag;

import java.util.List;

import com.dawnlightning.ucqa.R;
import com.zhy.Adapter.TabAdapter;
import com.zhy.view.TitleBar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class MainFragment extends Fragment
{

	
	private Context context;
	private List<String> userinfo;
	private TitleBar titlebar;
	public MainFragment(Context context,List<String> userinfo)
	{
		
		this.context=context;
		this.userinfo=userinfo;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_profile, null);
		
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}
	private void initview(){
		titlebar=(TitleBar) getView().findViewById(R.id.TitleBar3);
		 titlebar.setBackgroundColor(getResources().getColor(R.color.blue));
		 titlebar.showLeft("我", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
				}
			});
	}

	@Override
	public void onStart() {
		// TODO 自动生成的方法存根
		super.onStart();
		 initview();
	}

	@Override
	public void onResume() {
		initview();
		super.onResume();
	}
	
}
