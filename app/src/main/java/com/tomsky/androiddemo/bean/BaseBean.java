package com.tomsky.androiddemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public abstract class BaseBean implements Parcelable {
    public static final int TYPE_A = 0;
    public static final int TYPE_B = 1;
    public static final int TYPE_C = 2;

    public int type;
    public String beanStr;

    @Override
    public int describeContents() {
        return 0;
    }

    public BaseBean() {
    }

    protected BaseBean(Parcel in) {
        this.beanStr = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(beanStr);
    }

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel source) {
            return getConcreteClass(source);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };

    public static BaseBean getConcreteClass(Parcel in) {
        int type = in.readInt();
        switch (type) {
            case TYPE_A:
                return new BeanA(in);
            case TYPE_B:
                return new BeanB(in);
            case TYPE_C:
                return new BeanC(in);
        }
        return null;
    }
}
