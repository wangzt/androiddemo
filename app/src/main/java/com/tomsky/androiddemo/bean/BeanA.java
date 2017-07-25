package com.tomsky.androiddemo.bean;

import android.os.Parcel;

/**
 * Created by j-wangzhitao on 17-7-25.
 */

public class BeanA extends BaseBean {
    public String valA;
    public int priceA;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(type);
        super.writeToParcel(dest, flags);
        dest.writeString(this.valA);
        dest.writeInt(this.priceA);
    }

    public BeanA() {
        type = TYPE_A;
        beanStr = "A_Str";
    }

    protected BeanA(Parcel in) {
        super(in);
        this.type = TYPE_A;
        this.valA = in.readString();
        this.priceA = in.readInt();
    }

//    public static final Creator<BeanA> CREATOR = new Creator<BeanA>() {
//        @Override
//        public BeanA createFromParcel(Parcel source) {
//            return new BeanA(source);
//        }
//
//        @Override
//        public BeanA[] newArray(int size) {
//            return new BeanA[size];
//        }
//    };

    @Override
    public String toString() {
        return "BeanA{" +
                "valA='" + valA + '\'' +
                ", priceA=" + priceA +
                ", beanStr=" + beanStr +
                '}';
    }
}
