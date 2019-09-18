package com.chezi008.libcontacts.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * - @Description:联系人
 * - @Author:  chezi008/chezi008@qq.com
 * - @Time:  2019/6/13 9:51
 */
public class ContactBean extends IndexBean implements Parcelable {
    private String id;
    private String title;
    private int type;
    private String account; // 电话
    private String file_url;
    private String type_title;
    private String xq_title;

    /**
     * 设置本地图片请设置为
     *"intres"+R.mipmap.ic_group_avatar
     */
    private String avatar;
    /**
     * 是否选择，checkbox的状态由该字段控制
     */
    private boolean isChoose;
    /**
     * checkBox的enable状态
     */
    private boolean checkEnable = true;
    /**
     * 未读消息数量
     */
    private int num;


    public ContactBean(){

    }

    public ContactBean(String name){

    }


    protected ContactBean(Parcel in) {
        id = in.readString();
        title = in.readString();
        type = in.readInt();
        avatar = in.readString();
        isChoose = in.readByte() != 0;
        checkEnable = in.readByte() != 0;
        num = in.readInt();
        index =  in.readInt();
        account= in.readString();
        file_url= in.readString();
        type_title= in.readString();
        xq_title= in.readString();
        py= in.readString();
    }

    public static final Creator<ContactBean> CREATOR = new Creator<ContactBean>() {
        @Override
        public ContactBean createFromParcel(Parcel in) {
            return new ContactBean(in);
        }

        @Override
        public ContactBean[] newArray(int size) {
            return new ContactBean[size];
        }
    };

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        //设置对应的英文字母
        String upperCase = Pinyin.toPinyin(title.charAt(0)).toUpperCase();
        String value = String.valueOf(upperCase.charAt(0));
        if (!value.matches("[A-Z]")) {
            //如果不是A-Z字母开头
            value = "#";
        }
        this.setPy(value);
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }

    public boolean isCheckEnable() {
        return checkEnable;
    }

    public void setCheckEnable(boolean checkEnable) {
        this.checkEnable = checkEnable;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getType_title() {
        return type_title;
    }

    public void setType_title(String type_title) {
        this.type_title = type_title;
    }

    public String getXq_title() {
        return xq_title;
    }

    public void setXq_title(String xq_title) {
        this.xq_title = xq_title;
    }

    public String getPy() {
        return py;
    }

    public void setPy(String py) {
        this.py = py;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContactBean){
            ContactBean cobj = (ContactBean) obj;
            return id.equals(cobj.id);
        }
        return super.equals(obj);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeInt(type);
        dest.writeString(avatar);
        dest.writeByte((byte) (isChoose ? 1 : 0));
        dest.writeByte((byte) (checkEnable ? 1 : 0));
        dest.writeInt(num);
        dest.writeInt(index);
        dest.writeString(account);
        dest.writeString(file_url);
        dest.writeString(type_title);
        dest.writeString(xq_title);
        dest.writeString(py);
    }
}
