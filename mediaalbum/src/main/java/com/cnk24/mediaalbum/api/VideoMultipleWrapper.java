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
package com.cnk24.mediaalbum.api;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntRange;

import com.cnk24.mediaalbum.Filter;
import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.app.media.MediaActivity;

import java.util.ArrayList;

/**
 * 20180817 SJK: Created
 */
public final class VideoMultipleWrapper extends BasicChoiceVideoWrapper<VideoMultipleWrapper, ArrayList<MediaFile>, String, ArrayList<MediaFile>>
{
    private int mLimitCount = Integer.MAX_VALUE;
    private Filter<Long> mDurationFilter;

    public VideoMultipleWrapper(Context context) {
        super(context);
    }

    /**
     * Set the list has been selected.
     *
     * @param checked the data list.
     */
    public final VideoMultipleWrapper checkedList(ArrayList<MediaFile> checked) {
        this.mChecked = checked;
        return this;
    }

    /**
     * Set the maximum number to be selected.
     *
     * @param count the maximum number.
     */
    public VideoMultipleWrapper selectCount(@IntRange(from = 1, to = Integer.MAX_VALUE) int count) {
        this.mLimitCount = count;
        return this;
    }

    /**
     * Filter video duration.
     *
     * @param filter filter.
     */
    public VideoMultipleWrapper filterDuration(Filter<Long> filter) {
        this.mDurationFilter = filter;
        return this;
    }

    @Override
    public void start() {
        MediaActivity.sSizeFilter = mSizeFilter;
        MediaActivity.sMimeFilter = mMimeTypeFilter;
        MediaActivity.sDurationFilter = mDurationFilter;
        MediaActivity.sResult = mResult;
        MediaActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, MediaActivity.class);
        intent.putExtra(Media.KEY_INPUT_WIDGET, mWidget);
        intent.putParcelableArrayListExtra(Media.KEY_INPUT_CHECKED_LIST, mChecked);

        intent.putExtra(Media.KEY_INPUT_FUNCTION, Media.FUNCTION_CHOICE_VIDEO);
        intent.putExtra(Media.KEY_INPUT_CHOICE_MODE, Media.MODE_MULTIPLE);
        intent.putExtra(Media.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Media.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Media.KEY_INPUT_LIMIT_COUNT, mLimitCount);
        intent.putExtra(Media.KEY_INPUT_CAMERA_QUALITY, mQuality);
        intent.putExtra(Media.KEY_INPUT_CAMERA_DURATION, mLimitDuration);
        intent.putExtra(Media.KEY_INPUT_CAMERA_BYTES, mLimitBytes);
        mContext.startActivity(intent);
    }
}
