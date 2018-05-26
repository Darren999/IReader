package com.darren.ireader;


import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {
    private NavigationView navigationView;
    private Toolbar toolbar;
    private DrawerLayout drawer_layout;


    @Override
    public int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        System.out.println("MainActivity initData");
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

        //clear background
        getWindow().setBackgroundDrawable(null);

    }

    @Override
    protected void initListener() {
        System.out.println("MainActivity initListener");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState();
        drawer_layout.addDrawerListener(toggle);

        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_feedback:
                break;
            case R.id.nav_checkupdate:
                break;
            case R.id.nav_about:
            {
                Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                startActivity(intent);
                break;
            }
        }
        return false;
    }




}
