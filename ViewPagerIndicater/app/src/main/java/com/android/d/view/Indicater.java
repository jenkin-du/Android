package com.android.d.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.d.viewpagerindicater.R;

/**
 * 自定义布局指示器
 * Created by D on 2016/5/8.
 */
public class Indicater extends LinearLayout {


    //画三角形相关的成员变量
    private Paint paint;
    private Path path;
    private int triangleWidth;
    private int triangleHeight;
    private static final float RATIO_TRIANGLE_WIDTH = 1 / 6F;

    //与三角形位移有关的成员变量
    private int initedX;
    private int translatedX = 0;

    //与自定义属性有关
    private int visivleTab;
    private static final int DEFAULT_VISIBLE_COUNT = 4;
    private static final int COLOR_HIGHTLINGHT = 0XFFFFFFFF;
    private static final int COLOR_NOMAL = 0xcecacaff;

    //与监听器有关,自定义接口，用于外部的调用
    private ViewPager viewPager;

    public interface PagerOnChangeListener {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onPageSelected(int position);

        public void onPageScrollStateChanged(int state);

    }

    private PagerOnChangeListener pagerOnChangeListener;

    public void setPagerOnChangeListener(PagerOnChangeListener pagerOnChangeListener) {
        this.pagerOnChangeListener = pagerOnChangeListener;
    }

    public Indicater(Context context) {
        this(context, null);
    }

    public Indicater(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取自定义属性
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.Indicater);
        visivleTab = arr.getInteger(R.styleable.Indicater_tabCount, DEFAULT_VISIBLE_COUNT);

        if (visivleTab < 0) {
            visivleTab = DEFAULT_VISIBLE_COUNT;
        }
        arr.recycle();

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setPathEffect(new CornerPathEffect(3));//设置圆角效果
    }


    /**
     * 在此方法中获得view的宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        triangleWidth = (int) (w / visivleTab * RATIO_TRIANGLE_WIDTH);
        initedX = w / visivleTab / 2 - triangleWidth / 2;

        //初始化三角形
        initTriangle();
    }


    /**
     * 初始化三角形
     */
    private void initTriangle() {

        triangleHeight = triangleWidth / 2;

        path = new Path();
        path.moveTo(0, 0);
        //画三角形形状
        path.lineTo(triangleWidth, 0);
        path.lineTo(triangleWidth / 2, -triangleHeight);
        path.close();
    }


    /**
     * 绘制三角形
     *
     * @param canvas 画布
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(initedX + translatedX, getHeight() + 2);
        canvas.drawPath(path, paint);

        canvas.restore();

        super.dispatchDraw(canvas);
    }


    /**
     * 指示器跟随手指移动
     */
    public void scroll(int position, float Offset) {

        int tabWidth = getWidth() / visivleTab;//getWidth()获得控件的宽度
        translatedX = (int) (tabWidth * (position + Offset));

        //当tab移动至最后一个时，移动容器
        if ((position >= (visivleTab - 2)) && Offset > 0 &&
                getChildCount() > visivleTab && position < getChildCount() - 2) {

            if (visivleTab != 1) {
                int offsetX = (position - (visivleTab - 2)) * tabWidth + (int) (tabWidth * Offset);
                this.scrollTo(offsetX, 0);//将容器移动到相应坐标
            } else {
                int offsetX = (int) (position * tabWidth + tabWidth * Offset);
                this.scrollTo(offsetX, 0);
            }
        }


        invalidate();//重绘

    }


    /**
     * 当view的xml布局加载完之后，调用此方法
     */
    @Override
    protected void onFinishInflate() {

        int count = getChildCount();//得到子元素的个数
        if (count == 0)
            return;

        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);//得到对应的子元素
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;

            lp.width = getSreenWidth() / visivleTab;

            view.setLayoutParams(lp);
        }

        itemOnClick();
        super.onFinishInflate();
    }


    /**
     * //获得屏幕宽度
     */
    private int getSreenWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     * 添加监听器
     */
    public void setOnPagerLinstener(ViewPager viewPager, final int pos) {

        this.viewPager = viewPager;

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                scroll(position, positionOffset);

                if (pagerOnChangeListener != null) {
                    pagerOnChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

            }

            @Override
            public void onPageSelected(int position) {

                if (pagerOnChangeListener != null) {
                    pagerOnChangeListener.onPageSelected(position);
                }

                //设置当前选项卡颜色高亮
                hightlightTextColor(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (pagerOnChangeListener != null) {
                    pagerOnChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        //设置当前的选项卡位置
        viewPager.setCurrentItem(pos);
        hightlightTextColor(pos);
    }


    /**
     * 重置文本颜色
     */
    private void resetTextColor() {

        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            if (view instanceof TextView) {
                ((TextView) view).setTextColor(COLOR_NOMAL);
            }
        }

    }


    /**
     * 设置颜色高亮
     *
     * @param pos 位置
     */
    private void hightlightTextColor(int pos) {

        resetTextColor();

        View view = getChildAt(pos);
        if (view instanceof TextView) {
            ((TextView) view).setTextColor(COLOR_HIGHTLINGHT);
        }
    }


    private void itemOnClick() {

        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View view = getChildAt(i);
            final int j = i;

            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(j);
                }
            });
        }
    }
}
