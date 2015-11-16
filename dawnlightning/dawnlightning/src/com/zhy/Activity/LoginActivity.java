package com.zhy.Activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.zhy.Bean.BaseBean;
import com.zhy.Bean.UserBean;
import com.zhy.Bean.UserBeanData;
import com.zhy.Db.SharedPreferenceDb;
import com.zhy.Util.AppUtils;
import com.zhy.Util.HttpConstants;
import com.zhy.Util.HttpUtil;
import com.zhy.Util.MoodApplication;
import com.zhy.Util.ResultCallback;
import com.dawnlightning.ucqa.R;
import com.zhy.view.EditTextWithDel;
import com.zhy.view.TitleBar;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class LoginActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout tvRegister;
	
	private Button btnLogin;
	
	private EditTextWithDel etPhone;
	
	private EditTextWithDel etPassWord;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		MoodApplication.getInstance().addActivity(this);
		
		setContentView(R.layout.userlogin);
		
		initViews();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		TitleBar titleBar=(TitleBar)findViewById(R.id.TitleBar);
		 if(!"0".equals(String.valueOf(new SharedPreferenceDb(LoginActivity.this).getChangeTheme()))){
			 titleBar.setBackgroundColor(new SharedPreferenceDb(LoginActivity.this).getChangeTheme());
		 }else{
			 titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
		 }
		 titleBar.showLeft("登陆", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
	}
	
	@Override
	public void initViews() {
		// TODO Auto-generated method stub
		super.initViews();
		
		// tvRegister=(RelativeLayout)findViewById(R.id.newUserRegister);
		 //tvRegister.setOnClickListener(this);
		 
		 etPhone=(EditTextWithDel)findViewById(R.id.accounts);
		 etPassWord=(EditTextWithDel)findViewById(R.id.password);
		 
		 
		 
		 btnLogin=(Button)findViewById(R.id.login);
		 btnLogin.setOnClickListener(this);
		 
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
		if(view==tvRegister){
			SkipActivityforClass(LoginActivity.this, RegisterActivity.class);
			if(new SharedPreferenceDb(LoginActivity.this).getAnimation()==true){
				overridePendingTransition(R.anim.fade, R.anim.hold);
			}
			
		}else if(view==btnLogin){
			
			if(!etPhone.getText().toString().isEmpty()){
				
				if(!etPassWord.getText().toString().isEmpty()){
					
					
					if(AppUtils.checkNetwork(LoginActivity.this)==true){
						//SkipActivityforClass(LoginActivity.this, MainActivity.class);
						
						List<NameValuePair> allP=new ArrayList<NameValuePair>();
						allP.add(new BasicNameValuePair("username", etPhone.getText().toString().trim()));
						allP.add(new BasicNameValuePair("password", etPassWord.getText().toString().trim()));
						
						Map<String,String> params=new HashMap<String, String>();
						params.put("username", etPhone.getText().toString().trim());
						params.put("password", etPassWord.getText().toString().trim());
						
						initProgressDialog();
						
						new	HttpUtil().doPost(HttpConstants.HTTP_LOGIN, allP, new ResultCallback() {
							
							@Override
							public void getReslt(String result) {
								// TODO Auto-generated method stub
								//&& "1".equals(result)
								if(!result.isEmpty() ){
									BaseBean b=JSON.parseObject(result, BaseBean.class);
									Log.println(11, "_________>", b.getData());
									if("0".equals(b.getCode())){
										
										UserBeanData dt=JSON.parseObject(b.getData(), UserBeanData.class);
										Log.println(11, "_________>", dt.getSpace());
										UserBean u=JSON.parseObject(dt.getSpace(),UserBean.class);
										ArrayList<String> userinfo =new ArrayList<String>();
										userinfo.add(u.getUsername());
										userinfo.add(dt.getM_auth());
										userinfo.add(u.getUid());
										new SharedPreferenceDb(LoginActivity.this).setUserId(u.getUsername());
										new SharedPreferenceDb(LoginActivity.this).setName(u.getName());
										Intent intent=new Intent();
										intent.putStringArrayListExtra("user",userinfo);
									
										intent.setClass(LoginActivity.this, MainActivity_1.class);
										startActivity(intent);
										finish();
										if(new SharedPreferenceDb(LoginActivity.this).getAnimation()==true){
											overridePendingTransition(R.anim.slide_left,
													R.anim.slide_right);
										}
										close();
									}else{
										showToast("用户名或密码错误");
										close();
									}
								}else{
									Toast.makeText(LoginActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
									close();
								}
							}
						});
						
						
					}else{
						showToast("亲，您还没有联网了!");
					}
					
				}else{
					showToast("密码不能为空");
				}
			}else{
				showToast("用户名不能为空");
			}
			

			
			
		}
			
		}
	
	
	public void showToast(String str){
		Toast.makeText(LoginActivity.this, str, Toast.LENGTH_LONG).show();
	}
	
	
}
