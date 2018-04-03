package com.android.djs.jigsaw;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.Random;

/**
 * 自定义view
 * Created by DJS on 2017/3/30.
 */
public class JigsawSurfaceView extends SurfaceView implements View.OnTouchListener, SurfaceHolder.Callback, Runnable {

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private boolean isDrawing = true;

    public int mHSegment = 4;
    private int mWSegment = 3;
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


    public JigsawSurfaceView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.mContext = context;
        init(screenWidth, screenHeight);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //开启线程画图
        new Thread(JigsawSurfaceView.this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

        isDrawing = false;
    }


    /**
     * 初始化
     *
     * @param screenWidth  屏幕宽
     * @param screenHeight 屏幕高
     */
    private void init(int screenWidth, int screenHeight) {

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setOnTouchListener(this);


        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.land);
        Bitmap mBitmap = Bitmap.createScaledBitmap(bitmap, screenWidth, screenHeight, true);

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

        //将图片混乱
        arrDisorder();

    }


    @Override
    public void run() {

        while (isDrawing) {
            Canvas canvas;
            canvas = mSurfaceHolder.lockCanvas();
            if (null != canvas) {
                drawPicture(canvas);
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }

        }
    }

    /**
     * 画图片
     *
     * @param canvas 画布
     */
    private void drawPicture(Canvas canvas) {

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

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {


        x = event.getX();
        y = event.getY();


//        Log.i("jigSur", (int)x+","+(int)y);

//        if (x > bias + (indexX - 1) * mSegW && x < bias + (indexX + 1) * mSegW &&
//                y > bias + (indexY - 1) * mSegH && y < bias + (indexY + 1) * mSegH) {

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
                        && downX < indexX * mSegW
                        && downX > (indexX - 1) * mSegW
                        && downY > indexY * mSegH
                        && downY < (indexY + 1) * mSegH
                        && upY > indexY * mSegH
                        && upY < (indexY + 1) * mSegH) {

                    swapLocation(3);
                }
                //向左
                if (upX - downX < -mSegW / 4
                        && downX > bias + (indexX + 1) * mSegW
                        && downX < (indexX + 2) * mSegW
                        && downY > bias + indexY * mSegH
                        && downY < bias + (indexY + 1) * mSegH
                        && upY > bias + indexY * mSegH
                        && upY < bias + (indexY + 1) * mSegH) {

                    swapLocation(1);
                }


                //向下
                if (upY - downY > mSegW / 4
                        && downY < indexY * mSegH
                        && downY > (indexY - 1) * mSegH
                        && downX > indexX * mSegW
                        && downX < (indexX + 1) * mSegW
                        && upX > indexX * mSegW
                        && upX < (indexX + 1) * mSegW) {

                    swapLocation(2);
                }

                //向上
                if (upY - downY < -mSegW / 4
                        && downY > (indexY + 1) * mSegH
                        && downY < (indexY + 2) * mSegH
                        && downX > indexX * mSegW
                        && downX < (indexX + 1) * mSegW
                        && upX > indexX * mSegW
                        && upX < (indexX + 1) * mSegW) {

                    swapLocation(4);
                }

                //检查是否成功
                checkSuccess();
                break;
//            }
        }

        return true;
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

        Log.i("jigg", "swapLocation: indexX:" + indexX + ",indexY:" + indexY);

        int x, y;

        switch (direction) {
            //向左
            case 3:

                for (int i = 0; i < mHSegment; i++) {
                    for (int j = 0; j < mWSegment; j++) {
                        x = mJBmps[i][j].getLeft() / mSegW;
                        y = mJBmps[i][j].getTop() / mSegH;


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
                        x = mJBmps[i][j].getLeft() / mSegW;
                        y = mJBmps[i][j].getTop() / mSegH;


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
                        x = mJBmps[i][j].getLeft() / mSegW;
                        y = mJBmps[i][j].getTop() / mSegH;

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
                        x = mJBmps[i][j].getLeft() / mSegW;
                        y = mJBmps[i][j].getTop() / mSegH;

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
    }

    private void arrDisorder() {
        int left;
        int top;

        int r, s;
        int ir, jr;
        int is, js;

        Random random = new Random();
        for (int i = 0; i < mHSegment; i++) {
            for (int j = 0; j < mWSegment; j++) {

                r = random.nextInt(1000) % (mHSegment * mWSegment - 1);
                s = random.nextInt(1000) % (mHSegment * mWSegment - 1);

                while (s == r) {
                    s = random.nextInt(1000) % (mHSegment * mWSegment - 1);
                }

                Log.i("jig", "s: " + s + ",r:" + r);

                ir = r / mWSegment;
                jr = r - ir * mWSegment;

                is = s / mWSegment;
                js = s - is * mWSegment;

                Log.i("jig", "ir: " + ir + ",jr:" + jr + ",is:" + is + ",js:" + js);


                left = mJBmps[ir][jr].getLeft();
                top = mJBmps[ir][jr].getTop();

                mJBmps[ir][jr].setLeft(mJBmps[is][js].getLeft());
                mJBmps[ir][jr].setTop(mJBmps[is][js].getTop());

                mJBmps[is][js].setLeft(left);
                mJBmps[is][js].setTop(top);
            }


        }
    }

   private void checkSuccess()
    {
        int index = 0;
        if (indexX == mWSegment - 1 && indexY == mHSegment - 1) {

            for (int i = 0; i < mHSegment ; i++) {
                for (int j = 0; j < mWSegment; j++) {

                    if (i == mHSegment - 1 && j == mWSegment - 1) {
                        break;
                    }

                    if (mJBmps[i][j].getLeft() == mJBmps[i][j].getRightLeft()&&mJBmps[i][j].getTop() == mJBmps[i][j].getRightLeft()) {
                        index++;
                    }
                }
            }
        }

        if (index == mHSegment*mWSegment - 1) {

            AlertDialog.Builder builder=new AlertDialog.Builder(mContext);
            builder.setTitle("提示!!!");
            builder.setMessage("恭喜你拼图成功！");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
           builder.show();
        }
    }
}
