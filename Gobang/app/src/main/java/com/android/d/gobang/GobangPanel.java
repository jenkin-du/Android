package com.android.d.gobang;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义五子棋画布
 * Created by D on 2016/5/5.
 */
public class GobangPanel extends View {

    private int panelWidth;//画板宽度
    private double lineHeight;//行高
    private int MAX_LINE = 15;//行数
    private int MAX_IN_LINE = 5;

    private Paint paint = new Paint();

    private Point startPoint = new Point();
    private Point endPoint = new Point();

    //定义棋子类
    private Bitmap whiteStone;
    private Bitmap blackStone;

    private double ratio = 0.75;//比例，

    //白棋先手
    private boolean isWhite = true;
    private ArrayList<Point> whiteArr = new ArrayList<>();//保存已经下的棋子位置，用于重绘
    private ArrayList<Point> blackArr = new ArrayList<>();

    private boolean isOver = false;
    private boolean whiteWin = false;

    //保存临时的棋子
    private Point tempPoint = new Point();


    public GobangPanel(Context context, AttributeSet attrs) {
        super(context, attrs);

        setBackgroundResource(R.color.color);//半透明的背景

        init();
    }


    private void init() {
        paint.setColor(0x88000000);//设置颜色
        paint.setDither(true);
        paint.setAntiAlias(true);//抗锯齿
        paint.setStyle(Paint.Style.STROKE);

        //初始化棋子
        whiteStone = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        blackStone = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }


    /**
     * 自定义测量
     *
     * @param widthMeasureSpec  宽
     * @param heightMeasureSpec 高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widht = Math.min(heightSize, widthSize);   //取得宽高的最小值

        if (heightMode == MeasureSpec.UNSPECIFIED) {
            widht = heightSize;
        } else if (widthMode == MeasureSpec.UNSPECIFIED) {
            widht = widthSize;
        }

        setMeasuredDimension(widht, widht);//设置尺寸为最小正方形
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //获得控件的宽度和行高
        panelWidth = w;
        lineHeight = panelWidth * 1.0 / MAX_LINE;

        //设置棋子的大小为网格的3/4
        int stoneWidth = (int) (lineHeight * ratio);
        whiteStone = Bitmap.createScaledBitmap(whiteStone, stoneWidth, stoneWidth, false);
        blackStone = Bitmap.createScaledBitmap(blackStone, stoneWidth, stoneWidth, false);

    }


    /**
     * 自定义绘图
     *
     * @param canvas 画布
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画网格
        drawBroad(canvas);
        //画棋子
        drawPiece(canvas);
        //判断游戏是否结束
        checkGameOver(canvas);
    }

    /**
     * 判断游戏是否结束
     */
    private void checkGameOver(Canvas canvas) {
        //判断白棋或黑棋是否五子连珠
        boolean isWhiteWin = checkFiveLine(whiteArr);

        if (isWhiteWin) {
            isOver = true;

            whiteWin = isWhiteWin;
            String text = "白棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();

            drawWinLine(canvas);
            return;

        }

        boolean isBlackWin = checkFiveLine(blackArr);
        if (isBlackWin) {
            isOver = true;

            whiteWin = isBlackWin;
            String text = "黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
            drawWinLine(canvas);
        }

    }


    /**
     * 将连上五子的棋画上红线
     *
     * @param canvas 画布
     */
    private void drawWinLine(Canvas canvas) {

        paint.setColor(Color.RED);

        int startX = (int) ((startPoint.x + 0.5) * lineHeight);
        int startY = (int) ((startPoint.y + 0.5) * lineHeight);

        int endX = (int) ((endPoint.x + 0.5) * lineHeight);
        int endY = (int) ((endPoint.y + 0.5) * lineHeight);

        canvas.drawLine(startX, startY, endX, endY, paint);
    }


