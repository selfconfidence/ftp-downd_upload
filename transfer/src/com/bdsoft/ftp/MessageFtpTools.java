package com.bdsoft.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import com.bdsoft.service.TimeTaskFtp;
import com.bdsoft.tools.Address;

/**
 * WRITER--：ZCJ 
 * TIME--- ：2017年4月2日/上午10:24:53
 * FUNCTION：连接FTP服务器/获得连接/文件上传/文件下载/登录FTP服务器/退出FTP服务器
 * EXPLAIN-：该方法是进行FTP服务器的连接，文件上传，文件下载的公共静态方法使用时可以直接类名调用
 */
public class MessageFtpTools {
	
 
	/** 
	* @author 李阔  
	* @version 创建时间： 
	* 方法说明： 扫描  ftp: ftpAddress.he_uploadPathName   路径   按照一定时间下载
	* @return 
	*/
	public static void downloadFile(String other_flag,String other_content,String my_flag,String my_content,String hostname,int port,String username,String password) {
		//如果文件夹不存在则创建
		File smsfile = new File(my_content);
		 if(!smsfile.exists()){
			 smsfile.mkdirs();
		 }
		 //连接FTP服务器
		FTPClient ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
//			if (FTPReply.isPositiveCompletion(replyCode)) {
//				 TimeTaskFtp.logger.info("con ok");
//			}
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				 TimeTaskFtp.logger.info("con error");
				return  ;
			}
			//开通端口传输数据
			ftpClient.enterLocalPassiveMode();
			ftpClient.setFileType(ftpClient.BINARY_FILE_TYPE);
			// 切换FTP目录
			ftpClient.changeWorkingDirectory(other_flag);
			FTPFile[] ftpFiles = ftpClient.listFiles();
//			TimeTaskFtp.logger.info("准备下载文件个数："+ftpFiles.length);
			//下载完成时 删除flag 下文件
			for (FTPFile file : ftpFiles) {
				    TimeTaskFtp.logger.info("download "+my_flag + "/" + file.getName() +" to 接口机");
					File file_down_paths = new File(my_flag + "/" + file.getName());
					OutputStream os = null;
					os = new FileOutputStream(file_down_paths);
					ftpClient.retrieveFile(file.getName(), os);
					ftpClient.dele(file.getName());
					os.close();
			}
			ftpClient.changeWorkingDirectory(other_content);
			for (FTPFile file : ftpFiles) {
			    TimeTaskFtp.logger.info("download "+my_content + "/" + file.getName()+" to 接口机");
				File file_down_paths = new File(my_content + "/" + file.getName());
				OutputStream os  = new FileOutputStream(file_down_paths);
				ftpClient.retrieveFile(file.getName(), os);
				os.close();
		}
			ftpClient.logout();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 if (ftpClient.isConnected()) {
					try {
						ftpClient.disconnect();
					} catch (IOException e) {
						e.printStackTrace();
					}
		       }
			}
	}
 
	/** 
	* @author 李阔  
	* @version 创建时间：2017年5月13日 
	* 方法说明： 扫描  本地ftpAddress.my_uploadPathName   路径   按照一定时间上传
	* @return 
	 * @throws IOException 
	 * @throws SocketException 
	*/
	public static void uploadFile(String my_up_flag,String my_up_content,String other_flag,String other_up_content,String hostname,int port ,String username,String password) throws SocketException, IOException {
		 FTPClient ftpClient = new FTPClient();
		 // 连接FTP服务器
		 ftpClient.connect(hostname, port);
		 // 登录FTP服务器
	     ftpClient.login(username, password);
		 // 是否成功登录FTP服务器
		 int replyCode = ftpClient.getReplyCode();
		 if (!FTPReply.isPositiveCompletion(replyCode)) {
			return  ;
		 }
		 //开通端口传输数据
		 ftpClient.enterLocalPassiveMode();
		 ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		 ftpClient.changeWorkingDirectory(other_flag);
		 File rootDir = new File(my_up_flag);
		 String[] fileList =  rootDir.list();
		 if(!rootDir.isDirectory()){
			 TimeTaskFtp.logger.info("文件夹 不存在"+rootDir.getAbsolutePath());
		 }else{
//		    TimeTaskFtp.logger.info("准备上传文件个数："+fileList.length);
		    for (int i = 0; i < fileList.length; i++) {
			    String filename = rootDir.getAbsolutePath()+"/"+fileList[i];
			    TimeTaskFtp.logger.info("up "+filename+"to 短信服务器");
			    File tempFile =new File(filename); 
	 			 //获取文件名
			    InputStream inputStream = new FileInputStream(tempFile);
				ftpClient.storeFile(tempFile.getName(), inputStream);
				inputStream.close();
				tempFile.delete();
			}
		 }
		//切换文件路径
		 ftpClient.changeWorkingDirectory(other_up_content);
		 File rootDir_content = new File(my_up_content);
		 
		 if(!rootDir.isDirectory()){
			 TimeTaskFtp.logger.info("文件夹 不存在"+rootDir.getAbsolutePath());
		 }else{
//		    TimeTaskFtp.logger.info("准备上传文件个数："+fileList.length);
		    for (int i = 0; i < fileList.length; i++) {
			    String filename = rootDir_content.getAbsolutePath()+"/"+fileList[i];
			    TimeTaskFtp.logger.info("up "+filename+"to 短信服务器");	
			    File tempFile =new File(filename); 
	 			 //获取文件名
			    InputStream inputStream = new FileInputStream(tempFile);
				ftpClient.storeFile(tempFile.getName(), inputStream);
				inputStream.close();
				tempFile.delete();
			}
//		    TimeTaskFtp.logger.info("upload ok");
		    
		 }
		//退出登录FTP服务器
		 ftpClient.logout();
	        if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
	       }
}
	public static void lanjie(String other_flag,String other_content,String my_flag,String my_content,String hostname,int port,String username,String password) {
		//如果文件夹不存在则创建
		File smsfile = new File(my_content);
		 if(!smsfile.exists()){
			 smsfile.mkdirs();
		 }
		 //连接FTP服务器
		FTPClient ftpClient = new FTPClient();
		try {
			System.out.println(hostname);
			System.out.println(port);
			System.out.println(username);
			System.out.println(password);
			// 连接FTP服务器
			ftpClient.connect(hostname, port);
			// 登录FTP服务器
			ftpClient.login(username, password);
			// 验证FTP服务器是否登录成功
			int replyCode = ftpClient.getReplyCode();
			if (FTPReply.isPositiveCompletion(replyCode)) {
				 TimeTaskFtp.logger.info("con ok");
			}
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				 TimeTaskFtp.logger.info("con error");
				return  ;
			}
		 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			 
			}
	}
 
	public static String myUpToHeDown() {
		 //连接FTP服务器
		FTPClient my_ftpClient = new FTPClient();
 		FTPClient he_ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
//			my_ftpClient.connect(Address.my_host, Address.my_port);
			he_ftpClient.connect(Address.C_host, Address.C_port);
			// 登录FTP服务器
//			my_ftpClient.login(Address.my_username, Address.my_password);
			he_ftpClient.login(Address.C_username, Address.C_password);
			// 验证FTP服务器是否登录成功
			int my_replyCode = my_ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(my_replyCode)) {
				return "";
			}
			int he_replyCode = he_ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(he_replyCode)) {
				return "";
			}
			he_ftpClient.makeDirectory(Address.C_content);
			//  切换FTP目录
			he_ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
	
			my_ftpClient.changeWorkingDirectory(Address.my_content);
			my_ftpClient.setBufferSize(1024);
			my_ftpClient.setFileType(my_ftpClient.BINARY_FILE_TYPE);
			FTPFile[] ftpFiles = my_ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
 					 //获取文件流
 					 InputStream in = my_ftpClient.retrieveFileStream(file.getName());
 					 
					 //上传到外部ftp地址                                                      
					 he_ftpClient.storeFile(Address.C_content+"/"+file.getName(), in);
				
					 in.close();
					 my_ftpClient.completePendingCommand();
					 //删除my   ftp地址文件
					 // my_ftpClient.dele(file.getName());
			}
			 TimeTaskFtp.logger.info("更新成功");
		} catch (Exception e) {
			 TimeTaskFtp.logger.info("更新失败");
		} finally {
			if (my_ftpClient.isConnected()) {
				try {
					my_ftpClient.logout();
				} catch (IOException e) {

				}
			}
			if (he_ftpClient.isConnected()) {
				try {
					he_ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return "";
	}
	public static String HeUpToMyDown()
	{
		 //连接FTP服务器
		FTPClient my_ftpClient = new FTPClient();
 		FTPClient he_ftpClient = new FTPClient();
		try {
			// 连接FTP服务器
			// my_ftpClient.connect(Address.my_host, Address.my_port);
			he_ftpClient.connect(Address.C_host, Address.C_port);
			// 登录FTP服务器
			// my_ftpClient.login(Address.my_username, Address.my_password);
			he_ftpClient.login(Address.C_username, Address.C_password);
			// 验证FTP服务器是否登录成功
			int my_replyCode = my_ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(my_replyCode)) {
				return "";
			}
			int he_replyCode = he_ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(he_replyCode)) {
				return "";
			}
			my_ftpClient.makeDirectory(Address.my_content);
			my_ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			//	切换FTP目录
			he_ftpClient.changeWorkingDirectory(Address.C_content);
			he_ftpClient.setBufferSize(1024);
			he_ftpClient.setFileType(he_ftpClient.BINARY_FILE_TYPE);
			FTPFile[] ftpFiles = he_ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
					 //获取文件流
 					 InputStream in = he_ftpClient.retrieveFileStream(file.getName());
 					 
					 //上传到外部ftp地址                                                      
					 my_ftpClient.storeFile(Address.my_content+"/"+file.getName(), in);
				
					 in.close();
					 he_ftpClient.completePendingCommand();
					 //删除my   ftp地址文件
					 //  he_ftpClient.dele(file.getName());
			}
			 TimeTaskFtp.logger.info("更新成功");
		} catch (Exception e) {
			 TimeTaskFtp.logger.info("更新失败");
		} finally {
			if (my_ftpClient.isConnected()) {
				try {
					my_ftpClient.logout();
				} catch (IOException e) {

				}
			}
			if (he_ftpClient.isConnected()) {
				try {
					he_ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return "";
	}
	
}   

