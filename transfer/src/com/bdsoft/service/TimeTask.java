package com.bdsoft.service;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.bdsoft.database.OracleCon;
import com.bdsoft.tools.Address;

/**
 * @author ����
 * ����˵����  oracle to mysql  ���ݸ���   �˴β����� ��ɾ���ٲ���
 *
 */
public class TimeTask {
	
	public static Logger logger = Logger.getLogger(TimeTask.class);  
	public static void main(String[] args)throws FileNotFoundException {
		// run in a second
		//��ȡ�����ļ� ftp��ַ
		Address.setProperties();
		PropertyConfigurator.configure(Address.logaddr);
		//ִ��ʱ����
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
				    		logger.info("����ͬ������ϵͳ���ݿ�"); 
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