    /**
     * 判断是否五子连珠
     *
     * @param points 已下的棋子集合
     */
    private boolean checkFiveLine(List<Point> points) {

        boolean win;
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            //判断水平方向是否连五子
            win = chechHorizontal(x, y, points);
            if (win) return true;
            win = chechVertical(x, y, points);
            if (win) return true;
            win = chechSlash(x, y, points);
            if (win) return true;
            win = chechBackSlash(x, y, points);
            if (win) return true;
        }
        return false;
    }


    /**
     * 判断左右是否五子连珠
     */
    private boolean chechHorizontal(int x, int y, List<Point> points) {

        startPoint.x = x;
        startPoint.y = y;

        endPoint.x = x;
        endPoint.y = y;
        int count = 1;
        //往左检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
                endPoint.x = x - i;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }


        //往右检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
                startPoint.x = x + i;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }

        return false;

    }


    /**
     * 判断上下是否五子连珠
     */
    private boolean chechVertical(int x, int y, List<Point> points) {

        startPoint.x = x;
        startPoint.y = y;

        endPoint.x = x;
        endPoint.y = y;
        int count = 1;
        //往上检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x, y - i))) {
                endPoint.y = y - i;
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }


        //往下检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                startPoint.y = y + i;
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }

        return false;

    }


    /**
     * 判断正斜线是否五子连珠
     */
    private boolean chechSlash(int x, int y, List<Point> points) {

        startPoint.x = x;
        startPoint.y = y;

        endPoint.x = x;
        endPoint.y = y;
        int count = 1;
        //往上检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y - i))) {
                endPoint.x = x + i;
                endPoint.y = y - i;
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }


        //往下检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
                startPoint.x = x - i;
                startPoint.y = y + i;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }

        return false;

    }


    /**
     * 判断反斜线是否五子连珠
     */
    private boolean chechBackSlash(int x, int y, List<Point> points) {

        startPoint.x = x;
        startPoint.y = y;

        endPoint.x = x;
        endPoint.y = y;
        int count = 1;
        //往上检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                endPoint.x = x + i;
                endPoint.y = y + i;
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }


        //往下检测连接数
        for (int i = 1; i < MAX_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y - i))) {
                startPoint.x = x - i;
                startPoint.y = y - i;
                count++;
            } else {
                break;
            }
        }

        if (count == MAX_IN_LINE) {
            return true;
        }

        return false;

    }


    /**
     * 画棋子
     *
     * @param canvas 画布
     */
    private void drawPiece(Canvas canvas) {

        //画白棋
        for (int i = 0, n = whiteArr.size(); i < n; i++) {
            Point point = whiteArr.get(i);

            int x = (int) ((point.x + ((1 - ratio) / 2)) * lineHeight);
            int y = (int) ((point.y + ((1 - ratio) / 2)) * lineHeight);

            canvas.drawBitmap(whiteStone, x, y, null);
        }

        //画黑棋
        for (int i = 0, n = blackArr.size(); i < n; i++) {
            Point point = blackArr.get(i);

            int x = (int) ((point.x + ((1 - ratio) / 2)) * lineHeight);
            int y = (int) ((point.y + ((1 - ratio) / 2)) * lineHeight);

            canvas.drawBitmap(blackStone, x, y, null);
        }

    }


    /**
     * 画网格
     *
     * @param canvas 画布
     */
    private void drawBroad(Canvas canvas) {

        //画横线
        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int) (panelWidth - lineHeight / 2);
            int y = (int) ((i + 0.5) * lineHeight);

            canvas.drawLine(startX, y, endX, y, paint);

        }
        //画竖线
        for (int i = 0; i < MAX_LINE; i++) {
            int startY = (int) (lineHeight / 2);
            int endY = (int) (panelWidth - lineHeight / 2);
            int x = (int) ((i + 0.5) * lineHeight);

            canvas.drawLine(x, startY, x, endY, paint);
        }
    }


    /**
     * 重新处理点击事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (isOver) {

            return false;
        }


        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();


            Point p = getllValidPoint(x, y);

            //将每个棋子都保存在临时坐标里，便于悔棋
            tempPoint = getllValidPoint(x, y);

            if (whiteArr.contains(p) || blackArr.contains(p)) {
                return false;
            }


            if (isWhite) {
                whiteArr.add(p);
            } else {
                blackArr.add(p);
            }

            isWhite = !isWhite;

            invalidate();//重绘
            return true;
        }

        return true;
    }

    private Point getllValidPoint(int x, int y) {

        return new Point((int) (x / lineHeight), (int) (y / lineHeight));
    }


    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instanceGameOver";
    private static final String INSTANCE_WHITE_ARR = "instanceWhiteArr";
    private static final String INSTANCE_blace_ARR = "instanceblaceArr";
    private static final String START_POINT = "startPoint";
    private static final String END_POINT = "endPoint";
    private static final String IS_WHITE = "isWhite";
    private static final String TEMP_POINT = "tempPoint";

    /**
     * 状态的保存于恢复,在屏幕旋转时保存，自定义view在xml布局中必须声明ID，否则无效
     */
    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();

        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARR, whiteArr);
        bundle.putParcelableArrayList(INSTANCE_blace_ARR, blackArr);
        bundle.putParcelable(START_POINT, startPoint);
        bundle.putParcelable(END_POINT, endPoint);
        bundle.putBoolean(IS_WHITE, isWhite);
        bundle.putParcelable(TEMP_POINT, tempPoint);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {


        if (state instanceof Bundle) {

            Bundle bundle = (Bundle) state;
            isOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            whiteArr = bundle.getParcelableArrayList(INSTANCE_WHITE_ARR);
            blackArr = bundle.getParcelableArrayList(INSTANCE_blace_ARR);
            startPoint = bundle.getParcelable(START_POINT);
            endPoint = bundle.getParcelable(END_POINT);
            isWhite = bundle.getBoolean(IS_WHITE);
            tempPoint = bundle.getParcelable(TEMP_POINT);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));

            return;
        }
        super.onRestoreInstanceState(state);
    }


    //再来一局
    public void reStart() {

        whiteArr.clear();
        blackArr.clear();

        isOver = false;
        isWhite = false;

        invalidate();//重绘view
    }

    /**
     * 悔棋功能
     */
    public void undo() {

        if (!isOver) {
            if (isWhite) {
                blackArr.remove(tempPoint);
                isWhite = !isWhite;
                invalidate();

            } else {
                whiteArr.remove(tempPoint);
                isWhite = !isWhite;
                invalidate();
            }
        }

    }
}
