package com.illidan.dengqian.bg220.picture;
import com.illidan.dengqian.bg220.R;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class BasicActivity extends AppCompatActivity {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }
}
