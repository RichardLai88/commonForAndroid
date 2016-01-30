package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.richardlai.myview.R;

/**
 * Created by Richard.Lai on 2016/1/15.
 */
public class CustomTextView extends View {
    private String mTitleText = "default";

    private int mTitleTextColor =  Color.RED;

    private int mTitleTextSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16,getResources().getDisplayMetrics());

    private Rect mBound;
    private Paint mPaint;

    public CustomTextView(Context mContext, AttributeSet attrs) {
        this(mContext, attrs, 0);


    }

    public CustomTextView(Context mContext) {
        this(mContext, null);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.customTitleView, defStyle, 0);

        int n = a.getIndexCount();

        for(int i= 0; i < n; i++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.customTitleView_titleText: mTitleText = a.getString(attr);
                    break;
                case R.styleable.customTitleView_titleTextColor: mTitleTextColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.customTitleView_titleTextSize : mTitleTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }

        }
        a.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);

        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height ;
        if (widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSize;
        } else
        {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textWidth = mBound.width();
            int desired = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            width = desired;
        }

        if (heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSize;
        } else
        {
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            float textHeight = mBound.height();
            int desired = (int) (getPaddingTop() + textHeight + getPaddingBottom());
            height = desired;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
     //   mPaint.setColor(Color.YELLOW);
     //   canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
        mPaint.setColor(Color.RED);

        int centerX = (getWidth() / 2 - mBound.width() / 2) / 2;
        int centerY =  getHeight() / 2;

        canvas.drawCircle(centerX,centerY,mTitleTextSize / 2,mPaint);

        mPaint.setColor(mTitleTextColor);
        canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
    }

    public void setTitleText(String value) {
        mTitleText = value;
        invalidate();
    }

    public void setTextSize(int size) {
        mTitleTextSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,getResources().getDisplayMetrics());
    }
}
