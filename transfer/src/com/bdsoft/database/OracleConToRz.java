package com.bdsoft.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.PropertyConfigurator;

import com.bdsoft.service.TimeTask;
import com.bdsoft.tools.Address;


public class OracleConToRz {
	 
	public static  void  LoadRzFromOracle()
	{
	    Connection con = null;// 创建一个数据库连接
	    try
	    {
	        Class.forName(Address.oracle_driver);// 加载Oracle驱动程序
	        String url = Address.oracle_toRz_url;// 127.0.0.1是本机地址，orcl是精简版Oracle的默认数据库名
	        String user = Address.oracle_toRz_username; 
	        String password =Address.oracle_toRz_pwd;// 你安装时选设置的密码
	        con = DriverManager.getConnection(url, user, password);// 获取连接
	        MysqlCon.MysqlCon();
//	        MysqlCon.mysqlcon.setAutoCommit(false); //mysql 手动提交
	        TimeTask.logger.info("con  ok.. 正在查询 ..");
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
	        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	        //oracle数据读取
	        
	       
	        String module_name = "研究室日志";//yjs
//	        String module_name = "舆情监控协调日志";//cy创意
//	        String module_name = "业务管理中心日志";//xts协调室
//	        String module_name = "研究院工作日志";//yjy
//	        String module_name = "智库办工作日志";//zkb
	        //查询 模块id
			String module_sql = "select MODULE_ID from t_log_module where MODULE_NAME= '"+module_name+"' ";
			PreparedStatement pst1 = MysqlCon.mysqlcon.prepareStatement(module_sql);
			 ResultSet  ret1 = pst1.executeQuery();
			 String module_id="";
			 if(ret1.next()){
				 //模块id
				  module_id = ret1.getString("MODULE_ID") ;
			 }
			
			 //根据模块id  查询tab表中的页签和文本标签
			 String tab_sql = "select guid,TAB_TYPE,TAB_NAME from t_log_tab where MODULE_ID = '"+module_id+"' and TAB_ZT = '1' ";
			 PreparedStatement pst2 = MysqlCon.mysqlcon.prepareStatement(tab_sql);
			 ResultSet  ret2 = pst2.executeQuery();
			
			 List sql_list2 = new ArrayList();
			 //标签名称  标签id  标签类型（页签）  
			 while (ret2.next()) {  
				 Map sql_map2 = new HashMap();
				 sql_map2.put("guid", ret2.getString("guid"));
				 sql_map2.put("TAB_NAME", ret2.getString("TAB_NAME"));
				 sql_map2.put("TAB_TYPE", ret2.getString("TAB_TYPE"));
				 sql_list2.add(sql_map2);
			 }
			 
			 
			//查询研究室日志表的数据
			String info_sql= "select WIM_ID,WIT_ID,WIT_MODIFYER,case WIT_ID when 1 then '待办事项' when 2 then '已办事项' when 3 then '特别提示' END as WIT_NAME,WIT_CONTENT "
					+ "from TB_YJS_WORKINFO";
			//查询创意（舆情监控协调日志）
//			String info_sql= "select WIM_ID,WIT_ID,WIT_MODIFYER,case WIT_ID when 1 then '待办事项' when 2 then '已办事项' when 3 then '特别提示' END as WIT_NAME,WIT_CONTENT "
//					+ "from TB_CY_WORKINFO";
			//查询协调室（业务管理中心日志）
//			String info_sql= "select WIM_ID,WIT_ID,WIT_MODIFYER,case WIT_ID when 1 then '待办事项' when 2 then '已办事项' when 3 then '特别提示' END as WIT_NAME,WIT_CONTENT "
//					+ "from TB_XTS_WORKINFO";
			//查询研究院
//			String info_sql= "select WIM_ID,WIT_ID,WIT_MODIFYER,case WIT_ID when 1 then '待办事项' when 2 then '已办事项' when 3 then '特别提示' END as WIT_NAME,WIT_CONTENT "
//					+ "from TB_YJY_WORKINFO";
			//查询智库办
//			String info_sql= "select WIM_ID,WIT_ID,WIT_MODIFYER,case WIT_ID when 1 then '待办事项' when 2 then '已办事项' when 3 then '特别提示' END as WIT_NAME,WIT_CONTENT "
//					+ "from TB_ZKB_WORKINFO";
			 PreparedStatement pst3 = con.prepareStatement(info_sql);
			 ResultSet  ret3 = pst3.executeQuery();
			
			 List sql_list3= new ArrayList();
			 //原日志表数据
			 while (ret3.next()) {
				 Map sql_map3 = new HashMap();
				 sql_map3.put("WIM_ID", ret3.getString("WIM_ID"));//ID
				 sql_map3.put("WIT_NAME", ret3.getString("WIT_NAME"));//页签名称
				 sql_map3.put("WIT_CONTENT", ret3.getString("WIT_CONTENT"));//日志内容
				 sql_map3.put("WIT_MODIFYER", ret3.getString("WIT_MODIFYER"));//最后操作人
				 sql_list3.add(sql_map3);
			 }
			 
			 
				//查询研究室数据
				String b_sql = "  SELECT  AA.WIT_CREATER,AA.WIM_ID,AA.WIT_DATE,AA.WIT_CLASS,AA.WIT_LEADER,AA.WIT_SECRET,AA.LAST_EDITER FROM "
						+ "(SELECT WIM_ID,WIT_DATE,WIT_CREATER,WIT_CLASS,WIT_LEADER,WIT_SECRET,LAST_EDITER FROM TB_YJS_WORKINFOMAIN )AA "
						+ " LEFT JOIN  "
						+ "(SELECT SUM(B.RZ),B.WIM_ID FROM (SELECT 1 AS RZ ,WIM_ID FROM TB_YJS_WORKINFO ) B "
						+ "GROUP BY B.WIM_ID)BB ON AA.WIM_ID = BB.WIM_ID";
				//查询创意（舆情监控协调日志）
//				String b_sql = "  SELECT  AA.WIT_CREATER,AA.WIM_ID,AA.WIT_DATE,AA.WIT_CLASS,AA.WIT_LEADER,AA.WIT_SECRET,AA.LAST_EDITER FROM "
//						+ "(SELECT WIM_ID,WIT_DATE,WIT_CREATER,WIT_CLASS,WIT_LEADER,WIT_SECRET,LAST_EDITER FROM TB_CY_WORKINFOMAIN )AA "
//						+ " LEFT JOIN  "
//						+ "(SELECT SUM(B.RZ),B.WIM_ID FROM (SELECT 1 AS RZ ,WIM_ID FROM TB_CY_WORKINFO ) B "
//						+ "GROUP BY B.WIM_ID)BB ON AA.WIM_ID = BB.WIM_ID";
				//查询协调室（业务管理中心日志）
//				String b_sql = "  SELECT  AA.WIT_CREATER,AA.WIM_ID,AA.WIT_DATE,AA.WIT_CLASS,AA.WIT_LEADER,AA.WIT_SECRET,AA.LAST_EDITER FROM "
//						+ "(SELECT WIM_ID,WIT_DATE,WIT_CREATER,WIT_CLASS,WIT_LEADER,WIT_SECRET,LAST_EDITER FROM TB_XTS_WORKINFOMAIN )AA "
//						+ " LEFT JOIN  "
//						+ "(SELECT SUM(B.RZ),B.WIM_ID FROM (SELECT 1 AS RZ ,WIM_ID FROM TB_XTS_WORKINFO ) B "
//						+ "GROUP BY B.WIM_ID)BB ON AA.WIM_ID = BB.WIM_ID";
				//查询研究院
//				String b_sql = "  SELECT  AA.WIT_CREATER,AA.WIM_ID,AA.WIT_DATE,AA.WIT_CLASS,AA.WIT_LEADER,AA.WIT_SECRET,AA.LAST_EDITER FROM "
//						+ "(SELECT WIM_ID,WIT_DATE,WIT_CREATER,WIT_CLASS,WIT_LEADER,WIT_SECRET,LAST_EDITER FROM TB_YJY_WORKINFOMAIN )AA "
//						+ " LEFT JOIN  "
//						+ "(SELECT SUM(B.RZ),B.WIM_ID FROM (SELECT 1 AS RZ ,WIM_ID FROM TB_YJY_WORKINFO ) B "
//						+ "GROUP BY B.WIM_ID)BB ON AA.WIM_ID = BB.WIM_ID";
				//查询智库办
//				String b_sql = "  SELECT  AA.WIT_CREATER,AA.WIM_ID,AA.WIT_DATE,AA.WIT_CLASS,AA.WIT_LEADER,AA.WIT_SECRET,AA.LAST_EDITER FROM "
//						+ "(SELECT WIM_ID,WIT_DATE,WIT_CREATER,WIT_CLASS,WIT_LEADER,WIT_SECRET,LAST_EDITER FROM TB_ZKB_WORKINFOMAIN )AA "
//						+ " LEFT JOIN  "
//						+ "(SELECT SUM(B.RZ),B.WIM_ID FROM (SELECT 1 AS RZ ,WIM_ID FROM TB_ZKB_WORKINFO ) B "
//						+ "GROUP BY B.WIM_ID)BB ON AA.WIM_ID = BB.WIM_ID";
				 PreparedStatement pst4 = con.prepareStatement(b_sql);
				 ResultSet  ret4 = pst4.executeQuery();
				
				 List sql_list4= new ArrayList();
				 //原表白班数据数据
				 while (ret4.next()) {  
					 Map sql_map4 = new HashMap();
					 
					 sql_map4.put("WIM_ID", ret4.getString("WIM_ID"));//id
					 sql_map4.put("WIT_CREATER", ret4.getString("WIT_CREATER"));//创建人
					 sql_map4.put("WIT_DATE", ret4.getString("WIT_DATE"));//时间
					 sql_map4.put("LAST_EDITER", ret4.getString("LAST_EDITER"));//最后操作人
					 sql_map4.put("WIT_CLASS", ret4.getString("WIT_CLASS"));//值班类型1：白班  2：夜班
					 sql_map4.put("WIT_LEADER", ret4.getString("WIT_LEADER"));//值班领导
					 sql_map4.put("WIT_SECRET", ret4.getString("WIT_SECRET"));//值班秘书
					 sql_list4.add(sql_map4);
				 }
				
		
			
			if (sql_list4.size() > 0) {
				for (int k = 0; k < sql_list4.size(); k++) {
					Map<String, Object> list4_map = (Map<String, Object>) sql_list4.get(k);

					// 向工作日志表中插入数据
					String sql_sel = "insert into t_log_record(GUID,MODULE_ID,TITLE,LOG_TYPE,LAST_USER,DOUSER,DODATE)  values (?,?,?,?,?,?,?)";
					MysqlCon.mysqlpsts = MysqlCon.mysqlcon.prepareStatement(sql_sel);

					String user_id = list4_map.get("LAST_EDITER") == null ? ""
							: list4_map.get("LAST_EDITER").toString();
					String create_id = list4_map.get("WIT_CREATER") == null ? ""
							: list4_map.get("WIT_CREATER").toString();
					String guid = UUID.randomUUID().toString();
					// insert到mysql数据库中
					MysqlCon.mysqlpsts.setString(1, guid);
					MysqlCon.mysqlpsts.setString(2, module_id);
					MysqlCon.mysqlpsts.setString(3, "默认标题");
					MysqlCon.mysqlpsts.setString(4, "1");
					MysqlCon.mysqlpsts.setString(5, user_id);
					MysqlCon.mysqlpsts.setString(6, create_id);
					MysqlCon.mysqlpsts.setString(7, sd.format(new Date()));
					MysqlCon.mysqlpsts.execute();

					String sql = "insert into t_log_tab_relation (GUID,LOG_ID,TAB_ID,TAB_CONT,DODATE,DOUSER)  values (?,?,?,?,?,?)";
					MysqlCon.mysqlpsts = MysqlCon.mysqlcon.prepareStatement(sql);
					String wim_id = "";
					if (sql_list3.size() > 0) {
						for (int i = 0; i < sql_list3.size(); i++) {
							Map<String, Object> sql_map = (Map<String, Object>) sql_list3.get(i);
							if (list4_map.get("WIM_ID").equals(sql_map.get("WIM_ID"))) {
								wim_id = sql_map.get("WIM_ID").toString();
								if (sql_map.get("WIT_NAME").equals("待办事项")) {
									if (sql_list2.size() > 0) {
										for (int j = 0; j < sql_list2.size(); j++) {
											Map<String, Object> list2_map = (Map<String, Object>) sql_list2.get(j);
											if (list2_map.get("TAB_NAME").equals("待办事项")) {
												MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
												MysqlCon.mysqlpsts.setString(2, guid);
												MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
												MysqlCon.mysqlpsts.setString(4, sql_map.get("WIT_CONTENT") == null ? ""
														: sql_map.get("WIT_CONTENT").toString());
												MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
												MysqlCon.mysqlpsts.setString(6, user_id);
												MysqlCon.mysqlpsts.execute();
											}

										}
									}

								} else if (sql_map.get("WIT_NAME").equals("特别提示")) {
									if (sql_list2.size() > 0) {
										for (int j = 0; j < sql_list2.size(); j++) {
											Map<String, Object> list2_map = (Map<String, Object>) sql_list2.get(j);
											if (list2_map.get("TAB_NAME").equals("特别提示")) {
												MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
												MysqlCon.mysqlpsts.setString(2, guid);
												MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
												MysqlCon.mysqlpsts.setString(4, sql_map.get("WIT_CONTENT") == null ? ""
														: sql_map.get("WIT_CONTENT").toString());
												MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
												MysqlCon.mysqlpsts.setString(6, user_id);
												MysqlCon.mysqlpsts.execute();
											}

										}
									}
								} else if (sql_map.get("WIT_NAME").equals("已办事项")) {
									if (sql_list2.size() > 0) {
										for (int j = 0; j < sql_list2.size(); j++) {
											Map<String, Object> list2_map = (Map<String, Object>) sql_list2.get(j);
											if (list2_map.get("TAB_NAME").equals("已办事项")) {
												MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
												MysqlCon.mysqlpsts.setString(2, guid);
												MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
												MysqlCon.mysqlpsts.setString(4, sql_map.get("WIT_CONTENT") == null ? ""
														: sql_map.get("WIT_CONTENT").toString());
												MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
												MysqlCon.mysqlpsts.setString(6, user_id);
												MysqlCon.mysqlpsts.execute();
											}

										}
									}
								}

							}
						}
					}

					if (list4_map.get("WIM_ID").equals(wim_id)) {
						if (sql_list2.size() > 0) {
							for (int j = 0; j < sql_list2.size(); j++) {
								Map<String, Object> list2_map = (Map<String, Object>) sql_list2.get(j);

								if (list2_map.get("TAB_NAME").equals("时间")) {
									MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
									MysqlCon.mysqlpsts.setString(2, guid);
									MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
									MysqlCon.mysqlpsts.setString(4, list4_map.get("WIT_DATE") == null ? ""
											: list4_map.get("WIT_DATE").toString());// 时间
									MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
									MysqlCon.mysqlpsts.setString(6, user_id);
									MysqlCon.mysqlpsts.execute();
								} else if (list2_map.get("TAB_NAME").equals("白班领导")) {
									MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
									MysqlCon.mysqlpsts.setString(2, guid);
									MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
									if (list4_map.get("WIT_CLASS").equals("1")) {
										MysqlCon.mysqlpsts.setString(4, list4_map.get("WIT_LEADER") == null ? ""
												: list4_map.get("WIT_LEADER").toString());
										MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
										MysqlCon.mysqlpsts.setString(6, user_id);
										MysqlCon.mysqlpsts.execute();
									}
								} else if (list2_map.get("TAB_NAME").equals("白班秘书")) {
									MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
									MysqlCon.mysqlpsts.setString(2, guid);
									MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
									if (list4_map.get("WIT_CLASS").equals("1")) {
										MysqlCon.mysqlpsts.setString(4, list4_map.get("WIT_SECRET") == null ? ""
												: list4_map.get("WIT_SECRET").toString());
										MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
										MysqlCon.mysqlpsts.setString(6, user_id);
										MysqlCon.mysqlpsts.execute();
									}
								} else if (list2_map.get("TAB_NAME").equals("夜班领导")) {
									MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
									MysqlCon.mysqlpsts.setString(2, guid);
									MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
									if (list4_map.get("WIT_CLASS").equals("2")) {
										MysqlCon.mysqlpsts.setString(4, list4_map.get("WIT_LEADER") == null ? ""
												: list4_map.get("WIT_LEADER").toString());
										MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
										MysqlCon.mysqlpsts.setString(6, user_id);
										MysqlCon.mysqlpsts.execute();
									}
								} else if (list2_map.get("TAB_NAME").equals("夜班秘书")) {
									MysqlCon.mysqlpsts.setString(1, UUID.randomUUID().toString());
									MysqlCon.mysqlpsts.setString(2, guid);
									MysqlCon.mysqlpsts.setString(3, list2_map.get("guid").toString());
									if (list4_map.get("WIT_CLASS").equals("2")) {
										MysqlCon.mysqlpsts.setString(4, list4_map.get("WIT_SECRET") == null ? ""
												: list4_map.get("WIT_SECRET").toString());
										MysqlCon.mysqlpsts.setString(5, sd.format(new Date()));
										MysqlCon.mysqlpsts.setString(6, user_id);
										MysqlCon.mysqlpsts.execute();
									}
								}
							}

						}
					}

				}

			}
			
	
//        	 TimeTask.logger.info("oabase.hrperson_oa  共"+size+"条");
//			 TimeTask.logger.info("t_from_oa_hrperson 插入成功");
			 //hrperson_oa 同步成功
			
			 ret1.close();
			 pst1.close();
			 ret2.close();
			 pst2.close();
			 ret3.close();
			 pst3.close();
			 ret4.close();
			 pst4.close();
//			 ret5.close();
//			 pst5.close();
			 con.close();
			 
			 TimeTask.logger.info("同步完成 ， mysql,Oracle 连接关闭");
			 System.out.println("执行结束！");
	    }
	    catch (Exception e)
	    {
	        e.printStackTrace();
	        TimeTask.logger.error(e);
	    }
	}
 
	public static void main(String[] args) throws ClassNotFoundException, SQLException { 
		Address.setProperties();
		PropertyConfigurator.configure(Address.logaddr);
		System.out.println(Address.oracle_toRz_url);
		System.out.println(Address.oracle_toRz_username);
		System.out.println(Address.oracle_toRz_pwd);
		LoadRzFromOracle(); 
	}
}
