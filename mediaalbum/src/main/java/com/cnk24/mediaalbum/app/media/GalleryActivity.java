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
package com.cnk24.mediaalbum.app.media;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.mvp.BaseActivity;
import com.cnk24.mediaalbum.util.MediaUtils;

import java.util.ArrayList;

/**
 * 20180819 SJK: Created
 */
public class GalleryActivity extends BaseActivity implements Contract.GalleryPresenter
{
    public static ArrayList<MediaFile> sMediaFiles;
    public static int sCheckedCount;
    public static int sCurrentPosition;

    public static Callback sCallback;

    private Widget mWidget;
    private int mFunction;
    private int mAllowSelectCount;

    private Contract.GalleryView<MediaFile> mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_activity_gallery);
        mView = new GalleryView<>(this, this);
        Bundle argument = getIntent().getExtras();
        assert argument != null;
        mWidget = argument.getParcelable(Media.KEY_INPUT_WIDGET);
        mFunction = argument.getInt(Media.KEY_INPUT_FUNCTION);
        mAllowSelectCount = argument.getInt(Media.KEY_INPUT_LIMIT_COUNT);

        mView.setupViews(mWidget, true);
        mView.bindData(new PreviewMediaAdapter(this, sMediaFiles));

        if (sCurrentPosition == 0) onCurrentChanged(sCurrentPosition);
        else mView.setCurrentItem(sCurrentPosition);
        setCheckedCount();
    }

    private void setCheckedCount() {
        String completeText = getString(R.string.media_menu_finish);
        completeText += "(" + sCheckedCount + " / " + mAllowSelectCount + ")";
        mView.setCompleteText(completeText);
    }

    @Override
    public void onCurrentChanged(int position) {
        sCurrentPosition = position;
        mView.setTitle(sCurrentPosition + 1 + " / " + sMediaFiles.size());

        MediaFile mediaFile = sMediaFiles.get(position);
        mView.setChecked(mediaFile.isChecked());
        mView.setLayerDisplay(mediaFile.isDisable());

        if (mediaFile.getMediaType() == MediaFile.TYPE_VIDEO) {
            mView.setDuration(MediaUtils.convertDuration(mediaFile.getDuration()));
            mView.setDurationDisplay(true);
        } else {
            mView.setDurationDisplay(false);
        }
    }

    @Override
    public void onCheckedChanged() {
        MediaFile mediaFile = sMediaFiles.get(sCurrentPosition);
        if (mediaFile.isChecked()) {
            mediaFile.setChecked(false);
            sCallback.onPreviewChanged(mediaFile);
            sCheckedCount--;
        } else {
            if (sCheckedCount >= mAllowSelectCount) {
                int messageRes;
                switch (mFunction) {
                    case Media.FUNCTION_CHOICE_IMAGE: {
                        messageRes = R.plurals.media_check_image_limit;
                        break;
                    }
                    case Media.FUNCTION_CHOICE_VIDEO: {
                        messageRes = R.plurals.media_check_video_limit;
                        break;
                    }
                    default: {
                        throw new AssertionError("This should not be the case.");
                    }
                }
                mView.toast(getResources().getQuantityString(messageRes, mAllowSelectCount, mAllowSelectCount));
                mView.setChecked(false);
            } else {
                mediaFile.setChecked(true);
                sCallback.onPreviewChanged(mediaFile);
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
                case Media.FUNCTION_CHOICE_IMAGE: {
                    messageRes = R.string.media_check_image_little;
                    break;
                }
                case Media.FUNCTION_CHOICE_VIDEO: {
                    messageRes = R.string.media_check_video_little;
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
        sMediaFiles = null;
        sCheckedCount = 0;
        sCurrentPosition = 0;
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
         * @param mediaFile target item.
         */
        void onPreviewChanged(MediaFile mediaFile);
    }
}
