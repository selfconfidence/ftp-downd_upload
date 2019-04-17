package com.bdsoft.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.bdsoft.service.TimeTask;
import com.bdsoft.tools.Address;

public class MysqlCon {
	public static Connection mysqlcon;
	public static PreparedStatement mysqlpsts ;
	public static Connection getConn(String username,String password){
	    String driver=Address.mysql_driver;//"com.mysql.jdbc.Driver";
	    String url=Address.mysql_url;
	    TimeTask.logger.info("mysql 连接成功 ");
	    Connection conn=null;
	    try {
	        Class.forName(driver);
	        conn=DriverManager.getConnection(url,username,password);
	    } catch (ClassNotFoundException e) {            
	        e.printStackTrace();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return conn;
	}
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
 		 Address.setProperties();
 		 MysqlCon();
 		 mysqlcon.setAutoCommit(false); //mysql 手动提交
 		 try {
 			tbsj();
 			mysqlcon.commit();
 			System.out.println("执行成功");
		} catch (Exception e) {
			// TODO: handle exception
			mysqlcon.rollback();
		}
    	 MysqlCon.mysqlpsts.close();
		 mysqlcon.close();	 
	}
	public static void MysqlCon(){
		 mysqlcon = getConn(Address.mysql_username,Address.mysql_pwd);
	}
	public static void ExecuteMysql(String sql) throws SQLException{
		  mysqlpsts = mysqlcon.prepareStatement(sql);  
		  mysqlpsts.executeUpdate();
	}
	//同步按钮
	public static String tbsj() throws SQLException {
		String sql = "select count(1) count from t_from_oa_hrperson";  
		//TODO  int  加日志
		PreparedStatement pst = mysqlcon.prepareStatement(sql);
		ResultSet  ret = pst.executeQuery();
		if(ret.next())
		{
			 int x =ret.getInt("count");
			 if(x>0)
				{
				 MysqlCon.ExecuteMysql("delete from hrperson"); 
				 String ssql="insert into hrperson(hrid,username ,sex ,orgcode , orgname ,deptcode , deptname ,jobcode ,job ,officephone, homephone ,homeaddress,netphone  ,mobilephone, faxphone , officeaddress ,station ,headship , personsort , vpn  ,email  ,sequence, depsequence )  select  hrid,username ,sex ,orgcode , orgname ,deptcode ,  deptname , jobcode ,job ,officephone, homephone ,homeaddress, netphone  ,mobilephone,faxphone,officeaddress,station,headship, personsort, vpn,email,sequence, depsequence  from t_from_oa_hrperson";
				 MysqlCon.ExecuteMysql(ssql);
				 MysqlCon.mysqlcon.commit(); // 提交  
				 TimeTask.logger.info("人员数据刷新成功");
					try {
						WebServiceMhts.syncInfoRY();
						TimeTask.logger.info("人员排序推送成功");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						 TimeTask.logger.error("人员排序推送失败");
						 e.printStackTrace();
					}
				}
		}
		//重新获取连接
		 MysqlCon.MysqlCon();
		 MysqlCon.mysqlcon.setAutoCommit(false); //mysql 手动提交
		sql = "select count(1) count from t_from_hrorg";
	    pst = mysqlcon.prepareStatement(sql);
	    ret = pst.executeQuery();
	    if(ret.next())
		{
	    	 int x =ret.getInt("count");
	    	 if(x>0)
	 		{
	    		 MysqlCon.ExecuteMysql("delete from hrorg ");
	    		 MysqlCon.ExecuteMysql("insert into hrorg select  * from t_from_hrorg");
	    		 MysqlCon.mysqlcon.commit();;  // 提交  
	    		 TimeTask.logger.info("机构数据刷新成功");
	 			try {
 	 				WebServiceMhts.syncInfoJG();
 	 				TimeTask.logger.info("机构排序推送成功");
	 			} catch (Exception e) {
	 				// TODO Auto-generated catch block
	 				TimeTask.logger.error("机构排序推送失败");
	 				e.printStackTrace();
	 			}
	 		}
		}
	    ret.close();
	    pst.close();
		return "操作成功";
	}
	
}
