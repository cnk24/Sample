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

import com.cnk24.mediaalbum.Media;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.MediaFolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 20180819 SJK: Created
 */
public class MediaReadTask extends AsyncTask<Void, Void, MediaReadTask.ResultWrapper>
{
    public interface Callback {
        /**
         * Callback the results.
         *
         * @param mediaFolders album folder list.
         */
        void onScanCallback(ArrayList<MediaFolder> mediaFolders, ArrayList<MediaFile> checkedFiles);
    }

    static class ResultWrapper {
        private ArrayList<MediaFolder> mMediaFolders;
        private ArrayList<MediaFile> mMediaFiles;
    }

    private int mFunction;
    private List<MediaFile> mCheckedFiles;
    private MediaReader mMediaReader;
    private Callback mCallback;

    public MediaReadTask(int function, List<MediaFile> checkedFiles, MediaReader mediaReader, Callback callback) {
        this.mFunction = function;
        this.mCheckedFiles = checkedFiles;
        this.mMediaReader = mediaReader;
        this.mCallback = callback;
    }

    @Override
    protected ResultWrapper doInBackground(Void... params) {
        ArrayList<MediaFolder> mediaFolders;
        switch (mFunction) {
            case Media.FUNCTION_CHOICE_IMAGE: {
                mediaFolders = mMediaReader.getAllImage();
                break;
            }
            case Media.FUNCTION_CHOICE_VIDEO: {
                mediaFolders = mMediaReader.getAllVideo();
                break;
            }
            default: {
                throw new AssertionError("This should not be the case.");
            }
        }

        ArrayList<MediaFile> checkedFiles = new ArrayList<>();

        if (mCheckedFiles != null && !mCheckedFiles.isEmpty()) {
            List<MediaFile> mediaFiles = mediaFolders.get(0).getMediaFiles();
            for (MediaFile checkMediaFile : mCheckedFiles) {
                for (int i = 0; i < mediaFiles.size(); i++) {
                    MediaFile mediaFile = mediaFiles.get(i);
                    if (checkMediaFile.equals(mediaFile)) {
                        mediaFile.setChecked(true);
                        checkedFiles.add(mediaFile);
                    }
                }
            }
        }
        ResultWrapper wrapper = new ResultWrapper();
        wrapper.mMediaFolders = mediaFolders;
        wrapper.mMediaFiles = checkedFiles;
        return wrapper;
    }

    @Override
    protected void onPostExecute(ResultWrapper wrapper) {
        mCallback.onScanCallback(wrapper.mMediaFolders, wrapper.mMediaFiles);
    }
}
