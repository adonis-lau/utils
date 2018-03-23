package com.ideal.decrypt;

import com.ideal.decryptor.DecodeManager;
import com.ideal.encryptor.EncodeManager;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2018/1/22 17:42
 */
public class Test {
    public static void main(String[] args) {

        String before = "1";

        System.out.println("before：" + before);
        //加密
        String cipher = EncodeManager.EncodeMessageUTF8(before, "meepo4", "hdfs-encrypt", "Encrypt");
        System.out.println(cipher);
        //解密
        DecodeManager decodeManager = new DecodeManager();
        String after = decodeManager.DecodeMessageUTF8(cipher, "meepo4", "hdfs-encrypt", "Encrypt");
        System.out.println("after： " + after);
    }
}