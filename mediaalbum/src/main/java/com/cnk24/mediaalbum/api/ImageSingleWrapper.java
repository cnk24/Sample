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

import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.app.media.MediaActivity;

import java.util.ArrayList;

/**
 * 20180814 SJK: Created
 */
public final class ImageSingleWrapper extends BasicChoiceWrapper<ImageSingleWrapper, ArrayList<MediaFile>, String, MediaFile>
{

    public ImageSingleWrapper(Context context) {
        super(context);
    }

    @Override
    public void start() {
        MediaActivity.sSizeFilter = mSizeFilter;
        MediaActivity.sMimeFilter = mMimeTypeFilter;
        MediaActivity.sResult = mResult;
        MediaActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, MediaActivity.class);
        intent.putExtra(Media.KEY_INPUT_WIDGET, mWidget);

        intent.putExtra(Media.KEY_INPUT_FUNCTION, Media.FUNCTION_CHOICE_IMAGE);
        intent.putExtra(Media.KEY_INPUT_CHOICE_MODE, Media.MODE_SINGLE);
        intent.putExtra(Media.KEY_INPUT_COLUMN_COUNT, mColumnCount);
        intent.putExtra(Media.KEY_INPUT_ALLOW_CAMERA, mHasCamera);
        intent.putExtra(Media.KEY_INPUT_LIMIT_COUNT, 1);
        mContext.startActivity(intent);
    }
}
