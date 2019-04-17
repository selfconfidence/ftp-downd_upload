package com.bdsoft.ftp;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Vector;

import com.bdsoft.service.TimeTaskFtp;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * WRITER--：ZCJ 
 * TIME--- ：2017年4月2日/上午10:24:53
 * FUNCTION：连接FTP服务器/获得连接/文件上传/文件下载/登录FTP服务器/退出FTP服务器
 * EXPLAIN-：该方法是进行FTP服务器的连接，文件上传，文件下载的公共静态方法使用时可以直接类名调用
 */
public class MessageSFtpTools {
	private static  ChannelSftp sftp;
	private static   Channel channel;
	private static   Session session;
	/**
	 *   下载远程文件到本地
	 * @param other_flag     远程文件路径  //未用
	 * @param other_content  远程文件路径 
	 * @param my_flag        下载文件到本地的一个文件夹
	 * @param my_content     下载文件到本地的一个文件夹
	 * @param hostname        远程主机IP
	 * @param port            远程主机端口
	 * @param username          root
	 * @param password         root
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static Object[] downloadFile(String other_content,String my_content,String hostname,int port,String username,String password) throws JSchException, SftpException{
		GetSftp(hostname, username, password, port);
		Vector list = sftp.ls(other_content);
		
		 //设置每天生成一个文件夹
		String localPath  = my_content+"/"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
   	    File file = new File(localPath);
   	    if(!file.exists()){
   	    	//如果没有当前目录,那么就创建一个
   	    	file.mkdirs();
   	    }
   	 
      /*  if(list.size()>0)
        {
        	 Iterator it = list.iterator();
             while (it.hasNext())
             {
               LsEntry entry = (LsEntry) it.next();
               String filename = entry.getFilename();
               //判断xml 结尾的文件
               if(filename.endsWith("xml"))
               {
            	   TimeTaskFtp.logger.info("download "+filename+" to 接口机");
            	   sftp.get("/home/"+other_flag+filename,my_flag);
            	   sftp.rm(other_flag+filename);
               }
             }
        }*/
//        list = sftp.ls(other_content);
		
		//递归逻辑的实现
		 /**
		  * 可以这样,判断当前字符串是否有 . 如果有点就根据条件下载到指定文件夹,如果没有就是一个文件夹
		  * 文件夹的情况就可以再次 调用此方法,并且拼接文件即可 递归变量,不更改字符串数值可以达到此目的.
		  */
		
        if(list.size()>0)
        {
        	 Iterator it = list.iterator();
             while (it.hasNext())
             {
               LsEntry entry = (LsEntry) it.next();
               String filename = entry.getFilename();
              
               if(filename.endsWith("xml")){
            	   TimeTaskFtp.logger.error("download "+filename+" to file");
            	   sftp.get(other_content+filename,localPath);
               }
              
               if(filename.endsWith("png")){
            	   TimeTaskFtp.logger.error("download "+filename+" to file");
            	   sftp.get(other_content+filename,localPath);
               }
               
               if(filename.endsWith("log")){
            	   TimeTaskFtp.logger.error("download "+filename+" to file");
            	   sftp.get(other_content+filename,localPath);
               }
               //判断当前是否是文件夹目录
               //防止远程有 .  ..这样格式的内容.
               if(!filename.contains(".")){
            	   if(!filename.contains("..")){
            		   // 如果没有 包含. 说明是一个文件夹,我们拼接在进去
                	   downloadFile(other_content+filename+"/",my_content,hostname,port,username,password);
            	   }
            	 
               }
             }
        }
        return new Object[]{sftp,channel,session};
//        TimeTaskFtp.logger.info("down ok   session  isclosed");
	}
	/** 
	 *  本地文件上传到远程服务器
	 * 
	 * @param my_up_flag
	 * @param my_up_content
	 * @param other_flag
	 * @param other_up_content
	 * @param hostname
	 * @param port
	 * @param username
	 * @param password
	 * @throws SocketException
	 * @throws IOException
	 * @throws JSchException
	 * @throws SftpException
	 */
	public static Object[] uploadFile(String my_up_content,String other_up_content,String hostname,int port ,String username,String password) throws SocketException, IOException, JSchException, SftpException {
		 GetSftp(hostname, username, password, port);
		 File rootDir = new File(other_up_content);
		
		 String[] fileList =  rootDir.list();
		 /*
		 if(!rootDir.isDirectory()){
			 TimeTaskFtp.logger.info("文件夹 不存在"+rootDir.getAbsolutePath());
		 }else{
			 for (int i = 0; i < fileList.length; i++) {
				    String filename = rootDir.getAbsolutePath()+"/"+fileList[i];
				    TimeTaskFtp.logger.info("up "+filename+" to 应用服务器");
				    File tempFile =new File(filename); 
				    sftp.put(filename,other_flag);
					tempFile.delete();
				}
		 }*/
		 fileList =  rootDir.list();
		 if(!rootDir.isDirectory()){
			 TimeTaskFtp.logger.info("文件夹 不存在"+rootDir.getAbsolutePath());
		 }else{
			 for (int i = 0; i < fileList.length; i++) {
				    String filename = rootDir.getAbsolutePath()+"/"+fileList[i];
				    TimeTaskFtp.logger.info("up "+filename+" to 应用服务器");
				    File tempFile =new File(filename); 
				    sftp.put(filename,other_up_content);
					tempFile.delete();
				}
		 }
		 
        return new Object[]{sftp,channel,session};
//        TimeTaskFtp.logger.info("up ok   session  isclosed");
	}
	/** 
	* @author 李阔  
	* 方法说明： 获取连接
	* @return 
	*/
	public static void GetSftp(String hostname,String username,String password,int port) throws JSchException
	{
		String ftpHost = hostname;
        String ftpUserName = username; 
        String ftpPassword = password;
        JSch jsch = new JSch(); // 创建JSch对象
        session = jsch.getSession(ftpUserName, ftpHost,port); // 根据用户名，主机ip，端口获取一个Session对象
        if (ftpPassword != null) {
            session.setPassword(ftpPassword); // 设置密码
        }
        session.setConfig("StrictHostKeyChecking", "no");
        session.setTimeout(300000); // 设置timeout时间
        session.connect(); // 通过Session建立链接
        channel= session.openChannel("sftp"); // 打开SFTP通道
        channel.connect(); // 建立SFTP通道的连接
        sftp = (ChannelSftp) channel;
	}
	
	
	 
}   

