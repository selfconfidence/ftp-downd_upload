package com.bdsoft.service;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.bdsoft.database.OracleCon;
import com.bdsoft.tools.Address;

/**
 * @author 李阔
 * 方法说明：  oracle to mysql  数据更新   此次操作是 先删除再插入
 *
 */
public class TimeTask {
	
	public static Logger logger = Logger.getLogger(TimeTask.class);  
	public static void main(String[] args)throws FileNotFoundException {
		// run in a second
		//读取配置文件 ftp地址
		Address.setProperties();
		PropertyConfigurator.configure(Address.logaddr);
		//执行时间间隔
		final long timeInterval = Address.time_jg*1000;
		Runnable runnable = new Runnable() {
			public void run() {
				while (true) {
					SimpleDateFormat sdf=new SimpleDateFormat("HHmm");
				    String time=sdf.format(new Date());
				    String[] exe_time = Address.execute_time.split(",");
				    for(int i = 0 ; i<exe_time.length;i++)
				    {
				    	if(time.equals(exe_time[i]))
				    	{
				    		logger.info("正在同步人事系统数据库"); 
		 					OracleCon.OracleToMySql();
				    	}
				    }
					// ------- ends here
					try {
						Thread.sleep(timeInterval);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
	}

}
