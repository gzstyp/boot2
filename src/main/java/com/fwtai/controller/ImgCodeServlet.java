package com.fwtai.controller;

import com.fwtai.config.ConfigFile;
import org.apache.commons.lang3.RandomStringUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Random;

/**
 * 生成图形验证码
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月12日 01:09:28
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ImgCodeServlet extends HttpServlet implements Serializable {

	private static final long serialVersionUID = 1L;
	/**图片的宽度*/
	private final static short WIDTH = 120;
	/**图片的高度*/
	private final static short HEIGHT = 40;
	/**图片默认的显示个数*/
	private final static short LENGTH = 5;
	private final static Random random = new Random();

	/**
	 * 画随机码图
	 * @param text
	 * @param g
	 * @param width
	 * @param height
	 * @throws IOException
	*/
	private final void Render(final String text,final Graphics g,final short width,final short height) throws IOException {
        g.setColor(Color.WHITE); // 背景色
        g.fillRect(0, 0, width, height);
        g.setColor(new Color(173, 180, 190));
        for (int i = 0; i < 50; i++){
            g.drawOval(random.nextInt(110),random.nextInt(10),100 + random.nextInt(10),100 + random.nextInt(10));
        }
        final Font mFont = new Font("Arial", Font.BOLD, 35);//字体大小
        g.setFont(mFont);
        g.setColor(getRandColor(128,200));
        g.drawString(text,2,33);//字迹位置
	}

	/**
	 * 给定范围获得随机颜色
	 * @param
	 * @作者 田应平
	 * @QQ 444141300
	 * @创建时间 2019/9/9 1:03
	*/
    protected final Color getRandColor(int fc, int bc){
        if(fc > 255)
            fc = 254;
        if(bc > 255)
            bc = 255;
        final int result = bc - fc;
        int r = fc + random.nextInt(result);
        int g = fc + random.nextInt(result);
        int b = fc + random.nextInt(result);
        return new Color(r, g, b);
    }

	/**固定生成5个图形验证码*/
	protected final String rand(final int length){
		final String chars = "qwertyuioplkjhgfdsazxcvbnmMNBVCXZASDFGHJKLPOIUYTREWQ0123456789";
		final String image = RandomStringUtils.random(length < 5 ? LENGTH : length,chars);
		return image.replaceAll("0","W").replaceAll("o","R").replaceAll("O","p").replaceAll("1","T").replaceAll("l","7");
	}
	
	/**随机生成5或4个图形验证码*/
	protected final String rand(){
		final int length = new Random().nextInt(6) < 4 ? 5 : 4;
		final String chars = "qwertyuioplkjhgfdsazxcvbnmMNBVCXZASDFGHJKLPOIUYTREWQ0123456789";
		final String image = RandomStringUtils.random(length,chars);
		return image.replaceAll("0","W").replaceAll("o","R").replaceAll("O","p").replaceAll("1","T").replaceAll("l","7");
	}

	@Override
	protected void service(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException, IOException{
		final HttpSession session = req.getSession();
		resp.setContentType("image/jpeg");
		final ServletOutputStream sos = resp.getOutputStream();
		// 设置浏览器不要缓存此图片
		resp.setHeader("Pragma", "No-cache");
		resp.setHeader("Cache-Control", "no-cache");
		resp.setDateHeader("Expires", 0);
		final String rands = rand();//随机生成5或4个图形验证码
		// 创建内存图象并获得其图形上下文
		final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		final Graphics g = image.getGraphics();
		Render(rands,g,WIDTH,HEIGHT);
		g.dispose();
		// 将图像输出到客户端
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ImageIO.write(image, "JPEG", bos);
		byte[] buf = bos.toByteArray();
		resp.setContentLength(buf.length);
		// 下面的语句也可写成： bos.writeTo(sos);
		sos.write(buf);
		bos.close();
		sos.close();
		// 将当前验证码存入到 Session 中,便于登录验证图形验证码
		session.setAttribute(ConfigFile.imageCode,rands);
	}
}