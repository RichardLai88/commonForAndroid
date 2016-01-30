package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.richardlai.myview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Richard.Lai on 2016/1/22.
 */
public class GestureLockViewGroup extends RelativeLayout {

    private int mColorFingerOn = 0xFF378FC9;
    private int mColorFingerUp = 0xFFFF0000;
    private int mColorSuccess = 0xFF87A952;
    private int mColorFail;
    private int mColorNoFingerInnerCircle = 0xFF939090;
    private int mColorNoFingerOuterCircle = 0xFFE0DBDB;
    private int mColorBackground;
    private int mCount = 3;
    private int mTimes = 5;

    private Paint mPaint;
    private Path mPath;

    private int mWidth;
    private int mHeight;
    private int mLastPathX;
    private int mLastPathY;
    private Point mtmpTarget = new Point();

    private GestureLockView[] mGestureLockViews;
    private int mGestureLockViewWidth;
    private int mMarginBetweenLockView;
    private List<Integer> mChoose = new ArrayList<>();
    private int[] answer = {1, 2, 3, 4, 5};

    private OnGestureLockViewListener onGestureLockViewListener;



    public GestureLockViewGroup(Context context) {
        this(context, null);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GestureLockViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GestureLockViewGroup, defStyleAttr, 0);
        int count = ta.getIndexCount();

        for(int i = 0; i < count; i++) {
            int index = ta.getIndex(i);

            switch (index) {
                case R.styleable.GestureLockViewGroup_color_finger_on :
                    mColorFingerOn = ta.getColor(index, mColorFingerOn);
                    break;
                case R.styleable.GestureLockViewGroup_color_finger_up :
                    mColorFingerUp = ta.getColor(index, mColorFingerUp);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_inner_circle :
                    mColorNoFingerInnerCircle = ta.getColor(index, mColorNoFingerInnerCircle);
                    break;
                case R.styleable.GestureLockViewGroup_color_no_finger_outer_circle :
                    mColorNoFingerOuterCircle = ta.getColor(index, mColorNoFingerOuterCircle);
                    break;
                case R.styleable.GestureLockViewGroup_color_background:
                    mColorBackground = ta.getColor(index, mColorBackground);
                    break;
                case R.styleable.GestureLockViewGroup_count :
                    mCount = ta.getInt(index, mCount);
                    break;
                case R.styleable.GestureLockViewGroup_tryTimes:
                    mTimes = ta.getInt(index, mTimes);
                    break;
                default:
                    break;
            }
        }
        ta.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
/*        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);*/
        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeight = MeasureSpec.getSize(heightMeasureSpec);

        mWidth = mWidth < mHeight ? mWidth : mHeight;

