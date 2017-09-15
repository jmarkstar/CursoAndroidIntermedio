package com.jmarkstar.s3;

/**
 * Created by jmarkstar on 9/15/17.
 */

public class MyDataObject {
    private int var1;
    private String var2;

    public MyDataObject(int var1, String var2) {
        this.var1 = var1;
        this.var2 = var2;
    }

    public int getVar1() {
        return var1;
    }

    public void setVar1(int var1) {
        this.var1 = var1;
    }

    public String getVar2() {
        return var2;
    }

    public void setVar2(String var2) {
        this.var2 = var2;
    }
}
