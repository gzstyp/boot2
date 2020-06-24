package com.fwtai.tool;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * 文件操作类
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月12日 11:29:19
 * @QQ号码 444141300
 * @主页 http://www.fwtai.com
*/
public final class ToolFile{

	public final static Resource getResource(final String filePath){
		return new FileSystemResource(filePath);
	}
	
	public final static boolean delFile(final String filePath){
		final Resource res = new FileSystemResource(filePath);
		if(res.exists()){
			try {
				res.getFile().delete();
				return true;
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return false;
	}
		
	/**
	 * 删除文件
	 * @作者 田应平
	 * @创建时间 2017年1月10日 上午10:50:23
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public final static void delete(final File file){
		if(file.isFile()){
			file.delete();
			return;
		}
		if(file.isDirectory()){
			File[] childFiles = file.listFiles();
			if(childFiles == null || childFiles.length == 0){
				file.delete();
				return;
			}
			for(int i = 0; i < childFiles.length; i++){
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

    /**
     * 对文件夹的文件批量重命名
     * @param pathDir 文件夹|目录
     * @param expression 需替换的字符串,如 _fwtai.com,如果被替换的有括号的话,先替换括号的内容再用 \\(\\)替换括号
     * @param target 需要替换成的字符串,如 ""
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2019/4/18 10:22
    */
    public static final boolean dirRename(final String pathDir,final String expression,final String target){
        final File dir = new File(pathDir);
        if (expression == null) {
            return false;
        }
        try {
            if(dir.isDirectory()){
                final File[] childFiles = dir.listFiles();
                for(int i = 0; i < childFiles.length; i++){
                    try {
                        final String name = childFiles[i].getPath();
                        final String fileName = name.replaceAll(expression,(target==null ? "" : target));
                        new File(name).renameTo(new File(fileName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

	/**
	 * 压缩|打包成.zip文件,压缩包含当前目录及目录下的文件夹,出异常则压缩失败
	 * @param folderPath 需要压缩的目标目录;如 D:\\zip\\dir
	 * @param zipFilePath 压缩成功后zip文件,含全路径;如 D:\\zip\\dir\\zipxx.zip
	*/
	public final static void zipCompressFolder(final String folderPath,final String zipFilePath) throws Exception{
		final ZipFile zipFile = new ZipFile(zipFilePath);
		final ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);    
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		zipFile.addFolder(folderPath,parameters);
	}
	
	/**
	 * 压缩|打包成.zip文件,压缩包不含当前的压缩目录及目录下的文件夹,出异常则压缩失败
	 * @param folderPath 需要压缩的目标目录;如 D:\\zip\\dir
	 * @param zipFilePath 压缩成功后zip文件,含全路径;如 D:\\zip\\dir\\zipxx.zip
	*/
	public final static void zipCompress(final String folderPath,final String zipFilePath)throws Exception{
		final ZipFile zipFile = new ZipFile(zipFilePath);
		final File[] files = new File(folderPath).listFiles();
		final ArrayList<File> listFiles = new ArrayList<File>();
		for(File file : files){
            if(!file.isDirectory())
            listFiles.add(file);
        }
		final ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);    
		parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
		zipFile.addFiles(listFiles,parameters);
	}
	
	private final static byte[] analysisSHA256(final String filename) throws Exception{
    	final InputStream fis =  new FileInputStream(filename);
    	final byte[] buffer = new byte[1024];
    	final MessageDigest complete = MessageDigest.getInstance("SHA-256");
        int numRead;
        do{
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);
        fis.close();
        return complete.digest();
    }
    
	/**获取文件SHA256值*/
    public final static String getSHA256(final String filename) throws Exception{
    	final byte[] b = analysisSHA256(filename);
        String result = "";
        for (int i=0; i < b.length; i++){
            result += Integer.toString(( b[i] & 0xff ) + 0x100, 16).substring(1);
        }
        return result;
    }

	/**
	 * 复制文件夹到另外的文件夹
	 * @param
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2018/5/1 18:19
	*/
	public static final boolean fileCopy(final File s,final File t) {
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();//得到对应的文件通道
			out = fo.getChannel();//得到对应的文件通道
			in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

    public final boolean writeToFile(final String filePath,final String content){
        FileWriter fw = null;
        File f = new File("a.txt");
        try{
            if(!f.exists()){
                f.createNewFile();
            }
            fw = new FileWriter(f);
            BufferedWriter out = new BufferedWriter(fw);
            out.write(content,0,content.length() - 1);
            out.close();
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }

    protected static final boolean bilibili(final String pathDir){
        final File dir = new File(pathDir);
        try {
            if(dir.isDirectory()){
                final File[] childFiles = dir.listFiles();
                for(int i = 0; i < childFiles.length; i++){
                    final String name = childFiles[i].getPath();
                    int prefix = name.lastIndexOf("(A");
                    int suffix = name.lastIndexOf(").")+1;
                    try {
                        final String expression = name.substring(prefix,suffix);
                        final String fileName = name.replaceAll(expression,"");
                        new File(name).renameTo(new File(fileName));
                        dirRename(pathDir,"\\(\\)","");
                    } catch (Exception e){
                    }
                }
            }
            return true;
        } catch (Exception e){
            return false;
        }
    }
}