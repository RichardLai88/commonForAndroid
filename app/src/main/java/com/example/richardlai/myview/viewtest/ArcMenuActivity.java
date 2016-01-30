package com.example.richardlai.myview.viewtest;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.richardlai.myview.R;
import com.example.richardlai.myview.myview.ArcMenu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

/**
 * Created by Richard.Lai on 2016/1/18.
 */
@EActivity(R.layout.activity_arc_menu)
public class ArcMenuActivity extends AppCompatActivity {

    @ViewById
    ArcMenu mArcMenuLeftTop;

    @ViewById
    TextView tvText;

    @AfterViews
    void initViews() {
        //动态添加一个MenuItem
        ImageView people = new ImageView(this);
        people.setImageResource(R.drawable.composer_with);
        people.setTag("People");
        mArcMenuLeftTop.addView(people);


        mArcMenuLeftTop
                .setOnMenuItemClickListener(new ArcMenu.OnMenuItemClickListener()
                {
                    @Override
                    public void onClick(View view, int pos)
                    {
                        Toast.makeText(ArcMenuActivity.this,
                                pos + ":" + view.getTag(), Toast.LENGTH_SHORT)
                                .show();
                    }
                });
    }

    @Click(R.id.tvText)
    void tvTextOnCLick() {
        Toast.makeText(ArcMenuActivity.this,
                "tvText :" + tvText.getText(), Toast.LENGTH_SHORT)
                .show();
    }
}
