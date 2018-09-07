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
package com.cnk24.sample.app;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cnk24.mediaalbum.Action;
import com.cnk24.mediaalbum.Album;
import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.ItemAction;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.impl.OnAlbumItemClickListener;
import com.cnk24.mediaalbum.impl.OnCheckedClickListener;
import com.cnk24.sample.R;
import com.cnk24.sample.app.data.RecyclerViewDataAdapter;

import java.util.ArrayList;

public class ImageActivity extends AppCompatActivity
{
    private Toolbar mToolbar;
    private TextView mTvMessage;

    private RecyclerViewDataAdapter mAdapter;
    private ArrayList<AlbumFile> mAlbumFiles;

    private boolean mDeleteFlag = false;
    private ArrayList<AlbumFile> mDeleteAlbumFiles = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTvMessage = findViewById(R.id.tv_message);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        // LinearLayoutManager : 가로 또는 세로 스크롤 목록
        // GridLayoutManager : 그리드 형식의 목록
        // StaggeredGridLayoutManager : 지그재그형의 그리드 형식 목록
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        //recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //Divider divider = new Api21ItemDivider(Color.TRANSPARENT, 5, 5);
        //recyclerView.addItemDecoration(divider);

        mAdapter = new RecyclerViewDataAdapter(this);
        mAdapter.setAlbumItemClickListener(new OnAlbumItemClickListener() {
            @Override
            public void onAlbumItemClick(View view, AlbumFile albumFile) {
                previewImage(albumFile);
            }
        });
        mAdapter.setCheckedClickListener(new OnCheckedClickListener() {
            @Override
            public void onCheckedClick(CompoundButton button, AlbumFile albumFile) {
                mDeleteAlbumFiles.add(albumFile);
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void setDelete(boolean flag) {
        if (mAlbumFiles != null) {
            mDeleteFlag = flag;
            invalidateOptionsMenu();

            mAdapter.setDeleteFlag(flag);
            mAdapter.notifyDataSetChanged(mAlbumFiles);
        }
    }

    private void deleteAlbumFile() {
        if (mDeleteAlbumFiles.size() > 0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes 버튼을 클릭했을때 처리
                            setDelete(false);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No 버튼을 클릭했을때 처리
                            setDelete(false);
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.question_content).setPositiveButton(R.string.question_yes, dialogClickListener)
                    .setNegativeButton(R.string.question_no, dialogClickListener).show();
        }
    }

    /**
     * Select picture, from album.
     */
    private void selectImage() {
        Album.image(this)
                .multipleChoice()
                .camera(false)
                .columnCount(4)
                .selectCount(10)
                .checkedList(mAlbumFiles)
                .widget(
                        Widget.newDarkBuilder(this)
                                .title(mToolbar.getTitle().toString())
                                .build()
                )
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        mAlbumFiles = result;
                        mAdapter.notifyDataSetChanged(mAlbumFiles);
                        mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {
                        Toast.makeText(ImageActivity.this, R.string.canceled, Toast.LENGTH_LONG).show();
                    }
                })
                .start();
    }

    /**
     * Preview image, to media.
     */
    private void previewImage(AlbumFile albumFile) {
        if (mAlbumFiles == null || mAlbumFiles.size() == 0) {
            Toast.makeText(this, R.string.no_selected, Toast.LENGTH_LONG).show();
        } else {
            int position = mAlbumFiles.indexOf(albumFile);

            Album.galleryAlbum(this)
                    .checkable(false)
                    .checkedList(mAlbumFiles)
                    .currentPosition(position)
                    .widget(
                            Widget.newDarkBuilder(this)
                                    .title(mToolbar.getTitle().toString())
                                    .build()
                    )
                    //.onResult(new Action<ArrayList<AlbumFile>>() {
                    //    @Override
                    //    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                    //        mAlbumFiles = result;
                    //        mAdapter.notifyDataSetChanged(mAlbumFiles);
                    //        //mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    //    }
                    //})
                    .start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home: {
                finish();
                break;
            }
            //case R.id.menu_eye: {
            //    previewImage(0);
            //    break;
            //}
            case R.id.menu_select_image: {
                selectImage();
                break;
            }
            case R.id.menu_select: {
                setDelete(true);
                break;
            }
            case R.id.menu_cancel: {
                setDelete(false);
                break;
            }
            case R.id.menu_delete: {
                deleteAlbumFile();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.menu_select_image).setVisible(!mDeleteFlag);
        menu.findItem(R.id.menu_select).setVisible(!mDeleteFlag);

        menu.findItem(R.id.menu_cancel).setVisible(mDeleteFlag);
        menu.findItem(R.id.menu_delete).setVisible(mDeleteFlag);
        return true;
    }

}
