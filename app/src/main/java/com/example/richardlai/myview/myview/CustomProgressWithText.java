package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.richardlai.myview.R;

/**
 * Created by Richard.Lai on 2016/1/15.
 */
public class CustomProgressWithText extends View {

    private int mFirstColor;

    private int mSecondColor;

    private int mTextColor;

    private int mSpeed;

    private int mCircleWidth;

    private int mProgress;

    private String progressText = "40%";

    private int textSize;

    private Paint mPaint;

    private Paint textPaint;

    private Rect mBound;

    private boolean doLoops = true;

    private boolean textIsVisible = false;

    private boolean isNext = false;

    public CustomProgressWithText(Context context) {
        this(context, null);
    }

    public CustomProgressWithText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressWithText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 所有的属性值都在attrs 中。
        // 用typeArray的好处就在与可以通过数组的方式集体找到所需要的属性，
        // 在R.java文件中的CustomProgress数组中存的是所包含属性的id
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
                    textSize = ta.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomProgress_doLoops:
                    doLoops = ta.getBoolean(attr, true);
                    break;
                case R.styleable.CustomProgress_textIsVisible:
                    textIsVisible = ta.getBoolean(attr, false);
                    break;
                case R.styleable.CustomProgress_android_textColor:
                    mTextColor = ta.getColor(attr, Color.RED);
                    break;
                default: break;
            }
        }
        ta.recycle();
        if(mPaint == null) {
            mPaint = new Paint();
        }
        if(textPaint == null) {
            textPaint = new Paint();
        }

        if(progressText != null && progressText.length() < 1) {
            mPaint.setTextSize(textSize);
            mBound = new Rect();
            mPaint.getTextBounds(progressText, 0, progressText.length() / 2, mBound);
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
                        if(!doLoops) {
                            progressText = "100%";
                            postInvalidate();
                            break;
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthModel = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
      /*  int heightModel = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(widthMeasureSpec);*/

        int width;
      //  int height;
        // EXACTLY  一般指的是写了特定的值（100dp）或者是match_parent
        // AT_MOST 表示子布局限制在一个最大值内，一般为WARP_CONTENT
        // UNSPECIFIED：表示子布局想要多大就多大，很少使用
        if(mBound == null) {
            mBound = new Rect();
        }
        if(widthModel == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(progressText, 0, progressText.length(), mBound);

            float textWidth = mBound.width()  + mCircleWidth;
            int desired = (int) (getPaddingLeft() + textWidth * 2 + getPaddingRight());
            width = desired;
        }
       /* if(heightModel == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            mPaint.setTextSize(textSize);
            mPaint.getTextBounds(progressText, 0, progressText.length(), mBound);
            float textHeight = mBound.height() + mCircleWidth;
            int desired = (int) (getPaddingTop() + textHeight * 2 + getPaddingBottom());
            height = desired;
        }*/
        //正方形
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int center = getWidth() / 2;
        int radius = center - mCircleWidth / 2;

        if(mPaint == null) {
            mPaint = new Paint();
        }
        if(textPaint == null) {
            textPaint = new Paint();
        }

        if(mBound == null ) {
            mBound = new Rect();
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

        if(textIsVisible) {
            textPaint.setTextSize(textSize);
            textPaint.getTextBounds(progressText, 0, progressText.length(), mBound);
            textPaint.setColor(mTextColor);
            canvas.drawText(progressText, center - mBound.width() / 2, center + (mBound.height() / 2), textPaint);
        }

    }
}
