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
package com.cnk24.mediaalbum.app.gallery;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.cnk24.mediaalbum.Action;
import com.cnk24.mediaalbum.Album;
import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.ItemAction;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.impl.OnItemClickListener;
import com.cnk24.mediaalbum.mvp.BaseActivity;
import com.cnk24.mediaalbum.util.AlbumUtils;

import java.util.ArrayList;

/**
 * 20180822 SJK Created.
 */
public class GalleryAlbumActivity extends BaseActivity implements Contract.GalleryPresenter {

    public static Action<ArrayList<AlbumFile>> sResult;
    public static Action<String> sCancel;

    public static ItemAction<AlbumFile> sClick;
    public static ItemAction<AlbumFile> sLongClick;

    private Widget mWidget;
    private ArrayList<AlbumFile> mAlbumFiles;
    private int mCurrentPosition;
    private boolean mCheckable;

    private Contract.GalleryView<AlbumFile> mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_gallery);
        mView = new GalleryView<>(this, this);

        Bundle argument = getIntent().getExtras();
        assert argument != null;
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        mAlbumFiles = argument.getParcelableArrayList(Album.KEY_INPUT_CHECKED_LIST);
        mCurrentPosition = argument.getInt(Album.KEY_INPUT_CURRENT_POSITION);
        mCheckable = argument.getBoolean(Album.KEY_INPUT_GALLERY_CHECKABLE);

        mView.setTitle(mWidget.getTitle());
        mView.setupViews(mWidget, mCheckable);
        PreviewAdapter<AlbumFile> adapter = new PreviewAlbumAdapter(this, mAlbumFiles);
        if (sClick != null) {
            adapter.setItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    sClick.onAction(GalleryAlbumActivity.this, mAlbumFiles.get(mCurrentPosition));
                }
            });
        }
        if (sLongClick != null) {
            adapter.setItemLongClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    sLongClick.onAction(GalleryAlbumActivity.this, mAlbumFiles.get(mCurrentPosition));
                }
            });
        }
        mView.bindData(adapter);

        if (mCurrentPosition == 0) onCurrentChanged(mCurrentPosition);
        else mView.setCurrentItem(mCurrentPosition);
        setCheckedCount();
    }

    private void setCheckedCount() {
        int checkedCount = 0;
        for (AlbumFile albumFile : mAlbumFiles) {
            if (albumFile.isChecked()) checkedCount += 1;
        }

        String completeText = getString(R.string.album_menu_finish);
        completeText += "(" + checkedCount + " / " + mAlbumFiles.size() + ")";
        mView.setCompleteText(completeText);
    }

    @Override
    public void onCurrentChanged(int position) {
        mCurrentPosition = position;
        mView.setSubTitle(position + 1 + " / " + mAlbumFiles.size());

        AlbumFile albumFile = mAlbumFiles.get(position);
        if (mCheckable) mView.setChecked(albumFile.isChecked());
        mView.setLayerDisplay(albumFile.isDisable());

        if (albumFile.getMediaType() == AlbumFile.TYPE_VIDEO) {
            if (!mCheckable) mView.setBottomDisplay(true);
            mView.setDuration(AlbumUtils.convertDuration(albumFile.getDuration()));
            mView.setDurationDisplay(true);
        } else {
            if (!mCheckable) mView.setBottomDisplay(false);
            mView.setDurationDisplay(false);
        }
    }

    @Override
    public void onCheckedChanged() {
        AlbumFile albumFile = mAlbumFiles.get(mCurrentPosition);
        albumFile.setChecked(!albumFile.isChecked());

        setCheckedCount();
    }

    @Override
    public void complete() {
        if (sResult != null) {
            ArrayList<AlbumFile> checkedList = new ArrayList<>();
            for (AlbumFile albumFile : mAlbumFiles) {
                if (albumFile.isChecked()) checkedList.add(albumFile);
            }
            sResult.onAction(checkedList);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        if (sCancel != null) sCancel.onAction("User canceled.");
        finish();
    }

    @Override
    public void finish() {
        sResult = null;
        sCancel = null;
        sClick = null;
        sLongClick = null;
        super.finish();
    }
}