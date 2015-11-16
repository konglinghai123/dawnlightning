package com.zhy.Activity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import me.maxwin.view.IXListViewLoadMore;
import me.maxwin.view.IXListViewRefreshListener;
import me.maxwin.view.XListView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dawnlightning.ucqa.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.Activity.ConsultActivity.SelectPopuWindow;
import com.zhy.Adapter.ContentAdapter;
import com.zhy.Adapter.MainGridViewAdapter;
import com.zhy.Adapter.MainListViewAdapter;
import com.zhy.Bean.BaseBean;
import com.zhy.Bean.Comment;
import com.zhy.Bean.ConsultBean;
import com.zhy.Bean.ConsultMessageBean;
import com.zhy.Bean.Detailed;
import com.zhy.Bean.Pics;
import com.zhy.Bean.SeccodeBean;
import com.zhy.Util.AppUtils;
import com.zhy.Util.HttpConstants;
import com.zhy.Util.HttpUtil;
import com.zhy.Util.LvHeightUtil;
import com.zhy.Util.ResultCallback;
import com.zhy.Util.UnitTransformUtil;
import com.zhy.upload.HttpStringResult;
import com.zhy.view.NoScrollGridView;
import com.zhy.view.TitleBar;

public class NewsContentActivity extends BaseActivity implements IXListViewLoadMore,IXListViewRefreshListener,OnClickListener{

