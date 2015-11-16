package com.zhy.Activity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.alibaba.fastjson.JSON;
import com.dawnlightning.ucqa.R;
import com.zhy.Bean.BaseBean;
import com.zhy.Bean.SeccodeBean;
import com.zhy.Db.SharedPreferenceDb;
import com.zhy.Util.AppUtils;
import com.zhy.Util.HttpConstants;
import com.zhy.Util.HttpUtil;
import com.zhy.Util.ResultCallback;

import com.zhy.view.EditTextWithDel;
import com.zhy.view.TitleBar;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;



@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class RegisterActivity extends BaseActivity implements OnClickListener,OnCheckedChangeListener {

	private TitleBar titleBar;

	
	private EditTextWithDel etPhone;
	
	private EditTextWithDel etPwd;
	
	private EditTextWithDel etName;
	
	private EditTextWithDel etSecorndpwd;
	SeccodeBean sb;//验证码类
	private EditTextWithDel code;
	private TextView respondcode;

	
	private Button btnRegister;
	
	
	private boolean isSelect=false;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.register);
		initViews();
		
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		TitleBar titleBar=(TitleBar)findViewById(R.id.titleBar);
		 if(!"0".equals(String.valueOf(new SharedPreferenceDb(RegisterActivity.this).getChangeTheme()))){
			 titleBar.setBackgroundColor(new SharedPreferenceDb(RegisterActivity.this).getChangeTheme());
		 }else{
			 titleBar.setBackgroundColor(getResources().getColor(R.color.blue));
		 }
		 titleBar.showLeft("注册", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
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
		
		
		
		etPhone=(EditTextWithDel)findViewById(R.id.accounts);
		etPwd=(EditTextWithDel)findViewById(R.id.password);
		etName=(EditTextWithDel)findViewById(R.id.name);
		etSecorndpwd=(EditTextWithDel)findViewById(R.id.secondpassword);
		code=(EditTextWithDel) findViewById(R.id.inputcode);
		respondcode=(TextView)findViewById(R.id.secordcode);
	
		btnRegister=(Button)findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(this);
	
		if(AppUtils.checkNetwork(RegisterActivity.this)==true){
		List<NameValuePair> allP=new ArrayList<NameValuePair>();
		new	HttpUtil().doPost(HttpConstants.HTTP_FRIST_REGISTER, allP, new ResultCallback() {

			@Override
			public void getReslt(String result) {
				// TODO 自动生成的方法存根
				if(!result.isEmpty() ){
					BaseBean b=JSON.parseObject(result, BaseBean.class);
					if("0".equals(b.getCode().toString().trim())){
						sb=JSON.parseObject(b.getData(),SeccodeBean.class);
						respondcode.setText(sb.getSeccode());
						
					}else{
						Toast.makeText(RegisterActivity.this, "获取验证码失败", Toast.LENGTH_LONG).show();
						close();
					}
				
					}else{
						Toast.makeText(RegisterActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
						close();
					}
				
			}
			
			
		});}else{
			showToast("亲，您还没有联网了!");
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		 if(arg0==btnRegister){
			
			checkUserInput();
		}
	}

	
	public void checkUserInput(){
		if(!"".equals(etPhone.getText().toString().trim())){
			
		
				
				if(!etPwd.getText().toString().trim().isEmpty()){
					
					if(!etName.getText().toString().trim().isEmpty()){
						
						
							if(AppUtils.checkNetwork(RegisterActivity.this)==true){
								if(etPwd.getText().toString().trim().equals(etSecorndpwd.getText().toString().trim()))
								{
									if(code.getText().toString().trim().equals(respondcode.getText().toString().trim())){
								      executeHttp();
								
									}else{
										showToast("验证码不正确");
									}
								}else{
									showToast("两次密码不一样");
								}
							}else{
								showToast("亲，您还没有联网了!");
							}
							
						
					}else{
						showToast("给自己一个好听的昵称吧");
					}
					
				}else{
					showToast("密码不能为空");
				}
				
		
		}else {
			showToast("用户名不能为空");
		}
	}
	
	
	public void executeHttp(){
		
		List<NameValuePair> allP=new ArrayList<NameValuePair>();
		allP.add(new BasicNameValuePair("phone", etPhone.getText().toString().trim()));
		allP.add(new BasicNameValuePair("password", etPwd.getText().toString().trim()));
		allP.add(new BasicNameValuePair("scpassword",etSecorndpwd.getText().toString().trim()));
		allP.add(new BasicNameValuePair("inputcode",code.getText().toString().trim()));
		allP.add(new BasicNameValuePair("key", sb.getSeccode_auth().toString().trim()));
		//sb.getSeccode_auth().toString().trim())
		Map<String,String> params=new HashMap<String, String>();
		params.put("phone", etPhone.getText().toString().trim());
		params.put("password", etPwd.getText().toString().trim());
		params.put("scpassword",etSecorndpwd.getText().toString().trim());
		params.put("name", etName.getText().toString().trim());
		params.put("inputcode",sb.getSeccode_auth().toString().trim());
		
		
		
		
		initProgressDialog();
		new	HttpUtil().doPost(HttpConstants.HTTP_REGISTER, allP, new ResultCallback() {

			@Override
			public void getReslt(String result) {
				// TODO 自动生成的方法存根
				
				if(!result.isEmpty() ){
					BaseBean b=JSON.parseObject(result, BaseBean.class);
					if("0".equals(b.getCode().toString().trim())){
						showToast(b.getData());
						close();
					}else{
						Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_LONG).show();
						close();
					}
					
				}else{
					Toast.makeText(RegisterActivity.this, "服务器连接失败...", Toast.LENGTH_LONG).show();
					close();
				}
				
			}
			
			
		});
		
		/*fh.post(HttpConstants.HTTP_REGISTER, parms, new AjaxCallBack<Object>() {
			
			@Override
			public void onFailure(Throwable t, int errorNo, String strMsg) {
				// TODO Auto-generated method stub
				super.onFailure(t, errorNo, strMsg);
				Toast.makeText(RegisterActivity.this, "服务器连接失败...", Toast.LENGTH_LONG).show();
				close();
			}
			
			@Override
			public void onSuccess(Object t) {
				// TODO Auto-generated method stub
				super.onSuccess(t);
				showToast("注册返回过来的数据:"+String.valueOf(t));
				close();
			}
		});*/
	}
	
	public void showToast(String str){
		Toast.makeText(RegisterActivity.this, str, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		// TODO Auto-generated method stub
		if(isChecked){
			isSelect=true;
		}else{
			isSelect=false;
		}
	}
}
