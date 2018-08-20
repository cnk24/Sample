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

import android.os.AsyncTask;

import com.cnk24.mediaalbum.MediaFile;

/**
 * 20180819 SJK: Created
 */
public class PathConvertTask extends AsyncTask<String, Void, MediaFile>
{
    public interface Callback {
        /**
         * The task begins.
         */
        void onConvertStart();

        /**
         * Callback results.
         *
         * @param mediaFile result.
         */
        void onConvertCallback(MediaFile mediaFile);
    }

    private PathConversion mConversion;
    private Callback mCallback;

    public PathConvertTask(PathConversion conversion, Callback callback) {
        this.mConversion = conversion;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        mCallback.onConvertStart();
    }

    @Override
    protected MediaFile doInBackground(String... params) {
        return mConversion.convert(params[0]);
    }

    @Override
    protected void onPostExecute(MediaFile file) {
        mCallback.onConvertCallback(file);
    }
}