        if(mGestureLockViews == null) {
            mGestureLockViews = new GestureLockView[mCount * mCount];

            mGestureLockViewWidth = (int)(4 * mWidth * 1.0f / (5 * mCount + 1));
            mMarginBetweenLockView = mGestureLockViewWidth / 4;
            mPaint.setStrokeWidth(mMarginBetweenLockView * 0.95f);


            for(int i = 0; i < mGestureLockViews.length; i++) {
                mGestureLockViews[i] = new GestureLockView(getContext() ,mColorFingerOn,mColorFingerUp, mColorSuccess,
                        mColorNoFingerInnerCircle, mColorNoFingerOuterCircle);
                mGestureLockViews[i].setId(i + 1);
                RelativeLayout.LayoutParams rl = new LayoutParams(mGestureLockViewWidth, mGestureLockViewWidth);

                // 每行的非第一个，都是在前一个的后面
                if(i % mCount != 0) {
                    rl.addRule(RelativeLayout.RIGHT_OF, mGestureLockViews[i - 1].getId());
                }
                // 从第二行开始，每行都是在上面一个的正下方
                if(i - (mCount - 1) > 0) {
                    rl.addRule(RelativeLayout.BELOW, mGestureLockViews[i - mCount].getId());
                }
                // 设置边距
                int marginLeft = 0;
                int marginRight = mMarginBetweenLockView;
                int marginTop = 0;
                int marginBottom = mMarginBetweenLockView;
                // 第一列的左边需要添加边距
                if(i % mCount == 0) {
                    marginLeft = mMarginBetweenLockView;
                }
                rl.setMargins(marginLeft, marginTop, marginRight, marginBottom);
                mGestureLockViews[i].setModel(GestureLockView.Model.STATUS_NO_FINGER);
                addView(mGestureLockViews[i], rl);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        int action = ev.getAction();
        int x = (int)ev.getX();
        int y = (int)ev.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                // 重置状态
                reset();
                break;
            case MotionEvent.ACTION_MOVE:
                mPaint.setColor(mColorFingerOn);
                mPaint.setAlpha(50);
                GestureLockView childView = getChildByPos(x, y);
                if(childView != null) {
                    int cId= childView.getId();
                    if(!mChoose.contains(cId)) {
                        mChoose.add(cId);

                        if(onGestureLockViewListener != null) {
                            onGestureLockViewListener.onBlockSelected(cId);
                        }

                        childView.setModel(GestureLockView.Model.STATUS_FINGER_ON);
                        mLastPathX = (childView.getLeft() + childView.getRight()) / 2;
                        mLastPathY = (childView.getTop() + childView.getBottom()) / 2;

                        if(mChoose.size() == 1) {
                            mPath.moveTo(mLastPathX, mLastPathY);
                        } else {
                            mPath.lineTo(mLastPathX, mLastPathY);
                        }
                    }
                }

                mtmpTarget.x = x;
                mtmpTarget.y = y;
                break;
            case MotionEvent.ACTION_UP :
                mPaint.setColor(mColorFingerUp);
                mPaint.setAlpha(50);
                mTimes--;

                if(mChoose.size() > 0 && onGestureLockViewListener != null) {
                    boolean result = checkAnswers();
                    if(result) {
                        mPaint.setColor(mColorSuccess);
                        mPaint.setAlpha(50);
                        changeChildStatus(GestureLockView.Model.STATUS_SUCCESS);
                    } else {
                        changeChildStatus(GestureLockView.Model.STATUS_FINGER_UP);
                    }
                    onGestureLockViewListener.onGestureEvent(result);
                    if(mTimes == 0) {
                        onGestureLockViewListener.onUnMatchExceedBoundary();
                    }
                }

                Log.e("TAG", "mChoose = " + mChoose);

                mtmpTarget.x = mLastPathX;
                mtmpTarget.y = mLastPathY;
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if(mPath != null) {
            canvas.drawPath(mPath, mPaint);
        }

        if(mChoose.size() > 0) {
            if(mLastPathY != 0 && mLastPathX != 0) {
                canvas.drawLine(mLastPathX, mLastPathY, mtmpTarget.x, mtmpTarget.y, mPaint);
            }
        }
    }

    private GestureLockView getChildByPos(int x, int y) {

        for(int i = 0; i< mGestureLockViews.length; i++) {

            if(checkPositionInChild(mGestureLockViews[i], x, y)) {
                return mGestureLockViews[i];
            }
        }

        return null;
    }

    private boolean checkPositionInChild(View view, int x, int y) {

        int padding = (int)(mGestureLockViewWidth * 0.15);

        if(x >= view.getLeft() + padding && x <= view.getRight() - padding
                && y >= view.getTop() + padding && y <= view.getBottom() - padding ) {
            return true;
        }

        return false;
    }

    private void changeChildStatus(GestureLockView.Model model) {

        for(int i = 0; i < mGestureLockViews.length; i++) {

            if(mChoose.contains(mGestureLockViews[i].getId())) {
                mGestureLockViews[i].setModel(model);
            }
        }

    }

    private void reset() {
        mChoose.clear();
        mPath.reset();
        for(GestureLockView child : mGestureLockViews) {
            child.setModel(GestureLockView.Model.STATUS_NO_FINGER);
        }
    }

    private boolean checkAnswers() {

        if(answer.length != mChoose.size()) {
            return false;
        }
        for(int i = 0; i < answer.length; i++) {

            if(answer[i] != mChoose.get(i)) {
                return false;
            }
        }

        return true;
    }

    public void resetLock(){
        reset();
        invalidate();
    }

    public void setOnGestureLockViewListener(OnGestureLockViewListener listener) {
        this.onGestureLockViewListener = listener;
    }

    public void setAnswer(int[] answer) {
        this.answer = answer;
    }

    public void setTryTimes(int times) {
        this.mTimes = times;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    public interface OnGestureLockViewListener {

        public void onBlockSelected(int cId);

        public void onGestureEvent(boolean result);

        public void onUnMatchExceedBoundary();
    }
}
