/*
 * Copyright 2018 Seo Jeonggyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cnk24.sample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cnk24.sample.app.CameraActivity;
import com.cnk24.sample.app.ImageActivity;
import com.cnk24.sample.app.VideoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_image).setOnClickListener(this);
        findViewById(R.id.btn_video).setOnClickListener(this);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        boolean bNoLook = pref.getBoolean("NOLOOK", false);
        if (!bNoLook) {
            Help(true);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_camera: {
                startActivity(new Intent(this, CameraActivity.class));
                break;
            }
            case R.id.btn_image: {
                startActivity(new Intent(this, ImageActivity.class));
                break;
            }
            case R.id.btn_video: {
                startActivity(new Intent(this, VideoActivity.class));
                break;
            }
        }
    }

    private void NewAlbum() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_new_folder, null);
        final EditText name = (EditText)view.findViewById(R.id.et_name);
        Button btnAdd = (Button)view.findViewById(R.id.btn_add);
        Button btnCancel = (Button)view.findViewById(R.id.btn_cancel);

        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int)(size.x * 0.7f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window window = dialog.getWindow();
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, name.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    private void Help(boolean checkbox) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_help, null);
        TextView tvVersion = (TextView)view.findViewById(R.id.tv_app_version);
        final CheckBox cbLook = (CheckBox)view.findViewById(R.id.cb_look);
        Button btnOK = (Button)view.findViewById(R.id.btn_ok);

        cbLook.setVisibility(checkbox ? View.VISIBLE : View.GONE);

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            tvVersion.setText(info.versionName);
        } catch (PackageManager.NameNotFoundException e) {}


        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        Window window = dialog.getWindow();
        window.setAttributes(lp);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbLook.isChecked()) {
                    SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("NOLOOK", true);
                    editor.commit();
                }
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_new_folder: {
                NewAlbum();
                break;
            }
            case R.id.menu_help: {
                Help(false);
                break;
            }
        }
        return true;
    }
}
