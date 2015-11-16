package com.zhy.Adapter;

import java.util.ArrayList;
import java.util.List;


import com.dawnlightning.ucqa.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.zhy.Bean.ConsultMessageBean;
import com.zhy.Util.HttpConstants;

import com.zhy.loadimage.AsynImageLoader;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ConsultAdapter extends BaseAdapter{
	private ViewHolder holder;
	private LayoutInflater layoutInflater;
	private Context context;
	private List<ConsultMessageBean> list;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	
	public ConsultAdapter(Context c,List<ConsultMessageBean> list) {
		super();
		this.context=c;
		this.list=list;
		layoutInflater = (LayoutInflater) LayoutInflater.from(c);
		imageLoader.init(ImageLoaderConfiguration.createDefault(c));
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.images)
				.showImageOnFail(R.drawable.images).cacheInMemory().cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(20)).imageScaleType(ImageScaleType.IN_SAMPLE_INT)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	
	}
	public void setList(List<ConsultMessageBean> list){
		this.list = list;
	}
	public void addList(List<ConsultMessageBean> list){
		this.list.addAll(list);
 	}
	public void clearList(){
		this.list.clear();
 	}
	public List<ConsultMessageBean> getList(){
		return list;
	}
	public void removeItem(int position){
		if(list.size() > 0){
			list.remove(position);
		}
	}
	@Override
	public int getCount() {
		return list.size();
	}
	
	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (null == convertView) {
			// װ�ز����ļ� app_item.xml
			convertView = layoutInflater.inflate(R.layout.consult_items, null);
			holder = new ViewHolder();
		//	holder.id = (TextView) convertView.findViewById(R.id.id);
			holder.suject=(TextView) convertView.findViewById(R.id.subject);
			holder.pic=(ImageView) convertView.findViewById(R.id.consultpic);
			holder.message=(TextView) convertView.findViewById(R.id.message);
			holder.viewnum=(TextView) convertView.findViewById(R.id.numview);
			holder.replynum=(TextView) convertView.findViewById(R.id.numreply);
			holder.time=(TextView) convertView.findViewById(R.id.time);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ConsultMessageBean item = list.get(position); // ��ȡ��ǰ�����
		
		if (null != item) {
			holder.suject.setText(item.getSubject());
			holder.message.setText(Html.fromHtml(item.getMessage()).toString());
			holder.viewnum.setText("浏览:"+item.getViewnum());
			holder.replynum.setText("回复:"+item.getReplynum());
			holder.time.setText(item.getDateline());
			
			if(item.getPic().length()!=0&&item.getPic()!=null){
				//Log.println(6, "XXXXXXXXXXXXXXXXXXXXXxx",item.getPic());
				holder.pic.setVisibility(View.VISIBLE);
				imageLoader.displayImage(HttpConstants.HTTP_HEAD+HttpConstants.HTTP_IP+item.getPic(), holder.pic, options);
				//AsynImageLoader.showImageAsyn(holder.pic, item.getPic().toString(), R.drawable.ic_launcher);
			}else{
				holder.pic.setVisibility(holder.pic.GONE);
			
			}
				
		
			
			
		}
		return convertView;
	}
    private class ViewHolder {
    	TextView suject;
    	ImageView pic;
    	TextView message;
    	TextView viewnum;
    	TextView replynum;
    	TextView time;
    }
}
