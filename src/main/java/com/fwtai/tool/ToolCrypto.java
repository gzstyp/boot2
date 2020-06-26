package com.fwtai.tool;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;

/**
 * 非对称加密算法RSA算法组件
 * 非对称算法一般是用来传送对称加密算法的密钥来使用的，相对于DH算法，RSA算法只需要一方构造密钥，不需要
 * 大费周章的构造各自本地的密钥对了。DH算法只能算法非对称算法的底层实现。而RSA算法算法实现起来较为简单
 * 总结

 1、RSA与DH算法不同，只需要一套密钥就能完成加密、解密的工作

 2、通过代码能看出来，公钥长度明显小于私钥

 3、遵循：公钥加密-私钥解密，私钥加密-公钥解密的原则

 4、公钥和私钥肯定是完全不同

*/
public final class ToolCrypto {

    private final static String KEY_SHA = "SHA";
    private final static String KEY_SHA256 = "SHA-256";
    private final static String CHARSET_NAME = "utf-8";
    private final static String TOKEN = "DES";
    private final static String defaultKey = "fwt.cloud$Fwtai123YLZ";
    private final static String RSA = "RSA";
    private final static String PUBLIC_KEY = "RSAPublicKey";
    private final static String PRIVATE_KEY = "RSAPrivateKey";
    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    /**
     * sha256加密,意义不大
     * @param message
     * @return
    */
    private final static String encryptSha256(final String message) throws Exception {
        if (message == null) return "";
        final MessageDigest sha = MessageDigest.getInstance(KEY_SHA256);
        sha.update(message.getBytes(CHARSET_NAME));
        return byteArrayToHexString(sha.digest());
    }

    /**
     * sha加密,意义不大
     * @param message
     * @throws Exception
    */
    private final static String encryptSha(final String message) throws Exception {
        if (message == null) return "";
        final MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(message.getBytes(CHARSET_NAME));
        return byteArrayToHexString(sha.digest());
    }

    /**
     * 将一个字节转化成十六进制形式的字符串
     * @param b 字节数组
     * @return 字符串
    */
    private final static String byteToHexString(final byte b) {
        int ret = b;
        if (ret < 0) {
            ret += 256;
        }
        int m = ret / 16;
        int n = ret % 16;
        return hexDigits[m] + hexDigits[n];
    }

