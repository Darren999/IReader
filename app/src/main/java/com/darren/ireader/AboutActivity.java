package com.darren.ireader;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends BaseActivity{

    ImageView bannner;
    TextView tvVersion;
    Toolbar toolbar;
    CollapsingToolbarLayout toolbarLayout;
    AppBarLayout appBar;
//    CoordinatorLayout coord;

    @Override
    public int getLayoutRes() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        bannner = (ImageView)findViewById(R.id.bannner);
        tvVersion = (TextView)findViewById(R.id.tv_version);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        appBar = (AppBarLayout)findViewById(R.id.app_bar);
//        coord = (CoordinatorLayout)findViewById(R.id.coord);

        toolbar.setTitle(getResources().getString(R.string.app_name));
        tvVersion.setText("current vision:1.01");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }

    @Override
    protected void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
