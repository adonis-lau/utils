package com.cdc.smgp.msg;


import java.io.Serializable;

/**
 * @author Adonis Lau
 * @eamil adonis.lau.dev@gmail.com
 * @date Created in 2017/11/26 11:36
 */
public class Result implements Serializable {
    private static final long serialVersionUID = 1L;
    private int result;
    private String description;

    public Result(int result, String description) {
        this.result = result;
        this.description = description;
    }

    public int getResult() {
        return this.result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public boolean isSucce() {
        return this.result == 0;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
