package com.example.fileexplorer;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mPathTV;
    private ImageButton mBackIB;
    private ListView mFileLV;

    private File mDir;
    private File[] mFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if (hasSDCard) {
            initView();

            File rootDir = Environment.getExternalStorageDirectory();

            mDir = rootDir;
            mFiles = mDir.listFiles();

            refreshFileList(mFiles);
        } else {
            Toast.makeText(this, "无SD卡!", Toast.LENGTH_SHORT).show();
        }
    }

    private void initView() {
        mPathTV = findViewById(R.id.main_file_path_tv);
        mBackIB = findViewById(R.id.main_back_ib);
        mFileLV = findViewById(R.id.main_file_list_lv);

        mBackIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDir.getAbsolutePath().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
                    MainActivity.this.finish();
                } else {
                    mDir = mDir.getParentFile();
                    mFiles = mDir.listFiles();
                    refreshFileList(mFiles);
                }
            }
        });

        mFileLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mFiles[i].isFile()) {
                    Toast.makeText(MainActivity.this, "这是一个文件", Toast.LENGTH_SHORT).show();
                    return;
                }

                File[] files = mFiles[i].listFiles();
                if (files == null || files.length == 0) {
                    Toast.makeText(MainActivity.this, "文件夹为空", Toast.LENGTH_SHORT).show();
                } else {
                    mDir = mFiles[i];
                    mFiles = files;

                    refreshFileList(mFiles);
                }
            }
        });
    }

    private void refreshFileList(File[] files) {
        List<Map<String, Object>> data = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            Map<String, Object> m = new HashMap<>();
            m.put("name", files[i].getName());
            if (files[i].isFile()) {
                m.put("icon", R.mipmap.file);
            } else {
                m.put("icon", R.mipmap.folder);
            }

            data.add(m);
        }

        SimpleAdapter adapter = new SimpleAdapter(this, data, R.layout.lvitem_main_file, new String[]{"name", "icon"}, new int[]{R.id.lvitem_main_file_name_tv, R.id.lvitem_main_file_icon_iv});
        mFileLV.setAdapter(adapter);

        mPathTV.setText("当前路径" + mDir.getAbsolutePath());
    }
}