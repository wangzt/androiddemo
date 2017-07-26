package com.tomsky.androiddemo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by j-wangzhitao on 17-7-26.
 */

public class WraperBean implements Parcelable {
    public String name;
    public BaseBean baseBean;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeParcelable(this.baseBean, flags);
    }

    public WraperBean() {
    }

    protected WraperBean(Parcel in) {
        this.name = in.readString();
        this.baseBean = in.readParcelable(BaseBean.class.getClassLoader());
    }

    public static final Creator<WraperBean> CREATOR = new Creator<WraperBean>() {
        @Override
        public WraperBean createFromParcel(Parcel source) {
            return new WraperBean(source);
        }

        @Override
        public WraperBean[] newArray(int size) {
            return new WraperBean[size];
        }
    };

    @Override
    public String toString() {
        return "WraperBean{" +
                "name='" + name + '\'' +
                ", baseBean=" + baseBean +
                '}';
    }
}
