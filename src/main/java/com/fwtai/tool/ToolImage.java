package com.fwtai.tool;

import net.coobird.thumbnailator.Thumbnailator;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Encoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 图片处理
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-01-11 14:02
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolImage{

    /**把图片转为base64编码,推荐使用*/
    public final static String getImageBase64(final String imagePath) throws Exception{
        final InputStream in = new FileInputStream(imagePath);
        final byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        return new String(Base64.encodeBase64(data));
    }

    /**把图片转为base64编码,推荐使用*/
    public final static String getImageBase64(final File imageFile) throws Exception{
        final InputStream in = new FileInputStream(imageFile);
        final byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        return new String(Base64.encodeBase64(data));
    }

    /**把图片转为base64编码,会提示过时*/
    public final static String getBase64(final File imageFile) throws Exception{
        final InputStream in = new FileInputStream(imageFile);
        final byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        return new BASE64Encoder().encode(data);
    }

    /**把图片转为base64编码,会提示过时*/
    public final static String getBase64(final String imagePath) throws Exception{
        final InputStream in = new FileInputStream(imagePath);
        final byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        return new BASE64Encoder().encode(data);
    }

    /**采用第三方jar压缩图片*/
    public final static boolean createThumbnail(final File inFile,final File outFile,final int width,final int height){
        try{
            Thumbnailator.createThumbnail(inFile,outFile,width,height);
            return true;
        }catch(IOException e){
            e.printStackTrace();
            return false;
        }
    }
}