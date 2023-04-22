package com.mall.common.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;


/**
 * @description:
 * @author: wzh
 * @date: 2023/3/26 19:54
 */
@Slf4j
public class SecurityUtil {

    private static final String SECRET_KEY = "LhI7UXoF97n0E2TO";

    private static final String API_SECRET_KEY = "XZYE8HiXABKrBfXT";

    public synchronized static String DecryptAllPara(String apiUrl) {
        String decryptValue = "";
        try {
            decryptValue = URLDecoder.decode(Decrypt(apiUrl.getBytes(StandardCharsets.UTF_8)), "utf-8");
            decryptValue = URLDecoder.decode(decryptValue, "utf-8");
        } catch (Exception e) {
            log.error("DecryptAllPara====>解密全部参数失败:", e);
            e.printStackTrace();
        }
        return decryptValue;
    }

    /**
     * 解密
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String Decrypt(byte[] src) throws Exception {
        SecretKey sKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.DECRYPT_MODE, sKey);
        byte[] b = Base64Utils.decodeFromString(new String(src, StandardCharsets.UTF_8).trim());
        byte[] result = ci.doFinal(b);
        return new String(result, StandardCharsets.UTF_8);
    }


    /**
     * 解密
     *
     * @param src
     * @return
     * @throws Exception
     */
    public static String Decrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.DECRYPT_MODE, sKey);
        byte[] b = Base64Utils.decodeFromString(new String(src, StandardCharsets.UTF_8).trim());
        byte[] result = ci.doFinal(b);
        return new String(result, StandardCharsets.UTF_8);
    }


    /**
     * @param src
     * @throws Exception
     * @return加密
     */
    public static String Encrypt(byte[] src) throws Exception {
        SecretKey sKey = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
    }


    /**
     * @param src
     * @throws Exception
     * @return加密
     */
    public static String Encrypt(byte[] src, String key) throws Exception {
        SecretKey sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher ci = Cipher.getInstance("AES");
        ci.init(Cipher.ENCRYPT_MODE, sKey);
        byte[] b = ci.doFinal(src);
        return Base64Utils.encodeToString(b);
    }


    /**
     * 从cpapi转发到业务网关
     *
     * @param param
     * @return
     */
    public synchronized static String encryptAllPara(String param) {
        String encryptvalue = null;
        try {
            String data = "" + "_" + URLEncoder.encode(param, "utf-8");
            sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
            SecretKey sKey = new SecretKeySpec(API_SECRET_KEY.getBytes(), "AES");
            Cipher ci = Cipher.getInstance("AES");
            ci.init(Cipher.ENCRYPT_MODE, sKey);
            byte[] b = ci.doFinal(data.getBytes(StandardCharsets.UTF_8));
            encryptvalue = URLEncoder.encode(encoder.encode(b), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptvalue;
    }


    public static void main(String[] args) {
        try {
            //String param = "a=b=c";
            //System.out.println("请求加密param:" + param);
            //String secret = URLEncoder.encode(param, "utf-8");
            //System.out.println("URLenCode:" + secret);
            //String str = SecurityUtil.Encrypt(secret.getBytes(StandardCharsets.UTF_8), "LhI7UXoF97n0E2TO");
            //System.out.println("加密后str:" + str);
            //String encode = URLEncoder.encode(str, "utf-8");
            //System.out.println("(这个要传给服务器了)---加密后再Url.enCode:" + encode);

            String encode = "q1SK8BUp0W4xl%2F1ivuYtYzlhsLPMNL8eKDYVVfThTBtWV%2B5kmqfm9oaNo1HQT5q6OaX1FGvr%2BkiejTEIC0qbqWriUKd7z8SGzjOt79j1D%2FxdGHCVpvj%2Fl%2FPbWfQWfUyiVfzPCGbpAcmqOjBBUEdiTVk9nA0SuSru%2F4OmWINjaQWVd5VyT%2FV5WfxdMUEJiFr6dANIqKavRWtePbkoPEbEOpdfIk2Dl%2BXhai4EfAq5ANq9ygd5zoOPcIlRX6W%2FrZY7xYntd4eR2nNbEH%2FsXn%2B2v2Mew8qrVKc6cV2mncMbBSzDoK6xeMreJmgpyL8WKoWESArkgzHm%2F13bY3xrohlMQYk9LyBjufTs2aG0gzyNPbfdQwQMocugIkmMRIHjQtkExh%2BHcjmjVpvXe1w6sWKuH%2Bow3Bvu5SoY%2FTwSWZskpBAnkL%2Bwfwlkett%2FHUyCS5ec9urz2gC0KcqqNBajtHISmkjp1Sn9HdAA41ao8o4vIFDZjS4EnARiYl5Piq8qETIOaDhATMte%2B9IGrMYy7wzcIZPKoOIC4r0J6I%2Bq8gwezuceJqbWGcb%2BnqYVDCEoAB86vjahkknBTQa%2Bfti7JmtNDOaIfeD%2FAfNCUVhAuihVsgqRoYmCVcLuo%2FT9FZhZTCyNY3NgPoDmPWd2rKjK4TiuY3pmKVeu0bZW31Zc4u90nVHBezihW1q0qhRxWIBuCwt3NLmudK1WFtobD0NbDm6MKpu1mQ6e17AK5%2Fu15UoMqZJ1ht4VpvB6FAy1ntaWU1GytJbybGG4NtFSY8F0vQDBhGsJ3Uun%2BPpNDFDMKe5fN44X2u6BmFyjaAUJm70C6Hn0%2BgpGt8jHnhVxTPqfWCHGdCZn0BSFMimoN29rketyhSUsZ2GuRQggRKlWT3TgjZNbNmg0p%2FqwcbNFbcZiMz7qwg%3D%3D";
            System.out.println("------------------------------------------------------");
            String accessSecretData = URLDecoder.decode(encode, "utf-8");
            String result = SecurityUtil.Decrypt(accessSecretData.getBytes(StandardCharsets.UTF_8), "LhI7UXoF97n0E2TO");
            result = URLDecoder.decode(result, "utf-8");
            System.out.println("服务器解密后str:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}