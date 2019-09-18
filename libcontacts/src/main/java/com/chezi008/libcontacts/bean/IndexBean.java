package com.chezi008.libcontacts.bean;

import com.chezi008.libcontacts.listener.ISuspensionInterface;

/**
 * - @Description:
 * - @Author:  chezi008/chezi008@qq.com
 * - @Time:  2019/6/13 9:50
 */
public class IndexBean implements ISuspensionInterface {
    /**
     * 英文下标
     */
    protected String py;
    protected int index = -1;

    @Override
    public boolean isShowSuspension() {
        return index<0;
    }

    @Override
    public String getSuspensionTag() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    public String getPy() {
        return py;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
