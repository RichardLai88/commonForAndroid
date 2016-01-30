package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.richardlai.myview.R;

/**
 * Created by Richard.Lai on 2016/1/15.
 */
public class CustomProgress extends View {

    private int mFirstColor;

    private int mSecondColor;

    private int mSpeed;

    private int mCircleWidth;

    private int mProgress;

    private String progressText;

    private int textSize;

    private Paint mPaint;

    private boolean isNext = false;

    public CustomProgress(Context context) {
        this(context, null);
    }

    public CustomProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // defStyleAttr, defStyleRes；defStyleAttr是style中声明的属性集合，defStyleRes表示一个style的id
        // defStyleAttr的优先级更高，如果defStyleAttr为0或者在style中没有找到theme属性，则才会去找defStyleRes
        // attrs 则表示这个集合中的元素的数组
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomProgress, defStyleAttr, 0);

        int a = ta.getIndexCount();

        for(int i = 0; i < a; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.CustomProgress_firstColor :
                    mFirstColor = ta.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CustomProgress_secondColor :
                    mSecondColor = ta.getColor(attr, Color.RED);
                    break;
                case R.styleable.CustomProgress_circleWidth :
                    mCircleWidth = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgress_speed :
                    mSpeed = ta.getInt(attr, 20);
                    break;
                case R.styleable.CustomProgress_android_textSize :
                    textSize = ta.getIndex(attr);
                    break;
            }
        }
        ta.recycle();
        if(mPaint == null) {
            mPaint = new Paint();
        }

        new Thread() {
            public void run() {
                while (true) {
                    mProgress ++;
                    if(mProgress == 360) {
                        mProgress = 0;
                        if(!isNext) {
                            isNext = true;
                        } else {
                            isNext = false;
                        }
                    }
                    progressText = (int)(((double)mProgress / 360.00) * 100) + "%";
                    // 这个函数是用来刷新view的，
                    // 同样刷新view的还有invalidate()这个方法，但是这哥方法必须在ui线程中执行，所以如果想用这个方法，则需要使用handle来处理
                    postInvalidate();
                    try{
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;

        if(mPaint == null) {
            mPaint = new Paint();
        }
        mPaint.setStrokeWidth(mCircleWidth);//
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);

        if(!isNext) {
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        }
    }
}
