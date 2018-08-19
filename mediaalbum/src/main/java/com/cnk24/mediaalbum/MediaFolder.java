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
package com.cnk24.mediaalbum;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 20180817 SJK: Created
 */
public class MediaFolder implements Parcelable
{
    /**
     * Folder name.
     */
    private String name;
    /**
     * Image list in folder.
     */
    private ArrayList<MediaFile> mMediaFiles = new ArrayList<>();
    /**
     * checked.
     */
    private boolean isChecked;

    public MediaFolder() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MediaFile> getMediaFiles() {
        return mMediaFiles;
    }

    public void addMediaFile(MediaFile mediaFile) {
        mMediaFiles.add(mediaFile);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    protected MediaFolder(Parcel in) {
        name = in.readString();
        mMediaFiles = in.createTypedArrayList(MediaFile.CREATOR);
        isChecked = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeTypedList(mMediaFiles);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MediaFolder> CREATOR = new Creator<MediaFolder>() {
        @Override
        public MediaFolder createFromParcel(Parcel in) {
            return new MediaFolder(in);
        }

        @Override
        public MediaFolder[] newArray(int size) {
            return new MediaFolder[size];
        }
    };
}
