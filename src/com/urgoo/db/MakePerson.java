package com.urgoo.db;

/**
 * Created by urgoo_01 on 2016/6/22.
 */
public class MakePerson {
    public int type;
    public int item;
    public int state;
    public int num;

    public MakePerson() {
    }

    public MakePerson(int mType, int mItem, int mState, int mMun) {
        type = mType;
        item = mItem;
        state = mState;
        num = mMun;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int mNum) {
        num = mNum;
    }

    public int getState() {
        return state;
    }

    public void setState(int mState) {
        state = mState;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int mItem) {
        item = mItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int mType) {
        type = mType;
    }

    @Override
    public String toString() {
        return "make{" +
                "t=" + type +
                ", i=" + item +
                ", s=" + state +
                '}';
    }
}
