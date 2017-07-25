package com.tomsky.androiddemo.bean;

import android.os.Parcel;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class BeanC extends BaseBean {
    public String valC;
    public boolean pC;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(TYPE_C);
        super.writeToParcel(dest, flags);
        dest.writeString(this.valC);
        dest.writeByte(this.pC ? (byte) 1 : (byte) 0);
    }

    public BeanC() {
        type = TYPE_C;
        beanStr = "beanStrC";
    }

    protected BeanC(Parcel in) {
        super(in);
        this.valC = in.readString();
        this.pC = in.readByte() != 0;
    }

//    public static final Creator<BeanC> CREATOR = new Creator<BeanC>() {
//        @Override
//        public BeanC createFromParcel(Parcel source) {
//            return new BeanC(source);
//        }
//
//        @Override
//        public BeanC[] newArray(int size) {
//            return new BeanC[size];
//        }
//    };

    @Override
    public String toString() {
        return "BeanC{" +
                "valC='" + valC + '\'' +
                ", pC=" + pC +
                ", beanStr=" + beanStr +
                '}';
    }
}
