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
import com.cnk24.mediaalbum.app.camera.CameraActivity;

/**
 * 20180817 SJK: Created
 */
public class ImageCameraWrapper extends BasicCameraWrapper<ImageCameraWrapper>
{
    public ImageCameraWrapper(Context context) {
        super(context);
    }

    public void start() {
        CameraActivity.sResult = mResult;
        CameraActivity.sCancel = mCancel;
        Intent intent = new Intent(mContext, CameraActivity.class);

        intent.putExtra(Media.KEY_INPUT_FUNCTION, Media.FUNCTION_CAMERA_IMAGE);
        intent.putExtra(Media.KEY_INPUT_FILE_PATH, mFilePath);
        mContext.startActivity(intent);
    }
}
