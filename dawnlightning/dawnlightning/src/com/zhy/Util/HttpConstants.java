package com.zhy.Util;

public class HttpConstants {

	
	public final static String HTTP_HEAD="https://";
	
	
	
	//外网
	//public final static String HTTP_IP="ucqa.zorigin.top/";
	public final static String HTTP_IP="ucqa.dawnlightning.com/";
	
	//public final static String HTTP_IP="ucqa.hy.wongze.net:8080/";
	public final static String HTTP_CONTEXT="/capi/";
	
	public final static String HTTP_REQUEST=HTTP_HEAD+HTTP_IP+HTTP_CONTEXT;
	
	public final static String HTTP_FRIST_REGISTER=HTTP_REQUEST+"do.php?ac=register&op=seccode";//验证码
	public final static String HTTP_LOGIN=HTTP_REQUEST+"do.php?ac=login&username=@&password=*&loginsubmit=true";//登陆
	public final static String HTTP_REGISTER=HTTP_REQUEST+"do.php?ac=register&registersubmit=true&username=!&password=@&password2=#&seccode=$&m_auth=%";
	

	
	public final static String HTTP_CONSULT_DETAIL=HTTP_REQUEST+"space.php?do=bwzt&uid=!&id=@";
	
	public final static String HTTP_FORMHASH=HTTP_REQUEST+"cp.php?ac=bwztformhash&m_auth=!";
	
	public final static String HTTP_COMMENT=HTTP_REQUEST+"cp.php?ac=comment&inajax=1&m_auth=!";
	
	public final static String HTTP_SELECTION=HTTP_REQUEST+"space.php?do=bwzt&view=class";
	
	public final static String HTTP_SELECTION_EDITOR=HTTP_REQUEST+"cp.php?ac=bwzt&m_auth=!";
	
	public final static String HTTP_UPLOAD=HTTP_REQUEST+"cp.php?ac=upload&m_auth=!";
	
	public final static String HTTP_CONSULT_ALL=HTTP_REQUEST+"space.php?do=bwzt&view=all&orderby=dateline&page=#";
	
	public final static String HTTP_CONSULT=HTTP_REQUEST+"space.php?uid=!&do=bwzt&view=me&m_auth=@&page=#";
	
	public final static String HTTP_CONSULT_ALL_CLASS=HTTP_REQUEST+"space.php?do=bwzt&view=all&bwztclassid=!&page=#&orderby=dateline";
	
	public final static String HTTP_WRITE_CONSULT=HTTP_REQUEST+"cp.php?ac=bwzt&m_auth=@";
	
	
	
}
