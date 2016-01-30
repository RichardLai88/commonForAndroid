package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

import com.example.richardlai.myview.R;

/**
 * Created by Richard.Lai on 2016/1/18.
 */
public class ArcMenu extends ViewGroup implements View.OnClickListener {

    private static final String TAG = "RrcMenu";
    private Position mPosition =  Position.LEFT_TOP;
    private int mRadius = 100;
    private View mButton;
    private Status mCurrentStatus = Status.CLOSE;
    private OnMenuItemClickListener onMenuItemClickListener;



    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获取圆半径
        mRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                mRadius, getResources().getDisplayMetrics());

        // 获取自定义的属性
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.ArcMenu, defStyleAttr, 0);

        int n = ta.getIndexCount();

        for(int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.ArcMenu_position :
                    int val = ta.getInt(attr, 0);
                    switch (val) {
                        case 0: mPosition = Position.LEFT_TOP;
                            break;
                        case 1: mPosition = Position.RIGHT_TOP;
                            break;
                        case 2: mPosition = Position.RIGHT_BOTTOM;
                            break;
                        case 3: mPosition = Position.LEFT_BOTTOM;
                            break;
                        default: break;
                    }
                    break;
                case R.styleable.ArcMenu_radius :
                    mRadius = ta.getDimensionPixelSize(attr, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100f,
                            getResources().getDisplayMetrics()));
                    break;
                default: break;
            }
        }
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        for(int i = 0; i < count; i++) {
            getChildAt(i).measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void onClick(View view) {
        // 拿到第一个控件，点击的时候，打开menu
        mButton = findViewById(R.id.id_button);
        if(mButton == null) {
            mButton = getChildAt(0);
        }
        // 菜单按钮 旋转270°
        rotateView(mButton, 0f, 270f, 300);
        // 菜单打开时的动画
        toggleMenu(300);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        if(b) {
            layoutButton();
            int count = getChildCount();
            for(int j = 0; j < count - 1; j++) {
                View child = getChildAt(j + 1);

                child.setVisibility(View.GONE);

                int cl = (int)(mRadius * Math.sin(Math.PI / 2 / (count - 2)  * j));
                int ct = (int)(mRadius * Math.cos(Math.PI / 2 / (count - 2) * j));

                int cWidth = child.getMeasuredWidth();
                int cHeight = child.getMeasuredHeight();

                if(mPosition == Position.LEFT_BOTTOM
                        || mPosition == Position.RIGHT_BOTTOM) {
                    ct = getMeasuredHeight() - cHeight - ct;
                }

                if(mPosition == Position.RIGHT_BOTTOM || mPosition == Position.RIGHT_TOP) {
                    cl = getMeasuredWidth() - cWidth - cl;
                }
                Log.e(TAG, cl + " , " + ct);
                child.layout(cl, ct, cl + cWidth, ct + cHeight);
            }
        }
    }

    /**
     * 设置第一个控件的位置
     */
    private void layoutButton() {
        View cButton = getChildAt(0);

        cButton.setOnClickListener(this);

        int l = 0;
        int t = 0;
        int width = cButton.getMeasuredWidth();
        int height = cButton.getMeasuredHeight();

        switch (mPosition) {
            case LEFT_TOP:
                l = 0;
                t = 0;
                break;
            case RIGHT_TOP:
                l = getMeasuredWidth() - width;
                t = 0;
                break;
            case LEFT_BOTTOM:
                l = 0;
                t = getMeasuredHeight() - height;
                break;
            case RIGHT_BOTTOM:
                l = getMeasuredWidth() - width;
                t = getMeasuredHeight() - height;
                break;
            default: break;
        }
        Log.e(TAG, l + " , " + t + " , " + (l + width) + " , " + (t + height));
        cButton.layout(l,t, l + width, t + height);
    }

    /**
     * 旋转动画
     * @param view 需要执行动画的View
     * @param fromDegrees 初始角度
     * @param toDegrees  结束时的角度
     * @param durationMillis 动画执行时间
     */
    public static void rotateView(View view, float fromDegrees, float toDegrees, int durationMillis) {

        // 旋转动画， fromDegrees toDegrees，从一个角度到另一个角度。
        RotateAnimation rotate = new RotateAnimation(fromDegrees, toDegrees,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillAfter(true);
        view.startAnimation(rotate);
    }

    public void toggleMenu(int durationMillis) {
        int count = getChildCount();
        for(int i = 0; i < count - 1; i++) {
            final View child = getChildAt(i + 1);
            child.setVisibility(View.VISIBLE);

            int xFlag = 1;
            int yFlag = 1;

            // 左上、左下
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.LEFT_BOTTOM)
                xFlag = -1;
            // 左上、 右上
            if (mPosition == Position.LEFT_TOP
                    || mPosition == Position.RIGHT_TOP)
                yFlag = -1;

            int cl = (int)(mRadius * Math.sin(Math.PI / 2 / (count - 2) * i));
            int ct = (int)(mRadius * Math.cos(Math.PI / 2 / (count - 2) * i));

            AnimationSet animationSet = new AnimationSet(true);
            Animation animation = null;
            if(mCurrentStatus == Status.CLOSE) {
                // 设置插值器，OvershootInterpolator 向外面甩一定的距离然后回到最终位置，这里设置的是2F tension的距离
                animationSet.setInterpolator(new OvershootInterpolator(2F));

                /*  TranslateAnimation
                    float fromXDelta 动画开始的点离当前View X坐标上的差值
                    float toXDelta 动画结束的点离当前View X坐标上的差值
                    float fromYDelta 动画开始的点离当前View Y坐标上的差值
                    float toYDelta 动画开始的点离当前View Y坐标上的差值
                 */
                animation = new TranslateAnimation(xFlag * cl, 0f, yFlag * ct, 0f);
                child.setClickable(true);
                child.setFocusable(true);
            } else {
             //   animationSet.setInterpolator(new OvershootInterpolator(2F));
                animation = new TranslateAnimation(0f, xFlag * cl, 0f, yFlag * ct);
                child.setClickable(false);
                child.setFocusable(false);
            }
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (mCurrentStatus == Status.CLOSE) {
                        child.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            animation.setFillAfter(true);
            animation.setDuration(durationMillis);
            animation.setStartOffset((i * 100) / (count - 1));
            // 旋转动画类
            RotateAnimation rotate = new RotateAnimation(0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(durationMillis);
            // 设置动画结束后保留结束时的状态
            rotate.setFillAfter(true);
            animationSet.addAnimation(rotate);
            animationSet.addAnimation(animation);
            child.startAnimation(animationSet);
            final int index = i + 1;
            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onMenuItemClickListener != null) {
                        onMenuItemClickListener.onClick(child, index - 1);
                        menuItemAnim(index - 1);
                        changeStatus();
                    }
                }
            });
        }
        changeStatus();
        Log.e(TAG, mCurrentStatus.name() + "");
    }

    /**
     *
     * @param item 选中菜单的index
     */
    private void menuItemAnim(int item) {
        for(int i = 0; i < getChildCount() - 1; i++) {
            View child = getChildAt(i + 1);
            if(i == item) {
                child.startAnimation(scaleBigAnim(300));
            } else {
                child.startAnimation(scaleSmallAnim(300));
            }
            child.setClickable(false);
            child.setFocusable(false);
        }
    }

    /**
     * 缩小
     * @param durationMillis 动画时间
     * @return
     */
    private Animation scaleSmallAnim(int durationMillis){
        /*
            fromX toX x 坐标从一个相对于自己的位置到另外一个位置。
            fromY toY y 坐标从一个相对于自己的位置到另外一个位置
            pivotXType x 坐标偏移量的类型(也就是缩放中心的坐标)
         */
        Animation anim = new ScaleAnimation(1.0f, 0f, 1.0f, 0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        anim.setDuration(durationMillis);
        anim.setFillAfter(true);
        return anim;
    }

    /**
     * 放大
     * @param durationMillis 动画时间
     * @return
     */
    private Animation scaleBigAnim(int durationMillis) {
        // 在AnimationSet 中可以设置多个动画，可以同时执行，也可以顺序执行
        AnimationSet animSet = new AnimationSet(true);
        // 放大缩小动画效果，
        Animation anim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // 透明度动画效果
        Animation alphaAnimation = new AlphaAnimation(1, 0);
        animSet.addAnimation(anim);
        animSet.addAnimation(alphaAnimation);
        animSet.setDuration(durationMillis);
        animSet.setFillAfter(true);
        return animSet;
    }

    /**
     * 更改惨淡状态
     */
    private void changeStatus()
    {
        mCurrentStatus = (mCurrentStatus == Status.CLOSE ? Status.OPEN
                : Status.CLOSE);
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public enum Position
    {
        LEFT_TOP, RIGHT_TOP, RIGHT_BOTTOM, LEFT_BOTTOM;
    }
    public enum Status
    {
        OPEN, CLOSE
    }

    public interface OnMenuItemClickListener{
        void onClick(View view, int pos);
    }
}
