package com.zhy.Activity;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.zhy.Adapter.GridViewAdapter;
import com.zhy.Bean.BaseBean;
import com.zhy.Bean.SendConsult;
import com.zhy.Db.SharedPreferenceDb;

import com.zhy.Util.AppUtils;
import com.zhy.Util.HttpConstants;
import com.zhy.Util.HttpUtil;
import com.zhy.Util.ResultCallback;
import com.zhy.Util.SdCardUtil;
import com.zhy.Util.TimeUtil;
import com.dawnlightning.ucqa.R;

import com.zhy.upload.ProgressMultiPartEntity.ProgressListener;
import com.zhy.upload.HttpStringResult;
import com.zhy.upload.UploadPicture;
import com.zhy.view.TitleBar;
public class ConsultActivity extends BaseActivity {
	private TitleBar titlebar;
	private SendConsult content=new SendConsult();;
	private Spinner  bwztclassid;//症状分类
	private Spinner  bwztvisorid;//科室分类
	private EditText subject;
	private EditText age;
	private EditText message;
	private RadioGroup radiogroup;
	private RadioButton man;
	private ArrayList<String> userinfo;
	private HashMap<String, String> mapid = new HashMap<String, String>();//症状分类
	private HashMap<String, String> mapname = new HashMap<String, String>();//科室分类
	private Context context;
	private GridView gridView;
	private GridViewAdapter gridAdapter;
	List<Bitmap> allImages = new ArrayList<Bitmap>();
	List<String> imageUrl=new ArrayList<String>();
	private boolean selectFlag=false;//选择的状态
	private Dialog dialog;
	List<String> listclass=new ArrayList();
	List<String> listvisor=new ArrayList();
	String fileName = String.valueOf(System.currentTimeMillis()) + ".jpg"; 
	List<String> Listpicid=new ArrayList();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.consult);
		initobject();
		initview();
		getspinnerdate();
		
		initevent();
	}
	private void initobject(){
		context=this;
		
		Intent intent=getIntent();
		userinfo = intent.getStringArrayListExtra("user");
	}
	private void initview(){
		bwztclassid=(Spinner) findViewById(R.id.spinner1);
		bwztvisorid=(Spinner) findViewById(R.id.spinner2);
		titlebar=(TitleBar) findViewById(R.id.TitleBar3);
		subject=(EditText) findViewById(R.id.subject);
		age=(EditText) findViewById(R.id.age);
		message=(EditText) findViewById(R.id.message1);
		radiogroup=(RadioGroup) findViewById(R.id.sex);
		man=(RadioButton) findViewById(R.id.male);
		gridView = (GridView) findViewById(R.id.gridView);
		allImages.add(BitmapFactory.decodeResource(getResources(), R.drawable.icon_addpic_focused));
		gridAdapter = new GridViewAdapter(context, allImages);
		gridView.setAdapter(gridAdapter);
		
		gridView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					if (arg2 == allImages.size() - 1) {
						if (allImages.size() <= 6) {
							new SelectPopuWindow(context, gridView, arg2);
						} else {
							Toast.makeText(context, "照片不能超过6张",
									Toast.LENGTH_LONG).show();
						}

					}else{
						Intent intent=new Intent();
						intent.setClass(context, BrowseImageViewActivity.class);
						intent.putStringArrayListExtra("imageUrl", (ArrayList<String>) imageUrl);
						intent.putExtra("position", arg2);
						startActivity(intent);
						//Toast.makeText(WriteMoodActivity.this, arg2+""+"集合的长度:"+allImages.size(), Toast.LENGTH_LONG).show();
					}
				}

		});
		
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		IntentFilter intentFilter=new IntentFilter();
		intentFilter.addAction("delImage");
		registerReceiver(delBroadCast, intentFilter);
	}
	BroadcastReceiver delBroadCast=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context arg0, Intent arg1) {
			// TODO Auto-generated method stub
			String action=arg1.getAction();
			if(action.equals("delImage")){
				int position=arg1.getExtras().getInt("position");
				Log.i("广播收到的删除下标为:", position+"");
				imageUrl.remove(position);
				allImages.remove(position);
				gridAdapter.notifyDataSetChanged();
				
				for(int i=0;i<imageUrl.size();i++){
				
					Log.e("delete","图片:"+imageUrl.get(i));
				}
			}
					
		}
	};
	public class SelectPopuWindow extends PopupWindow {

		private Context context;
		private int ReuestCode;

		public SelectPopuWindow(Context mContext, View parent,
				final int requestCode) {
			this.ReuestCode = requestCode;
			this.context = mContext;
			View view = View
					.inflate(mContext, R.layout.item_popupwindows, null);
			view.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.fade_ins));
			LinearLayout ll_popup = (LinearLayout) view
					.findViewById(R.id.ll_popup);
			ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
					R.anim.push_bottom_in_2));

			setWidth(LayoutParams.FILL_PARENT);
			setHeight(LayoutParams.FILL_PARENT);
			setBackgroundDrawable(new BitmapDrawable());
			setFocusable(true);
			setOutsideTouchable(true);
			setContentView(view);
			showAtLocation(parent, Gravity.BOTTOM, 0, 0);
			update();

			Button bt1 = (Button) view
					.findViewById(R.id.item_popupwindows_camera);
			Button bt2 = (Button) view
					.findViewById(R.id.item_popupwindows_Photo);
			Button bt3 = (Button) view
					.findViewById(R.id.item_popupwindows_cancel);
			bt1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					/*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intent, requestCode + 1);*/
					
					Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
					openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
					startActivityForResult(openCameraIntent, 1);
					dismiss();
				}
			});
			bt2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					if(SdCardUtil.checkSdCard()==true){
						Intent intent = new Intent(Intent.ACTION_PICK);  
					       intent.setType("image/*");//相片类型  
					       startActivityForResult(intent, 7); 
					}else{
						Toast.makeText(context, "SD卡不存在", Toast.LENGTH_LONG).show();
					}
					
					dismiss();
				}
			});
			bt3.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dismiss();
				}
			});

		}
	}

	public void getImaged(Intent data){
		Log.e("ERROR","准备显示");
		Uri uri = null;
		if (data != null) {
			uri = data.getData();
			Log.e("URL",uri.toString());
			System.out.println("Data");
		}else{
			Log.e("ERROR","准备显示");
			uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),fileName));
		}
		//imageUrl.add(uri.getPath());
		Bitmap photo = null;
	
		
		if (uri != null) {
			photo = BitmapFactory.decodeFile(uri.getPath());
			Log.e("ERROR","没图片");
		}
		
		if(photo!=null){
			
			Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(photo, 88, 88); 
			allImages.add(allImages.size()-1,resizeBmp);
			Log.e("ERROR","传入成功");
			gridAdapter.notifyDataSetChanged();
			
			saveImageToFile(photo);
		}
		/*Bundle bundler=data.getExtras();
		Bitmap bitmap = (Bitmap) bundler.get("data");
		Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(bitmap, 88, 88); 
		allImages.add(allImages.size()-1,resizeBmp);*/
		//cropImage(uri, 150, 150, 8);
	}
	//截取图片
			public void cropImage(Uri uri, int outputX, int outputY, int requestCode){
				Intent intent = new Intent("com.android.camera.action.CROP");  
		        intent.setDataAndType(uri, "image/*");  
		        intent.putExtra("crop", "true");  
		        intent.putExtra("aspectX", 3);  
		        intent.putExtra("aspectY", 3);  
		        intent.putExtra("outputX", outputX);   
		        intent.putExtra("outputY", outputY); 
		        intent.putExtra("outputFormat", "JPEG");
		        intent.putExtra("noFaceDetection", true);
		        intent.putExtra("return-data", true);   
			    startActivityForResult(intent, requestCode);
			}
			
			@Override
			protected void onActivityResult(int requestCode, int resultCode, Intent data) {
				// TODO Auto-generated method stub
				super.onActivityResult(requestCode, resultCode, data);

				if (SdCardUtil.checkSdCard() == true) {
					if (requestCode == 1  && resultCode==RESULT_OK) {
						Log.e("ERROR","准备显示");
						getImaged(data);
					} else if (requestCode == 2 && resultCode==RESULT_OK) {
						getImaged(data);
					} else if (requestCode == 3 && resultCode==RESULT_OK) {
						getImaged(data);
					} else if (requestCode == 4 && resultCode==RESULT_OK) {
						getImaged(data);
					} else if (requestCode == 5 && resultCode==RESULT_OK) {
						getImaged(data);
					} else if (requestCode == 6 && resultCode==RESULT_OK) {
						getImaged(data);
					}else if(requestCode==7 && resultCode==RESULT_OK){
						ContentResolver resolver = getContentResolver();
						Uri uri=data.getData();
						Bitmap bm=null;
						if(uri!=null){
							try {
								bm = MediaStore.Images.Media.getBitmap(resolver, uri);
								
								Log.e("ERROR","获取路径成功");
							} catch (IOException e) {
								// TODO 自动生成的 catch 块
								e.printStackTrace();
							} 
							
						}
						if(bm!=null){
							Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(bm, 88, 88); 
							allImages.add(allImages.size()-1,resizeBmp);
							
							gridAdapter.notifyDataSetChanged();
							Log.e("ERROR","GirdView显示成功");
							saveImageToFile(bm);
							
						}
							/*String[] proj = {MediaStore.Images.Media.DATA};
						 
				            //好像是android多媒体数据库的封装接口，具体的看Android文档
				            Cursor cursor = managedQuery(uri, proj, null, null, null); 
				            //按我个人理解 这个是获得用户选择的图片的索引值
				            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				            //将光标移至开头 ，这个很重要，不小心很容易引起越界
				            cursor.moveToFirst();
				            //最后根据索引值获取图片路径
				            String path = cursor.getString(column_index);
				            imageUrl.add(path);*/
						//getImaged(data);
						
						//显得到bitmap图片
						/*
						try {
							
							cropImage(uri, 150, 150, 8);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}        */
					}/**
						裁剪完图片
					*/
					else if(requestCode==8 && resultCode==RESULT_OK){
						
						Bitmap photo = null;
						Uri photoUri = data.getData();
						
						if (photoUri != null) {
							photo = BitmapFactory.decodeFile(photoUri.getPath());
							Log.e("ERROR","没图片");
						}
						if (photo == null) {
							Bundle extra = data.getExtras();
							if (extra != null) {
				                photo = (Bitmap)extra.get("data"); 
				                Log.e("ERROR","传入失败");
				                ByteArrayOutputStream stream = new ByteArrayOutputStream();  
				                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				            }  
						}
						if(photo!=null){
							
							Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(photo, 88, 88); 
							allImages.add(allImages.size()-1,resizeBmp);
							Log.e("ERROR","传入成功");
							gridAdapter.notifyDataSetChanged();
							saveImageToFile(photo);
						}
					}

				} else {
					Toast.makeText(context, "SD卡不存在,请检查您的sd卡",
						Toast.LENGTH_LONG).show();
				}
			}
			
			
			public void saveImageToFile(Bitmap bitmap){
				SdCardUtil.createFileDir(SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEPHOTO+"/");
				//FileOutputStream fos = null;
				String fileName=SdCardUtil.getSdPath()+SdCardUtil.FILEDIR+"/"+SdCardUtil.FILEPHOTO+"/"+TimeUtil.getCurrentTimeForImage();
			
				if(fileName.length()>0){
					imageUrl.add(fileName);
				}
				Log.e("filename", fileName);
				  ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			        int options = 80;
			        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);  
			        while (baos.toByteArray().length / 1024 > 300) {   
			            baos.reset();  
			            options -= 10;  
			            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);  
			        }  
			        try {  
			            FileOutputStream fos = new FileOutputStream(fileName);  
			            fos.write(baos.toByteArray());  
			            fos.flush();  
			            fos.close();  
			        } catch (Exception e) {  
			            e.printStackTrace();  
			        }  
			}
			
	private void initevent(){
		//单选框
		if(man.isChecked()==true){
			content.setSex("男");
		}else{
			content.setSex("女");
		}
		 radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
             
	            @Override  
	            public void onCheckedChanged(RadioGroup group, int checkedId) {  
	                // TODO Auto-generated method stub  
	                if(checkedId==man.getId())  
	                {  
	                	content.setSex("男");
	                }else  
	                {  
	                	content.setSex("女");
	                }  
	            }  
	        }); 
		 //-----------------------------------------------
		
		 titlebar.setBackgroundColor(getResources().getColor(R.color.blue));
		 
		 titlebar.showLeft("", getResources().getDrawable(R.drawable.app_back), new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		 titlebar.showLeftAndRight("发布咨询", getResources().getDrawable(R.drawable.app_back),getResources().getDrawable(R.drawable.edit),null,new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					uploadpics();
					
				}
			});
	}
	private void getspinnerdate(){
	if(AppUtils.checkNetwork(context)==true){
		List<NameValuePair> allP=new ArrayList<NameValuePair>();
		new	HttpUtil().doPost(HttpConstants.HTTP_SELECTION, allP, new ResultCallback() {

			@SuppressLint("NewApi")
			@Override
			public void getReslt(String result) {
				// TODO 自动生成的方法存根
				if(!result.isEmpty() ){
					Log.e("Tag", result);
					try{
					BaseBean b=JSON.parseObject(result, BaseBean.class);
				
					if("0".equals(b.getCode().toString().trim())){
						
						JSONObject js=(JSONObject) JSON.parse(b.getData());
						
						JSONObject bwztid=(JSONObject) JSON.parse(js.getString("bwztclassarr"));
						JSONObject bwztname=(JSONObject) JSON.parse(js.getString("bwztdivisionarr"));
						Iterator it=bwztname.keySet().iterator();
						Iterator iterator=bwztid.keySet().iterator();
						
						while (iterator.hasNext()){ 
							
							//
							String s=iterator.next().toString();
							Log.e("str", s);
						
							
						
							mapid.put(bwztid.getString(s),s);
							listclass.add(bwztid.getString(s));
						
						}
						while(it.hasNext()){
							String s=it.next().toString();
							mapname.put(bwztname.getString(s), s);
							listvisor.add(bwztname.getString(s));
						}
						initspinner();
						close();
						
				
						
						
					}else{
						Toast.makeText(context, "获取分类失败", Toast.LENGTH_LONG).show();
					
					}}catch(Exception e){
						
					}
				
					}else{
						Toast.makeText(context, "服务器响应失败", Toast.LENGTH_LONG).show();
						
					}
				
			}
			
			
		});}else{
			Toast.makeText(context, "网络连接断开", Toast.LENGTH_LONG).show();
		}
}
	
	public void showToast(String str){
		Toast.makeText(ConsultActivity.this, str, Toast.LENGTH_LONG).show();
	}
	@Override
	protected void onStart() {
		
		super.onStart();
	
		
	}
	private void initspinner() {
		
		ArrayAdapter<String> adapterclass=new ArrayAdapter<String>(context,R.layout.spinner_item,listclass); 
		adapterclass.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		bwztclassid.setAdapter(adapterclass);
		bwztclassid.setSelection(1,true);
		bwztclassid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				content.setBwztclassid(mapid.get(listclass.get(arg2)));
				Log.e("classid", content.getBwztclassid());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
				content.setBwztclassid(mapid.get(listclass.get(0)));
				Log.e("classid", content.getBwztclassid());
			}
			
		});
		
		ArrayAdapter<String> adaptervisor=new ArrayAdapter<String>(context,R.layout.spinner_item,listvisor); 
		adaptervisor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		bwztvisorid.setAdapter(adaptervisor);
		bwztvisorid.setSelection(0,true);
		bwztvisorid.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				content.setBwztdivisionid(mapname.get(listvisor.get(arg2)));
				Log.e("visor", content.getBwztdivisionid());
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO 自动生成的方法存根
				content.setBwztdivisionid(mapname.get(listvisor.get(0)));
				Log.e("visor", content.getBwztdivisionid());
			}
			
		});
		//selection.setOnItemSelectedListener(listener);    
	}
	//上传图片
	@SuppressWarnings("static-access")
	private void uploadpics(){
		
		HashMap<String,String> p=new HashMap<String, String>();
		p.put("uid", userinfo.get(2).toString());
		p.put("topicid", String.valueOf(0));
		p.put("ac", "upload");
		p.put("albumid",String.valueOf(0));
		p.put("op", "uploadphoto2");
		p.put("uploadsubmit2", "true");
		List<File> allFile=new ArrayList<File>();
		for(int i=0;i<imageUrl.size();i++){
			File f=new File(imageUrl.get(i));
			allFile.add(f);
		}
		
		final List<Integer> uploadstuats=new ArrayList<Integer>();
		final int filecount=allFile.size();
		if( filecount>0){
		for(File f:allFile){
			
			UploadPicture upload=new UploadPicture(HttpConstants.HTTP_UPLOAD.replace("!", userinfo.get(1).toString()), p, null, "attach", f, new ResultCallback(){

				@SuppressLint("NewApi")
				@Override
				public void getReslt(String result) {
					Log.e("Upload",result);
					if(!result.isEmpty() ){
						BaseBean b=JSON.parseObject(result, BaseBean.class);
						if("0".equals(b.getCode().toString().trim())){
							try{
							JSONObject js=(JSONObject) JSON.parse(b.getData());
							JSONObject pic=(JSONObject) JSON.parse(js.getString("pic"));
							String picid=pic.getString("picid");
							Listpicid.add(picid);
							uploadstuats.add(0);
							if(uploadstuats.size()==filecount){
								if(!uploadstuats.contains(1)){
									sentconsult(Listpicid);
								}else{
									Toast.makeText(ConsultActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
								}
							
							}
							}catch(Exception e){
								Toast.makeText(ConsultActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
								return;
							}
							
						}else{
							Toast.makeText(ConsultActivity.this, "图片上传失败", Toast.LENGTH_LONG).show();
							uploadstuats.add(1);
						
						}
					
						}else{
							Toast.makeText(ConsultActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
							uploadstuats.add(1);
							
						}
					
				}
			
			});
			upload.PicPost();
		}
		}else{
			sentconsult(Listpicid);
		}
	
	
	}
	//发布咨询
	@SuppressLint("NewApi")
	private void sentconsult(List<String> listpicid){
		content.setAge(age.getText().toString().trim());
		content.setSubject(subject.getText().toString().trim());
		content.setMessage(message.getText().toString().trim());
		if(AppUtils.checkNetwork(ConsultActivity.this)==true){
			List<NameValuePair> allP=new ArrayList<NameValuePair>();
			allP.add(new BasicNameValuePair("m_auth",userinfo.get(1).toString())); 
			allP.add(new BasicNameValuePair("subject",content.getSubject())); 
			allP.add(new BasicNameValuePair("bwztclassid",content.getBwztclassid())); 
			allP.add(new BasicNameValuePair("bwztdivisionid",content.getBwztdivisionid())); 
			allP.add(new BasicNameValuePair("sex",content.getSex())); 
			allP.add(new BasicNameValuePair("age",content.getAge())); 
			allP.add(new BasicNameValuePair("message",content.getMessage())); 
			allP.add(new BasicNameValuePair("makefeed","1")); 
			allP.add(new BasicNameValuePair("bwztsubmit","true")); 
			allP.add(new BasicNameValuePair("formhash","677b6b45"));
			if(listpicid.size()!=0){
				for(int i=0;i<listpicid.size();i++){
					allP.add(new BasicNameValuePair(" picids["+listpicid.get(i)+"]",String.valueOf(i)));
				}
				
				
			}
			initProgressDialog();
			new	HttpUtil().doPost(HttpConstants.HTTP_WRITE_CONSULT.replace("@", userinfo.get(1).toString()), allP, new ResultCallback() {

				@Override
				public void getReslt(String result) {
					// TODO 自动生成的方法存根
					if(!result.isEmpty() ){
						BaseBean b=JSON.parseObject(result, BaseBean.class);
						if("0".equals(b.getCode().toString().trim())){
							
							Toast.makeText(ConsultActivity.this, "发布成功", Toast.LENGTH_LONG).show();
							close();
							
						}else{
							Toast.makeText(ConsultActivity.this, "发布失败", Toast.LENGTH_LONG).show();
						
						}
					
						}else{
							Toast.makeText(ConsultActivity.this, "服务器响应失败", Toast.LENGTH_LONG).show();
							
						}
					
				}
				
				
			});}else{
				Toast.makeText(ConsultActivity.this, "网络连接断开", Toast.LENGTH_LONG).show();
			}
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(delBroadCast);
		super.onDestroy();
	}
}
