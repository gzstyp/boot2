package com.fwtai;

import com.fwtai.tool.ToolCrypto;
import com.fwtai.tool.ToolOkHttp;
import com.fwtai.tool.ToolString;

import java.util.HashMap;

/**
 *公钥加密-私钥解密
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-23 19:27
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class RAS {

    /**公钥*/
    //private final static String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl9N4FYpWKZPHYY8x75zJwWXV2Q5MZdIt7Qs9oU2R4DIdZEX996KLdwEasuxXdT5ZSZ25qP+zuM6e6fhKb80zVqTt/xyLxzUco1FeeUFXLDvsTRToSnTvI9dI30b5OOMotfEBMkfVUM0k735KlBo3bHyaS07e3UNqoSR+HS/uhS18fFCUoAiwsex2Hp/TCQlZWh0EjjV3gWekJyBC5RBBKHqTH9hp5iN1OJPonOP5Lx7S3irEKr0a/NCIRIAWEfIvJlGsdECeNTt6VqxfNAI87LXGsCDT4r3Y3Nb23HwAIiKSUdeZBvOF1GXzh3ItpCtcv+A4yBuyOU4qLZuudRQWRQIDAQAB";
    /**私钥*/
    //private final static String keyPrivate = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCX03gVilYpk8dhjzHvnMnBZdXZDkxl0i3tCz2hTZHgMh1kRf33oot3ARqy7Fd1PllJnbmo/7O4zp7p+EpvzTNWpO3/HIvHNRyjUV55QVcsO+xNFOhKdO8j10jfRvk44yi18QEyR9VQzSTvfkqUGjdsfJpLTt7dQ2qhJH4dL+6FLXx8UJSgCLCx7HYen9MJCVlaHQSONXeBZ6QnIELlEEEoepMf2GnmI3U4k+ic4/kvHtLeKsQqvRr80IhEgBYR8i8mUax0QJ41O3pWrF80AjzstcawINPivdjc1vbcfAAiIpJR15kG84XUZfOHci2kK1y/4DjIG7I5Tiotm651FBZFAgMBAAECggEBAJcyv9ojxB7vzpV3t3E6oSn6snbQ6IBWSepRUXT7/RRalC6yDXO56k3/SYl6GLbn+p93dde5nk+jZVr4K5kfr8rPwTxYP1OJrxVuCTmUOq4QlwlEkvjjbuwwj5/b3IIspvRaKKjcJLGtOuVOzhKj8SIXDMmGqCeN5fjIkNecTltNH6MGz+GkTCoRMmWptJ4Gcv34sRzkvJHj0oacH8jWYENrnIrxFf7XdQTyo9Ep2Y01R7mgM6n+Oosma5y/S+uScRG9T6OsOn++VP16yoDhGWOTr5odjboPk1q6W5PQmtnLqe53jspwwiqCJ10CokSyt+V7rxlMLaNcoLGdoL1WF1kCgYEA3p2gR14+YRE0aosDeYVNzH0Zud5PHDMZLj9d0w78yohNgQj27SJk6RgWhj8DYZ42Mv7wUPQ03ZTtK+O/PUWHlL/YNCvVeGVtUWpNyLVNTEuncOIsCEWRqPlO5Q0QC+pFBkVRiULpHHteqIcBw80QqU/ADMeN6hAIkgMc67sR+dsCgYEArpgtfGEJFgebMrAAMIjHzsYbcxTrDMrfcmeip+xEtXanZyYJ2GR0mETkHfPrLAJpgN2e08oyFgDz1PzKglJXfZnrWQJzfG8xbuRaWgtHmQn/8XJO+uNq+LEDEzzBFVZlO21ctOQyx1DeXfdGwJ9M275vLsB46zi1nECPtj/iel8CgYAI3njLPxr3HQtcj0tFJSVzVyyWNiAqvHJtYELprLgvjXyg/m1wByXED5yeHpaQ5g+8umFyqDgO9+b3D6zH0SRUrcv8UB7CHDBdxQFyVJ7Owljaa3hboAHsnHojMAEKqHu1gz97W3oUE2ozBqXwWhQm2rGJEM47nAXrY0pcrehKbwKBgQCt2/InkkVG/6oHToXRztMRT+tTrPfnVDowJY0upLNC9HX83D8fkeEdJDT4ssVznK9M2OPpSMaZbLdiV5ydK7vd4iEfELbJqbxTDef/FI5mkjOhTk/hvAbwwOwsAurPAaprBBs2MqtyGVBlx/BXrgoOJFN4/ccrjNwkTZIOTQ2NBwKBgQC4vECcIDR8WmrD17TiL03SqJV8b2VmOJV8BHyASw5pP66vn1aYfHuq2vebWSOaP7q8VNrZzeZmQpW7u0f3ZVd1y68liHIK0Z5wVjTdHVhPxOft8WkwwzGmVuVZRdKBaH3pfyHbEzMC8MZpcZsCwYSWe0VsWKmbhBurtT1fP2RByw==";
    private final static String keyPrivate = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANYI5tJ0Tr2D9KAq4iYFle6DI/LquqMh+NkHiayrp3InsQqRbg+YBEFFUx+agttjkGdZghcIgq+PZ7vjxltThlIgOhEoRtQCzWLSP+GlIQ+frSHxEA9z/ci1BLKy9LU5Olx3yb15vABPqZOsVR79/pLwljrdWj4DgcopWYhffG37AgMBAAECgYEAnhwzT1Jl/G/nHQ6wwv+rU2eC/ebSesIUz1ZXKLwhVR3nFfR6xoA43HK2xgjhE+pH+WDQze0WhVQ9gDnyBPL9rICmDeNjq3owMLXYb9Y49VmM1D35HFXrjIhHGGMD+yNLsXHQW+z82qeD1Hk4JLin0FeWZObhWv2aKcYCp6H8QLkCQQD77U+QO8JwSdKZCsIzow5mmbJ3qBMVY33Tq4B4WHTJGsfWo07zWbjVqKKCmQQU60MIKowoconyOF2Q9v1clHUVAkEA2X7CrPLJoHRUZDO+j7fzfjf9yWzWoqhjMdTNF9yu5OVB0xhC/7mcFdVporIw1GBCv+gY7AYtiN14lk9Rff86zwJBAK85FQDd/9R4FEmsKP0faXHFb1M4jOVwiOEDxVxvMfN6dAp/Ffe+rQe2vimV4jfmln4Eke3EFNxrhfDBSeHtxC0CQHWmXzJkxvKX2sq1PuDJVdqx+QOLb3NfeQEMpsDArI6/AONqnVttkOQypEjQ8R8Azzq2Fz0dfmYpv0A3udJZTPsCQE2bqsFBmHGcblEaEbZzKI9bthFk3XhWMuMfiYwr6rffxAoztX9C3EMh5y/47FIg2mmbNPWgP01jNGVMJrFDDH4=";

    public static void main(String[] args){
        //publicPrivate();
        //privatePublic();
        final String data = "BLwPqaoigpyLVu0EwHrogDl7IYbbekMwzQFDivKClt6N4AdOOdSnUl4eeYx/N33ZuzZx61ZRAcrwND80bM7P1whw0GfyJ+EJUcOTyMHE+L+Ja1KG6UcvaNUj9usgiGXw8adLNdOBm7B9gNWEyWQybMAmeIE2PMZ3O1hkUxwM6fY=";
        final String decrypt = ToolCrypto.decryptByPrivateKey(data,keyPrivate);
        System.out.println("公钥加密-->私钥解密的数据:"+decrypt);
    }

    /**公钥加密-->私钥解密*/
    public final static void publicPrivate(){
        final String url = "http://192.168.3.25/user/publicPrivate?rows=1";
        /**初始化密钥*/
        final HashMap<String, Object> initKey = ToolCrypto.initKey(1024);
        /**得到私钥*/
        //final String privateKey = ToolCrypto.getPrivateKey(initKey);
        //通过私钥解密
        try {
            final String json = ToolOkHttp.ajaxGet(url).body().string();
            final HashMap<String,String> map = ToolString.parseJsonObject(json);
            final String data = map.get("data");
            final String decrypt = ToolCrypto.decryptByPrivateKey(data,keyPrivate);
            System.out.println("公钥加密-->私钥解密的数据:"+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**私钥加密-->公钥解密*/
    public final static void privatePublic(){
        final String url = "http://192.168.3.25/user/privatePublic?rows=0";
        /**初始化密钥*/
        final HashMap<String, Object> initKey = ToolCrypto.initKey(1024);
        /**得到私钥*/
        //final String privateKey = ToolCrypto.getPrivateKey(initKey);
        //通过公钥解密
        try {
            final String json = ToolOkHttp.ajaxGet(url).body().string();
            final HashMap<String,String> map = ToolString.parseJsonObject(json);
            final String data = map.get("data");
            //final String decrypt = ToolCrypto.decryptByPublicKey(data,publicKey);
            //System.out.println("私钥加密-->公钥解密的数据:"+decrypt);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}