package com.darren.ireader;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public abstract class BaseActivity extends AppCompatActivity {
    public abstract int getLayoutRes();
    protected abstract void initData();
    protected abstract void initListener();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        initData();
        initListener();
    }

    public View findViewById(@IdRes int id) {
        return getWindow().getDecorView().findViewById(id);
    }
}
