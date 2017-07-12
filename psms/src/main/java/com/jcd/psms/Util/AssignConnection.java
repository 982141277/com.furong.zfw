package com.jcd.psms.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public abstract class AssignConnection {
	public InputStream in;
	public PrintWriter out;
	public URLConnection connection;
	public String conid;
	public URL url;
	private boolean islistener;
	private boolean isrun = true;



	public AssignConnection(String assignUrl,String key){
		try {
			URL url = new URL(assignUrl);
			this.url = url;
			this.connection = url.openConnection();
			this.connection.setDoOutput(true);
			this.islistener=true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static AssignConnection getcon(String assignUrl,String key){
		AssignConnection ac =  new AssignConnection(assignUrl,key) {
			@Override
			public void mage(String str) {
			}
		};
		ac.islistener=false;
		return ac;
	}
	
	public void close(){
		isrun = false;
//		try {
//			connection.getInputStream().close();
//		} catch (IOException e) {
//
//		}
//		try {
//			connection.getOutputStream().close();
//		} catch (IOException e) {
//
//		}

	}
	
	public String connect(Map<String,String> param){
		try{
			connection.connect();
		}catch (IOException e) {
			try {
				connection = this.url.openConnection();
				connect(param);
			} catch (IOException e1) {
				return "";
			}
		}
		
		try {
			
			out = new PrintWriter(connection.getOutputStream());
			if(param!=null)
			for(String key:param.keySet()){
				out.println("&"+key+"="+param.get(key));
			}
			out.flush();
			out.close();
			
		} catch (IOException e) {
			
			e.printStackTrace();
			return "";
		}
		
		
		if(islistener){
			try {
				mage1();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			return "";
		}else{
			
			try {
				return mage2();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		}
		
	}
	
	public abstract void mage(String str)  throws IOException;
	
	public String mage2()throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
		int i=10;
		StringBuffer sb = new StringBuffer();
		String line;
		while(i>0&&isrun){
			while ((line = br.readLine()) != null&&isrun) {
				if(line!=null&&!line.isEmpty())
					sb.append(line);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i--;
		}
		br.close();
		return sb.toString();
	}
	
	public void mage1()throws IOException{
		BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		int i=30;
		String line;
		while(i>0&&isrun){
			while ((line = br.readLine()) != null&&isrun) {
				if(line!=null&&!line.isEmpty())
				mage(line);
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i--;
		}
		br.close();
	}
	
	
//	public static void main(String[] arg) throws InterruptedException, IOException{
//
//		String URL_ASSIGN = "http://localhost:8080/check_center/meinn.mn?c=connection&f=getuserauthor";
//		int num=500;
//		AssignConnection con = new AssignConnection(URL_ASSIGN,"jfjk") {
//			@Override
//			public void mage(String str) {
//
//
//			}
//		};
//		Map map = new HashMap();
//
//		con.connect(map);
//
//	}
	
}
