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

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.WorkerThread;

import com.cnk24.mediaalbum.Filter;
import com.cnk24.mediaalbum.MediaFile;
import com.cnk24.mediaalbum.MediaFolder;
import com.cnk24.mediaalbum.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 20180819 SJK: Created
 */
public class MediaReader
{
    private Context mContext;

    private Filter<Long> mSizeFilter;
    private Filter<String> mMimeFilter;
    private Filter<Long> mDurationFilter;

    public MediaReader(Context context, Filter<Long> sizeFilter, Filter<String> mimeFilter, Filter<Long> durationFilter) {
        this.mContext = context;

        this.mSizeFilter = sizeFilter;
        this.mMimeFilter = mimeFilter;
        this.mDurationFilter = durationFilter;
    }

    /**
     * Image attribute.
     */
    private static final String[] IMAGES = {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media.SIZE
    };

    /**
     * Scan for image files.
     */
    @WorkerThread
    private void scanImageFile(Map<String, MediaFolder> mediaFolderMap, MediaFolder allFileFolder) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                long size = cursor.getLong(6);

                MediaFile imageFile = new MediaFile();
                imageFile.setMediaType(MediaFile.TYPE_IMAGE);
                imageFile.setPath(path);
                imageFile.setBucketName(bucketName);
                imageFile.setMimeType(mimeType);
                imageFile.setAddDate(addDate);
                imageFile.setLatitude(latitude);
                imageFile.setLongitude(longitude);
                imageFile.setSize(size);

                if (mSizeFilter != null && mSizeFilter.filter(size)) {
                    imageFile.setDisable(true);
                }
                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                    imageFile.setDisable(true);
                }

                allFileFolder.addMediaFile(imageFile);
                MediaFolder mediaFolder = mediaFolderMap.get(bucketName);

                if (mediaFolder != null)
                    mediaFolder.addMediaFile(imageFile);
                else {
                    mediaFolder = new MediaFolder();
                    mediaFolder.setName(bucketName);
                    mediaFolder.addMediaFile(imageFile);

                    mediaFolderMap.put(bucketName, mediaFolder);
                }
            }
            cursor.close();
        }
    }

    /**
     * Video attribute.
     */
    private static final String[] VIDEOS = {
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DATE_ADDED,
            MediaStore.Video.Media.LATITUDE,
            MediaStore.Video.Media.LONGITUDE,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DURATION
    };

    /**
     * Scan for image files.
     */
    @WorkerThread
    private void scanVideoFile(Map<String, MediaFolder> mediaFolderMap, MediaFolder allFileFolder) {
        ContentResolver contentResolver = mContext.getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                VIDEOS,
                null,
                null,
                null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(0);
                String bucketName = cursor.getString(1);
                String mimeType = cursor.getString(2);
                long addDate = cursor.getLong(3);
                float latitude = cursor.getFloat(4);
                float longitude = cursor.getFloat(5);
                long size = cursor.getLong(6);
                long duration = cursor.getLong(7);

                MediaFile videoFile = new MediaFile();
                videoFile.setMediaType(MediaFile.TYPE_VIDEO);
                videoFile.setPath(path);
                videoFile.setBucketName(bucketName);
                videoFile.setMimeType(mimeType);
                videoFile.setAddDate(addDate);
                videoFile.setLatitude(latitude);
                videoFile.setLongitude(longitude);
                videoFile.setSize(size);
                videoFile.setDuration(duration);

                if (mSizeFilter != null && mSizeFilter.filter(size)) {
                    videoFile.setDisable(true);
                }
                if (mMimeFilter != null && mMimeFilter.filter(mimeType)) {
                    videoFile.setDisable(true);
                }
                if (mDurationFilter != null && mDurationFilter.filter(duration)) {
                    videoFile.setDisable(true);
                }

                allFileFolder.addMediaFile(videoFile);
                MediaFolder mediaFolder = mediaFolderMap.get(bucketName);

                if (mediaFolder != null)
                    mediaFolder.addMediaFile(videoFile);
                else {
                    mediaFolder = new MediaFolder();
                    mediaFolder.setName(bucketName);
                    mediaFolder.addMediaFile(videoFile);

                    mediaFolderMap.put(bucketName, mediaFolder);
                }
            }
            cursor.close();
        }
    }

    /**
     * Scan the list of pictures in the library.
     */
    @WorkerThread
    public ArrayList<MediaFolder> getAllImage() {
        Map<String, MediaFolder> mediaFolderMap = new HashMap<>();
        MediaFolder allFileFolder = new MediaFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.media_all_images));

        scanImageFile(mediaFolderMap, allFileFolder);

        ArrayList<MediaFolder> mediaFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getMediaFiles());
        mediaFolders.add(allFileFolder);

        for (Map.Entry<String, MediaFolder> folderEntry : mediaFolderMap.entrySet()) {
            MediaFolder mediaFolder = folderEntry.getValue();
            Collections.sort(mediaFolder.getMediaFiles());
            mediaFolders.add(mediaFolder);
        }
        return mediaFolders;
    }

    /**
     * Scan the list of videos in the library.
     */
    @WorkerThread
    public ArrayList<MediaFolder> getAllVideo() {
        Map<String, MediaFolder> mediaFolderMap = new HashMap<>();
        MediaFolder allFileFolder = new MediaFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.media_all_videos));

        scanVideoFile(mediaFolderMap, allFileFolder);

        ArrayList<MediaFolder> mediaFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getMediaFiles());
        mediaFolders.add(allFileFolder);

        for (Map.Entry<String, MediaFolder> folderEntry : mediaFolderMap.entrySet()) {
            MediaFolder mediaFolder = folderEntry.getValue();
            Collections.sort(mediaFolder.getMediaFiles());
            mediaFolders.add(mediaFolder);
        }
        return mediaFolders;
    }

    /**
     * Get all the multimedia files, including videos and pictures.
     */
    @WorkerThread
    public ArrayList<MediaFolder> getAllMedia() {
        Map<String, MediaFolder> mediaFolderMap = new HashMap<>();
        MediaFolder allFileFolder = new MediaFolder();
        allFileFolder.setChecked(true);
        allFileFolder.setName(mContext.getString(R.string.media_all_images_videos));

        scanImageFile(mediaFolderMap, allFileFolder);
        scanVideoFile(mediaFolderMap, allFileFolder);

        ArrayList<MediaFolder> mediaFolders = new ArrayList<>();
        Collections.sort(allFileFolder.getMediaFiles());
        mediaFolders.add(allFileFolder);

        for (Map.Entry<String, MediaFolder> folderEntry : mediaFolderMap.entrySet()) {
            MediaFolder mediaFolder = folderEntry.getValue();
            Collections.sort(mediaFolder.getMediaFiles());
            mediaFolders.add(mediaFolder);
        }
        return mediaFolders;
    }
}
