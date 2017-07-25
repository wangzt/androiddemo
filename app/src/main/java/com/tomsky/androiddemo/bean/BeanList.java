package com.tomsky.androiddemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class BeanList implements Parcelable {
    public List<BaseBean> beans;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.beans);
    }

    public BeanList() {
    }

    protected BeanList(Parcel in) {
        this.beans = in.createTypedArrayList(BaseBean.CREATOR);
    }

    public static final Creator<BeanList> CREATOR = new Creator<BeanList>() {
        @Override
        public BeanList createFromParcel(Parcel source) {
            return new BeanList(source);
        }

        @Override
        public BeanList[] newArray(int size) {
            return new BeanList[size];
        }
    };
}
