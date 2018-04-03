package com.android.djs.jigsaw;

import android.graphics.Bitmap;


public class JBitmap {

    private int rightLeft;
    private int rightTop;

    private int left;
    private int top;

    private Bitmap bitmap;

    public int getRightLeft() {
        return rightLeft;
    }

    public void setRightLeft(int rightLeft) {
        this.rightLeft = rightLeft;
    }

    public int getRightTop() {
        return rightTop;
    }

    public void setRightTop(int rightTop) {
        this.rightTop = rightTop;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public JBitmap(int rightLeft, int rightTop, int left, int top, Bitmap bitmap) {
        this.rightLeft = rightLeft;
        this.rightTop = rightTop;
        this.left = left;
        this.top = top;
        this.bitmap = bitmap;
    }

    @Override
    public String toString() {
        return "JBitmap{" +
                "left=" + left +
                ", top=" + top +
                '}';
    }
}
