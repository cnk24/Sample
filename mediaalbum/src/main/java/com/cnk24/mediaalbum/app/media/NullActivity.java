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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.cnk24.mediaalbum.Action;
import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.R;
import com.cnk24.mediaalbum.api.widget.Widget;
import com.cnk24.mediaalbum.app.Contract;
import com.cnk24.mediaalbum.mvp.BaseActivity;

/**
 * 20180819 SJK: Created
 */
public class NullActivity extends BaseActivity implements Contract.NullPresenter
{
    private static final String KEY_OUTPUT_IMAGE_PATH = "KEY_OUTPUT_IMAGE_PATH";

    public static String parsePath(Intent intent) {
        return intent.getStringExtra(KEY_OUTPUT_IMAGE_PATH);
    }

    private Widget mWidget;
    private int mQuality = 1;
    private long mLimitDuration;
    private long mLimitBytes;

    private Contract.NullView mView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_activity_null);
        mView = new NullView(this, this);

        Bundle argument = getIntent().getExtras();
        assert argument != null;
        int function = argument.getInt(Media.KEY_INPUT_FUNCTION);
        boolean hasCamera = argument.getBoolean(Media.KEY_INPUT_ALLOW_CAMERA);

        mQuality = argument.getInt(Media.KEY_INPUT_CAMERA_QUALITY);
        mLimitDuration = argument.getLong(Media.KEY_INPUT_CAMERA_DURATION);
        mLimitBytes = argument.getLong(Media.KEY_INPUT_CAMERA_BYTES);

        mWidget = argument.getParcelable(Media.KEY_INPUT_WIDGET);
        mView.setupViews(mWidget);
        mView.setTitle(mWidget.getTitle());

        switch (function) {
            case Media.FUNCTION_CHOICE_IMAGE: {
                mView.setMessage(R.string.media_not_found_image);
                mView.setMakeVideoDisplay(false);
                break;
            }
            case Media.FUNCTION_CHOICE_VIDEO: {
                mView.setMessage(R.string.media_not_found_video);
                mView.setMakeImageDisplay(false);
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

        if (!hasCamera) {
            mView.setMakeImageDisplay(false);
            mView.setMakeVideoDisplay(false);
        }
    }

    @Override
    public void takePicture() {
        Media.camera(this)
                .image()
                .onResult(mCameraAction)
                .start();
    }

    @Override
    public void takeVideo() {
        Media.camera(this)
                .video()
                .quality(mQuality)
                .limitDuration(mLimitDuration)
                .limitBytes(mLimitBytes)
                .onResult(mCameraAction)
                .start();
    }

    private Action<String> mCameraAction = new Action<String>() {
        @Override
        public void onAction(@NonNull String result) {
            Intent intent = new Intent();
            intent.putExtra(KEY_OUTPUT_IMAGE_PATH, result);
            setResult(RESULT_OK, intent);
            finish();
        }
    };
}
