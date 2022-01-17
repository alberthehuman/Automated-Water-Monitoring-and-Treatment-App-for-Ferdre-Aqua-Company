package com.example.waterapp4;

public class PointValue {
    long xvalue;
    double yvalue;

    public PointValue() {

    }
    public PointValue(long xvalue, int yvalue) {
        this.xvalue = xvalue;
        this.yvalue = yvalue;
    }

    public long getXvalue() {
        return xvalue;
    }

    public double getYvalue() {
        return yvalue;
    }
}
