package com.bdsoft.tools;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** 
* @author ����  
* @version ����ʱ�䣺2017��5��9�� ����4:55:22 
* ��˵��    ���������ļ�
* @return 
*/
public class Address {
	
	public static String logaddr;
	public static String isopen;
	
//	public static  String my_host ;   
//	public static  int my_port;			
//	public static  String my_username;
//	public static  String my_password;
	public static  String my_content;
	public static  String my_flag;
	public static  String my_up_content;
	public static  String my_up_flag;
	
	public static  String C_host;     
	public static  int C_port;			
	public static  String C_username;
	public static  String C_password;
	public static  String C_content;
	public static  String C_flag;
	public static  String C_up_content;
	public static  String C_up_flag;
	
	public static  String A_host;     
	public static  int A_port;			
	public static  String A_username;
	public static  String A_password;
	public static  String A_content;
	public static  String A_flag;
	public static  String A_up_content;
	public static  String A_up_flag;
	
	public static  String A2_host;     
	public static  int A2_port;			
	public static  String A2_username;
	public static  String A2_password;
	public static  String A2_content;
	public static  String A2_flag;
	public static  String A2_up_content;
	public static  String A2_up_flag;
	
	public static String mysql_driver;  //mysql ��ַ
	public static String mysql_url ;
	public static String mysql_username ;
	public static String mysql_pwd;
	
	public static String oracle_driver;  //mysql ��ַ
	public static String oracle_url ;
	public static String oracle_username ;
	public static String oracle_pwd;
	
	public static String oracle_toRz_url ;
	public static String oracle_toRz_username ;
	public static String oracle_toRz_pwd;
	
	public static int time_jg;
	public static String execute_time;
	
	public static String ENDPOINT; // ��Ա �ṹ   hosturl
	public static String SOAPACTION;
	//��ȡ�����ļ�
	public static void setProperties() {
	    Properties pps = new Properties();
	             try {															//	/transfer/resources/my.properties
	                 InputStream in = new BufferedInputStream (new FileInputStream("my.properties"));  
	                 pps.load(in);
	                 logaddr = pps.getProperty("logaddr");
	                 isopen =pps.getProperty("isopen");
	                 
// 	                 my_host =  pps.getProperty("my_host");
// 	                 my_port =  Integer.parseInt(pps.getProperty("my_port"));
//	                 my_username = pps.getProperty("my_username");
//	                 my_password = pps.getProperty("my_password");
	              	 my_content = pps.getProperty("my_content");
	             	 my_flag = pps.getProperty("my_flag");
	             	 my_up_content = pps.getProperty("my_up_content");
	             	 my_up_flag = pps.getProperty("my_up_flag");
	                 
	             	C_host =  pps.getProperty("C_host");
	             	C_port =  Integer.parseInt(pps.getProperty("C_port"));
	             	C_username = pps.getProperty("C_username");
	             	C_password = pps.getProperty("C_password");
	             	C_content = pps.getProperty("C_content");
	             	C_flag = pps.getProperty("C_flag");
	             	C_up_content = pps.getProperty("C_up_content");
	             	C_up_flag = pps.getProperty("C_up_flag");
	             	
	             	A_host =  pps.getProperty("A_host");
	             	A_port =  Integer.parseInt(pps.getProperty("A_port"));
	             	A_username = pps.getProperty("A_username");
	             	A_password = pps.getProperty("A_password");
	             	A_content = pps.getProperty("A_content");
	             	A_flag = pps.getProperty("A_flag");
	             	A_up_content = pps.getProperty("A_up_content");
	             	A_up_flag = pps.getProperty("A_up_flag");
	             	
	             	A2_host =  pps.getProperty("A2_host");
	             	A2_port =  Integer.parseInt(pps.getProperty("A2_port"));
	             	A2_username = pps.getProperty("A2_username");
	             	A2_password = pps.getProperty("A2_password");
	             	A2_content = pps.getProperty("A2_content");
	             	A2_flag = pps.getProperty("A2_flag");
	             	A2_up_content = pps.getProperty("A2_up_content");
	             	A2_up_flag = pps.getProperty("A2_up_flag");


	                 mysql_driver = pps.getProperty("mysql_driver");
	                 mysql_url = pps.getProperty("mysql_url");
	                 mysql_username = pps.getProperty("mysql_username");
	                 mysql_pwd = pps.getProperty("mysql_pwd");
	                 
	                 oracle_driver = pps.getProperty("oracle_driver");
	                 oracle_url = pps.getProperty("oracle_url");
	                 oracle_username = pps.getProperty("oracle_username");
	                 oracle_pwd = pps.getProperty("oracle_pwd");
	                 
	                 oracle_toRz_url = pps.getProperty("oracle_toRz_url");
	                 oracle_toRz_username = pps.getProperty("oracle_toRz_username");
	                 oracle_toRz_pwd = pps.getProperty("oracle_toRz_pwd");
	                 
	                 time_jg = Integer.parseInt(pps.getProperty("time_jg").toString());   //ʱ����
	                 execute_time = pps.getProperty("execute_time");   //ִ��ʱ��
	                 
	                 ENDPOINT=pps.getProperty("ENDPOINT"); 
	                 SOAPACTION = pps.getProperty("SOAPACTION");
	               
	           }catch (IOException e) {
	                e.printStackTrace();
	             }
	}
}
