package com.fwtai.tool;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * 密码生成密码加密数据
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年2月13日 下午10:31:34
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolSHA {

    private static final String SHA1 = "SHA-1";
    private final static String ALGORITHM = "DES";
    private final static String ENCODE = "utf-8";
    /*盐值加密*/
    private final static String SLAT = "www.fwtai.com";
    /*私盐加密*/
    private final static String privateSalt = "Www.Fwtai.Com";

    /**
     * SHA-1方式密码加密,仅加密一次
     * @作者 田应平
     * @创建时间 2017年1月1日 上午10:22:54
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    private final static String encryptHash(final String object){
        return String.valueOf(new Sha256Hash(SHA1,object));
    }

    /**
     * SHA-1方式用户名密码加密,已添加盐值加密,不可逆向
     * @作者 田应平
     * @创建时间 2017年1月1日 上午10:22:37
     * @QQ号码 444141300
     * @主页 http://www.fwtai.com
    */
    public final static String encoder(final Object userName,final Object password){
        return String.valueOf(new SimpleHash(SHA1,userName,encryptHash(password+privateSalt)));
    }

    /**
     * 加密,可逆向,可用当前方法decryptKey()解密
     * @作者 田应平
     * @创建时间 2017年11月27日 14:28:46
    */
    public final static String encryptKey(final String resource){
        try {
            return byte2hex(encrypt(resource.getBytes(ENCODE),SLAT.getBytes(ENCODE)));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 解密,可逆向,适用于当前的方法encryptKey()加密后的密文
     * @作者 田应平
     * @创建时间 2017年11月27日 14:29:44
    */
    public final static String decryptKey(final String ciphertext){
        try {
            return new String(decrypt(hex2byte(ciphertext.getBytes(ENCODE)),SLAT.getBytes(ENCODE)));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private final static byte[] hex2byte(final byte[] b){
        if ((b.length % 2) != 0)throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2){
            String item = new String(b,n,2);
            b2[n / 2] = (byte) Integer.parseInt(item,16);
        }
        return b2;
    }

    private final static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }

    private final static byte[] encrypt(final byte[] data,final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();
        final DESKeySpec dks = new DESKeySpec(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }

    private final static byte[] decrypt(final byte[] data,final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();
        final DESKeySpec dks = new DESKeySpec(key);
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
        return cipher.doFinal(data);
    }
}