    /**
     * 转换字节数组为十六进制字符串
     * @param bytes 字节数组
     * @return 十六进制字符串
    */
    private final static String byteArrayToHexString(final byte[] bytes) {
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            sb.append(byteToHexString(bytes[i]));
        }
        return sb.toString();
    }

    /**
     * 获取公钥和私钥
     * @return map.get("private");map.get("public");
     * @throws Exception
    */
    public final static HashMap<String, Object> getkey(final String publicKey,final String privateKey)  {
        final HashMap<String, Object> map = new HashMap<String, Object>(2);
        try {
            map.put("private",getPrivateKey(privateKey));
            map.put("public",getPublicKey(publicKey));
            return map;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 生成一对公钥私钥
     * @param keysize 生成密钥位数,密钥长度 512-65536 & 64的倍数,一般的有512、1024、2048
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018/4/9 15:04
    */
    public final static HashMap<String, Object> initKey(final int keysize){
        //获得对象 KeyPairGenerator 参数 RSA 1024个字节
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance(RSA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        keyPairGen.initialize(keysize);
        //通过对象 KeyPairGenerator 获取对象KeyPair
        final KeyPair keyPair = keyPairGen.generateKeyPair();
        //通过对象 KeyPair 获取RSA公私钥对象RSAPublicKey RSAPrivateKey
        final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        //公私钥对象存入map中
        final HashMap<String, Object> keyMap = new HashMap<String, Object>();
        keyMap.put(PUBLIC_KEY,publicKey);
        keyMap.put(PRIVATE_KEY,privateKey);
        return keyMap;
    }

    //从方法initKey()获得公钥
    public final static String getPublicKey(final HashMap<String, Object> keyMap) {
        //获得map中的公钥对象 转为key对象
        final Key key = (Key) keyMap.get(PUBLIC_KEY);
        //byte[] publicKey = key.getEncoded();
        //编码返回字符串
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    //从方法initKey()获得私钥
    public final static String getPrivateKey(final HashMap<String, Object> keyMap)  {
        //获得map中的私钥对象 转为key对象
        final Key key = (Key) keyMap.get(PRIVATE_KEY);
        //byte[] privateKey = key.getEncoded();
        //编码返回字符串
        return (new BASE64Encoder()).encodeBuffer(key.getEncoded());
    }

    /**java生成的私钥是pkcs8格式的公钥是x.509格式*/
    private final static PublicKey getPublicKey(final String key) throws Exception {
        final byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePublic(keySpec);
    }

    private final static PrivateKey getPrivateKey(final String key) throws Exception {
        final byte[] keyBytes = (new BASE64Decoder()).decodeBuffer(key);
        final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 通过公钥加密
     * @param data
     * @param publicKey
     * @return
    */
    public final static String encryptByPublicKey(final String data,final PublicKey publicKey) {
        try {
            final StringBuilder sb = new StringBuilder();
            final RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            int key_len = rsaPublicKey.getModulus().bitLength() / 8;
            final String[] datas = splitString(data,key_len - 11);
            for (String mes : datas) {
                sb.append(bcd2Str(cipher.doFinal(mes.getBytes(CHARSET_NAME))));
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过私钥加密
     * @param data
     * @param privateKey
     * @return
    */
    public final static String encryptByPrivateKey(final String data,final PrivateKey privateKey) {
        try {
            final StringBuilder sb = new StringBuilder();
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
            int key_len = rsaPrivateKey.getModulus().bitLength() / 8;
            final String[] datas = splitString(data,key_len - 11);
            for (String mes : datas) {
                sb.append(bcd2Str(cipher.doFinal(mes.getBytes(CHARSET_NAME))));
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过公钥加密
     * @param message
     * @param publicKey
     * @return
    */
    public final static String encryptByPublicKey(final String message,final String publicKey) {
        try {
            final PublicKey pk = getPublicKey(publicKey);
            return encryptByPublicKey(message,pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过私钥加密
     * @param message
     * @param privateKey
     * @return
    */
    public final static String encryptByPrivateKey(final String message,final String privateKey) {
        try {
            final PrivateKey pk = getPrivateKey(privateKey);
            return encryptByPrivateKey(message,pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过私钥解密
     * @param data
     * @param privateKey
     * @return
    */
    public final static String decryptByPrivateKey(final String data,final PrivateKey privateKey) {
        try {
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            final StringBuilder sb = new StringBuilder();
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            final int key_len = rsaPrivateKey.getModulus().bitLength() / 8;
            final byte[] bytes = data.getBytes(CHARSET_NAME);
            final byte[] bcd = ASCII_To_BCD(bytes,bytes.length);
            final byte[][] arrays = splitArray(bcd,key_len);
            for (final byte[] arr : arrays) {
                sb.append(new String(cipher.doFinal(arr),CHARSET_NAME));
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 通过公钥解密
     * @param data
     * @param publicKey
     * @return
     */
    public final static String decryptByPublicKey(final String data,final PublicKey publicKey) {
        try {
            final RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            final StringBuilder sb = new StringBuilder();
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            final int key_len = rsaPublicKey.getModulus().bitLength() / 8;
            final byte[] bytes = data.getBytes(CHARSET_NAME);
            final byte[] bcd = ASCII_To_BCD(bytes,bytes.length);
            final byte[][] arrays = splitArray(bcd,key_len);
            for (final byte[] arr : arrays) {
                sb.append(new String(cipher.doFinal(arr),CHARSET_NAME));
            }
            return sb.toString();
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**通过私钥解密*/
    public final static String decryptByPrivateKey(final String data,final String privateKey) {
        try {
            final PrivateKey pk = getPrivateKey(privateKey);
            return decryptByPrivateKey(data,pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**通过公钥解密*/
    public final static String decryptByPublicKey(final String data,final String publicKey) {
        try {
            final PublicKey pk = getPublicKey(publicKey);
            return decryptByPublicKey(data,pk);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 公钥加密二进制
     * @param bytes
     * @param publicKey
     * @return
     */
    private final static byte[] encryptByteByPublicKey(final byte[] bytes,final PublicKey publicKey){
        try {
            final RSAPublicKey rsaPublicKey = (RSAPublicKey) publicKey;
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE,rsaPublicKey);
            return cipher.doFinal(bytes);
        }catch (Exception e) {
            return null;
        }
    }

    /**
     * 解密
     * @param bytes
     * @param privateKey
     * @return
     */
    public final static byte[] decryptBytesByPrivateKey(byte[] bytes, PrivateKey privateKey) {
        try {
            final RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) privateKey;
            final Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            return cipher.doFinal(bytes);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private final static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i = 0; i < x + z; i++) {
            if (i == x + z - 1 && y != 0) {
                str = string.substring(i * len, i * len + y);
            } else {
                str = string.substring(i * len, i * len + len);
            }
            strings[i] = str;
        }
        return strings;
    }

    /**
     * ASCII码转BCD码
     */
    private final static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }

    private final static byte asc_to_bcd(byte asc) {
        byte bcd;
        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    /**
     * BCD转字符串
     */
    private final static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;
        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }

    private final static byte[][] splitArray(byte[] data, int len) {
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if (y != 0){
            z = 1;
        }
        byte[][] arrays = new byte[x + z][];
        byte[] arr;
        for (int i = 0; i < x + z; i++) {
            arr = new byte[len];
            if (i == x + z - 1 && y != 0) {
                System.arraycopy(data, i * len, arr, 0, y);
            } else {
                System.arraycopy(data, i * len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }

    /**
     * jdk1.8的base64加密
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月27日 10:48:50
   */
    private final static String base64Encrypt(final String text){
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * jdk1.8的base64解密
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018年9月27日 10:48:43
   */
    private final static String base64Decode(final String base64){
        return new String(Base64.getDecoder().decode(base64),StandardCharsets.UTF_8);
    }

    /**
     * 使用指定默认key加密
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2018/9/30 11:38
    */
    public static String encrypt(final String text){
        try {
            return byte2hex(encrypt(text.getBytes(CHARSET_NAME),defaultKey.getBytes(CHARSET_NAME)));
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 使用指定默认key解密
     * @return String
     * @author 田应平
     * @date 2018年9月30日 11:46:37
    */
    public static String decrypt(final String data){
        try {
            return new String(decrypt(hex2byte(data.getBytes(CHARSET_NAME)),defaultKey.getBytes(CHARSET_NAME)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //加密
    private static byte[] encrypt(final byte[] data,final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();//DES算法要求有一个可信任的随机数源
        final DESKeySpec dks = new DESKeySpec(key);//从原始密匙数据创建DESKeySpec对象
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TOKEN);//创建一个密匙工厂，然后用它把DESKeySpec转换成一个SecretKey对象
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance(TOKEN);// Cipher对象实际完成加密操作
        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);// 用密匙初始化Cipher对象
        return cipher.doFinal(data);//获取数据并加密,正式执行加密操作
    }

    //解密
    private static byte[] decrypt(final byte[] data,final byte[] key) throws Exception {
        final SecureRandom sr = new SecureRandom();//DES算法要求有一个可信任的随机数源
        final DESKeySpec dks = new DESKeySpec(key);//从原始密匙数据创建一个DESKeySpec对象
        final SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(TOKEN);//创建一个密匙工厂，然后用它把DESKeySpec对象转换成一个SecretKey对象
        final SecretKey securekey = keyFactory.generateSecret(dks);
        final Cipher cipher = Cipher.getInstance(TOKEN);//Cipher对象实际完成解密操作
        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);//用密匙初始化Cipher对象
        return cipher.doFinal(data);//现在，获取数据并解密正式执行解密操作
    }

    private static String byte2hex(final byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1){
                hs = hs + "0" + stmp;
            }else{
                hs = hs + stmp;
            }
        }
        return hs;
    }

    private static byte[] hex2byte(final byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("出错了,长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    public final static void main(String[] args){
        //final String data = "{\"code\":200,\"data\":\"13765121695\"}";
        //final String data = "13765121695";
        /**初始化密钥*/
        final HashMap<String, Object> initKey = ToolCrypto.initKey(1024);
        /**得到公钥*/
        final String publicKey = ToolCrypto.getPublicKey(initKey);
        /**得到私钥*/
        final String privateKey = ToolCrypto.getPrivateKey(initKey);
        //System.out.println("原始数据data:\n"+data);
        System.out.println("publicKey==\n"+publicKey);
        System.out.println("privateKey==\n"+privateKey);
        //通过公钥加密
        //final String s = ToolCrypto.encryptByPublicKey(data,publicKey);
        //System.out.println("加密的字符串==\n"+s);
        //通过私钥解密
        //final String decrypt = ToolCrypto.decryptByPrivateKey(s,privateKey);
        //System.out.println("通过私钥解密decrypt==\n"+decrypt);
    }
}