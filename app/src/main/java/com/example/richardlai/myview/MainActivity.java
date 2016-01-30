package com.example.richardlai.myview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.richardlai.myview.myview.GestureLockView;
import com.example.richardlai.myview.myview.GestureLockView.Model;
import com.example.richardlai.myview.myview.GestureLockViewGroup;
import com.example.richardlai.myview.viewtest.ArcMenuActivity_;
import com.example.richardlai.myview.viewtest.LoadingActivity_;
import com.example.richardlai.myview.viewtest.SlidingActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.logging.Logger;

/**
 * Created by Richard.Lai on 2016/1/13.
 */
@EActivity(R.layout.view_custom_progress)
public class MainActivity extends AppCompatActivity {

    @ViewById
    Button btnLoading;

    @ViewById
    GestureLockViewGroup glvgTest;

    @ViewById
    Button btnRestGesture;


    @AfterViews
    void iniViews() {
      /*  GestureLockView lockView = new GestureLockView(this, mFingerOnColor, mFingerUpColor, mSuccessColor
                , mNoFingerInnerCircleColor, mNoFingerOuterCircleColor );
    //    lockView.setModel(Model.STATUS_NO_FINGER);
        llEmptyLayout.addView(lockView);*/

        glvgTest.setOnGestureLockViewListener(new GestureLockViewGroup.OnGestureLockViewListener() {

            @Override
            public void onBlockSelected(int cId) {
                Log.e("TAG", "cId = " + cId);
            }

            @Override
            public void onGestureEvent(boolean result) {
                Log.e("TAG", "result = " + result);
            }

            @Override
            public void onUnMatchExceedBoundary() {
                Log.e("TAG", "onUnMatchExceedBoundary");
            }
        });
    }

    @Click(R.id.btnLoading)
    void btnLoadingOnClick() {
        Intent intent = new Intent(this, LoadingActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btnArcMenu)
    void btnArcMeneOnClick() {
        Intent intent = new Intent (this, ArcMenuActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btnSlide)
    void btnSlideOnClick() {
        Intent intent = new Intent(this, SlidingActivity_.class);
        startActivity(intent);
    }

    @Click(R.id.btnRestGesture)
    void btnRestGestureOnClick() {
        glvgTest.resetLock();
    }
}
