package com.tomsky.androiddemo.bean;

import android.os.Parcel;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class BeanB extends BaseBean {
    public boolean valB;
    public String pB;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        super.writeToParcel(dest, flags);
        dest.writeByte(this.valB ? (byte) 1 : (byte) 0);
        dest.writeString(this.pB);
    }

    public BeanB() {
        type = TYPE_B;
        beanStr = "BStr";
    }

    protected BeanB(Parcel in, int type) {
        super(in);
        this.type = type;
        this.valB = in.readByte() != 0;
        this.pB = in.readString();
    }

    public static final Creator<BeanB> CREATOR = new Creator<BeanB>() {
        @Override
        public BeanB createFromParcel(Parcel source) {
            return new BeanB(source, source.readInt());
        }

        @Override
        public BeanB[] newArray(int size) {
            return new BeanB[size];
        }
    };

    @Override
    public String toString() {
        return "BeanB{" +
                "type=" + type +
                "valB=" + valB +
                ", pB='" + pB + '\'' +
                ", beanStr=" + beanStr +
                '}';
    }
}
