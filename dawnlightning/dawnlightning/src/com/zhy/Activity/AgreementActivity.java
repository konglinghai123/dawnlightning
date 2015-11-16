package com.zhy.Activity;


import com.zhy.Util.HttpConstants;
import com.dawnlightning.ucqa.R;

import android.os.Bundle;
import android.webkit.WebView;

public class AgreementActivity extends BaseActivity {

	private WebView webView;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		
		
		setContentView(R.layout.agreement);
		webView=(WebView)findViewById(R.id.myWebView);
		
		//webView.loadUrl(HttpConstants.HTTP_AGREEMENT);
		 
	}
	
	
}
