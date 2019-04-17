package com.bdsoft.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.PropertyConfigurator;

import com.bdsoft.service.TimeTask;
import com.bdsoft.tools.Address;

public class OracleCon {

	public static  void  OracleToMySql()
	{
	    Connection con = null;// 创建一个数据库连接
	    try
	    {
	        Class.forName(Address.oracle_driver);// 加载Oracle驱动程序
	        String url = Address.oracle_url;// 127.0.0.1是本机地址，orcl是精简版Oracle的默认数据库名
	        String user = Address.oracle_username; 
	        String password =Address.oracle_pwd;// 
	        con = DriverManager.getConnection(url, user, password);// 获取连接
	        MysqlCon.MysqlCon();
	        MysqlCon.mysqlcon.setAutoCommit(false); //mysql 手动提交
	        TimeTask.logger.info("con  ok.. 正在查询 ..");     
	        String sql = "select ORGCODE,ORGNAME,DEPTCODE,DEPTNAME,HRID,USERNAME,SEX,JOBCODE,JOB,OFFICEPHONE,HOMEPHONE,HOMEADDRESS,NETPHONE,MOBILEPHONE,FAXPHONE,OFFICEADDRESS"
	        		+ ",STATION,HEADSHIP,PERSONSORT,VPN,EMAIL,IMAGE,SEQUENCE,DEPSEQUENCE"
	        		+ " from oabase.hrperson_oa";
			 PreparedStatement pst = con.prepareStatement(sql);
			 ResultSet  ret = pst.executeQuery();
			 int count=0;
			 MysqlCon.ExecuteMysql("delete from t_from_oa_hrperson");
			 int size= 0;
			 String sql_sel="insert into t_from_oa_hrperson  values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			 MysqlCon.mysqlpsts = MysqlCon.mysqlcon.prepareStatement(sql_sel);  
			 TimeTask.logger.info("查询成功   正在插入t_from_oa_hrperson表");
			 while (ret.next()) {  
				 MysqlCon.mysqlpsts.setString(1, ret.getString("HRID"));  
				 MysqlCon.mysqlpsts.setString(2, ret.getString("USERNAME"));  
				 MysqlCon.mysqlpsts.setString(3, ret.getString("SEX"));  
				 MysqlCon.mysqlpsts.setString(4, ret.getString("ORGCODE"));  
				 MysqlCon.mysqlpsts.setString(5, ret.getString("ORGNAME"));  
				 MysqlCon.mysqlpsts.setString(6, ret.getString("DEPTCODE"));  
				 MysqlCon.mysqlpsts.setString(7, ret.getString("DEPTNAME"));  
				 MysqlCon.mysqlpsts.setString(8, ret.getString("JOBCODE"));  
				 MysqlCon.mysqlpsts.setString(9, ret.getString("JOB"));  
				 MysqlCon.mysqlpsts.setString(10, ret.getString("OFFICEPHONE"));  
				 MysqlCon.mysqlpsts.setString(11, ret.getString("HOMEPHONE"));  
				 MysqlCon.mysqlpsts.setString(12, ret.getString("HOMEADDRESS"));  
				 MysqlCon.mysqlpsts.setString(13, ret.getString("NETPHONE"));  
				 MysqlCon.mysqlpsts.setString(14, ret.getString("MOBILEPHONE"));  
				 MysqlCon.mysqlpsts.setString(15, ret.getString("FAXPHONE"));  
				 MysqlCon.mysqlpsts.setString(16, ret.getString("OFFICEADDRESS"));  
				 MysqlCon.mysqlpsts.setString(17, ret.getString("STATION"));  
				 MysqlCon.mysqlpsts.setString(18, ret.getString("HEADSHIP"));  
				 MysqlCon.mysqlpsts.setString(19, ret.getString("PERSONSORT"));  
				 MysqlCon.mysqlpsts.setString(20, ret.getString("VPN"));  
				 MysqlCon.mysqlpsts.setString(21, ret.getString("EMAIL"));  
				 MysqlCon.mysqlpsts.setBlob(22, ret.getBlob("IMAGE"));  
				 MysqlCon.mysqlpsts.setString(23, ret.getString("SEQUENCE"));  
				 MysqlCon.mysqlpsts.setString(24, ret.getString("DEPSEQUENCE"));  
				 MysqlCon.mysqlpsts.addBatch();          // 加入批量处理  
	             if(count == 3000 ){ 
	            	 MysqlCon.mysqlpsts.executeBatch(); // 执行批量处理  
	            	 MysqlCon.mysqlcon.commit();;  // 提交  
					 count= 0 ;
	             }
	             count ++;
	             size++;
			 }
			 MysqlCon.mysqlpsts.executeBatch(); // 执行批量处理  
        	 MysqlCon.mysqlcon.commit();;  // 提交  
        	 TimeTask.logger.info("oabase.hrperson_oa  共"+size+"条");
			 TimeTask.logger.info("t_from_oa_hrperson 插入成功");
			 //hrperson_oa 同步成功
			 //开始同步oabase.hrorg_oa 表
			 sql="select * from oabase.hrorg_oa";
 			 pst = con.prepareStatement(sql);
			 ret = pst.executeQuery();
			 sql_sel = "insert into t_from_hrorg values(?,?,?,?)";
			 MysqlCon.ExecuteMysql("delete from t_from_hrorg");
			 MysqlCon.mysqlpsts = MysqlCon.mysqlcon.prepareStatement(sql_sel);  
			 count=0;
			 size=0;
			 while (ret.next()) {  
				  String orgname = ret.getString("ORGNAME");
				  String orgcode = ret.getString("ORGCODE");
				  String seq = ret.getString("SEQ");
				  String parentcode = ret.getString("PARENTCODE");
				  MysqlCon.mysqlpsts.setString(1, orgcode);
				  MysqlCon.mysqlpsts.setString(2, orgname);
				  MysqlCon.mysqlpsts.setString(3, seq);
				  MysqlCon.mysqlpsts.setString(4, parentcode);
				  MysqlCon.mysqlpsts.addBatch();          // 加入批量处理  
				  if(count == 500){ 
					     MysqlCon.mysqlpsts.executeBatch(); // 执行批量处理  
		            	 MysqlCon.mysqlcon.commit();;  // 提交  
						 count= 0 ;
		            }
				  count++;
				  size++;
			 }
			 MysqlCon.mysqlpsts.executeBatch(); // 执行批量处理  
        	 MysqlCon.mysqlcon.commit();;  // 提交  
        	 TimeTask.logger.info("t_from_hrorg 共"+size+"条");
			 TimeTask.logger.info("t_from_hrorg 插入成功");
			 //t_from_hrorg ->hrorg       t_from_oa_hrperson ->hrperson
			 try {
			 TimeTask.logger.info("t_from_hrorg ->hrorg    t_from_oa_hrperson ->hrperson");
				 MysqlCon.tbsj();
			 TimeTask.logger.info(" to hrorg hrperson   ok");
				 MysqlCon.mysqlcon.commit();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
				MysqlCon.mysqlcon.rollback();
				TimeTask.logger.info(" to hrorg hrperson error rollback");
			}
 			 MysqlCon.mysqlpsts.close();
 			 MysqlCon.mysqlcon.close();
			 ret.close();
			 pst.close();
			 con.close();
			 TimeTask.logger.info("同步完成 ， mysql,Oracle 连接关闭");
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
		System.out.println(Address.oracle_url);
		System.out.println(Address.oracle_username);
		System.out.println(Address.oracle_pwd);
		OracleToMySql(); 
	}
}
