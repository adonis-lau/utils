package com.cdc.smgp.msg.service;


import com.cdc.smgp.msg.Result;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/26 11:37
 */
public interface IMsgService {
    public abstract Result sendMsg(String paramString1, String paramString2, String[] paramArrayOfString, String paramString3);
}
