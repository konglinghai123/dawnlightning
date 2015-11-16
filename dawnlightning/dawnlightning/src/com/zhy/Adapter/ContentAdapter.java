package com.zhy.Adapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.dawnlightning.ucqa.R;

import com.zhy.Activity.PictureActivity;
import com.zhy.Bean.Comment;
import com.zhy.Bean.Detailed;
import com.zhy.Bean.Pics;
import com.zhy.Util.HttpConstants;
import com.zhy.Util.LvHeightUtil;
import com.zhy.Util.UnitTransformUtil;
import com.zhy.view.NoScrollGridView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ContentAdapter extends BaseAdapter {
	private Context context;
	private HashMap<String,Detailed> content;
	private LayoutInflater layoutInflater;
	private Title title;
	private Message message;
	private Picture picture;
	private Comment comment;
	private Setcomment setcomment;
	private ArrayList<String> piclsit;
	private  List<com.zhy.Bean.Comment> comList=new ArrayList();
	private MainListViewAdapter pinglunAdapter;
	private MainGridViewAdapter mainGridViewAdapter;
	private final static String [] fileds={"开头","正文","图片","评论"};
	public ContentAdapter(Context context,HashMap<String,Detailed> content){
		this.context=context;
		this.content=content;
		layoutInflater = (LayoutInflater) LayoutInflater.from(context);
	}
	private String  getkeys(int arg0){
		int postion=0;
		String nowkey="";
		for(String key:content.keySet()){
			if(postion==arg0){
				nowkey=key;
				break;
			}else{
				postion++;
			}
		}
		return nowkey;
	}
	@Override
	public int getCount() {
		// TODO 自动生成的方法存根
		return content.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO 自动生成的方法存根
		int postion=0;
		
		String nowkey="";
		for(String key:content.keySet()){
			if(postion==arg0){
				nowkey=key;
				break;
			}else{
				postion++;
			}
		}
		return content.get(nowkey);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO 自动生成的方法存根
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		String keys=fileds[arg0];
		Log.e("arg0", String.valueOf(arg0));
		//if (null == arg1) {
			switch(arg0){
			case 0:
				arg1=layoutInflater.inflate(R.layout.item_content_title,null);
				title=new Title();
				title.ivUserLogo=(ImageView) arg1.findViewById(R.id.iv_leftlogo);
				title.tvUserName=(TextView)arg1.findViewById(R.id.userName);
				title.tvUserAge=(TextView)arg1.findViewById(R.id.tvAge);
				title.moodinTime=(TextView)arg1.findViewById(R.id.sendTime);
				Log.e("keys", keys);
				arg1.setTag(R.id.title_id,title);
				break;
			case 1:
				arg1=layoutInflater.inflate(R.layout.item_content_message,null);
				message=new Message();
				message.moodinContent=(TextView) arg1.findViewById(R.id.checkInfo);
				message.subject=(TextView) arg1.findViewById(R.id.showInfo);
				Log.e("keys", keys);
				arg1.setTag(R.id.message_id,message);
				break;
			case 2:
				arg1=layoutInflater.inflate(R.layout.item_content_pictrue,null);
				picture=new Picture();
				picture.moodinGridView=(NoScrollGridView) arg1.findViewById(R.id.myGridView);
				Log.e("keys", keys);
				arg1.setTag(R.id.pictrue_id, picture);
				break;
			case 3:
				arg1=layoutInflater.inflate(R.layout.item_content_comment,null);
				comment=new Comment();
				comment.moodinListView=(ListView)arg1.findViewById(R.id.pinglunListView);
				Log.e("keys", keys);
				arg1.setTag(R.id.comment_id, comment);
				break;
		
			}
		Detailed dt=content.get(keys);
		
		if(title==null){
			title=	(Title)	 arg1.getTag(R.id.title_id);
		}
		if(message==null){
			message=(Message) arg1.getTag(R.id.message_id);
		}
		if(picture==null){
			picture=(Picture) arg1.getTag(R.id.pictrue_id);
		}
		if(comment==null){
			comment=(Comment) arg1.getTag(R.id.comment_id);
		}
		switch(keys){
		case "开头":
		
			//title.ivUserLogo
			title.tvUserName.setText(dt.getUsename());
			title.tvUserAge.setText(dt.getAge());
			title.moodinTime.setText(dt.getDatetime());
			break;
		case "正文":
		
			message.moodinContent.setText(dt.getContent());
			message.subject.setText(dt.getSubject());
		
			
			break;
		case "图片":
			if(dt.getPics()!=null){
				piclsit=new ArrayList();
				for(Pics p:dt.getPics()){
					
					piclsit.add(HttpConstants.HTTP_IP+p.getUrl());
					Log.e("url",HttpConstants.HTTP_IP+p.getUrl());
				}
				
				if (piclsit.size() > 0) {
					
					
					 mainGridViewAdapter = new MainGridViewAdapter(context, piclsit);
					//说明有两行
					if(piclsit.size()>3){
						
						LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) picture.moodinGridView.getLayoutParams(); // 取控件mGrid当前的布局参数
						linearParams.height = UnitTransformUtil.dip2px(context, 240);// 
						picture.moodinGridView.setLayoutParams(linearParams);
						picture.moodinGridView.setVerticalSpacing(1);
					}
					picture.moodinGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
					picture.moodinGridView.setAdapter(mainGridViewAdapter);
					picture.moodinGridView.setOnItemClickListener(new OnItemClickListener(){

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
						
							Intent i=new Intent();
							i.putStringArrayListExtra("imageUrl", piclsit);
							i.putExtra("position", 	arg2);
							i.setClass(context, PictureActivity.class);
							context.startActivity(i);
							
						}
						
					});
				}}
			break;
		case "评论":
			 if(null!=dt.getComment()&&dt.getComment().size()!=0){
				  comList = dt.getComment();
				  pinglunAdapter = new MainListViewAdapter(context, comList); 
				
				  comment.moodinListView.setAdapter(pinglunAdapter);
				  LvHeightUtil.setListViewHeightBasedOnChildren( comment.moodinListView);
						 
			  }else{
				 
			  }
			break;
		}
		// TODO 自动生成的方法存根
		return arg1;
	}
	
	public class Title{
		// 用户图像
		private ImageView ivUserLogo;

		// 用户昵称
		private TextView tvUserName;
		
		// 用户年龄
		private TextView tvUserAge;

		// 心情时间
		private TextView moodinTime;
	}
	public class Message{
		// 标题
		private TextView subject;

		// 心情内容
		private TextView moodinContent;
	}
	public class Picture{
		// 心情图片
		private NoScrollGridView moodinGridView;

	}
	public class Comment{
		// 心情评论
		private ListView moodinListView;
	}
	public class Setcomment{
		// 评论按钮
		private LinearLayout pinglun;
		
		//评论edittext
		private EditText etPingLunContent;
	}
	

}
