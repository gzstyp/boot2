package com.fwtai.service.module;

import com.alibaba.fastjson.JSONObject;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolCrypto;
import org.springframework.stereotype.Service;

/**
 * 公钥加密-私钥解密|私钥加密-->公钥解密
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2018-01-25 14:45
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
@Service
public class ApiService {

    /**公钥-(公钥加密-私钥解密)*/
    private final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl9N4FYpWKZPHYY8x75zJwWXV2Q5MZdIt7Qs9oU2R4DIdZEX996KLdwEasuxXdT5ZSZ25qP+zuM6e6fhKb80zVqTt/xyLxzUco1FeeUFXLDvsTRToSnTvI9dI30b5OOMotfEBMkfVUM0k735KlBo3bHyaS07e3UNqoSR+HS/uhS18fFCUoAiwsex2Hp/TCQlZWh0EjjV3gWekJyBC5RBBKHqTH9hp5iN1OJPonOP5Lx7S3irEKr0a/NCIRIAWEfIvJlGsdECeNTt6VqxfNAI87LXGsCDT4r3Y3Nb23HwAIiKSUdeZBvOF1GXzh3ItpCtcv+A4yBuyOU4qLZuudRQWRQIDAQAB";
    /**私钥*/
    private final String keyPrivate = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCX03gVilYpk8dhjzHvnMnBZdXZDkxl0i3tCz2hTZHgMh1kRf33oot3ARqy7Fd1PllJnbmo/7O4zp7p+EpvzTNWpO3/HIvHNRyjUV55QVcsO+xNFOhKdO8j10jfRvk44yi18QEyR9VQzSTvfkqUGjdsfJpLTt7dQ2qhJH4dL+6FLXx8UJSgCLCx7HYen9MJCVlaHQSONXeBZ6QnIELlEEEoepMf2GnmI3U4k+ic4/kvHtLeKsQqvRr80IhEgBYR8i8mUax0QJ41O3pWrF80AjzstcawINPivdjc1vbcfAAiIpJR15kG84XUZfOHci2kK1y/4DjIG7I5Tiotm651FBZFAgMBAAECggEBAJcyv9ojxB7vzpV3t3E6oSn6snbQ6IBWSepRUXT7/RRalC6yDXO56k3/SYl6GLbn+p93dde5nk+jZVr4K5kfr8rPwTxYP1OJrxVuCTmUOq4QlwlEkvjjbuwwj5/b3IIspvRaKKjcJLGtOuVOzhKj8SIXDMmGqCeN5fjIkNecTltNH6MGz+GkTCoRMmWptJ4Gcv34sRzkvJHj0oacH8jWYENrnIrxFf7XdQTyo9Ep2Y01R7mgM6n+Oosma5y/S+uScRG9T6OsOn++VP16yoDhGWOTr5odjboPk1q6W5PQmtnLqe53jspwwiqCJ10CokSyt+V7rxlMLaNcoLGdoL1WF1kCgYEA3p2gR14+YRE0aosDeYVNzH0Zud5PHDMZLj9d0w78yohNgQj27SJk6RgWhj8DYZ42Mv7wUPQ03ZTtK+O/PUWHlL/YNCvVeGVtUWpNyLVNTEuncOIsCEWRqPlO5Q0QC+pFBkVRiULpHHteqIcBw80QqU/ADMeN6hAIkgMc67sR+dsCgYEArpgtfGEJFgebMrAAMIjHzsYbcxTrDMrfcmeip+xEtXanZyYJ2GR0mETkHfPrLAJpgN2e08oyFgDz1PzKglJXfZnrWQJzfG8xbuRaWgtHmQn/8XJO+uNq+LEDEzzBFVZlO21ctOQyx1DeXfdGwJ9M275vLsB46zi1nECPtj/iel8CgYAI3njLPxr3HQtcj0tFJSVzVyyWNiAqvHJtYELprLgvjXyg/m1wByXED5yeHpaQ5g+8umFyqDgO9+b3D6zH0SRUrcv8UB7CHDBdxQFyVJ7Owljaa3hboAHsnHojMAEKqHu1gz97W3oUE2ozBqXwWhQm2rGJEM47nAXrY0pcrehKbwKBgQCt2/InkkVG/6oHToXRztMRT+tTrPfnVDowJY0upLNC9HX83D8fkeEdJDT4ssVznK9M2OPpSMaZbLdiV5ydK7vd4iEfELbJqbxTDef/FI5mkjOhTk/hvAbwwOwsAurPAaprBBs2MqtyGVBlx/BXrgoOJFN4/ccrjNwkTZIOTQ2NBwKBgQC4vECcIDR8WmrD17TiL03SqJV8b2VmOJV8BHyASw5pP66vn1aYfHuq2vebWSOaP7q8VNrZzeZmQpW7u0f3ZVd1y68liHIK0Z5wVjTdHVhPxOft8WkwwzGmVuVZRdKBaH3pfyHbEzMC8MZpcZsCwYSWe0VsWKmbhBurtT1fP2RByw==";

    /**公钥加密-私钥解密*/
    public String publicPrivate(final Integer row){
        final String data = ToolClient.executeRows(row);
        final JSONObject json = new JSONObject();
        //通过公钥加密
        final String key = ToolCrypto.encryptByPublicKey(data,publicKey);
        json.put("data",key);
        json.put("msg","操作成功");
        json.put("code",200);
        return json.toJSONString();
    }

    /**私钥加密-->公钥解密*/
    public String privatePublic(final Integer row){
        final String data = ToolClient.executeRows(row);
        final JSONObject json = new JSONObject();
        //通过私钥加密
        final String key = ToolCrypto.encryptByPrivateKey(data,keyPrivate);
        json.put("data",key);
        json.put("msg","操作成功");
        json.put("code",200);
        return json.toJSONString();
    }
}