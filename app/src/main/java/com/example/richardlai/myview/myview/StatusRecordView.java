package com.example.richardlai.myview.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Richard.Lai on 2016/1/30.
 */
public class StatusRecordView extends RelativeLayout {

    private int mColor = 0xFFFF0000;
    private int mStatusDefaultColor = 0xFF999999;
    private int mStatusSuccessColor =  0xFF22b14c;
    private int mStatusFailColor = 0xFFc81010;
    private int mLineColor = 0xFF666666;
    private int mStatusCircleSize;
    private int mTextSize;

    private int mWidth;
    private int mHeight;
    private float strokeWidth = 3;
    private Status currentStatus = Status.NORMAL;

    private Paint mPaint;
    private Path mPath;

    private String[] titles = {"已完成", "存续中", "未支付", "已预约"};
    private Status[] statuses = {Status.SUCCESS, Status.FAIL, Status.NORMAL, Status.NORMAL};
    private int count = 4;
    private TextView[] titleViews;

    public StatusRecordView(Context context) {
        this(context, null);
    }

    public StatusRecordView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusRecordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);
        int textHight = mHeight / (5 * count);
        if(titleViews == null) {
            titleViews = new TextView[count];
            for(int i = 0; i < titleViews.length ; i++) {
                titleViews[i] = new TextView(getContext());
                titleViews[i].setText(titles[i]);
                titleViews[i].setId(i + 1);
                RelativeLayout.LayoutParams rl = new LayoutParams(mWidth / 2, textHight);
                strokeWidth = textHight / 2;
                if(i > 0){
                    rl.addRule(RelativeLayout.BELOW, titleViews[i - 1].getId());

                }
                rl.setMargins(80, 0, 0, 0);
                addView(titleViews[i], rl);
            }
        }
        mPaint.setColor(mColor);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        mPaint.setColor(mLineColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);
        if(mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }

        mPaint.setStyle(Paint.Style.FILL);

        for(int j = 0; j < titleViews.length; j++) {
            View child = getChildAt(j);
            int centerX = child.getLeft() -20;
            int centerY = (child.getTop() + child.getBottom()) / 2;
            if(j == 0) {
                mPath.moveTo(centerX, centerY);
            } else {
                mPath.lineTo(centerX, centerY);
            }
            int color = getColorByStatus(statuses[j]);
            mPaint.setColor(color);
            canvas.drawCircle(centerX, centerY, 20, mPaint);
        }

    }
    private int getColorByStatus(Status status) {
       int color = mStatusDefaultColor;
        switch (status){
            case FAIL:
                color = mStatusFailColor;
                break;
            case SUCCESS:
                color = mStatusSuccessColor;
                break;
            case NORMAL:
                color = mStatusDefaultColor;
                break;
            default:
                break;
        }
        return color;
    }

    public enum Status{
        NORMAL, SUCCESS, FAIL
    }
}
