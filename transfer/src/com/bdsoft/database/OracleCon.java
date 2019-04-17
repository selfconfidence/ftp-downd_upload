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
	    Connection con = null;// ����һ�����ݿ�����
	    try
	    {
	        Class.forName(Address.oracle_driver);// ����Oracle��������
	        String url = Address.oracle_url;// 127.0.0.1�Ǳ�����ַ��orcl�Ǿ����Oracle��Ĭ�����ݿ���
	        String user = Address.oracle_username; 
	        String password =Address.oracle_pwd;// 
	        con = DriverManager.getConnection(url, user, password);// ��ȡ����
	        MysqlCon.MysqlCon();
	        MysqlCon.mysqlcon.setAutoCommit(false); //mysql �ֶ��ύ
	        TimeTask.logger.info("con  ok.. ���ڲ�ѯ ..");     
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
			 TimeTask.logger.info("��ѯ�ɹ�   ���ڲ���t_from_oa_hrperson��");
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
				 MysqlCon.mysqlpsts.addBatch();          // ������������  
	             if(count == 3000 ){ 
	            	 MysqlCon.mysqlpsts.executeBatch(); // ִ����������  
	            	 MysqlCon.mysqlcon.commit();;  // �ύ  
					 count= 0 ;
	             }
	             count ++;
	             size++;
			 }
			 MysqlCon.mysqlpsts.executeBatch(); // ִ����������  
        	 MysqlCon.mysqlcon.commit();;  // �ύ  
        	 TimeTask.logger.info("oabase.hrperson_oa  ��"+size+"��");
			 TimeTask.logger.info("t_from_oa_hrperson ����ɹ�");
			 //hrperson_oa ͬ���ɹ�
			 //��ʼͬ��oabase.hrorg_oa ��
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
				  MysqlCon.mysqlpsts.addBatch();          // ������������  
				  if(count == 500){ 
					     MysqlCon.mysqlpsts.executeBatch(); // ִ����������  
		            	 MysqlCon.mysqlcon.commit();;  // �ύ  
						 count= 0 ;
		            }
				  count++;
				  size++;
			 }
			 MysqlCon.mysqlpsts.executeBatch(); // ִ����������  
        	 MysqlCon.mysqlcon.commit();;  // �ύ  
        	 TimeTask.logger.info("t_from_hrorg ��"+size+"��");
			 TimeTask.logger.info("t_from_hrorg ����ɹ�");
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
			 TimeTask.logger.info("ͬ����� �� mysql,Oracle ���ӹر�");
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
