package com.darren.ireader.filechooser;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.darren.ireader.R;

import java.io.File;
import java.util.ArrayList;

public class DirectoryFragment extends Fragment implements View.OnClickListener {
    private final String TAG="DirectoryFragment";
    private DocumentSelectActivityDelegate delegate;
    private View fragmentView;
    private ListView listView;
    private DirectoryListAdapter listAdapter;
    private TextView emptyView;
    private ArrayList<DirectoryListItem> items = new ArrayList<DirectoryListItem>();
    private ArrayList<HistoryEntry> history = new ArrayList<HistoryEntry>();
    private File currentDir;
    private static String mTitle = "";


    public static interface DocumentSelectActivityDelegate {
        public void didSelectFiles(DirectoryFragment activity, ArrayList<String> files);

        public void startDocumentSelectActivity();

        public void updateToolBarName(String name);
    }

    private class DirectoryListItem {
        int icon;
        String title;
        String subtitle = "";
        String ext = "";
        String thumb;
        File file;
    }

    private class DirectoryListAdapter extends BaseAdapter {
        private Context mContext;
        public DirectoryListAdapter(Context context){
            mContext = context;
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = new TextDetailDocumentsCell(mContext);
            }

            TextDetailDocumentsCell textDetailCell = (TextDetailDocumentsCell) convertView;
            final DirectoryListItem item = items.get(position);

            if (item.icon != 0) {
                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, null, null, item.icon,false);
            } else {
                String type = item.ext.toUpperCase().substring(0,
                        Math.min(item.ext.length(), 4));

                ((TextDetailDocumentsCell) convertView)
                        .setTextAndValueAndTypeAndThumb(item.title,
                                item.subtitle, type, item.thumb, 0,false);
            }

            return convertView;
        }
    }

    private class HistoryEntry {
        int scrollItem;
        int scrollOffset;
        File dir;
        String title;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (fragmentView == null) {
            fragmentView = inflater.inflate(R.layout.document_select_layout,container,false);

            emptyView = (TextView)fragmentView.findViewById(R.id.searchEmptyView);
            listAdapter = new DirectoryListAdapter(getActivity());
            listView = (ListView)fragmentView.findViewById(R.id.listView);

            listView.setEmptyView(emptyView);
            listView.setAdapter(listAdapter);
            listRoots();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    if(position<0 || position>= items.size()){
                        return;
                    }

                    DirectoryListItem item = items.get(position);
                    File file = item.file;
                    if(file == null) {
                        HistoryEntry he = history.remove(history.size()-1);
                        mTitle = he.title;

                        updateName(mTitle);
                        if(he.dir != null){
                            listFiles(he.dir);
                        } else{
                            listRoots();
                        }

                        listView.setSelectionFromTop(he.scrollItem,he.scrollOffset);
                    } else if(file.isDirectory()){
                        HistoryEntry he = new HistoryEntry();
                        he.scrollItem = listView.getFirstVisiblePosition();
                        he.scrollOffset =  listView.getChildAt(0).getTop();
                        he.dir = currentDir;
                        he.title = mTitle.toString();
                        updateName(mTitle);
                        if(!listFiles(file)){
                            return;
                        }

                        history.add(he);
                        mTitle = item.title;
                        listView.setSelection(0);
                    } else {
                        if(file.canRead() == false){
                            showErrorBox("没有权限");
                            return;
                        } else{
                            showErrorBox("请选择正确的文件");
                            return ;
                        }
                    }
                }
            });
        }else {
            Log.d(TAG, Thread.currentThread().getStackTrace()[2].getMethodName()); //函数名
            Log.d(TAG, ""+Thread.currentThread().getStackTrace()[2].getLineNumber()); //行号
            ViewGroup parent = (ViewGroup) fragmentView.getParent();
            if (parent != null) {
                parent.removeView(fragmentView);
            }
        }

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        listAdapter.notifyDataSetChanged();
    }

    public boolean onBackPressed_() {
        if(history.size() > 0){
            HistoryEntry he = history.remove(history.size()-1);
            mTitle = he.title;

            updateName(mTitle);
            if(he.dir != null){
                listFiles(he.dir);
            }else{
                listRoots();
            }

            listView.setSelectionFromTop(he.scrollItem,he.scrollOffset);
            return true;
        }

        return false;
    }

    private void listRoots() {
        currentDir = null;
        items.clear();

        DirectoryListItem root = new DirectoryListItem();
        root.title = "/";
        root.subtitle ="系统目录";
        root.file = new File("/");
        root.icon = R.mipmap.directory;

        items.add(root);

        listAdapter.notifyDataSetChanged();
    }


    private boolean listFiles(File dir) {
        if(dir.canRead() == false){
            showErrorBox("没有权限");
            return false;
        }

        emptyView.setText("没有文件!");
        File[] files = null;

        files = dir.listFiles();
        if(files == null){
            showErrorBox("文件列表读取错误");
            return false;
        }

        currentDir = dir;
        items.clear();

        //parent dir
        DirectoryListItem parrentItem = new DirectoryListItem();
        parrentItem.file = null;
        parrentItem.title = "..";
        parrentItem.icon = R.mipmap.directory;
        parrentItem.subtitle = "文件夹";
        items.add(0,parrentItem);


        for(File file: files){
            if (file.getName().startsWith(".") || (!file.isDirectory() && !file.getName().endsWith(".txt"))) {
                continue;
            }

            DirectoryListItem item = new DirectoryListItem();
            item.file = file;
            item.title = file.getName();

            if(file.isDirectory()){
                item.icon = R.mipmap.directory;
                item.subtitle = "文件夹";
            } else{
                String fname = file.getName();
                String[] sp = fname.split("\\.");
                item.ext = sp.length>1 ? sp[sp.length-1] :"?";
                item.subtitle = formatFileSize(file.length());

                fname = fname.toLowerCase();
                if(fname.endsWith(".txt")){
                    item.thumb = file.getAbsolutePath();
                    Log.d(TAG,"thumb:"+item.thumb);
                }

            }

            items.add(item);
        }

        listAdapter.notifyDataSetChanged();

        return true;
    }

    @Override
    public void onClick(View view) {


    }

    public void updateName(String name) {
        if (delegate != null) {
            delegate.updateToolBarName(name);
        }
    }

    public void setDelegate(DocumentSelectActivityDelegate delegate) {
        this.delegate = delegate;
    }

    public void showReadBox(final String path) {
        if (getActivity() == null) {
            return ;
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle(getActivity().getString(R.string.app_name));
        dialog.setMessage(path);

        dialog.setPositiveButton("打开", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    public void showErrorBox(String err){
        if (getActivity() == null) {
            return;
        }

        new AlertDialog.Builder(getActivity()).setTitle(getActivity().getString(R.string.app_name))
                .setMessage(err).setPositiveButton("ok",null).show();
    }

    public static String formatFileSize(long size) {
        if (size < 1024) {
            return String.format("%d B", size);
        } else if (size < 1024 * 1024) {
            return String.format("%.1f KB", size / 1024.0f);
        } else if (size < 1024 * 1024 * 1024) {
            return String.format("%.1f MB", size / 1024.0f / 1024.0f);
        } else {
            return String.format("%.1f GB", size / 1024.0f / 1024.0f / 1024.0f);
        }
    }
}
