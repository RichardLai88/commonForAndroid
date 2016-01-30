package com.example.richardlai.myview.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Richard.Lai on 2016/1/22.
 */
public class GestureLockView extends View {

    private int mColorFingerOn = 0xFF378FC9;
    private int mColorFingerUp = 0xFFFF0000;
    private int mColorSuccess = 0xFF87A952;
    private int mColorFail;
    private int mColorNoFingerInnerCircle = 0xFF939090;
    private int mColorNoFingerOuterCircle = 0xFFE0DBDB;
    private int mColorBackground;

    private int mWidth;
    private int mHeight;
    private int mRadius;
    private Model mCurrentStatus = Model.STATUS_NO_FINGER;
    private int mStockWidth;

    private int centerX;
    private int centerY;
    private Paint mPaint;

    /**
     * 三角形的边长 1/3 mRate
     */
    private float mArrowRate = 0.33f;


    public GestureLockView(Context context, int mColorFingerOn, int mColorFingerUp,
                           int mColorSuccess, int mColorNoFingerInnerCircle, int mColorNoFingerOuterCircle) {
        super(context);

        this.mColorFingerOn = mColorFingerOn;
        this.mColorFingerUp = mColorFingerUp;
        this.mColorSuccess = mColorSuccess;
        this.mColorFail = mColorFingerUp;
        this.mColorNoFingerInnerCircle = mColorNoFingerInnerCircle;
        this.mColorNoFingerOuterCircle = mColorNoFingerOuterCircle;
      //  this.mColorBackground = mColorBackground;

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    public GestureLockView(Context context) {
        this(context, null);
    }

    public GestureLockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 取长和宽的最小值
        mWidth = mWidth > mHeight ? mHeight : mWidth;
        mRadius = centerX = centerY = mWidth / 2;
        mRadius -= mStockWidth / 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        switch (mCurrentStatus) {
            case STATUS_NO_FINGER:
                mPaint.setColor(mColorNoFingerOuterCircle);
                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, mRadius, mPaint);

                mPaint.setColor(mColorNoFingerInnerCircle);
                canvas.drawCircle(centerX, centerY, mRadius/4, mPaint);
                break;
            case STATUS_FINGER_ON:
                mPaint.setColor(mColorFingerOn);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(centerX, centerY, mRadius, mPaint);

                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, mRadius / 4, mPaint);
                break;
            case STATUS_FINGER_UP:
                mPaint.setColor(mColorFingerUp);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(centerX, centerY, mRadius, mPaint);

                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, mRadius / 4, mPaint);
                break;
            case STATUS_SUCCESS:
                mPaint.setColor(mColorSuccess);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(2);
                canvas.drawCircle(centerX, centerY, mRadius, mPaint);

                mPaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(centerX, centerY, mRadius / 4, mPaint);
                break;
            default:
                break;
        }
    }

    public void setModel(Model model) {
        this.mCurrentStatus = model;
        invalidate();
    }

    public enum Model{
        STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_SUCCESS, STATUS_FAIL, STATUS_FINGER_UP;
    }
}
