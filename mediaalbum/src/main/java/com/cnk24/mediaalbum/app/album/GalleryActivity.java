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
package com.cnk24.mediaalbum.app.album;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cnk24.mediaalbum.Album;
import com.cnk24.mediaalbum.AlbumFile;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.app.gallery.GalleryView;
import com.cnk24.mediaalbum.app.gallery.PreviewAlbumAdapter;
import com.cnk24.mediaalbum.mvp.BaseActivity;
import com.cnk24.mediaalbum.util.AlbumUtils;

import java.util.ArrayList;

/**
 * 20180819 SJK: Created
 */
public class GalleryActivity extends BaseActivity implements Contract.GalleryPresenter {

    public static ArrayList<AlbumFile> sAlbumFileList;
    public static AlbumFile sAlbumFile;
    public static int sCheckedCount;

    public static Callback sCallback;

    private Widget mWidget;
    private int mFunction;
    private int mAllowSelectCount;
    private int mCurrentPosition;

    private Contract.GalleryView<AlbumFile> mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity_gallery);
        mView = new GalleryView<>(this, this);
        Bundle argument = getIntent().getExtras();
        assert argument != null;
        mWidget = argument.getParcelable(Album.KEY_INPUT_WIDGET);
        mFunction = argument.getInt(Album.KEY_INPUT_FUNCTION);
        mAllowSelectCount = argument.getInt(Album.KEY_INPUT_LIMIT_COUNT);

        mView.setupViews(mWidget, true);
        mView.bindData(new PreviewAlbumAdapter(this, sAlbumFileList));

        mCurrentPosition = sAlbumFileList.indexOf(sAlbumFile);

        if (mCurrentPosition == 0) onCurrentChanged(mCurrentPosition);
        else mView.setCurrentItem(mCurrentPosition);
        setCheckedCount();
    }

    private void setCheckedCount() {
        String completeText = getString(R.string.media_menu_finish);
        completeText += "(" + sCheckedCount + " / " + mAllowSelectCount + ")";
        mView.setCompleteText(completeText);
    }

    @Override
    public void onCurrentChanged(int position) {
        mCurrentPosition = position;
        mView.setTitle(mCurrentPosition + 1 + " / " + sAlbumFileList.size());

        AlbumFile albumFile = sAlbumFileList.get(position);
        mView.setChecked(albumFile.isChecked());
        mView.setLayerDisplay(albumFile.isDisable());

        if (albumFile.getMediaType() == AlbumFile.TYPE_VIDEO) {
            mView.setDuration(AlbumUtils.convertDuration(albumFile.getDuration()));
            mView.setDurationDisplay(true);
        } else {
            mView.setDurationDisplay(false);
        }
    }

    @Override
    public void onCheckedChanged() {
        AlbumFile albumFile = sAlbumFileList.get(mCurrentPosition);
        if (albumFile.isChecked()) {
            albumFile.setChecked(false);
            sCallback.onPreviewChanged(albumFile);
            sCheckedCount--;
        } else {
            if (sCheckedCount >= mAllowSelectCount) {
                int messageRes;
                switch (mFunction) {
                    case Album.FUNCTION_CHOICE_IMAGE: {
                        messageRes = R.plurals.media_check_image_limit;
                        break;
                    }
                    case Album.FUNCTION_CHOICE_VIDEO: {
                        messageRes = R.plurals.media_check_video_limit;
                        break;
                    }
                    case Album.FUNCTION_CHOICE_ALBUM: {
                        messageRes = R.plurals.media_check_album_limit;
                        break;
                    }
                    default: {
                        throw new AssertionError("This should not be the case.");
                    }
                }
                mView.toast(getResources().getQuantityString(messageRes, mAllowSelectCount, mAllowSelectCount));
                mView.setChecked(false);
            } else {
                albumFile.setChecked(true);
                sCallback.onPreviewChanged(albumFile);
                sCheckedCount++;
            }
        }

        setCheckedCount();
    }

    @Override
    public void complete() {
        if (sCheckedCount == 0) {
            int messageRes;
            switch (mFunction) {
                case Album.FUNCTION_CHOICE_IMAGE: {
                    messageRes = R.string.media_check_image_little;
                    break;
                }
                case Album.FUNCTION_CHOICE_VIDEO: {
                    messageRes = R.string.media_check_video_little;
                    break;
                }
                case Album.FUNCTION_CHOICE_ALBUM: {
                    messageRes = R.string.media_check_album_little;
                    break;
                }
                default: {
                    throw new AssertionError("This should not be the case.");
                }
            }
            mView.toast(messageRes);
        } else {
            sCallback.onPreviewComplete();
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void finish() {
        sAlbumFileList = null;
        sAlbumFile = null;
        sCheckedCount = 0;
        sCallback = null;
        super.finish();
    }

    public interface Callback {
        /**
         * Complete the preview.
         */
        void onPreviewComplete();

        /**
         * Check or uncheck a item.
         *
         * @param albumFile target item.
         */
        void onPreviewChanged(AlbumFile albumFile);
    }
}