	private String text;
	private TitleBar contenttitlebar;
	private String formhash;
	private ArrayList<String> message;
	private Context context;
	private Detailed detailed;//详细
	private  List<Comment> comList=new ArrayList();
	private XListView contentlist;
	private EditText setcomment;
	private TextView commentnum;
	private ContentAdapter contentadapter;
	private final static String [] fileds={"开头","正文","图片","评论"};
	private HashMap<String,Detailed> content;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_content);
		initview();
		initdata();
		initevent();
	}

	private void initview(){
		contenttitlebar=(TitleBar)findViewById(R.id.content_TitleBar);	
		contenttitlebar.setBackgroundColor(getResources().getColor(R.color.blue));
		contentlist=(XListView) findViewById(R.id.contentListView);
		//contentlist.setPullRefreshEnable(this);
		//contentlist.setPullLoadEnable(this);
		setcomment=(EditText) findViewById(R.id.etPingLunContent);
		commentnum=(TextView) findViewById(R.id.pinglun_nums);
	

	}
	private void initdata(){
		context=this;
		Intent intent=getIntent();
		message=intent.getStringArrayListExtra("message");
		
	}
	private void initevent(){
		contenttitlebar.showLeftAndRight("详细", getResources().getDrawable(R.drawable.app_back),getResources().getDrawable(R.drawable.base_send_normal), new backlistener(), new commentlistener());
		setcomment.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showUpdataDialog(detailed.getComment());
				
			}
			
		});
		contentlist.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				
			}
			
		});
	}
	class backlistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			finish();
		}
	
	}
	class commentlistener implements OnClickListener{

		@Override
		public void onClick(View v) {
			
			
		}
	
	}
	@Override
	protected void onStart() {
		
		
		getcontent();
		
	
		super.onStart();
	}

	@Override
	public void onClick(View arg0) {
		
		
	}
	public void setcomment(final String str){
		
		final List<NameValuePair> allP=new ArrayList<NameValuePair>();
		allP.add(new BasicNameValuePair("commentsubmit","true"));
		allP.add(new BasicNameValuePair("formhash",formhash));
		allP.add(new BasicNameValuePair("id",message.get(3).toString()));
		allP.add(new BasicNameValuePair("idtype","bwztid"));
		allP.add(new BasicNameValuePair("message",str));
		new	HttpUtil().doPost(HttpConstants.HTTP_COMMENT.replace("!", message.get(1)), allP, new ResultCallback() {
			
			@SuppressLint("NewApi")
			@Override
			public void getReslt(String result) {
				// TODO 自动生成的方法存根
				if(!result.isEmpty() ){
				
					BaseBean b=JSON.parseObject(result, BaseBean.class);
					if("0".equals(b.getCode().toString().trim())){
						
						detailed.getComment().add(new Comment(message.get(0),str));
						 contentadapter.notifyDataSetChanged();
						 commentnum.setText(detailed.getComment().size()+"");
						//  moodinPinglunNums.setText(comList.size()+"");
						//showToast("评论成功"+b.getData());
					}else{
						Toast.makeText(NewsContentActivity.this, "评论失败", Toast.LENGTH_LONG).show();
					
					}
				
					}else{
						Toast.makeText(NewsContentActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
						
					}
				
			}
			
			
		});
	
	}
	public void getformhash(){
		if(AppUtils.checkNetwork(NewsContentActivity.this)==true){
			List<NameValuePair> allP=new ArrayList<NameValuePair>();
			allP.add(new BasicNameValuePair("m_auth",message.get(1).toString())); 
			new	HttpUtil().doPost(HttpConstants.HTTP_FORMHASH, allP, new ResultCallback() {

				@SuppressLint("NewApi")
				@Override
				public void getReslt(String result) {
					// TODO 自动生成的方法存根
					if(!result.isEmpty() ){
						BaseBean b=JSON.parseObject(result, BaseBean.class);
						if("0".equals(b.getCode().toString().trim())){
							try{
							JSONObject js=(JSONObject) JSON.parse(b.getData());
							formhash=js.getString("formhash");
							}catch(Exception e){
								Toast.makeText(NewsContentActivity.this, "获取验证码失败", Toast.LENGTH_LONG).show();
							}
							//showToast("获取防伪验证码成功"+formhash);
						}else{
							Toast.makeText(NewsContentActivity.this, "获取验证码失败", Toast.LENGTH_LONG).show();
						
						}
					
						}else{
							Toast.makeText(NewsContentActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
							
						}
					
				}
				
				
			});}else{
				showToast("亲，您还没有联网了!");
			}
	}
	@SuppressLint("NewApi")
	private void getcontent(){
		List<NameValuePair> allP=new ArrayList<NameValuePair>();
		allP.add(new BasicNameValuePair("uid",message.get(4)));
		allP.add(new BasicNameValuePair("id",message.get(3)));
	
		new	HttpUtil().doPost(HttpConstants.HTTP_CONSULT_DETAIL, allP, new ResultCallback(){

			@Override
			public void getReslt(String result) {
				if(!result.isEmpty() ){
					BaseBean b=JSON.parseObject(result, BaseBean.class);
					Log.e("msg", b.getData());
					if("0".equals(b.getCode())){
						//try{
						JSONObject j=(JSONObject) JSON.parse(b.getData());
						JSONObject js=(JSONObject) JSON.parse(j.getString("bwzt"));
						detailed=new Detailed();
						 detailed.setAge(js.getString("age"));
						 detailed.setContent(js.getString("message"));
						 detailed.setDatetime( TimeStamp2Date(js.getString("dateline")));
						 detailed.setSubject(js.getString("subject"));
						 detailed.setUid(js.getString("uid"));
						 detailed.setUsename(js.getString("username"));
						 detailed.setComment(JSON.parseArray(js.getString("replylist"),Comment.class));
						 JSONArray jsonArray = JSONArray.parseArray(js.getString("pics")); 
						 List<Pics> list=new ArrayList<Pics>();
						 if(jsonArray!=null){
						 for(int k=0;k<jsonArray.size();k++){  
				               System.out.print(jsonArray.get(k) + "\t");  
				               Log.e("strpic",jsonArray.get(k).toString());
				               JSONObject objpic=(JSONObject) JSON.parse(jsonArray.get(k).toString());
				              
				               Pics p=new Pics( objpic.getString("picurl"),"");
				               list.add(p);
				           }  
						
						
						 detailed.setPics(list);
						 }
						 content=new HashMap<String,Detailed>();
						 for(int i=0;i<fileds.length;i++){
							 Log.e("Tag",fileds[i]);
							 content.put(fileds[i], detailed);
						 }
					
						 contentadapter=new ContentAdapter(context,content);
						 contentlist.setAdapter(contentadapter);
						commentnum.setText(String.valueOf(detailed.getComment().size()));
						//}catch(Exception e){
							//Toast.makeText(context, "获取详细失败", Toast.LENGTH_LONG).show();
						//}
		
						//Log.e("msg",  detailed.getUsename());
						//Log.e("msg",  detailed.getComment().get(0).getMessage());
					
						
					}else{
						Toast.makeText(context, "获取详细失败", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(context, "服务器响应失败", Toast.LENGTH_LONG).show();
					
					}
				
				
			}
		
	
		
		});}
	
	public void showToast(String str){
		Toast.makeText(NewsContentActivity.this, str, Toast.LENGTH_LONG).show();
	}
	public String TimeStamp2Date(String timestampString){  
		  Long timestamp = Long.parseLong(timestampString)*1000;  
		  String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(timestamp));  
		  return date;  
		}
	
	
	protected void showUpdataDialog(final List<Comment> comList) {

        final com.zhy.dialog.CustomDialogUpd.Builder builder = new com.zhy.dialog.CustomDialogUpd.Builder(context);
        builder.setTitle("发表评论");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                // TODO Auto-generated method stub
                String  content= builder.getEditText().getText().toString().trim();
                setcomment(content);
                dialog.dismiss();
            }
        });
		// 当点取消按钮时进行登录
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

        builder.create().show();
    }

	@Override
	public void onRefresh() {
		// TODO 自动生成的方法存根
		
	}
	@Override
	public void onLoadMore() {
		// TODO 自动生成的方法存根
		
	}
}
