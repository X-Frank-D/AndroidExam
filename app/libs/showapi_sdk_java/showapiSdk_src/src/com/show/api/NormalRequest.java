package com.show.api;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.show.api.util.ShowApiLogger;
import com.show.api.util.ShowApiUtils;
import com.show.api.util.WebUtils;


/**
 * 通用的客户端。
 */
public class NormalRequest   {
	protected int connectTimeout = 3000;//3秒
	protected int readTimeout = 15000;//15秒
	protected String charset="utf-8";  //出去时的编码
	protected String charset_out="utf-8";  //读入时的编码，本来读入时编码可以程序自动识别，但有些网站输出头定义的是utf，但实际是gbk，此时就需要定义字段
	Proxy proxy =null;
	protected boolean printException = true; //是否输出异常

	public boolean isPrintException() {
		return printException;
	}

	public NormalRequest  setPrintException(boolean printException) {
		this.printException = printException;
		return this;
	}

	public NormalRequest setProxy(String proxyIp,int proxyPort) {
		this.proxy=  new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyIp, proxyPort));
		return this;
	}
	
	public Proxy getProxy( ) {
		return this.proxy;
	}

	public String getCharset_out() {
		return charset_out;
	}

	public void setCharset_out(String charset_out) {
		this.charset_out = charset_out;
	}


	protected String url;

	protected Map<String,String> textMap=new HashMap<String, String>();
	protected Map<String,File> uploadMap=new HashMap<String, File>();
	protected Map<String,String> headMap=new HashMap<String, String>();
	protected Map<String,List<String>> res_headtMap=new HashMap<String,List<String>>();//返回时的Map
	
	public Map<String, List<String>> getRes_headtMap() {
		return res_headtMap;
	}

	public void setRes_headtMap(Map<String, List<String>> res_headtMap) {
		this.res_headtMap = res_headtMap;
	}

	public NormalRequest( String url   ) {
		this.url=url;
	}
	
	
	public Map<String, String> getTextMap() {
		return textMap;
	}
	public void setTextMap(Map<String, String> textMap) {
		this.textMap = textMap;
	}
	public String getUrl() {
		return url;
	}
	public String getCharset() {
		return charset;
	}
	public Map<String, File> getUploadMap() {
		return uploadMap;
	}
	public void setUploadMap(Map<String, File> uploadMap) {
		this.uploadMap = uploadMap;
	}
	public Map<String, String> getHeadMap() {
		return headMap;
	}
	public void setHeadMap(Map<String, String> headMap) {
		this.headMap = headMap;
	}
	public int getConnectTimeout() {
		return connectTimeout;
	}
	public int getReadTimeout() {
		return readTimeout;
	}
	public NormalRequest setConnectTimeout(int connectTimeout) {
		this.connectTimeout = connectTimeout;
		return this;
	}
	public NormalRequest setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
		return this;
	}
	public NormalRequest setCharset(String charset) {
		this.charset=charset;
		return this;
	}
//	DEFAULT_CHARSET

	/**
	 * 设置客户端与showapi网关的最大长连接数量。
	 */
	public NormalRequest setUrl(String url) {
		this.url=url;
		return this;
	}
	
	
	/**
	 * 添加post体的字符串参数
	 */
	public NormalRequest addTextPara(String key,String value) {
		this.textMap.put(key,value);
		return this;
	}
	
	/**
	 * 添加post体的上传文件参数
	 */
	public NormalRequest addFilePara(String key,File item) {
		this.uploadMap.put(key,item);
		return this;
	}
	/**
	 * 添加head头的字符串参数
	 */
	public NormalRequest addHeadPara(String key,String value) {
		this.headMap.put(key,value);
		return this;
	}
	
	public String post()   {
		String res="";
		try {
			res= WebUtils.doPost(this);
		} catch (Exception e) {
			if(printException)e.printStackTrace();
			res="{ret_code:-1,error:\""+e.toString()+"\"}";
		}
		return res;
	}
	
	public byte[] postAsByte()   {
		byte res[]=null;
		try {
			res= WebUtils.doPostAsByte(this);
		} catch (Exception e) {
			if(printException)e.printStackTrace();
			try {
				res=("{ret_code:-1,error:\""+e.toString()+"\"}").getBytes("utf-8");
			} catch (UnsupportedEncodingException e1) {
				if(printException)e1.printStackTrace();
			}
		}
		return res;
	}
	
	
	public String get()   {
		String res="";
		try {
			res= WebUtils.doGet(this);
		} catch (Exception e) {
			if(printException)e.printStackTrace();
			res="{ret_code:-1,error:\""+e.toString()+"\"}";
		}
		return res;
	}
	
	public byte[] getAsByte()   {
		byte[]  res=null;
		try {
			res=WebUtils.doGetAsByte(this);
		} catch (Exception e) {
			if(printException)e.printStackTrace();
			try {
				res=("{ret_code:-1,error:\""+e.toString()+"\"}").getBytes("utf-8");
			} catch (UnsupportedEncodingException e1) {
				if(printException)e1.printStackTrace();
			}
		}
		return res;
	}
	
	public static void main(String adfas[]) throws  Exception{ 
		NormalRequest req =new NormalRequest("http://220.163.43.26:8080/ynviowww/surveil/index")
		.addHeadPara("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2427.7 Safari/537.36");
		byte b[]=req.getAsByte();
		String str=new String(b,"utf-8");
		System.out.println(str); 
//		Calendar now=Calendar.getInstance();
//		now.add(Calendar.DATE, -10);
////		now.get(Calendar.WEEK_OF_YEAR);
//		System.out.println(now.get(Calendar.DAY_OF_YEAR));
		
		Map map=req.getRes_headtMap();
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			Object k=it.next();
			System.out.println(k+"          "+map.get(k));
		}
				

	}
	
}
