package com.tomsky.androiddemo.api;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j-wangzhitao on 17-9-27.
 */

public class BaseBean implements Parcelable {
    public String uname;
    public String uid;
    public int age;

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uname);
        dest.writeString(this.uid);
        dest.writeInt(this.age);
    }

    public BaseBean() {
    }

    protected BaseBean(Parcel in) {
        this.uname = in.readString();
        this.uid = in.readString();
        this.age = in.readInt();
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel source) {
            return new BaseBean(source);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };
}
