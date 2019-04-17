package com.bdsoft.database;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;

import com.bdsoft.service.TimeTask;
import com.bdsoft.tools.Address;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



/** 
* @author 李阔  
* @version 创建时间：2017年6月20日 下午2:01:57 
* 类说明    门户推送
* @return 
*/
public class WebServiceMhts {
	
	public static String syncInfoRY() throws Exception
	{
		//TODO  加日志  错误日志
		String sql = "select depsequence,hrid,sequence from hrperson";
		PreparedStatement pst = MysqlCon.mysqlcon.prepareStatement(sql);
		ResultSet  ret = pst.executeQuery();
		List<Map<String,String>> list = new ArrayList<>();
		while(ret.next()){
			Map<String,String> map = new HashMap<>();
			String depsequence = ret.getString("depsequence");
			String hrid = ret.getString("hrid");
			String sequence = ret.getString("sequence");
			map.put("depsequence", depsequence);
			map.put("hrid", hrid);
			map.put("sequence", sequence);
			list.add(map);
		}
//		List<Map<String,Object>> list = JdbcTemplate.queryForList(sql);
		
		 TimeTask.logger.info("人事视图 To 门户 start");
		
		String retun = "";
		
		int size = list.size()/1000;
		
		for(int i = 0;i<size;i++){
			List new_list = new ArrayList();
			for(int j=0;j<1000;j++){
				new_list.add(list.get(i*1000+j));
			}
			JSONObject j = new JSONObject();
			retun = GetSyncInfo(JSONArray.fromObject(new_list).toString());
			 TimeTask.logger.info("人事视图 To 门户 1000条记录调用一次 "+(i+1)+"/"+size);
		}
		
		if((size*1000)<list.size()){
			List new_list = new ArrayList();
			for(int i = size*1000;i<list.size();i++ ){
				new_list.add(list.get(i));
			}
			retun =GetSyncInfo(JSONArray.fromObject(new_list).toString());
			 TimeTask.logger.info("人事视图 To 门户 1000条记录调用一次 补漏");
		}
		ret.close();
		pst.close();
		 TimeTask.logger.info("人事视图 To 门户 end");
		 TimeTask.logger.info(retun);
		return retun;
	}
	public static String syncInfoJG() throws Exception
	{
		String sql = "select orgcode orgcode,seq sequenceno from hrorg";
		PreparedStatement pst = MysqlCon.mysqlcon.prepareStatement(sql);
		ResultSet  ret = pst.executeQuery();
		List<Map<String,Object>> list = new ArrayList<>();
		while(ret.next()){
			Map<String,Object> map = new HashMap<>();
			String orgcode = ret.getString("orgcode");
			String sequenceno = ret.getString("sequenceno");
			map.put("orgcode", orgcode);
			map.put("sequenceno", sequenceno);
			list.add(map);
		}
		
		String retun =GetSyncInfo(JSONArray.fromObject(list).toString());
		 TimeTask.logger.info(retun);
		return retun;
	}
//	 public final static String ENDPOINT =Address.ENDPOINT;
//	 public final static String SOAPACTION = "http://192.168.170.99/jis/services/SyncInfo";
	public static String GetSyncInfo(String json) throws Exception
	{
		Call call = (Call)new Service().createCall();
		call.setTimeout(new Integer(60000));
        call.setTargetEndpointAddress(new URL(Address.ENDPOINT));
        call.setOperationName(new QName(Address.SOAPACTION,"syncInfo"));
        call.addParameter(new QName(Address.SOAPACTION, "arg0"), XMLType.XSD_STRING, ParameterMode.IN);
        call.setReturnType(XMLType.XSD_STRING);
        	String result = (String) call.invoke(new Object[]{json});
        	 TimeTask.logger.info("接口调用结束");
        	return "";
	}
	public static void main(String[] args) throws Exception {
		WebServiceMhts.GetSyncInfo("sad");
	}
}
