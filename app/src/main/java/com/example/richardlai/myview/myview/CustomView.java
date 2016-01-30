package com.example.richardlai.myview.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.richardlai.myview.R;

/**
 * Created by Richard.Lai on 2016/1/14.
 */
public class CustomView extends View {

    private static final int[] mAttr = { android.R.attr.text, R.attr.testAttr };
    private static final int ATTR_ANDROID_TEXT = 0;
    private static final int ATTR_TESTATTR = 1;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);


        int count = attrs.getAttributeCount();

        for(int i =0; i < count; i++ ) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            Log.e("TAG", "text = " + attrName + " , textAttr = " + attrValue);
        }

        Log.e("TAG", "typeArray ==>>");

        TypedArray ta = context.obtainStyledAttributes(attrs, mAttr);
        String text = ta.getString(ATTR_ANDROID_TEXT);
        int textAttr = ta.getInteger(ATTR_TESTATTR, -1);

        Log.e("TAG", "text = " + text + " , textAttr = " + textAttr);
        ta.recycle();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
