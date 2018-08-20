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

import android.content.Context;
import android.os.AsyncTask;

import com.cnk24.mediaalbum.MediaFile;

import java.util.ArrayList;

/**
 * 20180819 SJK: Created
 */
public class ThumbnailBuildTask extends AsyncTask<Void, Void, ArrayList<MediaFile>>
{
    public interface Callback {
        /**
         * The task begins.
         */
        void onThumbnailStart();

        /**
         * Callback results.
         *
         * @param mediaFiles result.
         */
        void onThumbnailCallback(ArrayList<MediaFile> mediaFiles);
    }

    private ArrayList<MediaFile> mMediaFiles;
    private Callback mCallback;

    private ThumbnailBuilder mThumbnailBuilder;

    public ThumbnailBuildTask(Context context, ArrayList<MediaFile> mediaFiles, Callback callback) {
        this.mMediaFiles = mediaFiles;
        this.mCallback = callback;
        this.mThumbnailBuilder = new ThumbnailBuilder(context);
    }

    @Override
    protected void onPreExecute() {
        mCallback.onThumbnailStart();
    }

    @Override
    protected ArrayList<MediaFile> doInBackground(Void... params) {
        for (MediaFile mediaFile : mMediaFiles) {
            int mediaType = mediaFile.getMediaType();
            if (mediaType == MediaFile.TYPE_IMAGE) {
                mediaFile.setThumbPath(mThumbnailBuilder.createThumbnailForImage(mediaFile.getPath()));
            } else if (mediaType == MediaFile.TYPE_VIDEO) {
                mediaFile.setThumbPath(mThumbnailBuilder.createThumbnailForVideo(mediaFile.getPath()));
            }
        }
        return mMediaFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<MediaFile> mediaFiles) {
        mCallback.onThumbnailCallback(mediaFiles);
    }
}
