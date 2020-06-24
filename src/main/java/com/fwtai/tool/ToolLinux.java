package com.fwtai.tool;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
ToolLinux scp = ToolLinux.getInstance(ip, port,uname,pwd);
scp.putFile("本地文件路径", fileFeed.getName()+".tmp", "推送文件，到服务器的目录路径", null);
scp.putFile("/data/mysql5.7.19.tar.gz","/data/");
scp.getFile(remoteFile, localTargetDirectory);
*/
public final class ToolLinux {

	static private ToolLinux instance;
	
	private String ip;
	private int port;
	private String username;
	private String password;

	static synchronized public ToolLinux getInstance(final String ip, final int port,final String username,final String passward){
		if (instance == null){
			instance = new ToolLinux(ip, port, username, passward);
		}
		return instance;
	}

	public ToolLinux(String IP, int port, String username, String passward) {
		this.ip = IP;
		this.port = port;
		this.username = username;
		this.password = passward;
	}

	public final void getFile(String remoteFile, String localTargetDirectory) {
		Connection conn = new Connection(ip,port);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			if (isAuthenticated == false) {
				System.err.println("身份验证失败");
			}
			SCPClient client = new SCPClient(conn);
			client.get(remoteFile, localTargetDirectory);
			conn.close();
		} catch (IOException ex) {
			Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public final void putFile(String localFile, String remoteTargetDirectory){
		Connection conn = new Connection(ip,port);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			if (isAuthenticated == false) {
				System.err.println("身份验证失败");
			}
			SCPClient client = new SCPClient(conn);
			client.put(localFile, remoteTargetDirectory);
			conn.close();
		} catch (IOException ex) {
			Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public final void putFile(String localFile, String remoteFileName, String remoteTargetDirectory, String mode) {
		Connection conn = new Connection(ip,port);
		try {
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPassword(username, password);
			if (isAuthenticated == false) {
				System.err.println("身份验证失败");
			}
			SCPClient client = new SCPClient(conn);
			if ((mode == null) || (mode.length() == 0)) {
				mode = "0600";
			}
			client.put(localFile, remoteFileName, remoteTargetDirectory, mode);
			// 重命名
			ch.ethz.ssh2.Session sess = conn.openSession();
			String tmpPathName = remoteTargetDirectory + File.separator + remoteFileName;
			String newPathName = tmpPathName.substring(0, tmpPathName.lastIndexOf("."));
			sess.execCommand("mv " + remoteFileName + " " + newPathName);// 重命名回来
			conn.close();
		} catch (IOException ex) {
			Logger.getLogger(SCPClient.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public final static byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream(1024 * 1024);
			byte[] b = new byte[1024 * 1024];
			int i;
			while ((i = fis.read(b)) != -1) {
				byteArray.write(b, 0, i);
			}
			fis.close();
			byteArray.close();
			buffer = byteArray.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}
	
	/**Linux运行命令行,多个命令行请用  && 连接,用法：ToolLinux.linuxCommand("rm -rf "+path);*/
	public final static boolean linuxCommand(final String command){
		final String[] cmds = {"/bin/sh","-c",command};
		try {
			final Process pro = Runtime.getRuntime().exec(cmds);
	        pro.waitFor();
	        final InputStream in = pro.getInputStream();
	        new BufferedReader(new InputStreamReader(in));
	        return true;
		} catch (Exception e){
			return false;
		}
	}
}