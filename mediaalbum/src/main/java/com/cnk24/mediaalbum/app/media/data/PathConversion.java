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
package com.cnk24.mediaalbum.app.media.data;

import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;
import android.text.TextUtils;

import com.cnk24.mediaalbum.Filter;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.util.MediaUtils;

import java.io.File;

/**
 * 20180819 SJK: Created
 */
public class PathConversion
{
    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;

    public PathConversion(Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
    }

    @WorkerThread
    @NonNull
    public MediaFile convert(String filePath) {
        File file = new File(filePath);

        MediaFile mediaFile = new MediaFile();
        mediaFile.setPath(filePath);

        File parentFile = file.getParentFile();
        mediaFile.setBucketName(parentFile.getName());

        String mimeType = MediaUtils.getMimeType(filePath);
        mediaFile.setMimeType(mimeType);
        long nowTime = System.currentTimeMillis();
        mediaFile.setAddDate(nowTime);
        mediaFile.setSize(file.length());
        int mediaType = 0;
        if (!TextUtils.isEmpty(mimeType)) {
            if (mimeType.contains("video"))
                mediaType = MediaFile.TYPE_VIDEO;
            if (mimeType.contains("image"))
                mediaType = MediaFile.TYPE_IMAGE;
        }
        mediaFile.setMediaType(mediaType);

        if (mSizeFilter != null && mSizeFilter.filter(file.length())) {
            mediaFile.setDisable(true);
        }
        if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
            mediaFile.setDisable(true);
        }

        if (mediaType == MediaFile.TYPE_VIDEO) {
            MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(filePath);
                player.prepare();
                mediaFile.setDuration(player.getDuration());
            } catch (Exception ignored) {
            } finally {
                player.release();
            }

            if (mDurationFilter != null && mDurationFilter.filter(mediaFile.getDuration())) {
                mediaFile.setDisable(true);
            }
        }
        return mediaFile;
    }
}
