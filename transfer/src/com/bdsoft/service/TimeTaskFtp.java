package com.bdsoft.service;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bdsoft.ftp.MessageFtpTools;
import com.bdsoft.ftp.MessageSFtpTools;
import com.bdsoft.tools.Address;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;

/**
 * @author
 */
public class TimeTaskFtp {
	public static Logger logger = Logger.getLogger(TimeTaskFtp.class);  
	public static void main(String[] args)throws FileNotFoundException {
		// run in a second
		//��ȡ�����ļ� ftp��ַ
		Address.setProperties();
		PropertyConfigurator.configure(Address.logaddr);
		final long timeInterval = Address.time_jg*1000;
		
		Runnable runnable = new Runnable() {
			public void run() {
				while (true) {
					if(Address.isopen.equals("true"))
					{
						//TODO  ����־ 
						try {
							//TODO
							//MessageFtpTools.downloadFile(Address.C_up_flag,Address.C_up_content,Address.my_flag,Address.my_content,Address.C_host,Address.C_port,Address.C_username,Address.C_password); 
							//MessageSFtpTools.uploadFile(Address.my_flag,Address.my_content,Address.A_flag,Address.A_content,Address.A_host,Address.A_port,Address.A_username,Address.A_password);
							
							Object[] obj = MessageSFtpTools.downloadFile(Address.A_content,Address.my_content,Address.A_host,Address.A_port,Address.A_username,Address.A_password); 
							ChannelSftp sftp = (ChannelSftp) obj[0];
							Channel channel = (Channel) obj[1];
							Session session = (Session) obj[2];
							sftp.disconnect();
							channel.disconnect();
							session.disconnect();

							//MessageSFtpTools.downloadFile(Address.A2_up_flag,Address.A2_up_content,Address.my_up_flag,Address.my_up_content,Address.A2_host,Address.A2_port,Address.A2_username,Address.A2_password);
							obj = MessageSFtpTools.uploadFile(Address.my_up_content,Address.C_up_content,Address.C_host,Address.C_port,Address.C_username,Address.C_password);
							 sftp = (ChannelSftp) obj[0];
							 channel = (Channel) obj[1];
							 session = (Session) obj[2];
							sftp.disconnect();
							channel.disconnect();
							session.disconnect();
							logger.info("完成操作!");	
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else{
						logger.info("ͬ�����ر�");
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
