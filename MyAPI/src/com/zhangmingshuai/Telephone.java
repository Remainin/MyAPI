/**
 * 
 */
package com.zhangmingshuai;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;


/**
 * CreateDate：2017-4-20下午11:39:00
 * Location：HIT
 * Author: Zhang Mingshuai
 * TODO
 * return
 */
public class Telephone {
	public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
 
    //配置您申请的KEY
    public static final String APPKEY ="48e639a71bbd471c48831f2116c75579";
 
    //1.手机归属地查询
    public static String[] getRequest1(String tel){
  
        String result =null;
        String[] back = new String[6];
        String url ="http://apis.juhe.cn/mobile/get";//请求接口地址
        Map params = new HashMap();//请求参数
            params.put("phone",tel.substring(0, 7));//需要查询的手机号码或手机号码前7位
            params.put("key",APPKEY);//应用APPKEY(应用详细页查询)
            params.put("dtype","");//返回数据的格式,xml或json，默认json
 
        try {
            result =net(url, params, "GET");
            JSONObject object = JSONObject.fromObject(result);
            if(object.getInt("error_code")==0){
            	String a[] = object.get("result").toString().split(",|:|\\{|\\}");
            	back[0] = a[2];
            	back[1] = a[4];
            	back[2] = a[6];				//将得到的json型结果，提取有用的部分保存在字符串数组中
            	back[3] = a[8];
            	back[4] = a[10];
            	back[5] = a[12];
            	System.out.println(a[0]);
                System.out.println(object.get("result"));
            }else{
            	back[0] = "error_code";
            	back[1] = object.get("error_code").toString();
            	back[2] = "reason";
            	back[3] = (String)object.get("reason").toString();
                System.out.println(object.get("error_code")+":"+object.get("reason"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		return back;
    }
 
 
 
//    public static void main(String[] args) {
//    	String[] a = Telephone.getRequest1("18245028537");
//    	System.out.println(a[0]);
//    	System.out.println(a[1]);
//    	System.out.println(a[2]);
//    	System.out.println(a[3]);
//    	System.out.println(a[4]);
//    	System.out.println(a[5]);
//    }
 
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map params,String method) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(urlencode(params));
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                 
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
 
    //将map型转为请求参数型
    public static String urlencode(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}
