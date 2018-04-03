package com.android.djs.jigsaw;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 自定义画图类
 * Created by DJS on 2017/3/28.
 */
public class JigsawView extends View implements View.OnClickListener, View.OnTouchListener {


    private Context mContext;
    private Bitmap mBitmap;

    public int mHSegment = 4;
    private int mWSegment = 4;
    private int mSegH;
    private int mSegW;
    private JBitmap[][] mJBmps;
    private Paint mPaint;

    private int mImageWidth;
    private int mImageHeight;

    float x;
    float y;

    private float downX;
    private float downY;

    private float upX;
    private float upY;

    private int indexX;
    private int indexY;

    private int bias = MainActivity.BIAS;


    public JigsawView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.mContext = context;
        setOnClickListener(this);
        setOnTouchListener(this);
        setWillNotDraw(false);// 防止onDraw方法不执行


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.land);
        mBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);

        mImageWidth = mBitmap.getWidth();
        mImageHeight = mBitmap.getHeight();

        //根据屏幕宽度和豪赌确定分块大小，及分块数
        mSegH = mImageHeight / mHSegment;
        mWSegment = mImageWidth / mSegH;
        mSegW = mImageWidth / mWSegment;
        mImageWidth = mWSegment * mSegW;

        indexX = mWSegment - 1;
        indexY = mHSegment - 1;

        mJBmps = new JBitmap[mHSegment][mWSegment];
        JBitmap jbitmap;
        Bitmap bit;


        //将位图分块乘位图数组
        for (int i = 0; i < mHSegment; i++) {
            for (int j = 0; j < mWSegment; j++) {
                bit = Bitmap.createBitmap(mBitmap, j * mSegW, i * mSegH, mSegW, mSegH);
                jbitmap = new JBitmap(j * mSegW, i * mSegH, j * mSegW, i * mSegH, bit);
                mJBmps[i][j] = jbitmap;
            }
        }
        mPaint = new Paint();


    }

    @Override
    protected void onDraw(Canvas canvas) {

        mPaint.setColor(Color.rgb(0, 255, 255));
        canvas.drawRect(0, 0, mImageWidth, mImageHeight, mPaint);

        for (int i = 0; i < mHSegment; i++) {
            for (int j = 0; j < mWSegment; j++) {
                if (i == mHSegment - 1 && j == mWSegment - 1) {
                    break;
                }
                canvas.drawBitmap(mJBmps[i][j].getBitmap(), mJBmps[i][j].getLeft(), mJBmps[i][j].getTop(), null);
            }
        }

        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(2);
        //画网格
        for (int i = 0; i <= mWSegment; i++) {
            canvas.drawLine(i * mSegW, 0, i * mSegW, mImageHeight, mPaint);
        }
        for (int i = 0; i <= mHSegment; i++) {
            canvas.drawLine(0, i * mSegH, mImageWidth, i * mSegH, mPaint);
        }

        postInvalidateDelayed(300);//300毫秒刷新
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        x = event.getX();
        y = event.getY();

        if (x > bias + (indexX - 1) * mSegW && x < bias + (indexX + 1) * mSegW &&
                y > bias + (indexY - 1) * mSegH && y < bias + (indexY + 1) * mSegH) {

            switch (event.getAction()) {
                //按下
                case MotionEvent.ACTION_DOWN:

                    downX = x;
                    downY = y;
                    break;
                case MotionEvent.ACTION_UP:

                    upX = x;
                    upY = y;

                    //向右
                    if (upX - downX > mSegW / 4
                            && downX < bias + indexX * mSegW
                            && downY > bias + indexY * mSegH
                            && downY < bias + (indexY + 1) * mSegH
                            && upY > bias + indexY * mSegH
                            && upY < bias + (indexY + 1) * mSegH) {

                        swapLocation(3);
                    }
                    //向左
                    if (upX - downX < -mSegW / 4
                            && downX > bias + (indexX + 1) * mSegW
                            && downY > bias + indexY * mSegH
                            && downY < bias + (indexY + 1) * mSegH
                            && upY > bias + indexY * mSegH
                            && upY < bias + (indexY + 1) * mSegH) {

                        swapLocation(1);
                    }

                    //向下
                    if (upY - downY > mSegW / 4
                            && downY < bias + indexY * mSegH
                            && downX > bias + indexX * mSegW
                            && downX < bias + (indexX + 1) * mSegW
                            && upX > bias + indexX * mSegW
                            && upX < bias + (indexX + 1) * mSegW) {

                        Toast.makeText(mContext,"向下",Toast.LENGTH_SHORT).show();
                        swapLocation(2);
                    }

                    //向上
                    if (upY - downY < -mSegW / 4
                            && downY > bias + (indexY + 1) * mSegH
                            && downX > bias + indexX * mSegW
                            && downX < bias + (indexX + 1) * mSegW
                            && upX > bias + indexX * mSegW
                            && upX < bias + (indexX + 1) * mSegW) {

                        swapLocation(4);
                    }
                    break;
            }
        }


        return false;
    }

    /**
     * 根据方向改变
     *
     * @param direction 方向
     */
    private void swapLocation(int direction) {

        int top = indexY * mSegH;
        int left = indexX * mSegW;

        boolean isSwap = false;


        int x, y;

        switch (direction) {
            //向左
            case 3:

                for (int i = 0; i < mHSegment; i++) {
                    for (int j = 0; j < mWSegment; j++) {
                        x = mJBmps[i][j].getLeft() / mWSegment;
                        y = mJBmps[i][j].getTop() / mHSegment;


                        if (x == indexX - 1 && y == indexY) {


                            mJBmps[i][j].setLeft(left);
                            mJBmps[i][j].setTop(top);

                            isSwap = true;

                            break;
                        }
                    }
                }

                if (isSwap) {
                    indexX--;
                }
                break;
            //向右
            case 1:

                for (int i = 0; i < mHSegment; i++) {
                    for (int j = 0; j < mWSegment; j++) {
                        x = mJBmps[i][j].getLeft() / mWSegment;
                        y = mJBmps[i][j].getTop() / mHSegment;


                        if (x == indexX + 1 && y == indexY) {


                            mJBmps[i][j].setLeft(left);
                            mJBmps[i][j].setTop(top);

                            isSwap = true;

                            break;
                        }
                    }
                }

                if (isSwap) {
                    indexX++;
                }
                break;
            //向上
            case 2:

                for (int i = 0; i < mHSegment; i++) {
                    for (int j = 0; j < mWSegment; j++) {
                        x = mJBmps[i][j].getLeft() / mWSegment;
                        y = mJBmps[i][j].getTop() / mHSegment;


                        if (y == indexY - 1 && x == indexX) {


                            mJBmps[i][j].setLeft(left);
                            mJBmps[i][j].setTop(top);

                            isSwap = true;

                            break;
                        }
                    }
                }

                if (isSwap) {
                    indexY--;
                }
                break;
            //向下
            case 4:

                for (int i = 0; i < mHSegment; i++) {
                    for (int j = 0; j < mWSegment; j++) {
                        x = mJBmps[i][j].getLeft() / mWSegment;
                        y = mJBmps[i][j].getTop() / mHSegment;


                        if (y == indexY + 1 && x == indexX) {


                            mJBmps[i][j].setLeft(left);
                            mJBmps[i][j].setTop(top);

                            isSwap = true;

                            break;
                        }
                    }
                }

                if (isSwap) {
                    indexY++;
                }
                break;
            default:
                break;
        }

        //重绘
        invalidate();
    }
}
