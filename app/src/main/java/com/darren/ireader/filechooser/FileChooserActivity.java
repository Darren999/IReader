package com.darren.ireader.filechooser;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import com.darren.ireader.BaseActivity;
import com.darren.ireader.R;

import java.util.ArrayList;

public class FileChooserActivity extends BaseActivity {
    private Toolbar toolbar;


    private FragmentManager fragmentManager = null;
    private FragmentTransaction fragmentTransaction = null;
    private DirectoryFragment mDirectoryFragment;
    @Override
    public int getLayoutRes() {
        return R.layout.activity_filechooser;
    }

    @Override
    protected void initData() {
        toolbar = (Toolbar)findViewById(R.id.tool_bar);
        toolbar.setTitle("目录");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        mDirectoryFragment = new DirectoryFragment();
        mDirectoryFragment.setDelegate(new DirectoryFragment.DocumentSelectActivityDelegate() {
            @Override
            public void didSelectFiles(DirectoryFragment activity, ArrayList<String> files) {
                mDirectoryFragment.showReadBox(files.get(0).toString());
            }

            @Override
            public void startDocumentSelectActivity() {

            }

            @Override
            public void updateToolBarName(String name) {
                toolbar.setTitle(name);
            }
        });

        fragmentTransaction.add(R.id.fragment_container,mDirectoryFragment,"");
        fragmentTransaction.commit();

    }

    @Override
    protected void initListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if(mDirectoryFragment.onBackPressed_()){
            return ;
        }

        super.onBackPressed();
    }

}
