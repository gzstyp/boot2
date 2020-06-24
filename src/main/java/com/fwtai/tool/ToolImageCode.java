package com.fwtai.tool;

import java.awt.*;
import java.util.Random;

/**
 * 图形验证码
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019-12-22 14:19
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class ToolImageCode{

    private String arithmetic;
    private Integer sessionValue;

    public final void draw(final String text,final Graphics g,final short width,final short height){
        g.setColor(Color.WHITE); // 背景色
        g.fillRect(0,0,width,height);
        g.setColor(new Color(173,180,190));
        final Random random = new Random();
        for (int i = 0; i < 50; i++){
            g.drawOval(random.nextInt(88),random.nextInt(10),100 + random.nextInt(10),100 + random.nextInt(10));
        }
        final Font mFont = new Font("Arial,Serif,Sans",Font.BOLD,30);//字体大小
        g.setFont(mFont);
        g.setColor(getRandColor(128,200));
        g.drawString(text,0,31);//字迹位置
        g.dispose();//销毁图像
    }

    private Color getRandColor(int fc, int bc){
        if(fc > 255)
            fc = 254;
        if(bc > 255)
            bc = 255;
        final int result = bc - fc;
        final Random random = new Random();
        int r = fc + random.nextInt(result);
        int g = fc + random.nextInt(result);
        int b = fc + random.nextInt(result);
        return new Color(r, g, b);
    }

    public final ToolImageCode build(){
        final int subtractor = new Random().nextInt(99);
        final int minuend = new Random().nextInt(99);
        int result = subtractor - minuend;
        String arithmetic = subtractor+"-"+minuend+"=?";
        final String[] arr = {"+","-","x"};
        final int random = new Random().nextInt(3);
        final String operator = arr[random];
        switch (operator){
            case "+":
                result = minuend + subtractor;
                arithmetic = minuend+"+"+subtractor+"=?";
                break;
            case "x":
                result = minuend * subtractor;
                arithmetic = minuend+"×"+subtractor+"=?";
                break;
            default:
                if(minuend > subtractor){
                    result = minuend - subtractor;
                    arithmetic = minuend+"-"+subtractor+"=?";
                }
                break;
        }
        this.sessionValue = result;
        this.arithmetic = arithmetic;
        return this;
    }

    public final Integer getSessionValue(){
        return sessionValue;
    }

    public final String getArithmetic(){
        return arithmetic;
    }

    /*
        用法:
        @GetMapping("/getImageCode")
        public void getImageCode(final HttpServletRequest request,final HttpServletResponse response) throws IOException{
            final HttpSession session = request.getSession();
            response.setContentType("image/jpeg");
            final ServletOutputStream sos = response.getOutputStream();
            response.setHeader("Pragma", "No-cache no-store");
            response.setHeader("Cache-Control","no-cache");
            response.setDateHeader("Expires",0);
            final short width = 122;
            final short height = 40;
            final ToolImageCode imageCode = new ToolImageCode().build();
            final Integer sessionValue = imageCode.getSessionValue();
            final String arithmetic = imageCode.getArithmetic();
            final BufferedImage image = new BufferedImage(width,height, BufferedImage.TYPE_3BYTE_BGR);
            imageCode.draw(arithmetic,image.getGraphics(),width,height);
            final ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(image,"JPEG",bos);
            final byte[] buf = bos.toByteArray();
            response.setContentLength(buf.length);
            sos.write(buf);
            bos.close();
            sos.close();
            session.setAttribute("imageCode",sessionValue);
        }
     */
}